package cn.izualzhy.gateway.config;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;

import java.util.Map;

@Configuration
public class DebugHandlerMappingsConfig {

    @Bean
    public ApplicationRunner printHandlerMappings(ApplicationContext ctx) {
        return args -> {
            Map<String, HandlerMapping> mappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                    ctx,
                    HandlerMapping.class,
                    true,
                    false
            );

            mappings.forEach((name, mapping) -> {
                System.out.println("name: " + name + " handler: " + mapping.getClass());
            });
        };
    }
}
