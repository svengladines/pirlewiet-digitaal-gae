package be.pirlewiet.digitaal.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import be.occam.utils.spring.configuration.ConfigurationProfiles;
import be.pirlewiet.digitaal.web.util.DataGuard;
import be.pirlewiet.digitaal.web.util.NoopGuard;

@Configuration
@Profile(ConfigurationProfiles.PRODUCTION)
public class PirlewietAppEngineConfig {
	
	final static String JPA_PKG = "be.pirlewiet.digitaal";
	
	@Configuration
	static class AppEngineConfig {
		
		@Bean
		DataGuard dataGuard() {
			
			return new NoopGuard();
			
		}
		
	}
	
}
