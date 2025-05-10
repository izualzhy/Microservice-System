package cn.izualzhy.gateway.filter;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Component
public class TestFilter implements GatewayFilter, Ordered {
    private final Map<String, String> urlMapping;

    @Autowired
    public TestFilter(@Value("#{${dynamic-route.url-mapping}}") Map<String, String> urlMapping) {
        this.urlMapping = urlMapping;
    }
    @PostConstruct
    public void init() {
        log.info("urlMappings: " + urlMapping);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("INFO DynamicRouteFilter.filter() urlMapping : {}", urlMapping);
        log.debug("DEBUG DynamicRouteFilter.filter() urlMapping : {}", urlMapping);
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info("DynamicRouteFilter.filter() query : {} rawQuery : {}", request.getURI().getQuery(), request.getURI().getRawQuery());
        String query = request.getURI().getRawQuery();

        String clientIp = request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
        int clientPort = request.getRemoteAddress() != null ? request.getRemoteAddress().getPort() : -1;

        log.info("Client IP: {}, Port: {}", clientIp, clientPort);

        String forwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (forwardedFor != null) {
            log.info("X-Forwarded-For: {}", forwardedFor);
        } else {
            log.info("X-Forwarded-For is null");
        }

        String[] segments = path.split("/");
        if (segments.length < 3) {
            return chain.filter(exchange);
        }
        String cluster = segments[2];

        String targetUri = urlMapping.get(cluster);
        URI newUri = URI.create(targetUri + path + (query != null ? "?" + query : ""));

        log.info("forward to newUri: {}", cluster, targetUri, newUri);

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
