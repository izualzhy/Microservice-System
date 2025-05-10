package cn.izualzhy.webflux.config;

// 静态导入
import cn.izualzhy.webflux.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    // 注入用户处理器
    @Autowired
    private UserHandler userHandler = null;


    // 用户路由
    @Bean
    public RouterFunction<ServerResponse> userRouter() {
        RouterFunction<ServerResponse> router =
            // 对应请求URI的对应关系
            route(
                // GET请求及其路径
                GET("/router/user/{id}")
                // 响应结果为JSON数据流
                .and(accept(APPLICATION_STREAM_JSON)),
                // 定义处理方法
                userHandler :: getUser)
            // 增加一个路由
            .andRoute(
                // GET请求及其路径
                GET("/router/user/{userName}/{note}")
                .and(accept(APPLICATION_STREAM_JSON)),
                // 定义处理方法
                userHandler :: findUsers)
            // 增加一个路由
            .andRoute(
                // POST请求及其路径
                POST("/router/user")
                // 请求体为JSON数据流
                .and(contentType(APPLICATION_STREAM_JSON)
                // 响应结果为JSON数据流
                .and(accept(APPLICATION_STREAM_JSON))),
                // 定义处理方法
                userHandler :: insertUser)
            // 增加一个路由
            .andRoute(
                // PUT请求及其路径
                PUT("/router/user")
                // 请求体为JSON数据流
                .and(contentType(APPLICATION_STREAM_JSON))
                // 响应结果为JSON数据流
                .and(accept(APPLICATION_STREAM_JSON)),
                // 定义处理方法
                userHandler :: updateUser)
            .andRoute(
                // DELETE请求及其路径
                DELETE("/router/user/{id}")
                // 响应结果为JSON数据流
                .and(accept(APPLICATION_STREAM_JSON)),
                // 定义处理方法
                userHandler :: deleteUser)
            .andRoute(
                // PUT请求及其路径
                PUT("/router/user/name")
                // 响应结果为JSON数据流
                .and(accept(APPLICATION_STREAM_JSON)),
                // 定义处理方法
                userHandler:: updateUserName
            );
        return router;
    }
}