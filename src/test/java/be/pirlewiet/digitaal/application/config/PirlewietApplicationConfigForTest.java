package be.pirlewiet.digitaal.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import be.occam.utils.spring.configuration.ConfigurationProfiles;
import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.jtests.DevData;
import be.pirlewiet.digitaal.jtests.TestData;

@Configuration
public class PirlewietApplicationConfigForTest {
	
	@Profile( { ConfigurationProfiles.TEST } )
	public static class DbConfigForTest {
		
		//@Bean(destroyMethod="shutdown")
		public EmbeddedDatabase dataSource(){
			return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.HSQL)
				.setName("pirlewiet-" + ConfigurationProfiles.activeProfile() + "-" + System.currentTimeMillis() )
				.build();
		}
		
		//@Bean
		public HibernateJpaVendorAdapter vendorAdapter(){
			HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
			vendorAdapter.setDatabase(Database.HSQL);
			// vendorAdapter.setGenerateDdl(true);
			vendorAdapter.setShowSql(true);
			vendorAdapter.getJpaPropertyMap().put( "hibernate.hbm2ddl.auto", "create-drop" );
			// vendorAdapter.getJpaPropertyMap().put( "hibernate.hbm2ddl.import_files", "import.sql" );
			
			return vendorAdapter;
		}
		
		@Bean
		@Lazy(false)
		public TestData testData() {
			
			return new TestData();
			
		}
		
	}
	
	@Configuration
	@Profile( { ConfigurationProfiles.DEV } ) 
	public static class ConfigForDevelopment {
		
		@Bean
		@Lazy(false)
		public DevData devData( LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean ) {
			
			return new DevData();
			
		}
		
		@Bean
		public HeadQuarters secretariaat( ) {
			
			return new HeadQuarters( "sven.gladines@gmail.com" );
			
		}
		
		
	}
	
	
	
}