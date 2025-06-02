package cn.izualzhy.purewebflux.config;

import cn.izualzhy.purewebflux.handler.FunctionPersonHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * from: 《Java 异步编程实战》
 */

@Configuration
public class FunctionModelConfig {
    @Bean
    public FunctionPersonHandler handler() {
        return new FunctionPersonHandler();
    }
    @Bean
    public RouterFunction<ServerResponse> routerFunction(final
                                                         FunctionPersonHandler handler) {
        RouterFunction<ServerResponse> route = RouterFunctions.route()//1
                .GET("/getPersonF", RequestPredicates.accept(MediaType.
                        APPLICATION_JSON), handler::getPerson)//2
                .GET("/getPersonListF", RequestPredicates.accept(MediaType.
                        APPLICATION_JSON), handler::getPersonList)//3
                .build(); //4
        return route;
    }
}
