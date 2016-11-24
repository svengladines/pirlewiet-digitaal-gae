package be.pirlewiet.digitaal.application.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import be.pirlewiet.digitaal.web.controller.CodesController;
import be.pirlewiet.digitaal.web.controller.OrganisationController;
import be.pirlewiet.digitaal.web.controller.OrganisationsController;
import be.pirlewiet.digitaal.web.controller.PingController;
import be.pirlewiet.digitaal.web.controller.api.ApplicationController;
import be.pirlewiet.digitaal.web.controller.page.ApplicationPageController;
import be.pirlewiet.digitaal.web.controller.page.ApplicationPageModalsController;
import be.pirlewiet.digitaal.web.controller.page.ApplicationsPageController;
import be.pirlewiet.digitaal.web.controller.page.LogoutPageController;
import be.pirlewiet.digitaal.web.controller.page.OrganisationPageController;
import be.pirlewiet.digitaal.web.controller.page.StartPageController;

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
		
		@Bean
		public MessageSource messageSource() {
			
			ReloadableResourceBundleMessageSource  messageSource = new ReloadableResourceBundleMessageSource ();
			messageSource.setBasename( "classpath:pirlewiet-messages");
			//messageSource.setDefaultEncoding("utf-8");
			return messageSource;
			
		}
		
		@Bean
		public LocaleResolver localeResolver() {
			return new FixedLocaleResolver( Locale.forLanguageTag("nl") );
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
	public static class PageControllerConfig {
		
		@Bean
		public StartPageController pageController() {
			
			return new StartPageController();
			
		}
		
		@Bean
		public OrganisationPageController organisationPageController() {
			return new OrganisationPageController();
		}
		
		@Bean
		public ApplicationsPageController applicationsPageController() {
			return new ApplicationsPageController();
		}
		
		@Bean
		public ApplicationPageController applicationPageController() {
			return new ApplicationPageController();
		}
		
		@Bean
		public ApplicationPageModalsController applicationPageModalsController() {
			return new ApplicationPageModalsController();
		}
		
		@Bean
		public LogoutPageController logOutPageController() {
			return new LogoutPageController();
		}
		
	}
	
	@Configuration
	public static class ApiControllerConfig {
		
		@Bean
		public OrganisationsController organisationsController() {
			
			return new OrganisationsController();
			
		}
		
		@Bean
		public OrganisationController organisationController() {
			
			return new OrganisationController();
			
		}
		
		@Bean
		public PingController pingController() {
			
			return new PingController();
			
		}
		
		@Bean
		public CodesController codesController() {
			
			return new CodesController();
			
		}
		
		@Bean
		public ApplicationController applicationController() {
			return new ApplicationController();
		}
		
		/*
		
		@Bean
		public EnrollmentsController inschrijvingenController() {
			
			return new EnrollmentsController();
			
		}
		
		@Bean
		public ApplicationController inschrijvingController() {
			
			return new ApplicationController();
			
		}
		
		@Bean
		public EnrollmentController enrollmentController() {
			
			return new EnrollmentController();
			
		}
		
		@Bean
		public ParticipantController deelnemerController() {
			
			return new ParticipantController();
			
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
		public PDController pdController() {
			
			return new PDController();
			
		}
		*/
		
		
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
