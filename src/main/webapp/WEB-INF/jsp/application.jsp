<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib uri = "http://www.springframework.org/tags" prefix = "spring"%>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>
	
	<fmt:bundle basename="pirlewiet-messages">

	<c:choose>
		<c:when test="${isPirlewiet}">
	    <jsp:include page="/WEB-INF/jsp/menu-pirlewiet.jsp">
	    	<jsp:param name="active" value="enrollments"/>
	    </jsp:include>
		</c:when>
		<c:otherwise>
			<jsp:include page="/WEB-INF/jsp/menu.jsp">
		    	<jsp:param name="active" value="enrollments"/>
		    </jsp:include>
		</c:otherwise>
	</c:choose>

	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Dossier</h1>
					<p>
						Beheer een inschrijvingsdossier.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>
	
	<div class="container">
	
		<br/>
		<div class="row">
		
			<c:set var="application" value="${applicationResult.object}" />
			<c:set var="questionList" value="${applicationQuestionListResult.object}" />
			<c:set var="contact" value="${contactResult.object}" />
			
			<div class="col-sm-12 alert alert-info">
				<h4><strong>Status</strong><br/></h4>
				<p>
					<i><fmt:message key="application.status.${application.status.value}"/></i><br/>
					<fmt:message key="application.status.${application.status.value}.description"/> <br/>
				</p>
			</div>
			
		</div>
			<div class="row">
				<c:choose>
					<c:when test="${holidaysResult.value == 'NOK' }">
						<div class="col-sm-12 alert alert-warning">
							<i class="fa fa-2x fa-calendar pull-right"></i><h4><strong>Vakantie(s)</strong></h4>
							Opgelet:
							<ul>
								<li>minstens 1 vakantie selecteren</li>
								<li>enkel vakanties selecteren van hetzelfde type (KIKA, TIKA of GEZINS)</li>
								<br/>
							</ul> 
							Geen geldige vakantie(s)&nbsp;(<a href="javascript:void(0);" class="todo" data-attribute-modal="holidays">invullen/wijzigen</a>)
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
						<i class="fa fa-2x fa-calendar pull-right"></i><h4><strong>Vakantie(s)</strong></h4>
							<span>${application.holidayNames}</span>&nbsp;(<a href="javascript:void(0);" class="todo" data-attribute-modal="holidays">wijzigen</a>)<br/>
						</div>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${contactResult.value == 'NOK' }">
						<div class="col-sm-12 alert alert-warning">
							<i class="fa fa-2x fa-phone pull-right"></i><h4><strong>Contactpersoon</strong><br/></h4>
							<a href="javascript:void(0);" class="todo" data-attribute-modal="contact">Contactpersoon ingeven</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
						<i class="fa fa-2x fa-phone pull-right"></i><h4><strong>Contactpersoon</strong><br/></h4>
						<span>${application.contactPersonName}</span>&nbsp;(<a href="javascript:void(0);" class="todo" data-attribute-modal="contact">wijzigen</a>)
						</div>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${applicationQuestionListResult.value != 'OK'}">
						<div class="col-sm-12 alert alert-warning">
							<i class="fa fa-2x fa-2x fa-question pull-right"></i><h4><strong>Vragenlijst</strong><br/></h4>
							<span class="">Niet (volledig) ingevuld </span><br/>
							<a href="javascript:void(0);" class="todo" data-attribute-modal="qlist">Vragenlijst invullen/aanvullen</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
							<i class="fa fa-2x fa-2x fa-question pull-right"></i><h4><strong>Vragenlijst dossier</strong><br/></h4>
							<span>Ingevuld</span>&nbsp;(<a href="javascript:void(0);" class="todo" data-attribute-modal="qlist">wijzigen</a>)
						</div>
					</c:otherwise>
				</c:choose>
			<c:choose>
				<c:when test="${ enrollmentsResult.errorCode.code == 'APPLICATION_NO_ENROLLMENTS' }">
					<div class="col-sm-12 alert alert-warning">
						<i class="fa fa-2x fa-2x fa-users pull-right"></i><h4><strong>Deelnemer(s)</strong><br/></h4>
						<span class="">Nog geen deelnemers toegevoegd</span><br/>
						<a href="javascript:void(0);" class="todo" data-attribute-modal="enrollment">Deelnemer toevoegen</a>
					</div>
				</c:when>
				<c:otherwise>
					<div class="col-sm-12 alert ${enrollmentsResult.value == 'OK' ? 'alert-success' : 'alert-warning' }">
					
						<i class="fa fa-2x fa-users pull-right"></i><h4><strong>Deelnemer(s)</strong><br/></h4>
						<c:if test="${enrollmentsResult.value == 'NOK'}">
						<p>
							Er ontbreken nog gegevens van minstens 1 deelnemer. Kijk AUB de gegevens na. <br/>
						</p>
						</c:if>
						<p>
							<strong>Opgelet</strong>: voor KIKA, TIKA en VOV-vakanties moet de medische fiche <strong>volledig</strong> ingevuld zijn van alle deelnemers voordat je het dossier kan doorsturen. <br/> <br/>
						</p>
						
						<div class="container">
							<c:forEach items="${enrollmentsResult.object}" var="enrollmentResult">
								<c:set var="enrollment" value="${enrollmentResult.object}"/>
								<div class="row">
									<div class="col-sm-5 alert alert-info">
										<strong>Deelnemer</strong>
										<c:choose>
											<c:when test="${isPirlewiet}">
												<a href="/enrollment-${enrollment.uuid}-pirlewiet.html" class="pull-right"><spring:message code="enrollment.status.${enrollment.status.value}"/></a>
											</c:when>
											<c:otherwise>
												<span class="pull-right"><strong><spring:message code="enrollment.status.${enrollment.status.value}"/></strong></span>
											</c:otherwise>
										</c:choose>
										<br/><br/>
										<i class="fa fa-user"></i>&nbsp;<span class="x">${enrollment.participant.givenName}&nbsp;${enrollment.participant.familyName}</span>&nbsp;(<a href="javascript:void(0);" class="todo" data-attribute-modal="enrollment" data-attribute-uuid="${enrollment.uuid}">Wijzig</a>)&nbsp;&nbsp;<c:if test="${enrollment.applicationUuid != null }">(<a href="javascript:void(0);" class="enrollment-delete" data-attribute-uuid="${enrollment.uuid}" >Verwijder</a>)</c:if>&nbsp;&nbsp;<br/>
										<i class="fa fa-question"></i>&nbsp;<a href="javascript:void(0);" class="dam" data-attribute-modal="history" data-attribute-uuid="${enrollment.uuid}">Vragenlijst</a><br/>													
										<i class="fa fa-medkit"></i>&nbsp;<a href="javascript:void(0);" class="dam" data-attribute-modal="medical" data-attribute-uuid="${enrollment.uuid}">Medische fiche</a>											
													
										
									</div>
									<div class="col-sm-1">
									</div>
									<c:choose>
										<c:when test="${enrollmentResult.value == 'NOK'}">
											<div class="col-sm-5 alert alert-danger">
												<strong>Te doen</strong><br/>
												<ul>
												<c:if test="${fn:contains( enrollmentResult.errorCode.code, 'PARTICIPANT_DATA_' )}">
													<li>
														&nbsp;<span class="x">${enrollment.participant.givenName}&nbsp;${enrollment.participant.familyName}</span>&nbsp;((<a href="javascript:void(0);" class="todo" data-attribute-modal="enrollment" data-attribute-uuid="${enrollment.uuid}">Wijzig</a>)&nbsp;&nbsp;<c:if test="${enrollment.applicationUuid != null }">(<a href="javascript:void(0);" class="enrollment-delete" data-attribute-uuid="${enrollment.uuid}" >Verwijder</a>)</c:if>&nbsp;&nbsp;&nbsp;
													</li>
												</c:if>
												<c:if test="${fn:contains( enrollmentResult.errorCode.code, 'PARTICIPANT_HISTORY' )}">
													<li>
														<a href="javascript:void(0);" class="todo" data-attribute-modal="history" data-attribute-uuid="${enrollment.uuid}">Vragenlijst invullen</a>											
													</li>
												</c:if>
												<c:if test="${fn:contains( enrollmentResult.errorCode.code, 'PARTICIPANT_MEDIC' )}">
													<li>
														<a href="javascript:void(0);" class="todo" data-attribute-modal="medical" data-attribute-uuid="${enrollment.uuid}">Medische fiche invullen</a><br/>
														Opgelet: alle vragen uit de lijst moeten beantwoord worden. Vul eventueel "niet van toepassing" (NVT) in.											
													</li>
												</c:if>
												</ul>
											</div>
										</c:when>
										<c:otherwise>
											
										</c:otherwise>
									</c:choose>
								</div>
							</c:forEach><br/>
							<a href="javascript:void(0);" class="todo" data-attribute-modal="enrollment">Deelnemer toevoegen</a>									
						</div>
					</div>
				</c:otherwise>
			</c:choose>
			<c:choose>
					
					<c:when test="${application.status.value =='DRAFT'}">
						<div class="col-sm-12 alert alert-info">
							<i class="fa fa-2x fa-envelope pull-right"></i><h4><strong>Indienen</strong><br/></h4>
							<c:choose>
								<c:when test="${(holidaysResult.value == 'OK') && (contactResult.value == 'OK') && (applicationQuestionListResult.value == 'OK') && (enrollmentsResult.value == 'OK') }">
									<p>
										Je inschrijving is volledig. Je kan deze nu versturen.
									</p>
									<br/>
									<button type="button" id="application-submit" class="btn btn-primary"><i class="fa fa-envelope"></i>&nbsp;&nbsp;Verstuur</button>
									<button type="button" id="application-cancel" class="btn btn-danger pull-right"><i class="fa fa-trash"></i>&nbsp;&nbsp;Verwijder</button>
								</c:when>
								<c:otherwise>
									<span class="text-warning">Je inschrijving is nog niet volledig, je kan deze nog niet versturen.</span><br/>
									<span class="text-warning">Een inschrijving is pas volledig als alle bovenstaande kaders groen zijn.</span><br/>
								</c:otherwise>
							</c:choose>
						</div>
					</c:when>
					<c:when test="${application.status.value =='CANCELLED'}">
						<div class="col-sm-12 alert alert-info">
							<p>
								<h4><strong>Acties</strong></h4>
							</p>
							De inschrijving is geannuleerd. Je kan hier geen actie meer voor ondernemen.
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-info">
							<p>
								<strong>Acties</strong>
							</p>
							<br/>
							<button type="button" id="application-cancel" class="btn btn-danger"><i class="fa fa-3 fa-trash"></i>&nbsp;&nbsp;Annuleer</button>
						</div>
					</c:otherwise>
					</c:choose>	
		</div>
						
			<div id="modal" class="modal fade" tabindex="-1" role="dialog">
			</div>

	</div><!-- container -->
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
	
    <script>
    	var $jq = jQuery.noConflict();
    	
    	var show = function( id ) {
    		$jq(".panel" ).removeClass("show").addClass("hidden");
    		$jq("#" + id ).removeClass("hidden").addClass("show");
    	};
    	
		var saveContact = function( id ) {
			var c = new Contact( $jq("#contact-uuid").val(), $jq("#contact-given-name").val(), $jq("#contact-family-name").val(), $jq("#contact-phone").val(), $jq("#contact-email").val() );
			putContact ( id, c, $jq("#contact-save" ),$jq("#contact-status" ), refresh );
		};
		
		var toParticipant = function( id ) {
			
			window.location.hash = "#modal-participant-" + id;
			window.location.reload();
			
		};
		
		var addParticipant = function( applicationUuid ) {
			
			var address 
				= new Address( 
						$jq("#address-zipcode").val(), 
						$jq("#address-city").val(), 
						$jq("#address-street").val(), 
						$jq("#address-number").val() );
			
			var participant
				= new Participant( 
					$jq("#participant-given-name").val(),
					$jq("#participant-family-name").val(),
					$jq("#participant-gender").val(),
					$jq("#participant-birth-day").val(),
					$jq("#participant-phone").val(),
					$jq("#participant-email").val()
				);
			
			var enrollment =
				new Enrollment( 
						applicationUuid,
					participant,
					address);
			
			postEnrollment( applicationUuid, enrollment, refresh );
			
		};
		
		var saveParticipant = function( applicationUuid, enrollmentUuid ) {
			
			var address 
				= new Address( 
					$jq("#address-zipcode").val(), 
					$jq("#address-city").val(), 
					$jq("#address-street").val(), 
					$jq("#address-number").val() );
		
			var participant
				= new Participant( 
					$jq("#participant-given-name").val(),
					$jq("#participant-family-name").val(),
					$jq(".participant-gender:checked").val(),
					$jq("#participant-birth-day").val(),
					$jq("#participant-phone").val(),
					$jq("#participant-email").val()
			);
		
		var enrollment =
			new Enrollment( 
					applicationUuid,
				participant,
				address,
				enrollmentUuid);
		
			putEnrollment( applicationUuid, enrollment, $jq("#enrollment-save" ), $jq("#enrollment-status" ), refresh );
			
		};
		
		var saveParticipantList = function( id ) {
			return saveQList( id, "history" );
		};
		
		var saveQList = function( id, tag ) {
			var list
				= [];
			$jq( "input:checked.q[data-tag='" + tag + "']" ).each( function( index, element ) {
				list.push( new QuestionAndAnswer( element.name, tag, element.value ) );
			});
			$jq( "input[type='text'].q[data-tag='" + tag + "']" ).each( function( index, element ) {
				list.push( new QuestionAndAnswer( element.id, tag, element.value ) );
			});
			$jq( "textarea.q[data-tag='" + tag + "']" ).each( function( index, element ) {
				list.push( new QuestionAndAnswer( element.id, tag, element.value ) );
			});
			putQList ( id, list, $jq("#q-save-" + tag ),$jq("#q-status-" + tag ), refresh );
		};
		
		var saveEnrollmentQList = function( applicationUuid, enrollmentUuid, tag ) {
			var list
				= [];
			$jq( "input:checked.q[data-tag='" + tag + "']" ).each( function( index, element ) {
				list.push( new QuestionAndAnswer( element.name, tag, element.value ) );
			});
			$jq( "input[type='text'].q[data-tag='" + tag + "']" ).each( function( index, element ) {
				list.push( new QuestionAndAnswer( element.id, tag, element.value ) );
			});
			$jq( "textarea.q[data-tag='" + tag + "']" ).each( function( index, element ) {
				list.push( new QuestionAndAnswer( element.id, tag, element.value ) );
			});
			putEnrollmentQList ( applicationUuid, enrollmentUuid, list, $jq("#q-save-" + tag ),$jq("#q-status-" + tag ), refresh );
		};
		
		var saveApplicationStatus = function( applicationUuid, value ) {
			var comment = "";
			if ( value ) {
				var sx = new Status (value, comment ,true );
				busyButton( $jq("#enrollment-save" ) );
				putStatus ( applicationUuid, sx, $jq("#enrollment-save" ),$jq("#x-status" ), refresh );
				doneButton( $jq("#enrollment-save" ) );
			}
			else {
				var sx = new Status ( "AUTO", comment ,true );
				busyButton( $jq("#enrollment-save" ) );
				putStatus ( applicationUuid, sx, $jq("#enrollment-save" ),$jq("#x-status" ), refresh );
				doneButton( $jq("#enrollment-save" ) );
			}
			
			
		};
		
		var cancelApplication = function( applicationUuid ) {
			saveApplicationStatus( applicationUuid, "CANCELLED", $jq("#status-comment-text").val() );
		};
		
		var deleteDraftEnrollment = function( applicationUuid, enrollmentUuid ) {
			
			deleteEnrollment( applicationUuid, enrollmentUuid, $jq("#enrollment-delete" ),$jq("#delete-status" ), refresh );
			
		};
		
		var saveHolidays = function( id ) {
			var holidays
				= new Array();
			$jq( ".holiday:checked" ).each( function( index, element ) {
				holidays.push( new Holiday( element.value ) );
			});
			putHolidays ( id, holidays, $jq("#holiday-save"),$jq("#holiday-status" ), refresh );
		};
		
		var refresh = function( ) {
			window.location.hash="";
			window.location.reload();
		};
    	
		$jq(".todo,.dam").click( function( event ) {
			
			$jq("#modal").load( "/application-modals.html?uuid=${application.uuid}&q=" + $jq(this).attr("data-attribute-modal") + "&enrollmentUuid=" + $jq(this).attr("data-attribute-uuid"),
					function() {
				
				$jq("#holiday-save").click( function( event ) {
					
					clearStatus();
					busyButton( $jq(this) );
					
					saveHolidays( "${application.uuid}" );
					
				});
				
				$jq("#contact-save").click( function( event ) {
					
					clearStatus();
					busyButton( $jq(this) );
					
					saveContact( "${application.uuid}" );
					
				});
				
				$jq("#participant-save").click( function( event ) {
					
					clearStatus();
					busyButton( $jq(this) );
					
					if ( ( $jq(this).attr("data-attribute-uuid") != null ) && ( $jq(this).attr("data-attribute-uuid") != "" ) ) {
						saveParticipant( "${application.uuid}", $jq(this).attr("data-attribute-uuid") );	
					}
					else {
						addParticipant( "${application.uuid}" );
					}
					
				});
				
				$jq("#q-save-application").click( function( event ) {
					
					clearStatus();
					busyButton( $jq(this) );
					
					saveQList( "${application.uuid}", "application" );
					
				});
				
				$jq("#q-save-medical").click( function( event ) {
					
					clearStatus();
					busyButton( $jq(this) );
					
					saveEnrollmentQList( "${application.uuid}", $jq(this).attr("data-attribute-uuid"), "medic" );
					
				});
				
				$jq("#q-save-history").click( function( event ) {
					
					clearStatus();
					busyButton( $jq(this) );
					
					saveEnrollmentQList( "${application.uuid}", $jq(this).attr("data-attribute-uuid"), "history" );
					
				});
				
			});
			
			
			
			$jq("#modal").modal();
			
		});
		
		
		$jq("#application-submit").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveApplicationStatus( "${application.uuid}" );
			
		});
		
		$jq("#application-cancel").click( function( event ) {

			clearStatus();
			$jq(this).button('Even geduld...');
			
			cancelApplication( "${application.uuid}" );
			
		});
		
		$jq(".enrollment-delete").click( function( event ) {
			
			var uuid 
				= event.currentTarget.attributes["data-attribute-uuid"].value;
			
			$jq(this).button('Even geduld...');
			
			deleteDraftEnrollment( "${application.uuid}" , uuid );
			
		});
		
		$jq("#submit-show").click( function( event ) {
			
			show('div-submit');	
			
		});
		
		$jq( document ).ready(function() {
			
			buttons();
			
			if ( window.location.hash ) {
				$jq( "#" + window.location.hash.substring(1) ).modal();
			}
			
		});
		
    </script>
  </fmt:bundle>
  </body>
</html>
