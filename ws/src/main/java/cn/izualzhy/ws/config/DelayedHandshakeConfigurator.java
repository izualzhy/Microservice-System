package cn.izualzhy.ws.config;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.stereotype.Service;

// 1. 自定义 Configurator（独立类或内部类）
public class DelayedHandshakeConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        try {
            System.out.println("🕒 DelayedHandshakeConfigurator 模拟握手延迟中...");
            Thread.sleep(300000); // 300秒延迟
            System.out.println("🕒 DelayedHandshakeConfigurator 模拟握手延迟结束...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
