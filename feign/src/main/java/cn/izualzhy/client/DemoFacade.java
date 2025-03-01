package cn.izualzhy.client;

import cn.izualzhy.config.DemoClientFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//@FeignClient(value = "demoFacade", url = "http://localhost:6001")
@FeignClient(value = "demoFacade", url = "http://localhost:6001", configuration = DemoClientFeignConfig.class)
public interface DemoFacade {
    @GetMapping("/demo/nonExist")
    void nonExist();
}
