package cn.izualzhy.gateway.config;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class DebugHandlerMappingsConfig {

    @Bean
    public ApplicationRunner printHandlerMappings(ApplicationContext ctx) {
        return args -> {
            Map<String, HandlerMapping> mappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                    ctx,
                    HandlerMapping.class,
                    true,
                    false
            );

            mappings.forEach((name, mapping) -> {
                System.out.println("name: " + name + " handler: " + mapping.getClass());
            });
        };
    }

    @Bean
    ApplicationRunner showMappings(ConfigurableApplicationContext ctx) {
        return args -> {
            walk(ctx, 0);
        };
    }

    private void walk(ApplicationContext c, int level) {
        Arrays.stream(c.getBeanNamesForType(SimpleUrlHandlerMapping.class))
                .forEach(name ->
                        System.out.printf("level=%d  ctx=%s  beanName=%s%n",
                                level, c.getDisplayName(), name));

        if (c.getParent() != null) {
            walk(c.getParent(), level + 1);
        }
    }

    @Bean
    public ApplicationRunner listSimpleUrlHandlerMappings(ApplicationContext rootCtx) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) {

                System.out.println("\n===== SimpleUrlHandlerMapping per ApplicationContext =====");

                ApplicationContext ctx = rootCtx;
                int level = 0;

                // ① 逐层向父容器爬
                while (ctx != null) {

                    String ctxId   = ctx.getId();                         // application-1, management, …
                    String ctxType = ctx.getClass().getSimpleName();      // AnnotationConfigReactiveWebServerApplicationContext
                    System.out.printf(">> level %d — [%s] %s%n",
                            level, ctxId, ctxType);

                    // ② 取这一层里的所有 SimpleUrlHandlerMapping（不含祖先）
                    Map<String, SimpleUrlHandlerMapping> map =
                            BeanFactoryUtils.beansOfTypeIncludingAncestors(
                                    ctx,
                                    SimpleUrlHandlerMapping.class,
                                    true,          // includeNonSingletons
                                    false          // allowEagerInit
                            );

                    // ③ 过滤掉祖先容器里的同名 Bean，只打印属于当前容器的
                    Arrays.stream(ctx.getBeanNamesForType(SimpleUrlHandlerMapping.class))
                            .forEach(name -> {
                                SimpleUrlHandlerMapping bean = map.get(name);
                                System.out.printf("   %-35s -> %s%n",
                                        name, bean);
                            });

                    ctx = ctx.getParent();   // ④ 向上爬
                    level++;
                }
                System.out.println("==========================================================\n");
            }
        };
    }
}
