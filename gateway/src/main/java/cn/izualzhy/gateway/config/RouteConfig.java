package cn.izualzhy.gateway.config;

import cn.izualzhy.gateway.client.MyNettyHttpClient;
import cn.izualzhy.gateway.filter.TestFilter;
import cn.izualzhy.gateway.filter.RouteToDemoFilter;
import cn.izualzhy.gateway.function.RequestRewriteFunction;
import cn.izualzhy.gateway.function.ResponseRewriteFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RouteConfig {
    @Autowired
    public TestFilter testFilter;

    @Autowired
    public RouteToDemoFilter routeToDemoFilter;

    @Value("${gateway.timeout-ms:180000}")
    private Long gatewayTimeoutMs;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("test_route", r -> r.order(2).path("/test/**")
                        .filters(f -> f.filter(testFilter)
                        )
                        .uri("http://default-service-2")) // 这里的 uri 只是占位，无用。实际由过滤器修改。
                .route("welcome_route", r -> r.path("/welcome/**")
                        .filters(f -> f.filter(routeToDemoFilter))
                        .uri("http://default-service-3"))
//                .route("websocket_route", r -> r
//                        .path("/ws/**")
//                        .and()
//                        .header("Upgrade", "websocket") // 只处理 WebSocket 请求// 仅匹配 WebSocket 请求
//                        .uri("ws://localhost:8201")) // 转发到 WebSocket 服务器
                // 普通 HTTP 连接（http://）
//                .route("http_route", r -> r
//                        .path("/ws/**")  // 仍然匹配 /ws/**，但仅限于 HTTP
//                        .and()
//                        .not(r1 -> r1.header("Upgrade", "websocket")) // 过滤掉 WebSocket 请求
//                        .uri("http://localhost:6001")) // 转发到 HTTP 服务器
                .route("rewrite_response_upper", r -> r.order(1).path("/test/pathD/**")
                        .filters(f -> f.modifyResponseBody(String.class, String.class,
                                        (exchange, s) -> Mono.just(s.toUpperCase())))
                        .uri("http://127.0.0.1:6001"))
                .build();
    }

}
