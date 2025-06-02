package cn.izualzhy.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Log4j2
public class KeepAliveController {
    @GetMapping("/keepalive")
    public String keepalive(HttpServletRequest request) {
        int remotePort = request.getRemotePort();
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("trace_id", traceId);

        log.info("TraceId: {}, RemoteAddr: {}, RemotePort: {}, Thread: {}",
                traceId,
                request.getRemoteAddr(),
                remotePort,
                Thread.currentThread().getName());

        MDC.clear();
        return "ok";
    }
}
