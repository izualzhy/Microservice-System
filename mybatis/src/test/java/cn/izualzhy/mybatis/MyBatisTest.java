package cn.izualzhy.mybatis;

import cn.izualzhy.service.MyBatisUserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyBatisTest {
   // 注入服务接口
   @Autowired
   private MyBatisUserService myBatisUserService = null;

   public void setMyBatisUserService(MyBatisUserService myBatisUserService) {
      this.myBatisUserService = myBatisUserService;
   }

   @PostConstruct // 使用Bean生命周期方法进行测试
   public void testMyBatis() {
      var user = this.myBatisUserService.getUser(21L);
      System.out.println(user);
   }
}