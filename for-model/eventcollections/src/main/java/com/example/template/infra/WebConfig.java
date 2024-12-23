path: /src/main/java/{{options.package}}/infra
---
package {{options.package}}.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins({{#setOrigin}}{{/setOrigin}})
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

<function>
window.$HandleBars.registerHelper('setOrigin', function () {
    const host = window.location.host;
    return '"https://' + host + '"';
});
</function>