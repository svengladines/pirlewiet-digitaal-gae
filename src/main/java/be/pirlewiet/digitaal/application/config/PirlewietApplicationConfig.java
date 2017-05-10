package be.pirlewiet.digitaal.application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import be.occam.utils.spring.configuration.ConfigurationProfiles;
import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.Mapper;
import be.pirlewiet.digitaal.domain.Reducer;
import be.pirlewiet.digitaal.domain.people.AddressManager;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.CodeMan;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.EnrollmentManager;
import be.pirlewiet.digitaal.domain.people.Excelsior;
import be.pirlewiet.digitaal.domain.people.HolidayManager;
import be.pirlewiet.digitaal.domain.people.MailMan;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.domain.people.PersonManager;
import be.pirlewiet.digitaal.domain.people.QuestionAndAnswerManager;
import be.pirlewiet.digitaal.domain.people.ScenarioRunner;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.domain.scenario.SetEnrollmentHolidayNamesScenario;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.domain.service.PersonService;
import be.pirlewiet.digitaal.domain.service.QuestionAndAnswerService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.util.ExcelImporter;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;
import be.pirlewiet.digitaal.web.util.ProductionData;

@Configuration
@EnableTransactionManagement
public class PirlewietApplicationConfig {
	
	final static Logger logger
		= LoggerFactory.getLogger( PirlewietApplicationConfig.class );

	final static String BASE_PKG = "be.pirlewiet.digitaal";
	
	public final static String EMAIL_ADDRESS = "pirlewiet.digitaal@gmail.com";
	
	static class propertiesConfigurer {
		
		@Bean
		@Scope("singleton")
		public static PropertySourcesPlaceholderConfigurer propertiesConfig() {
			return new PropertySourcesPlaceholderConfigurer();
		}
		
	}
	
	@Configuration
	@Profile({ConfigurationProfiles.PRODUCTION,ConfigurationProfiles.DEV})
	// @Import( PirlewietAppEngineConfig.class )
	static class RepositoryConfigForProduction {
		
		@Bean
		@Lazy( false )
		public ProductionData productionData() {
			return new ProductionData();
		}
		
	}
	
	@Configuration
	public static class ServiceConfig {
		
		@Bean
		OrganisationService organisationService() {
			return new OrganisationService();
		}
		
		@Bean
		ApplicationService applicationService() {
			return new ApplicationService();
		}
		
		@Bean
		EnrollmentService enrollmentService() {
			return new EnrollmentService();
		}
		
		@Bean
		HolidayService holidayService() {
			return new HolidayService();
		}
		
		@Bean
		PersonService personService() {
			return new PersonService();
		}
		
		@Bean
		QuestionAndAnswerService questionAndAnswerService() {
			return new QuestionAndAnswerService();
		}
		
	}
	
	@Configuration
	public static class PeopleConfig {
		
		@Bean 
		HeadQuarters headQuarters() {
			return new HeadQuarters( "info@pirlewiet.be" );
		}
		
		@Bean
		DoorMan doorMan() {
			
			return new DoorMan();
			
		}
		
		@Bean
		OrganisationManager organisationManager() {
			
			return new OrganisationManager();
			
		}
		
		@Bean
		CodeMan codeMan() {
			
			return new CodeMan();
			
		}
		
		@Bean
		AddressManager addressManager() {
			return new AddressManager();
		}
		
		@Bean
		ApplicationManager applicationManager(@Value("${pirlewiet.currentYear}") int currentYear ) {
			return new ApplicationManager( currentYear );
		}
		
		@Bean
		EnrollmentManager enrollmentManager() {
			return new EnrollmentManager();
		}
		
		@Bean
		HolidayManager holidayManager() {
			return new HolidayManager();
		}
		
		@Bean
		PersonManager personManager() {
			return new PersonManager();
		}
		
		@Bean
		QuestionAndAnswerManager questionAndAnswerManager() {
			return new QuestionAndAnswerManager();
		}
		
		@Bean
		Secretary secretary() {
			return new Secretary();
		}
		
		@Bean
		Reducer reducer() {
			return new Reducer();
		}
		
		@Bean
		MailMan mailMan() {
			
			return new MailMan();
			
		}
		
		@Bean
		Excelsior excelsior() {
			
			return new Excelsior();
			
		}
		
		@Bean
		Mapper mapper() {
			
			return new Mapper();
			
		}
		
		@Bean
		ExcelImporter excelImporter() {
			return new ExcelImporter();
		}
		
		// can use 'real' javasender, it is stubbed by GAE
		@Bean
		public JavaMailSender javaMailSender () {
			JavaMailSenderImpl sender
				= new JavaMailSenderImpl();
			return sender;
		}
		
		@Bean
		public Organisation pDiddy() {
			
			Organisation pDiddy
				= new Organisation();
			
			pDiddy.setEmail( PirlewietUtil.PDIDDY_EMAIL );
			pDiddy.setUuid( PirlewietUtil.PDIDDY_ID );
			pDiddy.setCode( PirlewietUtil.PDIDDY_CODE );
			
			return pDiddy;
		}
		
		@Bean
		public ScenarioRunner scenarioRunner( SetEnrollmentHolidayNamesScenario setEnrollmentHolidayNamesScenario ) {
			// return new ScenarioRunner( setEnrollmentHolidayNamesScenario );
			return new ScenarioRunner( );
		}
		
		/*
		@Bean
		Secretary secretary( ) {
			
			return new Secretary(  );
			
		}
		
		@Bean
		ApplicationManager intaker() {
			
			return new ApplicationManager();
			
		}
		
		@Bean
		Detacher detacher() {
			
			return new Detacher();
			
		}
		
		@Bean
		Viewer viewer() {
			
			return new Viewer();
			
		}
		
		@Bean
		Reducer reducer() {
			
			return new Reducer();
			
		}
		
		@Bean
		HolidayManager holidayManager() {
			
			return new HolidayManager();
			
		}
		
		@Bean
		public FTPClient ftpClient() {
			return new FTPClient( "94.198.164.46", "pirlewietbe", "d;giTaal.15");
		}
		
		@Bean
		public SetQuestionsQIDScenario setOrganisationsUuidScenario() {
			return new SetQuestionsQIDScenario();
		}
		
		@Bean
		public ReadyToRockScenario readyToRockScenario() {
			return new ReadyToRockScenario();
		}
		
		@Bean
		public ReadyToRockOneScenario readyToRockOneScenario() {
			return new ReadyToRockOneScenario();
		}
		
		*/
		
	}
	
	@Configuration
	public static class ScenarioConfig {
		
		@Bean 
		SetEnrollmentHolidayNamesScenario setEnrollmentHolidayNamesScenario() {
			return new SetEnrollmentHolidayNamesScenario();
		}
		
	}
	
	@Configuration
	@Profile({ConfigurationProfiles.PRODUCTION})
	static class ConfigForProduction {
	
		@Bean
		public HeadQuarters secretariaat( ) {
			
			return new HeadQuarters( "info@pirlewiet.be" );
			// return new HeadQuarters( "sven.gladines@gmail.com" );
			
		}
		
	}
	
	/*
	@Configuration
	@Profile( { ConfigurationProfiles.PRODUCTION } ) 
	public static class DataConfig {
		@Bean
		@Lazy(false)
		public ProductionData productionData( LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean ) {
			
			return new ProductionData();
			
		}
	}
	*/
	
	/*
	@Configuration
	@ImportResource( "classpath:/META-INF/applicationContext-security.xml" )
	public static class SecurityConfig {
		
		@Bean
		CredentialsService credentialsService() {
			return new CredentialsService();
		}
		
		@Bean
		CredentialsRepository credentialsRepository() {
			return new CredentialsRepository();
		}
		
	}
	
	@Configuration
	// @EnableJpaRepositories(value="be.kuleuven.toledo.extern.infrastructure.repository.iam", entityManagerFactoryRef="iamLocalContainerEntityManagerFactoryBean", transactionManagerRef="iamTransactionManager")
	@Profile(ConfigurationProfiles.TEST)
	public static class DbConfig {
	
		@Bean
		public PlatformTransactionManager transactionManager(EntityManagerFactory localContainerEntityManagerFactoryBean) {
			JpaTransactionManager transactionManager = new JpaTransactionManager();
			transactionManager.setEntityManagerFactory( localContainerEntityManagerFactoryBean);
			return transactionManager;
		}
		
		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,HibernateJpaVendorAdapter vendorAdapter) {
			LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
			factory.setJpaVendorAdapter(vendorAdapter);
			factory.setPackagesToScan(BASE_PKG);
			factory.setDataSource(dataSource);
			factory.setPersistenceUnitName("pirlewiet-registrations");
			factory.afterPropertiesSet();
			return factory;
		}
		
	}
	*/
	
}