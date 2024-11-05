package be.pirlewiet.digitaal.application.config;

import be.pirlewiet.digitaal.web.filter.CookieFilter;
import com.googlecode.objectify.ObjectifyService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<ObjectifyService.Filter> objectifyFilter(){
        FilterRegistrationBean<ObjectifyService.Filter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new ObjectifyService.Filter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CookieFilter> cookieFilter(){
        FilterRegistrationBean<CookieFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new CookieFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }

}
