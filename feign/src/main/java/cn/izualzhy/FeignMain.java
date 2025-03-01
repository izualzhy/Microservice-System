package cn.izualzhy;

import cn.izualzhy.config.FeignConfig;
import feign.codec.Decoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableFeignClients(basePackages="cn.izualzhy")
public class FeignMain {
   public static void main(String[] args) {
      ApplicationContext ctx = SpringApplication.run(FeignMain.class, args);
      String[] names = ctx.getBeanNamesForType(Decoder.class);
      System.out.println("FeignMain , Decoder Bean Count : " + names.length);
      for (String name : names) {
         System.out.println("FeignMain , Decoder : " + name);

         Object x = ctx.getBean(name);
         System.out.println("FeignMain , Decoder bean : " + x);
      }

   }
}
