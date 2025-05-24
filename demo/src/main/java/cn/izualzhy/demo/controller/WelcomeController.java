package cn.izualzhy.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.owasp.encoder.Encode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
@Log4j2
public class WelcomeController {
    @Data
    @AllArgsConstructor
    class WelcomeMsg {
        public String name;
        public String msg;
    }
    @GetMapping("/{name}")
    public WelcomeMsg get(@PathVariable("name") String name) {
        log.info("getWelcome name = {}", name);

                // 包含特殊字符的日志内容
        String logMessage = "This is a log message with \"quotes\" and special characters.";

        // 对日志内容进行编码
        String encodedLogMessage = Encode.forJava(logMessage);

        log.info("Original log message: {}", logMessage);
        log.info("Encoded log message: {}", encodedLogMessage);

        return new WelcomeMsg(name, "welcome " + name);
    }
}
