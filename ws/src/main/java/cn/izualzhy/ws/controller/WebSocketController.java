package cn.izualzhy.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/websocket")
public class WebSocketController {
    // 跳转websocket页面
    @GetMapping("/index")
    public String websocket() {
        return "websocket";
    }
}