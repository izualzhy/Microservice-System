package cn.izualzhy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "userFacade", url = "http://localhost:6001")
public interface UserFacade {
   @GetMapping("/user/{name}")
   String get(@PathVariable("name") String name);

   @GetMapping("/nonExist/{name}")
   String getNonExist(@PathVariable("name") String name);
}

