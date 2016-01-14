<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
			
			<div class="col-sm-12 alert alert-info">
				<h4><strong>Status</strong><br/></h4>
				<p>
					<i><fmt:message key="enrollment.status.${inschrijving.status.value}"/></i><br/>
					<fmt:message key="enrollment.status.${inschrijving.status.value}.description"/> <br/>
				</p>
			</div>
			
		</div>
			<div class="row">
				<c:choose>
					<c:when test="${empty inschrijving.vakanties}">
						<div class="col-sm-12 alert alert-warning">
							<h4><strong>Vakantie(s)</strong></h4>
							<a href="#modal-vakantie" class="todo" data-toggle="modal">Vakantie selecteren</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
						<h4><strong>Vakantie(s)</strong><br/></h4>
						<c:forEach items="${inschrijving.vakanties}" var="vakantie">
							<span>${vakantie.naam}</span>&nbsp;(<a href="#modal-vakantie" class="todo" data-toggle="modal">wijzigen</a>)<br/>
						</c:forEach>
						</div>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${inschrijving.contactGegevens.name == null}">
						<div class="col-sm-12 alert alert-warning">
							<h4><strong>Contactpersoon</strong><br/></h4>
							<a href="#modal-contact" class="todo" data-toggle="modal" data-target="#modal-contact">Contactpersoon ingeven</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
						<h4><strong>Contactpersoon</strong><br/></h4>
						<span>${inschrijving.contactGegevens.name}</span>&nbsp;(<a href="#modal-contact" class="todo" data-toggle="modal" data-target="#modal-contact">wijzigen</a>)
						</div>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${not areAllMandatoryQuestionsAnswered}">
						<div class="col-sm-12 alert alert-warning">
							<h4><strong>Vragenlijst</strong><br/></h4>
							<span class="">Niet (volledig) ingevuld </span><br/>
							<a href="#modal-questions" class="todo" data-toggle="modal" data-target="#modal-questions">Vragenlijst invullen</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
							<h4><strong>Vragenlijst</strong><br/></h4>
							<span>Ingevuld</span>&nbsp;(<a href="#modal-questions" class="todo" data-toggle="modal" data-target="#modal-questions">wijzigen</a>)
						</div>
					</c:otherwise>
				</c:choose>
						<div class="col-sm-12 alert alert-success">
						<h4><strong>Deelnemer(s)</strong><br/></h4>
						<c:forEach items="${related}" var="enrollment">
								<span class="x">${enrollment.deelnemers[0].voorNaam}&nbsp;${enrollment.deelnemers[0].familieNaam}</span>&nbsp;(<a href="#modal-participant-${enrollment.uuid}" class="" data-toggle="modal" data-target="#modal-participant-${enrollment.uuid}">wijzig</a>)
								<button type="button" class="btn btn-success enrollment-set-status" data-uuid="${enrollment.uuid}" data-status="ACCEPTED"><i class="fa fa-bolt"></i>&nbsp;&nbsp;Aanvaard</button>
								<button type="button" class="btn btn-danger enrollment-set-status" data-uuid="${enrollment.uuid}" data-status="REJECTED"><i class="fa fa-bolt"></i>&nbsp;&nbsp;Weiger</button>
								<button type="button" class="btn btn-primary enrollment-set-status" data-uuid="${enrollment.uuid}" data-status="WAITINGLIST"><i class="fa fa-bolt"></i>&nbsp;&nbsp;Wachtlijst</button>
						</c:forEach><br/>
						</div>
				
		</div>
						
			<div id="modal-vakantie" class="modal fade" tabindex="-1" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">			
							<h2>Vakantie(s)</h2>
							<p>
								Selecteer de vakantie(s) waarvoor je wil inschrijven.
							</p>
						</div>
						<div class="modal-body">
							<c:if test="${empty vakanties}">
								<p class="text=info">Er is momenteel geen vakantie waar men voor kan inschrijven</p>
							</c:if>
							<c:forEach items="${vakanties}" var="vk">
								<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${vk.beginDatum}" var="start"></fmt:formatDate>	
								<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${vk.eindDatum}" var="end"></fmt:formatDate>
								<c:set var="contains" value="false" />
								<c:forEach items="${inschrijving.vakanties}" var="vakantie">	
									<c:choose>
										<c:when test="${vakantie.uuid eq vk.uuid}">
											<c:set var="contains" value="true" />
										</c:when>
									</c:choose>
									</c:forEach>
									<div class="checkbox">
										<label>
											<input type="checkbox" name="vak" class="vakantie" value="${vk.uuid}" ${contains == true ? "checked='checked'" : ""}>&nbsp;${vk.naam}&nbsp;&nbsp;&nbsp;(${start} t.e.m. ${end})
										</label>
									</div>
							</c:forEach>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
							<button type="button" id="vakantie-save" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;&nbsp;Sla op</button>
							<span id ="vakantie-status"></span>
						</div>
					</div>
				</div>	
			</div>
		
			<div id="modal-contact" class="modal fade" tabindex="-1" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">	
							<h2>Contactgegevens doorverwijzer</h2>
							
							<p>
								Geef hier de gegevens op van de persoon bij uw organisatie die ons secretariaat eventueel kan contacteren i.v.m deze inschrijving.
							</p>
						</div>
						<div class="modal-body">
							<form class="form-horizontal">
							<div class="form-group">
								<label for="contact-naam" class="col-sm-4 control-label">Naam (*)</label>
									<div class="col-sm-6">	
										<input id="contact-name" type="text" class="form-control" value="${inschrijving.contactGegevens.name}"></input>
									</div>
							</div>
							<div class="form-group">
								<label for="contact-naam" class="col-sm-4 control-label">Telefoon (*)</label>
									<div class="col-sm-4">	
										<input id="contact-phone" type="text" class="form-control" value="${inschrijving.contactGegevens.phone}"></input>
									</div>
							</div>
							<div class="form-group">
								<label for="contact-naam" class="col-sm-4 control-label">E-mail (*)</label>
									<div class="col-sm-3">	
										<input id="contact-email" type="email" class="form-control" value="${inschrijving.contactGegevens.email}"></input>
									</div>
							</div>
							</form>
						</div>
						<div class="modal-footer">
							<div class="form-group">
								<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
								<button type="button" id="contact-save" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;&nbsp;Sla op</button>
								<span id ="contact-status"></span>
							</div>
						</div>
					</div>
				</div>
			</div>
					
			<div id="modal-questions" class="modal fade" tabindex="-1" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
			
							<h2>Vragenlijst</h2>
						</div>
					<div class="modal-body">
						<form class="form-horizontal">
						<c:forEach items="${inschrijving.vragen}" var="vraag">
								<c:if test="${vraag.tag eq 'various' or vraag.tag eq 'fotos'}">
									<div class="form-group">
										<label class="col-sm-6 control-label">${vraag.vraag} (*)</label>
										<c:choose>
											<c:when test="${vraag.type eq 'YesNo'}">
												<div class="col-sm-6">
													<div class="checkbox">
														<label>
															<input type="radio" name="${vraag.uuid}" class="q" value="Y" ${vraag.antwoord eq 'Y' ? "checked='checked'" : ""}>&nbsp;Ja
														</label>
														&nbsp;&nbsp;&nbsp;
														<label>
															<input type="radio" name="${vraag.uuid}" class="q" value="N" ${vraag.antwoord eq 'N' ? "checked='checked'" : ""}>&nbsp;Neen
														</label>
													</div>
												</div>
											</c:when>
											<c:when test="${vraag.type eq 'Text'}">
												<div class="col-sm-3">
													<input id="${vraag.uuid}" type="text" class="form-control q" value="${vraag.antwoord}"></input>
												</div>
											</c:when>
											<c:when test="${vraag.type eq 'Area'}">
												<div class="col-sm-6">
													<textarea id="${vraag.uuid}" class="form-control q" rows="10" cols="64">${vraag.antwoord}</textarea>
												</div>
											</c:when>
										</c:choose>
										</div>
								</c:if>
						</c:forEach>
							</form>
					</div>
					<div class="modal-footer">
						<div class="form-group">
							<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
							<button type="button" id="q-save" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;&nbsp;Sla op</button>
							<span id ="q-status"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<c:forEach items="${related}" var="enrollment">
		
			<div id="modal-participant-${enrollment.uuid}" class="modal fade" tabindex="-1" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
			
							<h2>Deelnemer</h2>
						</div>
					<div class="modal-body">
			
						<c:if test="${inschrijving.uuid != enrollment.uuid}">
						
							<p class="alert alert-warning">
								Opgelet: voeg hier enkel deelnemers toe van hetzelfde gezin. Voor andere deelnemers moet je een aparte inschrijving maken.
							</p>
						
						</c:if>
				
						<form class="form-horizontal">
						
							<input id="deelnemer-id-${enrollment.uuid}" type="hidden" value="${enrollment.deelnemers[0].uuid}"></input>
							<div class="form-group">
								<label for="deelnemer-voor-${enrollment.uuid}" class="col-sm-4 control-label">Voornaam (*)</label>
								<div class="col-sm-6">	
									<input id="deelnemer-voor-${enrollment.uuid}" name="pd-given" type="text" class="form-control" value="${enrollment.deelnemers[0].voorNaam}"></input>
								</div>
							</div>
							<div class="form-group">
								<label for="deelnemer-familie-${enrollment.uuid}" class="col-sm-4 control-label">Familienaam (*)</label>
								<div class="col-sm-6">	
										<input id="deelnemer-familie-${enrollment.uuid}" name="pd-family" type="text" class="form-control" value="${enrollment.deelnemers[0].familieNaam}"></input>
								</div>
							</div>
							<div class="form-group">
								<label for="deelnemer-geslacht-${enrollment.uuid}" class="col-sm-4 control-label">Geslacht (*)</label>
								<div class="col-sm-3">
									<c:choose>
									<c:when test="${enrollment.deelnemers[0].geslacht eq 'F'}">
										<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="deelnemer-geslacht-${enrollment.uuid}" value="F" checked="checked">&nbsp;Vrouw
											</label>
										</div>
										<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="deelnemer-geslacht-${enrollment.uuid}" value="M">&nbsp;Man
											</label>
										</div>
									</c:when>
									<c:when test="${enrollment.deelnemers[0].geslacht eq 'M'}">
										<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="deelnemer-geslacht-${enrollment.uuid}" value="F">&nbsp;Vrouw
											</label>
										</div>
										<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="deelnemer-geslacht-${enrollment.uuid}" value="M" checked="checked">&nbsp;Man
											</label>
										</div>
									</c:when>
									<c:otherwise>
									<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="deelnemer-geslacht-${enrollment.uuid}" value="F">&nbsp;Vrouw
											</label>
										</div>
										<div class="checkbox">
											<label>
												<input type="radio" name="gender" class="deelnemer-geslacht-${enrollment.uuid}" value="M">&nbsp;Man
											</label>
										</div>
									</c:otherwise>
									</c:choose>
								</div>
							</div>
						<div class="form-group">
								<label for="participant-geboorte-${enrollment.uuid}" class="col-sm-4 control-label">Geboortedatum (*)</label>
								<div class="col-sm-3">
									<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${enrollment.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>	
									<input id="participant-geboorte-${enrollment.uuid}" type="text" class="form-control" value="${gd}" placeholder="28/08/1977"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="participant-telefoon-${enrollment.uuid}" class="col-sm-4 control-label">Telefoonnummer (*)</label>
								<div class="col-sm-4">
									<input id="participant-telefoon-${enrollment.uuid}" type="tel" class="form-control" value="${enrollment.deelnemers[0].telefoonNummer}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="participant-gsm-${enrollment.uuid}" class="col-sm-4 control-label">GSM-nummer</label>
								<div class="col-sm-4">
									<input id="participant-gsm-${enrollment.uuid}" type="tel" class="form-control" value="${enrollment.deelnemers[0].mobielNummer}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="deelnemer-email-${enrollment.uuid}" class="col-sm-4 control-label">E-mail</label>
								<div class="col-sm-6">
									<input id="deelnemer-email-${enrollment.uuid}" name="pd-email" type="email" class="form-control" value="${enrollment.deelnemers[0].email}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="adres-zipcode-${enrollment.uuid}" class="col-sm-4 control-label">PostCode (*)</label>
								<div class="col-sm-4">
									<input id="adres-zipcode-${enrollment.uuid}" name="pd-zip" type="text" class="form-control" value="${enrollment.adres.zipCode}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="adres-gemeente-${enrollment.uuid}" class="col-sm-4 control-label">Gemeente (*)</label>
								<div class="col-sm-6">
									<input id="adres-gemeente-${enrollment.uuid}" type="tel" class="form-control" value="${enrollment.adres.gemeente}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="adres-straat-${enrollment.uuid}" class="col-sm-4 control-label">Straat (*)</label>
								<div class="col-sm-6">
									<input id="adres-straat-${enrollment.uuid}" type="tel" class="form-control" value="${enrollment.adres.straat}"></input>
								</div>
						</div>
						<div class="form-group">
								<label for="adres-nummer-${enrollment.uuid}" class="col-sm-4 control-label">Huisnummer (*)</label>
								<div class="col-sm-2">
									<input id="adres-nummer-${enrollment.uuid}" type="tel" class="form-control" value="${enrollment.adres.nummer}"></input>
								</div>
						</div>
						<div id="status-comment" class="form-group">
							<label class="col-sm-4 control-label">Opmerking<br/>
							<span class="text-info">Deze opmerking mag maximaal 500 karakters bevatten.</span>
							</label>
							
							<div class="col-sm-8">
								<textarea id="status-comment-text" class="form-control" rows="10" cols="64"></textarea>
							</div>
					</div>
						<div class="form-group">
							<label for="participant-save-${enrollment.uuid}" class="col-sm-4 control-label"></label>
							<div class="col-sm-4">
								<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
								<button type="button" id="participant-save-${enrollment.uuid}" class="btn btn-primary participant-save" data-uuid="${enrollment.uuid}"><i class="fa fa-save"></i>&nbsp;&nbsp;Sla op</button>
								<span id ="participant-status-${enrollment.uuid}"></span>
							</div>
						</div>
						
						</form>
			
				</div>
			</div>
			</div>
		</div>
		
		</c:forEach>

	</div><!-- container -->
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
	
    <script>
    	var $jq = jQuery.noConflict();
    	
    	var show = function( id ) {
    		$jq(".panel" ).removeClass("show").addClass("hidden");
    		$jq("#" + id ).removeClass("hidden").addClass("show");
    	};
    	
		var saveVakanties = function( id ) {
			var list
				= "";
			$jq( ".vakantie:checked" ).each( function( index, element ) {
				if ( list.length > 0 ) {
					list = list.concat(",");
				}
				list = list.concat( element.value );	
			});
			putVakanties ( id, list, $jq("#vakantie-save"),$jq("#vakantie-status" ), refresh );
		};
		
		var saveContact = function( id ) {
			var c = new Contact( $jq("#contact-name").val(), $jq("#contact-phone").val(), $jq("#contact-email").val() );
			putContact ( id, c, $jq("#contact-save" ),$jq("#contact-status" ), refresh );
		};
		
		var toParticipant = function( id ) {
			
			window.location.hash = "#modal-participant-" + id;
			window.location.reload();
			
		};
		
		var addParticipant = function( reference ) {
			
			var enrollment =
				new Inschrijving( reference );
			
			postEnrollment( enrollment, reference, toParticipant );
			
		};
		
		var saveParticipant = function( id, isNew ) {
			
			var selector = id;
			
			var date = $jq("#participant-geboorte-" + selector).val() != "" ? moment.utc( $jq("#participant-geboorte-" + selector).val(), "DD/MM/YYYY") : null;
			
			var deelnemer
				= new Deelnemer( 
						$jq("#deelnemer-id-"+selector).val(),
						$jq("#deelnemer-voor-"+selector).val(),
						$jq("#deelnemer-familie-"+selector).val(),
						$jq(".deelnemer-geslacht-"+selector+":checked").val(),
						date,
						$jq("#participant-telefoon-"+selector).val(),
						$jq("#participant-gsm-"+selector).val(),
						$jq("#deelnemer-email-"+selector).val()
						);
			
			putDeelnemer( id, deelnemer, $jq("#participant-save-"+selector ),$jq("#participant-status-"+selector ), saveParticipantAddress );
			
		};
		
		var deleteParticipant = function( id ) {
			
			deleteEnrollment( id, $jq("#x-"+id ),$jq("#participant-delete-status-"+id ) );
			
		};
		
		var saveParticipantAddress = function( id ) {
			var selector = id;
			var a = new Adres( $jq("#adres-zipcode-"+selector).val(), $jq("#adres-gemeente-"+selector).val(), $jq("#adres-straat-"+selector).val(), $jq("#adres-nummer-"+selector).val() );
			putAddress ( id, a, $jq("#participant-save-"+selector ),$jq("#participant-status-"+selector ), refresh );
		};
		
		var saveVragen = function( id ) {
			var list
				= [];
			$jq( "input:checked.q" ).each( function( index, element ) {
				list.push( new Vraag( element.name, element.attributes["data-q"], element.value ) );
			});
			$jq( "input[type='text'].q" ).each( function( index, element ) {
				list.push( new Vraag( element.id, element.attributes["data-q"], element.value ) );
			});
			$jq( "textarea.q" ).each( function( index, element ) {
				list.push( new Vraag( element.id, element.attributes["data-q"], element.value ) );
			});
			putVragen ( id, list, $jq("#q-save" ),$jq("#q-status" ), refresh );
		};
		
		var saveStatus = function( id, value ) {
			var comment = $jq("#status-comment-text").val();
			if ( value ) {
				var sx = new Status (value, comment ,true );
				putStatus ( id, sx, $jq("#enrollment-save" ),$jq("#x-status" ), refresh );
			}
			else {
				var sx = new Status ( "AUTO", comment ,true );
				putStatus ( id, sx, $jq("#enrollment-save" ),$jq("#x-status" ), refresh );	
			}
			
			
		};
		
		var refresh = function( ) {
			window.location.hash="";
			window.location.reload();
		};
    	
		$jq("#vakantie-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveVakanties( "${inschrijving.uuid}" );
			
		});
		
		$jq("#contact-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveContact( "${inschrijving.uuid}" );
			
		});
		
		$jq("#q-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveVragen( "${inschrijving.uuid}" );
			
		});
		

		$jq(".participant-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveParticipant( $jq(this).attr("data-uuid"), false );
			
		});
		
		$jq("#participant-save-new").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveParticipant( $jq(this).attr("data-uuid"), true );
			
		});
		
		$jq("#enrollment-submit").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveStatus( "${inschrijving.uuid}" );
			
		});
		
		$jq(".enrollment-set-status").click( function( event ) {

			clearStatus();
			$jq(this).button('Even geduld...');
			
			var uuid = event.currentTarget.attributes["data-uuid"].value;
			var status = event.currentTarget.attributes["data-status"].value;
			
			saveStatus( uuid, status, $jq("#status-comment-text").val() );
			
		});
		
		$jq("#submit-show").click( function( event ) {
			
			show('div-submit');	
			
		});
		
		$jq( document ).ready(function() {
			
			if ( window.location.hash ) {
				$jq( "#" + window.location.hash.substring(1) ).modal();
			}
		    
		});
		
    </script>
  </fmt:bundle>
  </body>
</html>
