package cn.izualzhy.ws.service.impl;

import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**** imports ****/
//@ServerEndpoint("/ws")
@Service
public class WebSocketServiceImpl {
    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
    private static int onlineCount = 0;
    // concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServiceImpl对象
    private static CopyOnWriteArraySet<WebSocketServiceImpl>
            webSocketSet = new CopyOnWriteArraySet<>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        readHeaders(session, config);
        webSocketSet.add(this);     // 加入set中
        addOnlineCount();            // 在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage("有新的连接加入了！！");
//            sendMessage("A");
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    private void readHeaders(Session session, EndpointConfig config) {
        // 读取 headers
        HandshakeRequest request = (HandshakeRequest) config.getUserProperties().get(HandshakeRequest.class.getName());
        System.out.println("request: " + request);
        if (request != null) {
            Map<String, List<String>> headers = request.getHeaders();
            List<String> authHeaders = headers.get("Authorization");
            if (authHeaders != null && !authHeaders.isEmpty()) {
                System.out.println("Authorization Header: " + authHeaders.get(0));
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);   // 从set中删除
        subOnlineCount();            // 在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        // 群发消息
        for (WebSocketServiceImpl item : webSocketSet) {
            try {
                /*
                // 获取当前用户名称
                String userName = item.getSession()
                        .getUserPrincipal().getName();
                System.out.println(userName);
                */
                item.sendMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息
     * @param message 客户端消息
     * @throws IOException
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    // 返回在线数
    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    // 当连接人数增加时
    private static synchronized void addOnlineCount() {
        WebSocketServiceImpl.onlineCount++;
    }

    // 当连接人数减少时
    private static synchronized void subOnlineCount() {
        WebSocketServiceImpl.onlineCount--;
    }
}