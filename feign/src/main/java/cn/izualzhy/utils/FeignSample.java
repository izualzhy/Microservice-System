package cn.izualzhy.utils;

import cn.izualzhy.client.DemoFacade;
import cn.izualzhy.client.TestFeignClient;
import cn.izualzhy.client.UserFacade;
import feign.codec.Decoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class FeignSample {
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private DemoFacade demoFacade;
    @Autowired
    private TestFeignClient testFeignClient;

    @Autowired
    ApplicationContext ctx;

    public void sample() {
        TestFeignClient.InstanceCallStatRequest request = new TestFeignClient.InstanceCallStatRequest();
        List<TestFeignClient.InstanceCallStatResponse> a = testFeignClient.listInstances(request);
        System.out.println("FeignSample , listInstances : " + a);
        System.exit(0);

        String user = userFacade.get("Lily");
        System.out.println(user);

        String[] names = ctx.getBeanNamesForType(Decoder.class);
        System.out.println("FeignSample , Decoder Bean Count : " + names.length);
        for (String name : names) {
            System.out.println("FeignSample , Decoder : " + name);
        }

        FeignContext feignContext = ctx.getBean("feignContext", FeignContext.class);
        System.out.println("FeignSample , FeignContext Bean : " + feignContext);
        Decoder decoder = feignContext.getInstance("user", Decoder.class);
        System.out.println("FeignSample , Decoder from FeignContext: " + decoder);
        decoder = feignContext.getInstance("userFacade", Decoder.class);
        System.out.println("FeignSample , Decoder from FeignContext: " + decoder);



        try {
            String user2 = userFacade.getNonExist("Lily");
            System.out.println(user2);
        } catch (Exception e) {
            System.out.println("userFacade.getNonExist , Exception: " + e);
        }

        try {
            demoFacade.nonExist();
        } catch (Exception e) {
            System.out.println("demoFacade.nonExist , Exception: " + e);
        }
    }
}
