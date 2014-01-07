package be.pirlewiet.registrations.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import be.occam.utils.spring.configuration.ConfigurationProfiles;

@Configuration
public class PirlewietApplicationConfigForTest {
	
	@Configuration
	@Profile( { ConfigurationProfiles.TEST } )

	// @EnableJpaRepositories(value="be.kuleuven.toledo.extern.infrastructure.repository.iam")
	public static class DbConfigForTest {
		
		@Bean(destroyMethod="shutdown")
		public EmbeddedDatabase dataSource(){
			return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.HSQL)
				.setName("pirlewiet-" + ConfigurationProfiles.activeProfile() + "-" + System.currentTimeMillis() )
				.build();
		}
		
		@Bean
		public HibernateJpaVendorAdapter vendorAdapter(){
			HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
			vendorAdapter.setDatabase(Database.HSQL);
			// vendorAdapter.setGenerateDdl(true);
			vendorAdapter.setShowSql(true);
			vendorAdapter.getJpaPropertyMap().put( "hibernate.hbm2ddl.auto", "create-drop" );
			vendorAdapter.getJpaPropertyMap().put( "hibernate.hbm2ddl.import_files", "import.sql" );
			
			return vendorAdapter;
		}
		
	}
	
}