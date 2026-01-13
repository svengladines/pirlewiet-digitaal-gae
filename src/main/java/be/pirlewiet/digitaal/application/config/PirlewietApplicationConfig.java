package be.pirlewiet.digitaal.application.config;

import be.pirlewiet.digitaal.domain.HeadQuarters;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.scenario.*;
import be.pirlewiet.digitaal.domain.service.*;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.*;
import be.pirlewiet.digitaal.repository.impl.*;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.appengine.repackaged.io.grpc.ManagedChannelBuilder;
import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.format.datetime.DateFormatter;


@Configuration
public class PirlewietApplicationConfig {

	final static Logger logger
			= LoggerFactory.getLogger(PirlewietApplicationConfig.class);

	final static String BASE_PKG = "be.pirlewiet.digitaal";

	public final static String EMAIL_ADDRESS = "pirlewiet.digitaal@gmail.com";

	@Configuration
	public static class ServiceConfig {

		@Bean
		DateFormatter dateFormatter() {

			DateFormatter dateFormatter
					= new DateFormatter();

			dateFormatter.setPattern("dd/MM/yyyy");

			return dateFormatter;

		}

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
		ApplicationManager applicationManager(@Value("${pirlewiet.year}") int currentYear) {
			return new ApplicationManager(currentYear);
		}

		// can use 'real' javasender, it is stubbed by GAE
		@Bean
		public Organisation pDiddy() {

			Organisation pDiddy
					= new Organisation();

			pDiddy.setEmail(PirlewietUtil.PDIDDY_EMAIL);
			pDiddy.setUuid(PirlewietUtil.PDIDDY_ID);
			pDiddy.setCode(PirlewietUtil.PDIDDY_CODE);

			return pDiddy;
		}
		
		/*
		@Bean
		public ScenarioRunner scenarioRunner( SetEnrollmentHolidayNamesScenario setEnrollmentHolidayNamesScenario ) {
			// return new ScenarioRunner( setEnrollmentHolidayNamesScenario );
			return new ScenarioRunner( );
		}
		*/
	}

	@Configuration
	public static class ScenarioConfig {

		@Bean
		SetEnrollmentHolidayNamesScenario setEnrollmentHolidayNamesScenario() {
			return new SetEnrollmentHolidayNamesScenario();
		}

		@Bean
		InjectProductionDataScenario injectProductionDataScenario() {
			return new InjectProductionDataScenario();
		}

		@Bean
		DeleteOldEntitiesScenario deleteOldEntitiesScenario() {
			return new DeleteOldEntitiesScenario();
		}

		@Bean
		UnifyEnrollmentHolidaysScenario unifyEnrollmentHolidaysScenario() {
			return new UnifyEnrollmentHolidaysScenario();
		}

		@Bean
		ObjectifyScenario objectifyScenario() {
			return new ObjectifyScenario();
		}

	}

	@Configuration
	public static class RepositoryConfig {

		@Bean
		@Lazy(false)
		public Objectify objectify(Datastore datastore) {
			ObjectifyFactory factory = new ObjectifyFactory(datastore);
			ObjectifyService.init(factory);
			logger.info("objectify service initialized");
			return new Objectify(factory);
		}

		@Bean
		PersonRepository personRepository() {
			return new PersonRepositoryObjectify();
		}

		@Bean
		AddressRepository addressRepository() {
			return new AddressRepositoryObjectify();
		}

		@Bean
		ApplicationRepository applicationRepository() {
			return new ApplicationRepositoryObjectify();
		}

		@Bean
		EnrollmentRepository enrollmentRepository() {
			return new EnrollmentRepositoryObjectify();
		}

		@Bean
		HolidayRepository holidayRepository() {
			return new HolidayRepositoryObjectify();
		}

		@Bean
		OrganisationRepository organisationRepository() {
			return new OrganisationRepositoryObjectify();
		}

		@Bean
		QuestionAndAnswerRepository questionAndAnswerRepository() {
			return new QuestionAndAnswerRepositoryObjectify();
		}
	}

	@Configuration
	static class HeadQuarterConfig {

		@Bean
		public HeadQuarters headQuarters(@Value("${pirlewiet.email}")String email) {
			return new HeadQuarters(email);
		}

	}

	@Configuration
	@Profile("dev")
	static class DevConfig {

		@Bean
		public Datastore datastoreOptions(@Value("${gcp.project-id}")String projectId,@Value("${gcp.datastore.host}") String host) {
			DatastoreOptions.Builder builder = DatastoreOptions.newBuilder()
					.setProjectId(projectId)
					.setHost(host)
					.setCredentials(NoCredentials.getInstance());
			return builder.build().getService();
		}

	}


}