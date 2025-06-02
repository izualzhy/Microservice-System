//package cn.izualzhy.ws.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 确保 /ws 不作为静态资源路径被处理
//        registry.addResourceHandler("/ws/**").addResourceLocations("none:");
//    }
//}
