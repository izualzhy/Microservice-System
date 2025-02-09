package cn.izualzhy.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    @Autowired
    void init() {
        System.out.println("HelloService init");
    }
}
