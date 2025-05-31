package cn.izualzhy.ws.config;

import cn.izualzhy.ws.service.impl.WebSocketServiceImpl;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        System.out.println("✅ ServerEndpointExporter 已加载");
        return new ServerEndpointExporter();
    }

    // @Bean
    // public ServerEndpointConfig serverEndpointConfig() {
    //     return ServerEndpointConfig.Builder
    //             .create(WebSocketServiceImpl.class, "/ws")
    //             .configurator(new ServerEndpointConfig.Configurator() {
    //                 @Override
    //                 public void modifyHandshake(ServerEndpointConfig sec,
    //                                             jakarta.websocket.server.HandshakeRequest request,
    //                                             jakarta.websocket.HandshakeResponse response) {
    //                     // 模拟握手耗时
    //                     // try {
    //                     //     System.out.println("🕒 模拟握手延迟中...");
    //                     //     Thread.sleep(1); // 模拟 300 秒延迟
    //                     //     System.out.println("🕒 模拟握手延迟结束...");
    //                     // } catch (InterruptedException e) {
    //                     //     e.printStackTrace();
    //                     // }
    //
    //                     // 禁用 WebSocket 压缩
    //                     sec.getUserProperties().put("javax.websocket.endpoint.noCompression", "true");
    //                 }
    //             }).build();
    // }
}