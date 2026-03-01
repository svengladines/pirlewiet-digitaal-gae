package be.pirlewiet.digitaal.domain.scenario;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.ApplicationManager;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.people.EnrollmentManager;
import be.pirlewiet.digitaal.domain.people.PersonManager;
import be.pirlewiet.digitaal.domain.service.*;
import be.pirlewiet.digitaal.model.*;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.web.dto.PersonDTO;
import be.pirlewiet.digitaal.web.util.DataGuard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static be.occam.utils.javax.Utils.set;

/**
 * Scenario to force-sync all participants for submitted applications
 */
@Component
public class TouchMissingSalesforceContactsScenario extends Scenario {
	
	protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

	protected final List<EnrollmentStatus.Value> ENROLLMENT_STATUSES_TO_PROCESS = List.of(
			EnrollmentStatus.Value.TRANSIT,
			EnrollmentStatus.Value.ACCEPTED,
			EnrollmentStatus.Value.CONFIRMED,
			EnrollmentStatus.Value.REJECTED,
			EnrollmentStatus.Value.VISIT,
			EnrollmentStatus.Value.WAITINGLIST
	);

	@Autowired
	ApplicationManager applicationManager;
	
	@Autowired
    EnrollmentManager enrollmentmanager;
	
	@Autowired
	DataGuard dataGuard;
	
	public TouchMissingSalesforceContactsScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute( String... parameters ) {
		
		logger.info( "execute scenario [{}] ... ", this.getClass().getName() );
		
		this.applicationManager.findAll()
			.stream()
			.peek(a -> logger.info("application [{}]; status is [{}]", a.getUuid(), a.getStatus().getValue()))
			.filter(a ->ApplicationStatus.Value.SUBMITTED.equals(a.getStatus().getValue()))
			.forEach(application -> {
				this.enrollmentmanager.findByApplicationUuid(application.getUuid())
					.stream()
					.peek(e -> logger.info("enrollment [{}] has status [{}]", e.getUuid(), e.getStatus().getValue()))
					.filter(e -> ENROLLMENT_STATUSES_TO_PROCESS.contains(e.getStatus().getValue()))
					.forEach(enrollment -> {
						this.enrollmentmanager.touch(enrollment,application);
					});
			});
	}

}
