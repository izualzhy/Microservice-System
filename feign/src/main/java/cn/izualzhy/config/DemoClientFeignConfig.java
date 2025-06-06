package cn.izualzhy.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class DemoClientFeignConfig {
    @Bean
    ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    class CustomErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, feign.Response response) {
            System.out.println("------\nDemoClientFeignConfig methodKey: " + methodKey + " response: " + response + "\n------");
            return new RuntimeException("DemoClientFeignConfig test");
        }
    }
}
