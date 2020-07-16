package org.springframework.cloud.netflix.feign.ribbon;

import com.littlehow.apm.base.util.StopWatch;
import com.littlehow.apm.feign.advice.AdviceExecutor;
import com.littlehow.apm.feign.advice.WebAdviceContext;
import com.netflix.client.ClientException;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import feign.Client;
import feign.Request;
import feign.Response;
import feign.Util;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.io.IOException;
import java.net.URI;

public class LoadBalancerFeignClient implements Client {

    static final Request.Options DEFAULT_OPTIONS = new Request.Options();
    private final Client delegate;
    private CachingSpringLoadBalancerFactory lbClientFactory;
    private SpringClientFactory clientFactory;

    public LoadBalancerFeignClient(Client delegate,
                                   CachingSpringLoadBalancerFactory lbClientFactory,
                                   SpringClientFactory clientFactory) {
        this.delegate = delegate;
        this.lbClientFactory = lbClientFactory;
        this.clientFactory = clientFactory;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        StopWatch watch = StopWatch.getAndStart();
        Throwable t = null;
        Response response = null;
        WebAdviceContext context = WebAdviceContext.getInstance(request, watch.getStartTime());
        try {
            //前置调用
            AdviceExecutor.preExecute(context);
            URI asUri = URI.create(request.url());
            String clientName = asUri.getHost();
            URI uriWithoutHost = cleanUrl(request.url(), clientName);
            FeignLoadBalancer.RibbonRequest ribbonRequest = new FeignLoadBalancer.RibbonRequest(
                    this.delegate, request, uriWithoutHost);

            IClientConfig requestConfig = getClientConfig(options, clientName);
            response = lbClient(clientName).executeWithLoadBalancer(ribbonRequest,
                    requestConfig).toResponse();
            //将response设置为可重复读
            response = repetRead(response);
            return response;
        } catch (ClientException e) {
            t = e;
            IOException io = findIOException(e);
            if (io != null) {
                throw io;
            }
            throw new RuntimeException(e);
        } catch (Throwable t1) {
            //记录所有异常
            t = t1;
            throw t1;
        } finally {
            context.setResponse(response);
            context.setException(t);
            context.setDuring(watch.stop().during());
            AdviceExecutor.postExecute(context);
        }
    }

    /**
     * 将其设置为可重复度
     * @param response - 设置为空重复读
     * @return
     */
    private Response repetRead(Response response) {
        try {
            int status = response.status();
            if (response.body() != null && !response.body().isRepeatable()
                    && status != 204 && status != 205) {
                return response.toBuilder().body(Util.toByteArray(response.body().asInputStream())).build();
            }
        } catch (Exception e) {
            //skip
        }
        return response;
    }

    IClientConfig getClientConfig(Request.Options options, String clientName) {
        IClientConfig requestConfig;
        if (options == DEFAULT_OPTIONS) {
            requestConfig = this.clientFactory.getClientConfig(clientName);
        } else {
            requestConfig = new FeignOptionsClientConfig(options);
        }
        return requestConfig;
    }

    protected IOException findIOException(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof IOException) {
            return (IOException) t;
        }
        return findIOException(t.getCause());
    }

    public Client getDelegate() {
        return this.delegate;
    }

    static URI cleanUrl(String originalUrl, String host) {
        return URI.create(originalUrl.replaceFirst(host, ""));
    }

    private FeignLoadBalancer lbClient(String clientName) {
        return this.lbClientFactory.create(clientName);
    }

    static class FeignOptionsClientConfig extends DefaultClientConfigImpl {

        public FeignOptionsClientConfig(Request.Options options) {
            setProperty(CommonClientConfigKey.ConnectTimeout,
                    options.connectTimeoutMillis());
            setProperty(CommonClientConfigKey.ReadTimeout, options.readTimeoutMillis());
        }

        @Override
        public void loadProperties(String clientName) {

        }

        @Override
        public void loadDefaultValues() {

        }

    }
}
