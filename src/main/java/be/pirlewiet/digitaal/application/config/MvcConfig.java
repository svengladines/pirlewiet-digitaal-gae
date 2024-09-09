package be.pirlewiet.digitaal.application.config;

import java.util.Locale;

import be.pirlewiet.digitaal.web.controller.page.pirlewiet.*;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import be.pirlewiet.digitaal.web.controller.api.CodesController;
import be.pirlewiet.digitaal.web.controller.GlobalControllerExceptionHandler;
import be.pirlewiet.digitaal.web.controller.api.PingController;
import be.pirlewiet.digitaal.web.controller.api.ScenarioController;
import be.pirlewiet.digitaal.web.controller.api.ApplicationController;
import be.pirlewiet.digitaal.web.controller.api.ApplicationsController;
import be.pirlewiet.digitaal.web.controller.api.CodeRequestsController;
import be.pirlewiet.digitaal.web.controller.api.EnrollmentController;
import be.pirlewiet.digitaal.web.controller.api.EnrollmentsController;
import be.pirlewiet.digitaal.web.controller.api.MyOrganisationController;
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
	public static class ApiControllerConfig {

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
	
	/*
	@Bean
	MultipartResolver multipartResolver() {
		
		/*
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		
		resolver.setMaxInMemorySize( 2002400 );
		
		
		GMultipartResolver resolver
			= new GMultipartResolver();
		
		return resolver;
		
	}
	*/
	}
	
}
