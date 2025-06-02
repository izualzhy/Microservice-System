package cn.izualzhy.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Component
@WebFilter("/*")
@Log4j2
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 包装 request 以缓存 body
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        // 先继续执行后续过滤器链
        filterChain.doFilter(wrappedRequest, response);

        // 请求方法和 URI
        String method = wrappedRequest.getMethod();
        String uri = wrappedRequest.getRequestURI();
        String query = wrappedRequest.getQueryString();
        String fullUri = (query == null ? uri : uri + "?" + query);

        // 打印 headers
        Enumeration<String> headerNames = wrappedRequest.getHeaderNames();
        String headers = Collections.list(headerNames).stream()
                .map(name -> name + ": " + wrappedRequest.getHeader(name))
                .collect(Collectors.joining("; "));

        // 打印 body
        String body = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info("[Request] {} {}", method, fullUri);
        log.info("[Headers] {}", headers);
        if (!body.isBlank()) {
            log.info("[Body] {}", body);
        }
    }
}
