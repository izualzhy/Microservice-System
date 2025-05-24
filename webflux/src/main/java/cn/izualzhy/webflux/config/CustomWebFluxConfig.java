package cn.izualzhy.webflux.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.AbstractHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBufferFactory;

@Log4j2
@Configuration
public class CustomWebFluxConfig {

    @Bean
    public HandlerMapping myHandlerMapping() {
        return new AbstractHandlerMapping() {
            {
                // 设置优先级，高于默认路由（默认是 LOWEST_PRECEDENCE）
                setOrder(-1);
            }

            @Override
            protected Mono<Object> getHandlerInternal(ServerWebExchange exchange) {
                if (exchange.getRequest().getPath().value().equals("/customMapping/ping")) {
                    WebHandler handler = webExchange -> {
                        DataBufferFactory bufferFactory = webExchange.getResponse().bufferFactory();
                        byte[] content = "PingPong from custom WebHandler!\n".getBytes();
                        webExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
                        return webExchange.getResponse().writeWith(Mono.just(bufferFactory.wrap(content)));
                    };
                    return Mono.just(handler);
                }
                return Mono.empty();
            }
        };
    }
}
