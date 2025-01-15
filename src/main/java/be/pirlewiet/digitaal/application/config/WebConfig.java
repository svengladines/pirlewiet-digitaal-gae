package be.pirlewiet.digitaal.application.config;

import be.pirlewiet.digitaal.web.filter.CookieFilter;
import com.googlecode.objectify.ObjectifyService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

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
        registrationBean.addUrlPatterns("/api/*","/organisation/*", "/referenced/*", "/pirlewiet/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        return viewResolver;
    }

}
