package cn.izualzhy.controller;

import cn.izualzhy.client.DemoFacade;
import cn.izualzhy.utils.FeignSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {
    @Autowired
    private FeignSample feignSample;

    @GetMapping("/index")
    public String index() {
        feignSample.sample();
        return "index";
    }
}
