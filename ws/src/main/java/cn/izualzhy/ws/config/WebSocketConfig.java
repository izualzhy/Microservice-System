package cn.izualzhy.ws.config;

import cn.izualzhy.ws.service.impl.WebSocketServiceImpl;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
        @Bean
    public ServerEndpointConfig serverEndpointConfig() {
        return ServerEndpointConfig.Builder
                .create(WebSocketServiceImpl.class, "/ws")
                .configurator(new ServerEndpointConfig.Configurator() {
                    @Override
                    public void modifyHandshake(ServerEndpointConfig sec,
                                                jakarta.websocket.server.HandshakeRequest request,
                                                jakarta.websocket.HandshakeResponse response) {
                        // 禁用 WebSocket 压缩
                        sec.getUserProperties().put("javax.websocket.endpoint.noCompression", "true");
                    }
                }).build();
    }
}