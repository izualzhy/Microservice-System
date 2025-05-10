package cn.izualzhy.gateway.debug;

import cn.izualzhy.gateway.config.RouteConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {
		// RouteConfig.class,
})
public class GatewayDebugApplication {
    public static void main(String[] args) {
		SpringApplication.run(GatewayDebugApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p.path("/tmp/**")
						.filters(f -> f.addRequestParameter("test", "test")
								.addResponseHeader("return", "return"))
						.uri("http://localhost:6001/"))
				.route(p -> p.path("/test/**")
						.filters(f -> f.addRequestHeader("X-TestHeader", "foobar"))
						.uri("http://localhost:6001/"))
				.build();
	}
}

