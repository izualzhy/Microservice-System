package cn.izualzhy.demo.service.impl;

import cn.izualzhy.demo.service.HelloService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        log.info("HelloServiceImpl.hello() name: {}", name);
        return "Hello , " + name;
    }
}
