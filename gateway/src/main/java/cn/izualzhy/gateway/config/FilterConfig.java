package cn.izualzhy.gateway.config;

import cn.izualzhy.gateway.fitler.DynamicRouteFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Autowired
    public DynamicRouteFilter dynamicRouteFilter;
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("lilei_route", r -> r.path("/test/lilei/**")
                        .filters(f -> f)
                        .uri("http://localhost:6001"))
                .route("dynamic_forward_route", r -> r.path("/test/**")
                        .filters(f -> f.filter(dynamicRouteFilter))
                        .uri("http://default-service-2")) // 这里的 uri 只是占位，实际由过滤器修改
                .route("websocket_route", r -> r
                        .path("/ws/**")  // 仅匹配 WebSocket 请求
                        .uri("ws://localhost:8201")) // 转发到 WebSocket 服务器
                .build();
    }

}
