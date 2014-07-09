package be.pirlewiet.registrations.application.config;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import be.occam.utils.spring.configuration.ConfigurationProfiles;
import be.pirlewiet.registrations.model.SecretariaatsMedewerker;
import be.pirlewiet.registrations.repositories.SecretariaatsMedewerkerRepository;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="be.pirlewiet.registrations.repositories")
public class PirlewietApplicationConfig {
	
	final static Logger logger
		= LoggerFactory.getLogger( PirlewietApplicationConfig.class );

	final static String BASE_PKG = "be.pirlewiet.registrations";
	
	static class propertiesConfigurer {
		
		@Bean
		@Scope("singleton")
		public static PropertySourcesPlaceholderConfigurer propertiesConfig() {
			return new PropertySourcesPlaceholderConfigurer();
		}
		
	}
	
	@Configuration
	// @ComponentScan(basePackages="be.pirlewiet.registrations")
	@ImportResource( "classpath:/META-INF/applicationContext.xml" )
	public static class XmlConfig {
		
	}
	
	@Configuration
	public static class BeansConfig {
		
		@Bean
		SecretariaatsMedewerker secretariaatsMedewerker( SecretariaatsMedewerkerRepository secretariaatsmedewerkerRepository ) {
			
			List<SecretariaatsMedewerker> list
				= secretariaatsmedewerkerRepository.findAll();
			
			if ( list.isEmpty() ) {
				return new SecretariaatsMedewerker();
			}
			else {
				return list.get( 0 );
			}
			
		}
		
	}
	
	
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
	*/
	
	@Configuration
	// @EnableJpaRepositories(value="be.kuleuven.toledo.extern.infrastructure.repository.iam", entityManagerFactoryRef="iamLocalContainerEntityManagerFactoryBean", transactionManagerRef="iamTransactionManager")
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
	
	@Configuration
	@Profile( { ConfigurationProfiles.PRODUCTION, ConfigurationProfiles.DEV } )
	// @EnableJpaRepositories(value="be.kuleuven.toledo.extern.infrastructure.repository.iam", entityManagerFactoryRef="iamLocalContainerEntityManagerFactoryBean", transactionManagerRef="iamTransactionManager")
	public static class DbConfigForProduction {
	
		@Bean
		public MysqlDataSource dataSource() {
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/pirlewiet");
			dataSource.setUser("root");
			dataSource.setPassword("root");
			return dataSource;
		}
		
		@Bean
		public HibernateJpaVendorAdapter vendorAdapter(){
			HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
			vendorAdapter.setDatabase(Database.MYSQL);
			return vendorAdapter;
		}
		
	}
	
}