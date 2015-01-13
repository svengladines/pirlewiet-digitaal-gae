package be.pirlewiet.registrations.application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import be.occam.utils.spring.configuration.ConfigurationProfiles;
import be.pirlewiet.registrations.domain.BuitenWipper;
import be.pirlewiet.registrations.domain.CodeMan;
import be.pirlewiet.registrations.domain.Detacher;
import be.pirlewiet.registrations.domain.Intaker;
import be.pirlewiet.registrations.domain.OrganisationManager;
import be.pirlewiet.registrations.domain.PostBode;
import be.pirlewiet.registrations.domain.SecretariaatsMedewerker;
import be.pirlewiet.registrations.domain.scenarios.SetOrganisationsUuidScenario;
import be.pirlewiet.registrations.model.Vragen;

@Configuration
@EnableTransactionManagement
public class PirlewietApplicationConfig {
	
	final static Logger logger
		= LoggerFactory.getLogger( PirlewietApplicationConfig.class );

	final static String BASE_PKG = "be.pirlewiet.registrations";
	
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
		SecretariaatsMedewerker secretariaatsMedewerker( ) {
			
			return new SecretariaatsMedewerker( "pirlewiet.digitaal@gmail.com" );
			
		}
		
		@Bean
		BuitenWipper buitenWipper() {
			
			return new BuitenWipper();
			
		}
		
		@Bean
		CodeMan codeMan() {
			
			return new CodeMan();
			
		}
		
		@Bean
		PostBode postBode() {
			
			return new PostBode();
			
		}
		
		@Bean
		OrganisationManager organisationManager() {
			
			return new OrganisationManager();
			
		}
		
		@Bean
		Intaker intaker() {
			
			return new Intaker();
			
		}
		
		@Bean
		Detacher detacher() {
			
			return new Detacher();
			
		}
		
		
		@Bean
		public Vragen vragen () {
			return new Vragen();
		}
		
		@Bean
		public JavaMailSender javaMailSender () {
			JavaMailSenderImpl sender
				= new JavaMailSenderImpl();
			return sender;
		}
		
		@Bean
		public SetOrganisationsUuidScenario setOrganisationsUuidScenario() {
			return new SetOrganisationsUuidScenario();
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