/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package feign;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.littlehow.apm.base.ServerInfo;
import com.littlehow.apm.base.util.StopWatch;
import com.littlehow.apm.base.web.MyApplicationUrl;
import com.littlehow.apm.base.web.RemoteServerContext;
import com.littlehow.apm.feign.advice.AdviceExecutor;
import com.littlehow.apm.feign.advice.WebAdviceContext;
import feign.InvocationHandlerFactory.MethodHandler;
import feign.Request.Options;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;

import static feign.FeignException.errorExecuting;
import static feign.FeignException.errorReading;
import static feign.Util.checkNotNull;
import static feign.Util.ensureClosed;

final class SynchronousMethodHandler implements MethodHandler {

    private static final long MAX_RESPONSE_BUFFER_SIZE = 8192L;

    private final MethodMetadata metadata;
    private final Target<?> target;
    private final Client client;
    private final Retryer retryer;
    private final List<RequestInterceptor> requestInterceptors;
    private final Logger logger;
    private final Logger.Level logLevel;
    private final RequestTemplate.Factory buildTemplateFromArgs;
    private final Options options;
    private final Decoder decoder;
    private final ErrorDecoder errorDecoder;
    private final boolean decode404;

    private SynchronousMethodHandler(Target<?> target, Client client, Retryer retryer,
                                     List<RequestInterceptor> requestInterceptors, Logger logger,
                                     Logger.Level logLevel, MethodMetadata metadata,
                                     RequestTemplate.Factory buildTemplateFromArgs, Options options,
                                     Decoder decoder, ErrorDecoder errorDecoder, boolean decode404) {
        this.target = checkNotNull(target, "target");
        this.client = checkNotNull(client, "client for %s", target);
        this.retryer = checkNotNull(retryer, "retryer for %s", target);
        this.requestInterceptors =
                checkNotNull(requestInterceptors, "requestInterceptors for %s", target);
        this.logger = checkNotNull(logger, "logger for %s", target);
        this.logLevel = checkNotNull(logLevel, "logLevel for %s", target);
        this.metadata = checkNotNull(metadata, "metadata for %s", target);
        this.buildTemplateFromArgs = checkNotNull(buildTemplateFromArgs, "metadata for %s", target);
        this.options = checkNotNull(options, "options for %s", target);
        this.errorDecoder = checkNotNull(errorDecoder, "errorDecoder for %s", target);
        this.decoder = checkNotNull(decoder, "decoder for %s", target);
        this.decode404 = decode404;
    }

    @Override
    public Object invoke(Object[] argv) throws Throwable {
        RequestTemplate template = buildTemplateFromArgs.create(argv);
        Retryer retryer = this.retryer.clone();
        while (true) {
            try {
                return executeAndDecode(template);
            } catch (RetryableException e) {
                retryer.continueOrPropagate(e);
                if (logLevel != Logger.Level.NONE) {
                    logger.logRetry(metadata.configKey(), logLevel);
                }
                continue;
            }
        }
    }

    Object executeAndDecode(RequestTemplate template) throws Throwable {
        Request request = targetRequest(template);
        StopWatch watch = StopWatch.getAndStart();
        Throwable t = null;
        Response response;
        WebAdviceContext context = WebAdviceContext.getInstance(request, watch.getStartTime());
        try {
            String ipPort = MyApplicationUrl.ipPort(target.url(), target.name());
            if (ipPort != null) {
                RemoteServerContext.setHostPort(ipPort);
            }
            AdviceExecutor.preExecute(context);
            response = execute(request);
            response = repetRead(response);
            context.setResponse(response);
            if (Response.class != metadata.returnType()) {
                return decodeResponse(response);
            }
            return response;
        } catch (Throwable t1) {
            t = t1;
            throw t1;
        } finally {
            // 获取远程调用地址信息, 判断是否有提前服务名
            String url = template.url();
            if (context.attribute(AdviceExecutor.SERVICE_NAME_ATTRIBUTE) != null) {
                url = context.attribute(AdviceExecutor.SERVICE_NAME_ATTRIBUTE);
            }
            ServerInfo remoteServer = RemoteServerContext.getRemoteServer(target.name(), url);
            context.setException(t);
            context.setRemote(remoteServer);
            context.setDuring(watch.stop().during());
            AdviceExecutor.postExecute(context);
        }
    }

    Response execute(Request request) {
        if (logLevel != Logger.Level.NONE) {
            logger.logRequest(metadata.configKey(), logLevel, request);
        }

        Response response;
        long start = System.nanoTime();
        try {
            response = client.execute(request, options);
            // ensure the request is set. TODO: remove in Feign 10
            response.toBuilder().request(request).build();
        } catch (IOException e) {
            if (logLevel != Logger.Level.NONE) {
                logger.logIOException(metadata.configKey(), logLevel, e, elapsedTime(start));
            }
            throw errorExecuting(request, e);
        }
        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        boolean shouldClose = true;
        try {
            if (logLevel != Logger.Level.NONE) {
                response =
                        logger.logAndRebufferResponse(metadata.configKey(), logLevel, response, elapsedTime);
                // ensure the request is set. TODO: remove in Feign 10
                response.toBuilder().request(request).build();
            }
            if (response.body() == null
                    || response.body().length() == null
                    || response.body().length() <= 0) {
                return response;
            }
            // Ensure the response body is disconnected
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            // 这里操作后in其实已经close, 所以不需要继续close
            shouldClose = false;
            return response.toBuilder().body(bodyData).build();
        } catch (IOException e) {
            if (logLevel != Logger.Level.NONE) {
                logger.logIOException(metadata.configKey(), logLevel, e, elapsedTime);
            }
            throw errorReading(request, response, e);
        } finally {
            if (shouldClose) {
                ensureClosed(response.body());
            }
        }
    }

    Object decodeResponse(Response response) throws Throwable {
        if (response.status() >= 200 && response.status() < 300) {
            if (void.class == metadata.returnType()) {
                return null;
            } else {
                return decode(response);
            }
        } else if (decode404 && response.status() == 404 && void.class != metadata.returnType()) {
            return decode(response);
        } else {
            throw errorDecoder.decode(metadata.configKey(), response);
        }
    }

    long elapsedTime(long start) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    }

    Request targetRequest(RequestTemplate template) {
        for (RequestInterceptor interceptor : requestInterceptors) {
            interceptor.apply(template);
        }
        return target.apply(new RequestTemplate(template));
    }

    Object decode(Response response) throws Throwable {
        try {
            return decoder.decode(response, metadata.returnType());
        } catch (FeignException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new DecodeException(e.getMessage(), e);
        }
    }

    static class Factory {

        private final Client client;
        private final Retryer retryer;
        private final List<RequestInterceptor> requestInterceptors;
        private final Logger logger;
        private final Logger.Level logLevel;
        private final boolean decode404;

        Factory(Client client, Retryer retryer, List<RequestInterceptor> requestInterceptors,
                Logger logger, Logger.Level logLevel, boolean decode404) {
            this.client = checkNotNull(client, "client");
            this.retryer = checkNotNull(retryer, "retryer");
            this.requestInterceptors = checkNotNull(requestInterceptors, "requestInterceptors");
            this.logger = checkNotNull(logger, "logger");
            this.logLevel = checkNotNull(logLevel, "logLevel");
            this.decode404 = decode404;
        }

        public MethodHandler create(Target<?> target, MethodMetadata md,
                                    RequestTemplate.Factory buildTemplateFromArgs,
                                    Options options, Decoder decoder, ErrorDecoder errorDecoder) {
            return new SynchronousMethodHandler(target, client, retryer, requestInterceptors, logger,
                    logLevel, md, buildTemplateFromArgs, options, decoder,
                    errorDecoder, decode404);
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
            // skip
        }
        return response;
    }
}
