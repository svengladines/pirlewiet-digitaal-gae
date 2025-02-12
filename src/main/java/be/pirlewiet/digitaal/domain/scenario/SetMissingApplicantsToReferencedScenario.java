package be.pirlewiet.digitaal.domain.scenario;

import be.occam.utils.spring.web.Result;
import be.pirlewiet.digitaal.domain.people.DoorMan;
import be.pirlewiet.digitaal.domain.service.ApplicationService;
import be.pirlewiet.digitaal.domain.service.EnrollmentService;
import be.pirlewiet.digitaal.domain.service.HolidayService;
import be.pirlewiet.digitaal.domain.service.OrganisationService;
import be.pirlewiet.digitaal.model.Organisation;
import be.pirlewiet.digitaal.model.OrganisationType;
import be.pirlewiet.digitaal.web.dto.ApplicationDTO;
import be.pirlewiet.digitaal.web.dto.EnrollmentDTO;
import be.pirlewiet.digitaal.web.dto.HolidayDTO;
import be.pirlewiet.digitaal.web.dto.PersonDTO;
import be.pirlewiet.digitaal.web.util.DataGuard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static be.occam.utils.javax.Utils.isEmpty;

/*
* Set applicant for referenced applications for which it was not set during creation (was a bug)
* */
@Component
public class SetMissingApplicantsToReferencedScenario extends Scenario {
	
	protected final Logger logger = LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	OrganisationService organisationService;
	@Autowired
	ApplicationService applicationService;
	@Autowired
	DoorMan doorMan;
	@Autowired
	DataGuard dataGuard;
	
	public SetMissingApplicantsToReferencedScenario guard() {
    	this.dataGuard.guard();
    	return this;
    }

	@Override
	public void execute( String... parameters ) {
		
		logger.info( "execute scenario [{}] ... ", this.getClass().getName() );

		Organisation pirlewiet = this.doorMan.whoHasCode( "dig151" );
		
		this.organisationService.query(pirlewiet, OrganisationType.INDIVIDUAL).getObject().stream()
				.map(o -> o.getObject())
				.forEach(o -> {
					logger.info("Organisation [{}]; query applications", o.getName());
					Organisation organisation = Organisation.from(o);
					this.applicationService.query(organisation).getObject().stream()
							.map(r -> r.getObject())
							//S.filter(a -> isEmpty(a.getContactPersonUuid()))
							.forEach(a -> {
								logger.info("Applicationn [{}]; set applicant with name [{}]", a.getUuid(), o.getName());
								PersonDTO applicant = new PersonDTO();
								applicant.setEmail(o.getEmail());
								String oName = o.getName();
								applicant.setGivenName(oName.substring(0,oName.indexOf(" ")));
								applicant.setFamilyName(oName.substring(oName.indexOf(" ")+1));
								applicant.setPhone(o.getPhone());
								this.applicationService.updateApplicant(a.getUuid(),applicant);
							});
				});


		}
}
