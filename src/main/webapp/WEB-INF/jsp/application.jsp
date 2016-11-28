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

    <jsp:include page="/WEB-INF/jsp/menu.jsp">
    	<jsp:param name="active" value="enrollments"/>
    </jsp:include>

	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Inschrijving</h1>
					<p>
						Beheer een inschrijving.
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
					<c:when test="${application.holidayNames == null }">
						<div class="col-sm-12 alert alert-warning">
							<i class="fa fa-2x fa-calendar pull-right"></i><h4><strong>Vakantie(s)</strong></h4>
							<a href="javascript:void(0);" class="todo" data-attribute-modal="holidays">Vakantie(s) selecteren</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
						<i class="fa fa-2x fa-calendar pull-right"></i><h4><strong>Vakantie(s)</strong></h4>
							<span>${application.holidayNames}</span>&nbsp;(<a href="javascript:void(0);" class="todo" data-attribute-modal="holidays">Wijzigen</a>)<br/>
						</div>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${application.contactPersonName == null }">
						<div class="col-sm-12 alert alert-warning">
							<i class="fa fa-2x fa-phone pull-right"></i><h4><strong>Contactpersoon</strong><br/></h4>
							<a href="javascript:void(0);" class="todo" data-attribute-modal="contact">Contactpersoon ingeven</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
						<i class="fa fa-2x fa-phone pull-right"></i><h4><strong>Contactpersoon</strong><br/></h4>
						<span>${application.contactPersonName}</span>&nbsp;(<a href="javascript:void(0);" class="todo" data-attribute-modal="contact">Wijzigen</a>)
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
							<i class="fa fa-2x fa-2x fa-question pull-right"></i><h4><strong>Vragenlijst</strong><br/></h4>
							<span>Ingevuld</span>&nbsp;(<a href="javascript:void(0);" class="todo" data-attribute-modal="qlist">wijzigen</a>)
						</div>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${ enrollmentsResult.value != 'OK' }">
						<c:choose>
							<c:when test="${ enrollmentsResult.errorCode == 'APPLICATION_NO_ENROLLMENTS' }">
								<div class="col-sm-12 alert alert-warning">
									<i class="fa fa-2x fa-2x fa-users pull-right"></i><h4><strong>Deelnemer(s)</strong><br/></h4>
									<span class="">Nog geen deelnemers toegevoegd</span><br/>
									<a href="javascript:void(0);" class="todo" data-attribute-modal="enrollment">Vragenlijst invullen</a>Deelnemer toevoegen</a>
								</div>
							</c:when>
							<c:otherwise>
								<div class="col-sm-12 alert alert-warning">
									<i class="fa fa-2x fa-users pull-right"></i><h4><strong>Deelnemer(s)</strong><br/></h4>
									<p>
										Er ontbreken nog gegevens van minstens 1 deelnemer. Kijk AUB de gegevens na. <br/>
									</p>
									<p>
										<strong>Opgelet</strong>: voor KIKA, TIKA en VOV-vakanties moet ook de medische fiche <strong>volledig</strong> ingevuld zijn voordat je de inschrijving kan doorsturen. <br/> <br/>
									</p>
									<div class="container">
										<c:forEach items="${enrollmentsResult.object}" var="enrollmentResult">
											<c:set var="enrollment" value="${enrollmentResult.object}"/>
											<div class="row">
												<div class="col-sm-5 alert ${enrollmentResult.value == 'OK' ? 'alert-success' : ( fn:startsWith( enrollmentResult.errorCode, 'PARTICIPANT_DATA_' ) ? 'alert-danger' : 'alert-success' ) }">
													<i class="fa fa-user"></i>&nbsp;<span class="x">${enrollment.participant.givenName}&nbsp;${enrollment.participant.familyName}</span>&nbsp;(<a href="javascript:void(0);" class="todo" data-attribute-modal="enrollment">wijzigen</a>)&nbsp;&nbsp;<c:if test="${enrollment.applicationUuid != null }">(<a href="javascript:void(0);" class="enrollment-delete todo" data-uuid="${enrollment.uuid}" >verwijder</a>)&nbsp;&nbsp;</c:if>
												</div>
												<div class="col-sm-1">
												</div>
												<div class="col-sm-5 alert ${enrollmentResult.value == 'OK' ? 'alert-success' : ( fn:startsWith( enrollmentResult.errorCode, 'PARTICIPANT_MEDIC' ) ? 'alert-danger' : 'alert-success' ) }">
													<i class="fa fa-3 fa-medkit"></i>&nbsp;&nbsp;<a href="#modal-participant-${enrollment.uuid}-medical" class="todo" data-toggle="modal" data-target="#modal-participant-${enrollment.uuid}-medical">Medische fiche invullen/aanpassen</a>
												</div>
											</div>
										</c:forEach><br/>
										<a href="javascript:void(0);" class="todo" data-attribute-modal="enrollment">Deelnemer toevoegen</a>									
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
							<i class="fa fa-2x fa-2x fa-users pull-right"></i><h4><strong>Deelnemer(s)</strong><br/></h4>
							<div class="container">
								<c:forEach items="${enrollmentsResult.object}" var="enrollmentResult">
									<c:set var="enrollment" value="${enrollmentResult.object}"/>
									<div class="row">
										<div class="col-sm-4 alert ${enrollmentResult.value == 'OK' ? 'alert-success' : ( fn:startsWith( enrollmentResult.errorCode, 'PARTICIPANT_DATA_' ) ? 'alert-danger' : 'alert-success' ) }">
											<i class="fa fa-user"></i>&nbsp;<span class="x">${enrollment.participant.givenName}&nbsp;${enrollment.participant.familyName}</span>&nbsp;(<a href="javascript:void(0);" class="todo" data-attribute-modal="enrollment" data-attribute-uuid="${enrollment.uuid}">Wijzig</a>)&nbsp;&nbsp;<c:if test="${enrollment.applicationUuid != null }">(<a href="javascript:void(0);" class="enrollment-delete todo" data-uuid="${enrollment.uuid}" >Verwijder</a>)</c:if>&nbsp;&nbsp;
										</div>
										<div class="col-sm-2 alert alert-success">
											<strong><spring:message code="enrollment.status.${enrollment.status.value}"/></strong>
										</div>
										<div class="col-sm-4 alert ${enrollmentResult.value == 'OK' ? 'alert-success' : ( fn:startsWith( enrollmentResult.errorCode, 'PARTICIPANT_MEDIC' ) ? 'alert-danger' : 'alert-success' ) }">
											<i class="fa fa-3 fa-medkit"></i>&nbsp;&nbsp;<a href="#modal-participant-${enrollment.uuid}-medical" class="todo" data-toggle="modal" data-target="#modal-participant-${enrollment.uuid}-medical">Medische fiche invullen/aanpassen</a>
										</div>
									</div>
								</c:forEach><br/>
								<a href="javascript:void(0);" class="todo" data-attribute-modal="enrollment">Deelnemer toevoegen</a>									
							</div>
						</div>
					</c:otherwise>
				</c:choose>
					
				
			<c:choose>
					<c:when test="${applicationStatus.value =='DRAFT'}">
						<div class="col-sm-12 alert alert-info">
							<i class="fa fa-2x fa-envelope pull-right"></i><h4><strong>Indienen</strong><br/></h4>
							<c:choose>
								<c:when test="${(applicationHolidaysResult.value == 'OK') && (applicationContactResult.value == 'OK') && (applicationQuestionListResult.value == 'OK') && (enrollmentsResult.value == 'OK') }">
									<p>
										Je inschrijving is volledig. Je kan deze nu versturen.
									</p>
									<button type="button" id="enrollment-submit" class="btn btn-primary"><i class="fa fa-envelope"></i>&nbsp;&nbsp;Verstuur</button>
									<button type="button" id="enrollment-cancel" class="btn btn-danger"><i class="fa fa-trash"></i>&nbsp;&nbsp;Verwijder</button>
								</c:when>
								<c:otherwise>
									<span class="text-warning">Je inschrijving is nog niet volledig, je kan deze nog niet versturen.</span><br/>
									<span class="text-warning">Een inschrijving is pas volledig als alle bovenstaande kaders groen zijn.</span><br/>
								</c:otherwise>
							</c:choose>
						</div>
					</c:when>
					<c:when test="${applicationStatus.value =='CANCELLED'}">
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
							<button type="button" id="enrollment-cancel" class="btn btn-danger"><i class="fa fa-3 fa-bolt"></i>&nbsp;&nbsp;Annuleer</button>
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
			
			postEnrollment( applicationUuid, enrollment );
			
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
					$jq("#participant-gender").val(),
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
		
		var saveStatus = function( id, value ) {
			var comment = "";
			if ( value ) {
				var sx = new Status (value, comment ,true );
				putStatus ( id, sx, $jq("#enrollment-save" ),$jq("#x-status" ), refresh );
			}
			else {
				var sx = new Status ( "AUTO", comment ,true );
				putStatus ( id, sx, $jq("#enrollment-save" ),$jq("#x-status" ), refresh );	
			}
			
			
		};
		
		var cancel = function( id ) {
			saveStatus( id, "CANCELLED", $jq("#status-comment-text").val() );
		};
		
		var deleteDraftEnrollment = function( id ) {
			
			deleteEnrollment( id, $jq("#enrollment-delete" ),$jq("#delete-status" ), refresh );
			
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
    	
		$jq(".todo").click( function( event ) {
			
			$jq("#modal").load( "/application-modals.html?uuid=${application.uuid}&q=" + $jq(this).attr("data-attribute-modal") + "&enrollmentUuid=" + $jq(this).attr("data-attribute-uuid"),
					function() {
				
				$jq("#holiday-save").click( function( event ) {
					
					clearStatus();
					$jq(this).button('Even geduld...');
					
					saveHolidays( "${application.uuid}" );
					
				});
				
				$jq("#contact-save").click( function( event ) {
					
					clearStatus();
					$jq(this).button('Even geduld...');
					
					saveContact( "${application.uuid}" );
					
				});
				
				$jq("#participant-save").click( function( event ) {
					
					clearStatus();
					$jq(this).button('Even geduld...');
					
					if ( $jq(this).attr("data-attribute-uuid") != null ) {
						saveParticipant( "${application.uuid}", $jq(this).attr("data-attribute-uuid") );	
					}
					else {
						addParticipant( "${application.uuid}" );
					}
					
				});
				
				$jq("#q-save-application").click( function( event ) {
					
					clearStatus();
					$jq(this).button('Even geduld...');
					
					saveQList( "${application.uuid}", "application" );
					
				});
				
			});
			
			
			
			$jq("#modal").modal();
			
		});
		
		
		$jq(".btn-save-medic").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveQList( $jq(this).attr("data-uuid"), "medic" );
			
		});
		

		$jq("#enrollment-submit").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveStatus( "${application.uuid}" );
			
		});
		
		$jq("#enrollment-cancel").click( function( event ) {

			clearStatus();
			$jq(this).button('Even geduld...');
			
			cancel( "${application.uuid}" );
			
		});
		
		$jq(".enrollment-delete").click( function( event ) {
			
			var uuid 
				= event.currentTarget.attributes["data-uuid"].value;
			
			$jq(this).button('Even geduld...');
			
			deleteDraftEnrollment( uuid );
			
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
