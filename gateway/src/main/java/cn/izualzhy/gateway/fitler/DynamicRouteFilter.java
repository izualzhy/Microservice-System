package cn.izualzhy.gateway.fitler;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class DynamicRouteFilter implements GatewayFilter, Ordered {
    private final Map<String, String> urlMapping;

    @Autowired
    public DynamicRouteFilter(@Value("#{${dynamic-route.url-mapping}}") Map<String, String> urlMapping) {
        this.urlMapping = urlMapping;
    }
    @PostConstruct
    public void init() {
        System.out.println("urlMappings: " + urlMapping);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("DynamicRouteFilter.filter() urlMapping : " + urlMapping);
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        String[] segments = path.split("/");
        if (segments.length < 3) {
            return chain.filter(exchange);
        }
        String cluster = segments[2];

        // 如果找不到匹配的 cluster，则返回 404，并写入提示语
        if (!urlMapping.containsKey(cluster)) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            String message = "没有匹配的路由规则: " + cluster;
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

        String targetUri = urlMapping.get(cluster);
        URI newUri = URI.create(targetUri + path);

        System.out.println("xxx: " + cluster + ", targetUri: " + targetUri + ", newUri: " + newUri);

        ServerHttpRequest newRequest = request.mutate().uri(newUri).build();
        ServerWebExchange mutatedExchange = exchange.mutate().request(newRequest).build();
        mutatedExchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, newUri);

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER + 1;
    }
}
