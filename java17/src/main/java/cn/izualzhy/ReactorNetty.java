package cn.izualzhy;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class ReactorNetty {
    public static void main(String[] args) {
        DisposableServer server = HttpServer.create()//1．创建http服务器
                .host("localhost")//2．设置host
                .port(6083)//3．设置监听端口
                .route(routes -> routes//4．设置路由规则
                        .get("/hello", (request, response) -> response.
                                sendString(Mono.just("Hello World! ")))
                        .post("/echo", (request, response) -> response.
                                send(request.receive().retain()))
                        .get("/path/{param}",
                                (request, response) -> response.
                                        sendString(Mono.just(request.param("param"))))
                        .ws("/ws", (wsInbound, wsOutbound) -> wsOutbound.
                                send(wsInbound.receive().retain())))
                .bindNow();
        server.onDispose().block(); //5．阻塞方式启动服务器，同步等待服务停止
    }
}
