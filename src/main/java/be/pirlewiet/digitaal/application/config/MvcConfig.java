package be.pirlewiet.digitaal.application.config;

import java.util.Locale;

import org.gmr.web.multipart.GMultipartResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import be.pirlewiet.digitaal.web.controller.CodesController;
import be.pirlewiet.digitaal.web.controller.GlobalControllerExceptionHandler;
import be.pirlewiet.digitaal.web.controller.PingController;
import be.pirlewiet.digitaal.web.controller.api.ApplicationController;
import be.pirlewiet.digitaal.web.controller.api.ApplicationsController;
import be.pirlewiet.digitaal.web.controller.api.CodeRequestsController;
import be.pirlewiet.digitaal.web.controller.api.EnrollmentController;
import be.pirlewiet.digitaal.web.controller.api.EnrollmentsController;
import be.pirlewiet.digitaal.web.controller.api.OrganisationController;
import be.pirlewiet.digitaal.web.controller.api.OrganisationsController;
import be.pirlewiet.digitaal.web.controller.page.ApplicationPageController;
import be.pirlewiet.digitaal.web.controller.page.ApplicationPageModalsController;
import be.pirlewiet.digitaal.web.controller.page.ApplicationsPageController;
import be.pirlewiet.digitaal.web.controller.page.LogoutPageController;
import be.pirlewiet.digitaal.web.controller.page.OrganisationPageController;
import be.pirlewiet.digitaal.web.controller.page.OrganisationRegistrationPageController;
import be.pirlewiet.digitaal.web.controller.page.OrganisationsPageController;
import be.pirlewiet.digitaal.web.controller.page.StartPageController;
import be.pirlewiet.digitaal.web.controller.page.UploadOrganisationsPageController;
import be.pirlewiet.digitaal.web.controller.page.pirlewiet.PirlewietApplicationsPageController;
import be.pirlewiet.digitaal.web.controller.page.pirlewiet.PirlewietEnrollmentPageController;
import be.pirlewiet.digitaal.web.controller.page.pirlewiet.PirlewietOrganisationPageController;
import be.pirlewiet.digitaal.web.controller.page.pirlewiet.PirlewietOrganisationsPageController;

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
		public OrganisationRegistrationPageController organisationRegistrationPageController() {
			return new OrganisationRegistrationPageController();
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
		public OrganisationsPageController organisationsPageController() {
			return new OrganisationsPageController();
		}
		
		@Bean
		public LogoutPageController logOutPageController() {
			return new LogoutPageController();
		}
		
		@Bean
		public UploadOrganisationsPageController uploadOrganisationsPageController() {
			return new UploadOrganisationsPageController();
		}
		
		/*
		 * Pirlewiet
		 * 
		 */
		@Bean
		public PirlewietOrganisationsPageController pirlewietOrganisationsPageController() {
			return new PirlewietOrganisationsPageController();
		}
		
		@Bean
		public PirlewietOrganisationPageController pirlewietOrganisationPageController() {
			return new PirlewietOrganisationPageController();
		}
		
		@Bean
		public PirlewietApplicationsPageController pirlewietApplicationsPageController() {
			return new PirlewietApplicationsPageController();
		}
		
		@Bean
		public PirlewietEnrollmentPageController pirlewietEnrollmentPageController() {
			return new PirlewietEnrollmentPageController();
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
		public ApplicationsController applicationsController() {
			return new ApplicationsController();
		}
		
		@Bean
		public ApplicationController applicationController() {
			return new ApplicationController();
		}
		
		@Bean
		public EnrollmentsController enrollmentsController() {
			
			return new EnrollmentsController();
			
		}
		
		@Bean
		public EnrollmentController enrollmentController() {
			
			return new EnrollmentController();
			
		}
		
		@Bean
		public CodeRequestsController codeRequestsController() {
			return new CodeRequestsController();
		}
		
		/*
		
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
		public ScenarioController scenarioController() {
			
			return new ScenarioController();
			
		}
		
		@Bean
		public PDController pdController() {
			
			return new PDController();
			
		}
		*/
		
		@Bean
		GlobalControllerExceptionHandler globalControllerExceptionHandler() {
			return new GlobalControllerExceptionHandler();
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
	
	@Bean
	MultipartResolver multipartResolver() {
		
		/*
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		
		resolver.setMaxInMemorySize( 2002400 );
		*/
		GMultipartResolver resolver
			= new GMultipartResolver();
		
		return resolver;
		
	}
	
}
