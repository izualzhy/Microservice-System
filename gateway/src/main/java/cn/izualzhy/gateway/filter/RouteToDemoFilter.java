package cn.izualzhy.gateway.filter;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Log4j2
@Component
public class RouteToDemoFilter implements GatewayFilter, Ordered {
    @PostConstruct
    public void init() {
        log.info("RouteToDemoFilter init.");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("RouteToDemoFilter.filter() exchange : {}", exchange);
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String query = request.getURI().getRawQuery();

        String targetUri = "http://localhost:6001";
        URI newUri = URI.create(targetUri + path + (query != null ? "?" + query : ""));

        log.info("forward to newUri: {}", newUri);

        ServerHttpRequest newRequest = request.mutate().uri(newUri).build();
        ServerWebExchange mutatedExchange = exchange.mutate().request(newRequest).build();
        mutatedExchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, newUri);

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER + 2;
    }
}

