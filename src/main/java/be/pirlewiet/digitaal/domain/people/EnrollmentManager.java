package be.pirlewiet.digitaal.domain.people;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.pirlewiet.digitaal.domain.q.QuestionSheet;
import be.pirlewiet.digitaal.model.Application;
import be.pirlewiet.digitaal.model.Enrollment;
import be.pirlewiet.digitaal.model.EnrollmentStatus;
import be.pirlewiet.digitaal.model.Holiday;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.repositories.EnrollmentRepository;

import com.google.appengine.api.datastore.KeyFactory;

/*
 * 
 * 
 */
public class EnrollmentManager {

	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Resource
	protected EnrollmentRepository enrollmentRepository;
	
	@Resource
	protected QuestionAndAnswerManager questionAndAnswerManager;
	
	@Resource
	protected HolidayManager holidayManager;
	
	public EnrollmentManager( ) {
	}
	
	public List<Enrollment> findByApplicationUuid( String applicationUuid ) {
		
		List<Enrollment> enrollments
			= this.enrollmentRepository.findByApplicationUuid( applicationUuid );
		
		// TODO, sort ...
		
		return enrollments;
		
	}
	
	public Enrollment findOneByUuid( String uuid ) {
		return this.enrollmentRepository.findByUuid( uuid );
	}
	
	public Enrollment create( Enrollment toCreate ) {
		
		Enrollment created
			= this.enrollmentRepository.saveAndFlush( toCreate );
		
		created.setUuid( KeyFactory.keyToString( created.getKey() ) );
		created = this.enrollmentRepository.saveAndFlush( created );
		
		List<QuestionAndAnswer> medical
			= QuestionSheet.template().getVragen( ).get( Tags.TAG_MEDIC );
		
		for ( QuestionAndAnswer qna : medical ) {
			qna.setEntityUuid( created.getUuid() );
			this.questionAndAnswerManager.create( qna );
		}
		
		List<QuestionAndAnswer> history
			= QuestionSheet.template().getVragen( ).get( Tags.TAG_HISTORY );
	
		for ( QuestionAndAnswer qna : history ) {
			qna.setEntityUuid( created.getUuid() );
			this.questionAndAnswerManager.create( qna );
		}
		
		return created;
		
	}


	public Enrollment update( Enrollment toUpdate, Enrollment update ) {
		
		// TODO
		toUpdate.setHolidayName( update.getHolidayName() );
		toUpdate.setHolidayUuid( update.getHolidayUuid() );
		if ( update.getStatus() != null ) {
			toUpdate.setStatus( update.getStatus() );
		}
		
		Enrollment updated
			= this.enrollmentRepository.saveAndFlush( toUpdate );
		
		return updated;
		
	}
	
	
	public Enrollment updateHolidays( String uuid, List<Holiday> holidays ) {
		
		logger.info("application.updateHolidays");
		
		Enrollment enrollment
			= this.findOneByUuid( uuid );
		
		if ( enrollment != null ) {
			
			StringBuilder uuids
				= new StringBuilder();
			
			StringBuilder names
				= new StringBuilder();
			
			for ( int i=0; i < holidays.size(); i++ ) {
				
				Holiday holiday = holidays.get( i );
				
				uuids.append( holiday.getUuid() );
				
				Holiday one
					= this.holidayManager.findOneByUuid( holiday.getUuid() );
				
				logger.info("found holiday [{}], adding...", one.getName() );
				
				names.append( one.getName() );
				
				if ( i < holidays.size() -1 ) {
					uuids.append(",");
					names.append(",");
				}
				
			}
			
			enrollment.setHolidayUuid( uuids.toString() );
			
			enrollment = this.enrollmentRepository.saveAndFlush( enrollment );
			
		}
		
		return enrollment;
	}
	
	public void delete( Enrollment enrollment ) {
		
		this.enrollmentRepository.delete( enrollment );
		
	}
	
	public Enrollment updateStatus( String enrollmentUUid, EnrollmentStatus newStatus ) {
		
		Enrollment toUpdate
			= this.findOneByUuid( enrollmentUUid );
		
		toUpdate.setStatus( newStatus );
		
		Enrollment updated
			= this.enrollmentRepository.saveAndFlush( toUpdate );
		
		return updated;
		
	}
	
	public Enrollment template() {
		
		Enrollment enrollment 
			= new Enrollment();
		
		return enrollment;
		
	}

	public Enrollment updateQList( String uuid, List<QuestionAndAnswer> qList ) {
		
		logger.info("enrollment.updateQList");
		
		Enrollment enrollment
			= this.findOneByUuid( uuid );
		
		if ( enrollment != null ) {
			
			for ( QuestionAndAnswer q : qList ) {
				
				QuestionAndAnswer one
					= this.questionAndAnswerManager.findOneByUuid( q.getUuid() );
				
				if ( one != null ) {
					this.questionAndAnswerManager.update( one,q );
				}
				
			}
						
		}
		
		return enrollment;
	}
	

}
 