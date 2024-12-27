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
import org.springframework.format.datetime.DateFormatter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

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
import be.pirlewiet.digitaal.domain.people.Secretary;
import be.pirlewiet.digitaal.domain.scenario.DeleteOldEntitiesScenario;
import be.pirlewiet.digitaal.domain.scenario.InjectProductionDataScenario;
import be.pirlewiet.digitaal.domain.scenario.ObjectifyScenario;
import be.pirlewiet.digitaal.domain.scenario.SetEnrollmentHolidayNamesScenario;
import be.pirlewiet.digitaal.domain.scenario.UnifyEnrollmentHolidaysScenario;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.domain.service.PersonService;
import be.pirlewiet.digitaal.domain.service.QuestionAndAnswerService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.repository.AddressRepository;
import be.pirlewiet.digitaal.repository.ApplicationRepository;
import be.pirlewiet.digitaal.repository.EnrollmentRepository;
import be.pirlewiet.digitaal.repository.HolidayRepository;
import be.pirlewiet.digitaal.repository.OrganisationRepository;
import be.pirlewiet.digitaal.repository.PersonRepository;
import be.pirlewiet.digitaal.repository.QuestionAndAnswerRepository;
import be.pirlewiet.digitaal.repository.impl.AddressRepositoryObjectify;
import be.pirlewiet.digitaal.repository.impl.ApplicationRepositoryObjectify;
import be.pirlewiet.digitaal.repository.impl.EnrollmentRepositoryObjectify;
import be.pirlewiet.digitaal.repository.impl.HolidayRepositoryObjectify;
import be.pirlewiet.digitaal.repository.impl.Objectify;
import be.pirlewiet.digitaal.repository.impl.OrganisationRepositoryObjectify;
import be.pirlewiet.digitaal.repository.impl.PersonRepositoryObjectify;
import be.pirlewiet.digitaal.repository.impl.QuestionAndAnswerRepositoryObjectify;
import be.pirlewiet.digitaal.web.util.ExcelImporter;
import be.pirlewiet.digitaal.web.util.PirlewietUtil;


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
		ApplicationManager applicationManager(@Value("${pirlewiet.currentYear}") int currentYear) {
			return new ApplicationManager(currentYear);
		}

		// can use 'real' javasender, it is stubbed by GAE
		@Bean
		public JavaMailSender javaMailSender() {
			JavaMailSenderImpl sender
					= new JavaMailSenderImpl();
			return sender;
		}

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
		public Objectify objectify() {
			ObjectifyService.init();
			logger.info("objectify service initialized");
			return new Objectify();
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
}