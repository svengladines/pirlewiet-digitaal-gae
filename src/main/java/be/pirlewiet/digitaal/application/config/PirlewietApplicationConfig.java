package be.pirlewiet.digitaal.application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import be.occam.utils.ftp.FTPClient;
import be.occam.utils.spring.configuration.ConfigurationProfiles;
import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.Mapper;
import be.pirlewiet.digitaal.domain.Reducer;
import be.pirlewiet.digitaal.domain.Viewer;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.CodeMan;
import be.pirlewiet.digitaal.domain.people.Detacher;
import be.pirlewiet.digitaal.domain.people.HolidayManager;
import be.pirlewiet.digitaal.domain.people.MailMan;
import be.pirlewiet.digitaal.domain.people.OrganisationManager;
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.domain.q.QList;
import be.pirlewiet.digitaal.domain.scenario.ReadyToRockOneScenario;
import be.pirlewiet.digitaal.domain.scenario.ReadyToRockScenario;
import be.pirlewiet.digitaal.domain.scenario.SetQuestionsQIDScenario;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;

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
	
	}
	
	@Configuration
	public static class BeansConfig {
		
		@Bean
		Secretary secretariaatsMedewerker( ) {
			
			return new Secretary(  );
			
		}
		
		@Bean
		DoorMan buitenWipper() {
			
			return new DoorMan();
			
		}
		
		@Bean
		CodeMan codeMan() {
			
			return new CodeMan();
			
		}
		
		@Bean
		MailMan postBode() {
			
			return new MailMan();
			
		}
		
		@Bean
		OrganisationManager organisationManager() {
			
			return new OrganisationManager();
			
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
		Mapper mapper() {
			
			return new Mapper();
			
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
		
		@Bean
		ConfiguredVakantieRepository configuredVakantieRepository() {
			return new ConfiguredVakantieRepository();
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