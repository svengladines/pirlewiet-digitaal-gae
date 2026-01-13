package be.pirlewiet.digitaal.application.config;

import be.occam.utils.spring.config.ConfigurationProfiles;
import be.pirlewiet.digitaal.web.util.DataGuard;
import be.pirlewiet.digitaal.web.util.NoopGuard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
