package be.pirlewiet.digitaal.domain.people;

import static be.occam.utils.javax.Utils.isEmpty;
import static be.occam.utils.javax.Utils.list;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.QuestionType;
import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.repository.EnrollmentRepository;

@Component
public class Secretary {
	
	protected final Logger logger
		= LoggerFactory.getLogger( this.getClass() );

	@Autowired
    public Secretary( ) {
    }
    
  public Result<List<Result<QuestionAndAnswer>>> checkApplicationQuestionList( String applicationUUID, List<QuestionAndAnswer> list ) {
    	
	  	Result<List<Result<QuestionAndAnswer>>> result
    		= new Result<List<Result<QuestionAndAnswer>>>();
	  	
    	result.setValue( Result.Value.OK );
	
    	// check list
    	List<Result<QuestionAndAnswer>> individualResults 
			= this.areAllMandatoryQuestionsAnswered( applicationUUID, list, Tags.TAG_APPLICATION );
    	
    	boolean allOK = true;
		boolean allNOK = true;
		
    	for ( Result<QuestionAndAnswer> individualResult : individualResults ) {
    		
	    	if ( ! Result.Value.OK.equals( individualResult.getValue() ) ) {
	    		allOK = false;
	    	}
	    	else {
	    		allNOK = false;
	    	}
	    	
    	}
    	
    	if ( !allOK ) {
    		
    		result.setValue( allNOK ? Result.Value.NOK : Result.Value.PARTIAL );
    		
    	}
    	
    	result.setObject( individualResults );
	    	
    	return result;
    	
    }
  
  public Result<List<Result<QuestionAndAnswer>>> checkEnrollmentQuestionList( String enrollmentUUID, List<QuestionAndAnswer> list, String tag ) {
  	
	Result<List<Result<QuestionAndAnswer>>> result
  		= new Result<List<Result<QuestionAndAnswer>>>();
	  	
  	result.setValue( Result.Value.OK );
	
  	// check list
  	List<Result<QuestionAndAnswer>> individualResults 
			= this.areAllMandatoryQuestionsAnswered( enrollmentUUID, list, tag );
  	
  	boolean allOK = true;
		boolean allNOK = true;
		
  	for ( Result<QuestionAndAnswer> individualResult : individualResults ) {
  		
	    	if ( ! Result.Value.OK.equals( individualResult.getValue() ) ) {
	    		allOK = false;
	    	}
	    	else {
	    		allNOK = false;
	    	}
	    	
  	}
  	
  	if ( !allOK ) {
  		
  		result.setValue( allNOK ? Result.Value.NOK : Result.Value.PARTIAL );
  		
  	}
  	
  	result.setObject( individualResults );
	    	
  	return result;
  	
  }
  
  public  List<Result<QuestionAndAnswer>>  areAllMandatoryQuestionsAnswered( String entityUuid, List<QuestionAndAnswer> list, String tag ) {
  	
	  List<Result<QuestionAndAnswer>> individualResults
	  	= list();

		for ( QuestionAndAnswer questionAndAnswer : list ) {
			
			Result<QuestionAndAnswer> individualResult
				= new Result<QuestionAndAnswer>();
			
			individualResult.setValue( Value.OK );
			individualResult.setObject( questionAndAnswer );
			
			if ( ! QuestionType.Label.equals( questionAndAnswer.getType() ) ) {
			
				if ( isEmpty( questionAndAnswer.getAnswer() ) ) {

					individualResult.setValue( Value.NOK );
					logger.info( "[{}]; mandatory question [{}] was not answered", entityUuid, questionAndAnswer.getQuestion() );
					
					
				}
				
			}
			
			individualResults.add( individualResult );
			
		}
	
		return individualResults;
		
  }
  
  /*

    @Transactional(readOnly=false)
    public Enrollment createEnrollment( Enrollment inschrijving ) {
    	
    	if ( inschrijving.getUuid() != null ) {
    		return null;
    	}
    	
    	Enrollment saved
    		= null;
    	
    	inschrijving.setStatus( new Status( Status.Value.DRAFT ) );
    	
    	Organisation organisation 
			= this.buitenWipper.whoHasID( inschrijving.getorganisation().getUuid() );
    	// to detach ...
    	organisation.getAdres();
    	inschrijving.setorganisation( organisation );
    	
    	// TODO, remove hardcoded year!
    	inschrijving.setYear( 2016 );
    	
    	saved = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    		
 	    // stupid GAE ... set id manually
 	    saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
 	    inschrijving = this.inschrijvingXRepository.saveAndFlush( saved );
    	
    	if ( inschrijving.getReference() != null ) {
    		
    		Enrollment cloneFrom 
    			= this.findInschrijving( inschrijving.getReference() );
    		
    		if ( cloneFrom != null ) {
    			this.cloneEnrollment( cloneFrom, inschrijving );
    			return inschrijving;
    		}
    		else {
    			logger.warn( "could not clone, reference [{}] not found", inschrijving.getReference() );
    			throw new RuntimeException("clone failed"); 
    		}
    		
    	}
    	else {
    		
	    	
	    	inschrijving.getContactGegevens().setEmail( organisation.getEmail() );
	    	
	    	if ( ! this.isEmpty( organisation.getTelefoonNummer() ) ) {
	    		inschrijving.getContactGegevens().setPhone( organisation.getTelefoonNummer() );
	    	}
	    	else if ( ! this.isEmpty( organisation.getGsmNummer() ) ) {
	    		inschrijving.getContactGegevens().setPhone( organisation.getGsmNummer() );
	    	}
	    	
    		this.addEmptyParticipant( inschrijving );
    	
    	}
    	
    	return saved;
    }
    
    protected void cloneEnrollment( Enrollment cloneFrom, Enrollment cloneTo ) {
    	
    	cloneTo.setReference( cloneFrom.getUuid() );
    	
    	cloneTo.setVks( cloneFrom.getVks() );
    	
    	// copy contact
    	cloneTo.getContactGegevens().setName( cloneFrom.getContactGegevens().getName() );
    	cloneTo.getContactGegevens().setEmail( cloneFrom.getContactGegevens().getEmail() );
    	cloneTo.getContactGegevens().setPhone( cloneFrom.getContactGegevens().getPhone() );
    	
    	// copy address
    	Address fromAddress 
    		= cloneFrom.getAdres();
    	
    	cloneTo.getAdres().setGemeente( fromAddress.getGemeente() );
    	cloneTo.getAdres().setZipCode( fromAddress.getZipCode() );
    	cloneTo.getAdres().setStraat( fromAddress.getStraat() );
    	cloneTo.getAdres().setNummer( fromAddress.getNummer() );
    	
    	Enrollment saved
    		= this.inschrijvingXRepository.saveAndFlush( cloneTo );

    	// stupid GAE ... set id manually
    	saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
    	saved = this.inschrijvingXRepository.saveAndFlush( saved );
    	
    	saved = this.addEmptyParticipant( saved );
    	
    	QList vragen
			= QList.template();
    	
    	for ( String key : vragen.getVragen().keySet() ) {
    		
    		List<Vraag> list
    			= vragen.getVragen().get( key );
    		
    		for ( Vraag vraag : list ) {
    			this.addVraag( cloneTo, vraag );
    		}
    		
    	}
    	
    }
    
    @Transactional(readOnly=false)
    public Vakantie maakVakantie( Vakantie vakantie ) {
    	
    	if ( vakantie.getUuid() != null ) {
    		return null;
    	}
    	
    	Vakantie saved
    		= this.configuredVakantieRepository.saveAndFlush( vakantie );

    	// stupid GAE ... set id manually
    	saved.setUuid( KeyFactory.keyToString( saved.getKey() ) );
    	this.configuredVakantieRepository.saveAndFlush( saved );
    	
    	for ( String key : vragen.getVragen().keySet() ) {
    		
    		List<Vraag> list
    			= vragen.getVragen().get( key );
    		
    		for ( Vraag vraag : list ) {
    			Vraag savedVraag = this.vraagRepository.save( vraag );
    			saved.getVragen().add( savedVraag );
    		}
    		
    	}
    	
    	return saved;
    }
    
    // @Transactional(readOnly=true)
    // this cannot be @Transactional, as the GAE will throw "operating on too many entity groups in a single transaction" on production :o(
    public List<Application> applicationsOfOrganisation( Organisation organisation ) {
    	
    	List<Enrollment> inschrijvingen
    		= new ArrayList<Enrollment>( );
    	
    	// TODO, remove hardcoded year!
    	List<Enrollment> all
    		= this.inschrijvingXRepository.findByYear( 2016 );// = PirlewietUtil.isPirlewiet( organisation ) ? this.inschrijvingXRepository.findAll() : this.inschrijvingXRepository.findByorganisation( organisation );
    		// SGL|| findByorganisation does not work in GAE ... at least not out-of-the-box
    	
    	logger.info( "total number of enrollments for 2016: [{}]", all.size() );
    	
    	Iterator<Enrollment> it
    		= all.iterator();
    	
    	boolean isPirlewiet
    		= PirlewietUtil.isPirlewiet( organisation );
    	
    	logger.info( "find enrollments for organisation: [{}]", organisation.getUuid() );
    	
    	while ( it.hasNext() ) {
    		
    		Enrollment inschrijving
				= it.next();
    		
    		String uuid
    			=  inschrijving.getUuid();
    		
    		try {
	    		
	    		inschrijving = this.detacher.detach( inschrijving );
	    		
	    		if ( inschrijving == null ) {
	    			logger.warn( "[{}]; detached enrollment is null, skip", uuid );
	    			continue;
	    		}
	    		
	    		if ( (!isPirlewiet) && ( ! inschrijving.getorganisation().getUuid().equals( organisation.getUuid() ) ) ) {
	    			// logger.info( "x.[{}] versus o.[{}], no match", inschrijving.getorganisation().getUuid(), organisation.getUuid() );
	    			continue;
	    		}
	    		
	    		if ( inschrijving.getReference() != null ) {
	    			continue;
	    		}
	    		
	    		if ( isPirlewiet ) {
	    			
	    			if ( Value.DRAFT.equals( inschrijving.getStatus().getValue() ) ) {
	    				continue;
	    			}
	    			
	    		}
	    		
	    		inschrijvingen.add( inschrijving );
	    		
	    		// enkel toevoegen als de vakantie nog niet voorbij is
	    		if ( inschrijving.getVakantie().getEindDatum().after( new Date() ) ) {
	    			inschrijvingen.add( inschrijving );
	    		} 
    		}
    		catch( Exception e ) {
    			logger.warn( "failed to load enrollment", e );
    		}
    		
    	}
    	
    	Collections.sort( inschrijvingen, mostRecent );
    	
    	return inschrijvingen;
    	
    }
    
  public List<Enrollment> drafts( ) {
    	
    	List<Enrollment> inschrijvingen
    		= new ArrayList<Enrollment>( );
    	
    	List<Enrollment> all
    		= this.inschrijvingXRepository.findAll();// = PirlewietUtil.isPirlewiet( organisation ) ? this.inschrijvingXRepository.findAll() : this.inschrijvingXRepository.findByorganisation( organisation );
    		// SGL|| findByorganisation does not work in GAE ... at least not out-of-the-box
    	
    	logger.info( "total number of enrollments: [{}]", all.size() );
    	
    	Iterator<Enrollment> it
    		= all.iterator();
    	
    	while ( it.hasNext() ) {
    		
    		Enrollment inschrijving
    			= it.next();
    		
    		inschrijving = this.detach( inschrijving.getUuid() );
    		
    		if ( Value.DRAFT.equals( inschrijving.getStatus().getValue() ) ) {
    			inschrijvingen.add( inschrijving );	
    		}
    		
    	}
    	
    	return inschrijvingen;
    	
    }
    
    // @Transactional(readOnly=true)
    public Enrollment findInschrijving( String uuid ) {
    	
    	Enrollment inschrijving 
    		= this.detach( uuid );
    	
    	return inschrijving;
    	
    	
    }
    
    @Transactional(readOnly=false)
    public Enrollment deleteEnrollment( String uuid ) {
    	
    	Enrollment inschrijving 
			= this.findInschrijving( uuid );
    	
    	if ( inschrijving != null ) {
    		
    		if ( Status.Value.DRAFT.equals( inschrijving.getStatus().getValue() ) ) {
    			
    			if ( ( inschrijving.getReference() != null ) && ( ! inschrijving.getReference().isEmpty() ) ) {
    				// TODO: delete related data (questions,...)
    				this.inschrijvingXRepository.delete( inschrijving );
    			}
    			else {
    				throw new PirlewietException( "Je kan de hoofddeelnemer niet verwijderen." );
    			}
    			
    		}
    		else {
    			throw new PirlewietException( "Je kan een inschrijving enkel verwijderen als die nog niet ingediend is." );
    		}
    		
    	}
    	else {
    		throw new PirlewietException( "Inschrijving bestaat niet (meer)." );
    	}
    	
    	return inschrijving;
    	
    	
    }
    
    protected Enrollment detach( String uuid ) {
    	
    	return this.detacher.findAndDetach( uuid );
    	
    }
    
    public Participant deelnemer( String id ) {
    	
    	Participant deelnemer
			= this.deelnemerRepository.findByUuid( id );
    	
    	return deelnemer;    	
    	
    }
    
    @Transactional(readOnly=false)
    public Enrollment updateVragenLijst( String inschrijvingID, List<Vraag> vragen ) {
    	
    	Enrollment inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
    	Set<String> tags
    		= new HashSet<String>();
    	
    	for ( Vraag vraag : vragen ) {
    		
    		tags.add( vraag.getTag() );
    	
	    	for ( Vraag v : inschrijving.getVragen() ) {
	    		
	    		if ( v.getUuid().equals( ( vraag.getUuid() ) ) ) {
	    			
	    			v.setAntwoord( vraag.getAntwoord() );
	    			break;
	    			
	    		}
	    		
	    	}
    	}
    	
    	inschrijving = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    // TODO, return ResultDTO<>
    @Transactional(readOnly=false)
    public Enrollment updateVakanties( String inschrijvingID, String vakanties ) {
    	
    	if ( vakanties == null ) {
    		throw new PirlewietException("Selecteer minstens 1 vakantie");
    	}
    	
    	vakanties = vakanties.replaceAll("\"", "" ).trim();
    	
    	if ( isEmpty( vakanties ) ) {
    		throw new PirlewietException("Selecteer minstens 1 vakantie");
    	}
    	
    	Enrollment inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
    	// make sure only the same sort of holidays are selected
    	ResultDTO<VakantieType> type
    		= this.holidayManager.checkSingleType( vakanties );
    	
    	if ( ! ResultDTO.Value.OK.equals( type.getValue() ) ) {
    		throw new PirlewietException("Per inschrijving mag je maar 1 type vakantie selecteren, bv. KIKA 1 en KIKA 2, ...");
    	}
    	
    	inschrijving.setVks( vakanties );
    	
		inschrijving = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public Enrollment updateContact( String inschrijvingID, PersonInfo contactGegevens ) {
    	
    	Enrollment inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
    	if ( contactGegevens.getName().isEmpty() ) {
    		throw new RuntimeException("Geef de naam van de contactpersoon op");
    	}
    	
    	if ( contactGegevens.getPhone().isEmpty() ) {
    		throw new RuntimeException("Geef de GSM-nummer of een telefoonnummer van de contactpersoon op");
    	}
    	
    	if ( contactGegevens.getEmail().isEmpty() ) {
    		throw new RuntimeException("Geef het e-mailadres van de contactpersoon op");
    	}
    	
    	PersonInfo contact
    		= inschrijving.getContactGegevens( );
    	
    	contact.setName( contactGegevens.getName() );
    	contact.setPhone( contactGegevens.getPhone() );
    	contact.setEmail( contactGegevens.getEmail() );
		
		inschrijving = this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public Enrollment updateInschrijvingsAdres( String inschrijvingID, Address adres ) {
    	
    	Enrollment inschrijving
    		= this.findInschrijving( inschrijvingID );
    	
    	if ( isEmpty( adres.getGemeente() ) ) {
    		throw new RuntimeException("Geef de gemeente op");
    	}
    	
    	if ( isEmpty( adres.getStraat() ) ) {
    		throw new RuntimeException("Geef de straat op");
    	}
    	
    	if ( isEmpty( adres.getNummer() ) ) {
    		throw new RuntimeException("Geef huis- en eventueel busnummer op");
    	}
    	
    	inschrijving.setAdres( adres );
		
		this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
    @Transactional(readOnly=false)
    public  Participant updateDeelnemer( String id, Participant deelnemer ) {
    	
    	Enrollment inschrijving
    		= this.findInschrijving( id );
    	
    	Participant d
    		= inschrijving.getDeelnemers().get( 0 );
    	
    	if ( d != null ) {
    		
    		// TODO, check participant!
    		// assertParticipantCompleteness( deelnemer );
    		
    		d.setVoorNaam( deelnemer.getVoorNaam() );
    		d.setFamilieNaam( deelnemer.getFamilieNaam() );
    		d.setGeslacht( deelnemer.getGeslacht() );
    		d.setGeboorteDatum( deelnemer.getGeboorteDatum() );
    		d.setEmail( deelnemer.getEmail() );
    		d.setTelefoonNummer( deelnemer.getTelefoonNummer() );
    		d.setMobielNummer( deelnemer.getMobielNummer() );
    		
    		this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	}
    	
    	return d;
    	
    }
    
    @Transactional(readOnly=true)
    public List<Vakantie> actueleVakanties( ) {
    	
    	List<Vakantie> all
    		= this.configuredVakantieRepository.findAll();
    	
    	List<Vakantie> vakanties
    		= new ArrayList<Vakantie>( all.size() );
    	
    	for ( Vakantie v : all ) {
    		if ( v.getEindInschrijving().after( new Date() ) ) {
    			if ( ( v.getBeginInschrijving() != null ) && ( v.getBeginInschrijving().before( new Date() ) ) ) {
    				vakanties.add( v );
    			}
    			else {
    				logger.debug( "vakantie [{}] niet actueel, inschrijvingen nog niet begonnen", v.getUuid() );
    			}
    		}
    		else {
				logger.debug( "vakantie [{}] niet actueel, inschrijvingen reeds beeindigd", v.getUuid() );
			}
    	}
    	
    	Collections.sort( vakanties, this.vacationNameComparator );
    	
    	return vakanties;
    	
    }
    
    protected Enrollment addEmptyParticipant( Enrollment inschrijving ) {
    	
    	Participant participant
    		= new Participant();
    	
    	inschrijving.getDeelnemers().add( participant );
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	participant.setUuid( KeyFactory.keyToString( participant.getKey() ) );
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	QList vragen
			= QList.template();

    	for ( String key : vragen.getVragen().keySet() ) {
		
			List<Vraag> list
				= vragen.getVragen().get( key );
			
			for ( Vraag vraag : list ) {
				this.addVraag( inschrijving, vraag );
			}
		
    	}		
    	
    	return inschrijving;
    	
    }
    
    protected Enrollment addVraag( Enrollment inschrijving, Vraag vraag ) {
    	
    	// ^GAE this.persoonRepository.saveAndFlush( deelnemer );
    	// this.persoonRepository.saveAndFlush( deelnemer );
    	
    	inschrijving.getVragen().add( vraag );
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	vraag.setUuid( KeyFactory.keyToString( vraag.getKey() ) );
    	
    	this.inschrijvingXRepository.saveAndFlush( inschrijving );
    	
    	return inschrijving;
    	
    }
    
   
    
    @Transactional(readOnly=false)
	public void updateStatus( String inschrijvingID, Status status ) {
	
		Enrollment loaded
			= this.findInschrijving( inschrijvingID );
		
		if ( Status.Value.SUBMITTED.equals( status.getValue() ) ) {
			throw new PirlewietException( "Je moet nog een beslissing nemen over de inschrijving." );
		}
		
		Status.Value oldStatus
			= loaded.getStatus().getValue();
		
		loaded.getStatus().setValue( status.getValue() );
		loaded.getStatus().setComment( status.getComment() );
		this.inschrijvingXRepository.saveAndFlush( loaded );

		if ( status.getEmailMe() ) {
			
			MimeMessage message
				= formatUpdateMessageToOrganisation( loaded, oldStatus );

			if ( message != null ) {
				
				postBode.deliver( message );
				logger.info( "email sent" );
				
			}
			
		}
			
	}
    
    public void sendInitialCode( Organisation organisation ) {
    	
    	MimeMessage message
			= formatReadyToRockMessage( organisation );

		if ( message != null ) {
			
			postBode.deliver( message );
			logger.info( "email sent" );
			
		}
    	
    }
    
    protected boolean isEmpty( String x ) {
    	
    	return ( x == null ) || ( x.trim().isEmpty() );
    	
    }
    
    @Transactional(readOnly=true)
    public Organisation organisation( String uuid ) {
    	
    	Organisation organsiatie
    		= this.organisationRepository.findByUuid( uuid );
    	
    	if ( organsiatie != null ) {
    		logger.info( "found organsiatie with id [{}]", uuid );
    	}
    	
    	return organsiatie;
    	
    	
    }
    
    
    
    protected MimeMessage formatReadyToRockMessage( Organisation organisation ) {
		
  		MimeMessage message
  			= null;
  		
  		Configuration cfg 
  			= new Configuration();
  	
  		try {
  			
  			InputStream tis
  				= this.getClass().getResourceAsStream( "/templates/pirlewiet/ready.tmpl" );
  			
  			Template template 
  				= new Template("code", new InputStreamReader( tis ), cfg );
  			
  			Map<String, Object> model = new HashMap<String, Object>();
  					
  			model.put( "organisation", organisation );
  			model.put( "from", PirlewietApplicationConfig.EMAIL_ADDRESS );
  			
  			StringWriter bodyWriter 
  				= new StringWriter();
  			
  			template.process( model , bodyWriter );
  			
  			bodyWriter.flush();
  				
  			message = this.javaMailSender.createMimeMessage();
  			MimeMessageHelper helper = new MimeMessageHelper( message,"utf-8" );
  				
  			helper.setFrom( PirlewietApplicationConfig.EMAIL_ADDRESS, "Pirlewiet Digitaal" );
  			helper.setTo( organisation.getEmail() );
  			helper.setReplyTo( "info@pirlewiet.be" );
  			helper.setSubject( "Pirlewiet VZW - digitale inschrijvingen 2015" );
  				
  			String text
  				= bodyWriter.toString();
  				
  			logger.info( "email text is [{}]", text );
  				
  			helper.setText(text, true);
  				
  		}
  		catch( Exception e ) {
  			logger.warn( "could not write e-mail", e );
  			throw new RuntimeException( e );
  		}
  		
  		return message;
      	
      }
    
    protected String vakantieDetails( Enrollment inschrijving ) {
		
		StringBuilder b
			= new StringBuilder();
		
		for ( Vakantie v : inschrijving.getVakanties() ) {
			
			if ( v != null ) {
				b.append( b.length() == 0 ? v.getNaam() : new StringBuilder(", ").append( v.getNaam() ));
			}
			
		}
		
		return b.toString();
		
	} 
    
    public String areAllMandatoryQuestionsAnswered( Enrollment enrollment, String tag ) {
    	
    	String notAnswered
			= null;

		for ( Vraag vraag : enrollment.getVragen() ) {
			
			if ( ! Vraag.Type.Label.equals( vraag.getType() ) ) {
				if ( tag.equals( vraag.getTag() ) ) { 
					if ( ( vraag.getAntwoord() == null ) || ( vraag.getAntwoord().isEmpty() ) ) {
						logger.info( "[{}]; mandatory question [{}] was not answered", enrollment.getUuid(), vraag.getVraag() );
						notAnswered = vraag.getVraag();
						break;
					}
				}
			}
			
		}
	
		return notAnswered;
		
    }
    
    public ResultDTO<Enrollment> checkApplicationHolidaysStatus( Enrollment application ) {
    	
    	ResultDTO<Enrollment> result
    		= new ResultDTO<Enrollment>();
    	
    	result.setValue( ResultDTO.Value.OK );
    	result.setObject( application );
	
		if ( isEmpty( application.getVks() ) ) {
			
			result.setValue( ResultDTO.Value.NOK );
			result.setErrorCode( ErrorCode.APPLICATION_HOLIDAY_NONE );
			
		}
		else {
			
			ResultDTO<VakantieType> typeResult
				= this.holidayManager.checkSingleType( application.getVks() );
			
			result.setValue( typeResult.getValue() );
			result.setErrorCode( typeResult.getErrorCode() );
			
		}
	    	
    	return result;
    	
    }
    
    public ResultDTO<Enrollment> checkApplicationContactStatus( Enrollment application ) {
    	
    	ResultDTO<Enrollment> result
    		= new ResultDTO<Enrollment>();
    	
    	result.setValue( ResultDTO.Value.OK );
    	result.setObject( application );
	
    	// contact
		PersonInfo contact
			= application.getContactGegevens();
		
		boolean contactOK
			= true;
		
		contactOK &=  ( ! isEmpty( contact.getName() ) );
		contactOK &= ( ! isEmpty( contact.getEmail() ) );
		contactOK &= ( ! isEmpty( contact.getPhone() ) );
	    	
    	if ( ! contactOK ) {
    		result.setValue(  ResultDTO.Value.NOK );
    		result.setErrorCode( ErrorCode.APPLICATION_CONTACT_INCOMPLETE );
    	}
	    	
    	return result;
    	
    }
    
    
 public ResultDTO<List<ResultDTO<Enrollment>>> checkEnrollmentsStatus( Enrollment application ) {
    	
	 	ResultDTO<List<ResultDTO<Enrollment>>> result
    		= new ResultDTO<List<ResultDTO<Enrollment>>>();
    	
    	result.setValue( ResultDTO.Value.OK );
    	
    	List<Enrollment> enrollments
    		= this.findRelated( application, true );
    	
    	if ( enrollments == null ) {
    		result.setValue( ResultDTO.Value.NOK );
    		result.setErrorCode( ErrorCode.APPLICATION_NOT_FOUND );
    		return result;
    	}
    	else if ( enrollments.isEmpty() ) {
    		result.setValue( ResultDTO.Value.NOK );
    		result.setErrorCode( ErrorCode.APPLICATION_NO_ENROLLMENTS );
    		return result;
    	}
    	
    	// OK, we have enrollments
    	
    	List<ResultDTO<Enrollment>> enrollmentResults
    		= new ArrayList<ResultDTO<Enrollment>>( enrollments.size() );
    	
    	boolean allIsWell
    		= true;
    	
    	boolean allIsFalse
    		= true;
    	
    	for ( Enrollment enrollment : enrollments ) {
    		
    		ResultDTO<Enrollment> individualResult
    			= new ResultDTO<Enrollment>();
    		
    		individualResult.setValue( ResultDTO.Value.OK );
    		individualResult.setObject( enrollment );
    		
    		ResultDTO<Participant> participantResult
    			= new ResultDTO<Participant>();
    		
    		participantResult 
    			= this.checkParticipantData( enrollment.getDeelnemers().get( 0) );
    		
    		if ( ! ResultDTO.Value.OK.equals( participantResult.getValue() ) ) {
    			
    			individualResult.setValue( participantResult.getValue() );
    			individualResult.setErrorCode( participantResult.getErrorCode() );
    			
    		}
    		else {
    		
	    		ResultDTO<VakantieType> typeResult
	    			= this.holidayManager.checkSingleType( application.getVks() );
	    		
	    		// assume type has already been checked
	    		if ( ResultDTO.Value.OK.equals( typeResult.getValue() ) ) {
	    			
	    			VakantieType type
	    				= typeResult.getObject();
	    			
	    			if ( VakantieType.Kika.equals( type ) || VakantieType.Tika.equals( type ) || VakantieType.Vov.equals( type ) ) {
	    				
		    			ResultDTO<String> medicalResult
	    					= this.checkParticipantMedicalFile( enrollment );
	    			
		    			individualResult.setValue( medicalResult.getValue() );
		    			individualResult.setErrorCode( medicalResult.getErrorCode() );
	    				
	    			}
	    			
	    		}
    		}
    		
    		enrollmentResults.add( individualResult );
    		
    		allIsWell &= (ResultDTO.Value.OK.equals( individualResult.getValue() ) );
    		allIsFalse &= (! ResultDTO.Value.OK.equals( individualResult.getValue() ) );
    		
    	} 
    	
    	result.setValue( allIsWell ? ResultDTO.Value.OK : ( allIsFalse ? ResultDTO.Value.NOK : ResultDTO.Value.PARTIAL ) );
    	result.setObject( enrollmentResults );
    	
    	return result;
    	
    }
    
    public List<Enrollment> findRelated( Enrollment enrollment, boolean includeSelf ){
    	
    	String uuid
    		= enrollment.getUuid();
    	
    	if ( uuid == null ) {
    		return null;
    	}
    	
    	List<Enrollment> found
    		= this.inschrijvingXRepository.findByReference( uuid );
    	
    	List<Enrollment> related
    		= new ArrayList<Enrollment>( Math.max( 0, found.size() - 1 ) );
    	
    	if ( includeSelf ) {
    		related.add( enrollment );
    	}
    	
    	for ( Enrollment f : found ) {
    		
    		if ( ! uuid.equals( f.getUuid() ) ) {
    			related.add( f );
    		}
    		
    	}
    	
    	return related;
    	
    }
    
    public Status whatIsTheApplicationStatus( Enrollment application ) {
    	
    	Status status
    		= new Status( );
    	
		// an application gets status 'submitted', if any of the enrollments is submitted
    	// an application gets status 'complete' when none of the enrollments need handling 

    	boolean isComplete
    		= true;
    	
    	boolean isDraft
    		= false;
    	
    	List<Enrollment> related
    		= this.findRelated( application, true );
    	
    	for ( Enrollment enrollment : related ) {

			if ( this.needsHandling( enrollment ) ) {
				
				isComplete &= false;
				
			}
			else if ( ! this.hasBeenHandled( enrollment ) ) {
				
				// no need for handling and not handled ? then it must be DRAFT
				
				// DRAFT as long as any of the enrollment is in draft
				isDraft = true;
				
			}
    		
    	}
    	
    	if ( isDraft ) {
    		status.setValue( Value.DRAFT );
    	}
    	else if ( isComplete ) {
    		status.setValue( Value.COMPLETE );
    	}
    	else {
    		status.setValue( Value.SUBMITTED );
    	}
    	
    	return status;
    	
    }
    
    protected ResultDTO<Participant> checkParticipantData( Participant participant ) {
    	
    	ResultDTO<Participant> result
    		= new ResultDTO<Participant>();
    	
    	result.setObject( participant);
    	result.setValue( ResultDTO.Value.OK );
    	
    	if ( isEmpty(  participant.getVoorNaam() ) ) {
    		result.setValue( ResultDTO.Value.NOK );
    		result.setErrorCode( ErrorCode.PARTICIPANT_DATA_GIVEN_NAME_MISSING );
		}
    	else if ( isEmpty( participant.getFamilieNaam() ) ) {
    		result.setValue( ResultDTO.Value.NOK );
    		result.setErrorCode( ErrorCode.PARTICIPANT_DATA_FAMILY_NAME_MISSING );
		}
    	else if ( participant.getGeslacht() == null ) {
    		result.setValue( ResultDTO.Value.NOK );
    		result.setErrorCode( ErrorCode.PARTICIPANT_DATA_GENDER_MISSING );
		}
    	else if ( participant.getGeboorteDatum() == null ) {
    		result.setValue( ResultDTO.Value.NOK );
    		result.setErrorCode( ErrorCode.PARTICIPANT_DATA_BIRTHDAY_MISSING );
		}
    	else if ( isEmpty( participant.getTelefoonNummer() ) && isEmpty( participant.getMobielNummer() ) ) {
    		result.setValue( ResultDTO.Value.NOK );
    		result.setErrorCode( ErrorCode.PARTICIPANT_DATA_PHONE_MISSING );
		}
    	
    	return result;
    	
    } 
    
    public ResultDTO<String> checkParticipantMedicalFile( Enrollment enrollment ) {
    	
    	ResultDTO<String> result
    		= new ResultDTO<String>();
    	
    	result.setValue( ResultDTO.Value.OK );
	
    	// qlist
		String unAnswered
			= this.areAllMandatoryQuestionsAnswered( enrollment, Tags.TAG_MEDIC );
	    	
    	if ( unAnswered != null ) {
    		result.setValue(  ResultDTO.Value.NOK );
    		result.setErrorCode( ErrorCode.PARTICIPANT_MEDIC_QUESTION_MISSING );
    		result.setObject( unAnswered );
    	}
	    	
    	return result;
    	
    }
    
    protected boolean needsHandling( Enrollment enrollment ) {
    	
    	Value value
    		= enrollment.getStatus().getValue();
    	
    	return ( Value.SUBMITTED.equals( value ) );
    	
    }
    
    protected boolean hasBeenHandled( Enrollment enrollment ) {
    	
    	Value value
    		= enrollment.getStatus().getValue();
    	
    	return ( Value.ACCEPTED.equals( value ) || Value.REJECTED.equals( value )  || Value.WAITINGLIST.equals( value ) || Value.CANCELLED.equals( value ) );
    	
    }
    
    */
 
}