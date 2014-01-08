package be.pirlewiet.registrations.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import be.occam.utils.spring.configuration.ConfigurationProfiles;

@Configuration
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
	@ComponentScan(basePackages="be.pirlewiet.registrations")
	@ImportResource( "classpath:/META-INF/applicationContext.xml" )
	public static class XmlConfig {
		
	}
	
	@Configuration
	@ImportResource( "classpath:/META-INF/applicationContext-security.xml" )
	public static class SecurityConfig {
		
	}
	
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
		public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource,HibernateJpaVendorAdapter vendorAdapter) {
			LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
			factory.setJpaVendorAdapter(vendorAdapter);
			factory.setPackagesToScan(BASE_PKG);
			factory.setDataSource(dataSource);
			factory.setPersistenceUnitName("pirlewietRegistrations");
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