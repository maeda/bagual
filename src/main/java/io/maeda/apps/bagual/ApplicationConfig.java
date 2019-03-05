package io.maeda.apps.bagual;

import io.maeda.apps.bagual.interceptors.PhishingCheckingInterceptor;
import io.maeda.apps.bagual.interceptors.RedirectInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;

@EnableAsync
@Configuration
public class ApplicationConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(RedirectInterceptor interceptor, PhishingCheckingInterceptor phishingCheckingInterceptor) {
        return new WebMvcConfigurer(){
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(phishingCheckingInterceptor);
                registry.addInterceptor(interceptor);
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/{alias}+").setViewName("index.html");
                registry.addViewController("/warning").setViewName("warning.html");
            }
        };
    }
}
