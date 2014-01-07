package be.pirlewiet.registrations.application.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class MvcConfig {
	
	@Configuration
	@ComponentScan( basePackages="be.pirlewiet.registrations.view.controllers" )
	@ImportResource( "WEB-INF/dispatcher-servlet.xml" )
	public static class DispatcherConfig {
		
	}
		

}
