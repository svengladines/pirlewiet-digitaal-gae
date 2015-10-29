package be.pirlewiet.registrations.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import be.pirlewiet.registrations.web.controllers.CodeRequestsController;
import be.pirlewiet.registrations.web.controllers.CodesController;
import be.pirlewiet.registrations.web.controllers.DeelnemerController;
import be.pirlewiet.registrations.web.controllers.EnrollmentController;
import be.pirlewiet.registrations.web.controllers.EnrollmentsController;
import be.pirlewiet.registrations.web.controllers.OrganisationController;
import be.pirlewiet.registrations.web.controllers.OrganisationsController;
import be.pirlewiet.registrations.web.controllers.PDController;
import be.pirlewiet.registrations.web.controllers.PageController;
import be.pirlewiet.registrations.web.controllers.PingController;
import be.pirlewiet.registrations.web.controllers.ScenarioController;

@Configuration
@EnableWebMvc
public class MvcConfig {
	
	@Configuration
	public static class DispatcherConfig {
		
		@Bean
		public InternalResourceViewResolver internalResourceViewResolver() {
			InternalResourceViewResolver resolver
				= new InternalResourceViewResolver();
			resolver.setPrefix( "/WEB-INF/jsp/" );
			resolver.setSuffix( ".jsp" );
			return resolver;
		}
		
	}
	
	/*
	@Bean
	MultipartResolver multipartResolver() {
		
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		
		resolver.setMaxInMemorySize( 1024000 );
		
		return resolver;
		
	}
	*/
	
	@Configuration
	public static class ControllerConfig {
		
		@Bean
		public EnrollmentsController inschrijvingenController() {
			
			return new EnrollmentsController();
			
		}
		
		@Bean
		public EnrollmentController inschrijvingController() {
			
			return new EnrollmentController();
			
		}
		
		@Bean
		public DeelnemerController deelnemerController() {
			
			return new DeelnemerController();
			
		}
		
		@Bean
		public PageController pageController() {
			
			return new PageController();
			
		}
		
		@Bean
		public CodesController codesController() {
			
			return new CodesController();
			
		}
		
		@Bean
		public OrganisationsController organisatiesController() {
			
			return new OrganisationsController();
			
		}
		
		@Bean
		public OrganisationController organisatieController() {
			
			return new OrganisationController();
			
		}
		
		@Bean
		public CodeRequestsController codeRequestsController() {
			return new CodeRequestsController();
		}
		
		@Bean
		public ScenarioController scenarioController() {
			
			return new ScenarioController();
			
		}
		
		@Bean
		public PingController pingController() {
			
			return new PingController();
			
		}
		
		@Bean
		public PDController pdController() {
			
			return new PDController();
			
		}
		
		
	}
	
	@Configuration
	public static class FormatConfig {
		
		@Bean
		DateFormatter dateFormatter() {
			
			DateFormatter dateFormatter
				= new DateFormatter();
			
			dateFormatter.setPattern("dd/MM/yyyy");
			
			return dateFormatter;
			
		}
	}
		

}
