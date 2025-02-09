package cn.izualzhy.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        System.out.println("Request: " + method + " " + requestURL +
                           (queryString != null ? "?" + queryString : ""));
        return true;
    }
}
