package source;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class PrintSource {
    public static void main(String[] args) throws IOException {
        //String filePath = "apm-feign/target/classes/org/springframework/cloud/netflix/feign/ribbon/LoadBalancerFeignClient.class";
        //String filePath = "apm-feign/target/classes/feign/ReflectiveFeign$BuildTemplateByResolvingArgs.class";
        //String filePath = "apm-feign/target/classes/org/springframework/cloud/sleuth/instrument/web/client/feign/TraceFeignClient.class";
        String filePath = "apm-feign/target/classes/feign/SynchronousMethodHandler.class";
        byte[] b = Files.readAllBytes(Paths.get(filePath));
        System.out.println(b.length);
        System.out.println(new String(Base64.getEncoder().encode(b)));
    }
}
