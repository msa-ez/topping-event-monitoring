path: /src/main/java/{{options.package}}/config
---
package {{options.package}}.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
@WebFilter("/*")  // 모든 경로에 대해 필터 적용
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 작업 (필요한 경우 사용)
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // CORS 헤더 설정
        response.setHeader("Access-Control-Allow-Origin", {{#setOrigin}}{{/setOrigin}});
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // OPTIONS 요청에 대해 200 OK 상태 반환
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        // 필터 종료 시 필요한 작업 (필요한 경우 사용)
    }
}

<function>
window.$HandleBars.registerHelper('setOrigin', function () {
    const host = window.location.host;
    return '"https://' + host + '"';
});
</function>