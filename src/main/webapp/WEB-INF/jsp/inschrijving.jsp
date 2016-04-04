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
					<h1>Aanvraag</h1>
					<p>
						Beheer een aanvraag.
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
					<i><spring:message code="application.status.${applicationStatus.value}"/></i><br/>
					<spring:message code="application.status.${applicationStatus.value}.description"/> <br/>
				</p>
			</div>
			
		</div>
			<div class="row">
				<c:choose>
					<c:when test="${applicationHolidaysResult.value != 'OK'}">
						<div class="col-sm-12 alert alert-warning">
							<i class="fa fa-2x fa-calendar pull-right"></i><h4><strong>Vakantie(s)</strong></h4>
							<a href="#modal-vakantie" class="todo" data-toggle="modal">Vakantie selecteren</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
						<i class="fa fa-2x fa-calendar pull-right"></i><h4><strong>Vakantie(s)</strong></h4>
						<c:forEach items="${application.vakanties}" var="vakantie">
							<span>${vakantie.naam}</span>&nbsp;(<a href="#modal-vakantie" class="todo" data-toggle="modal">wijzigen</a>)<br/>
						</c:forEach>
						</div>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${applicationContactResult.value != 'OK'}">
						<div class="col-sm-12 alert alert-warning">
							<i class="fa fa-2x fa-phone pull-right"></i><h4><strong>Contactpersoon</strong><br/></h4>
							<a href="#modal-contact" class="todo" data-toggle="modal" data-target="#modal-contact">Contactpersoon ingeven</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
						<i class="fa fa-2x fa-phone pull-right"></i><h4><strong>Contactpersoon</strong><br/></h4>
						<span>${application.contactGegevens.name}</span>&nbsp;(<a href="#modal-contact" class="todo" data-toggle="modal" data-target="#modal-contact">wijzigen</a>)
						</div>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${applicationQuestionListResult.value != 'OK'}">
						<div class="col-sm-12 alert alert-warning">
							<i class="fa fa-2x fa-2x fa-question pull-right"></i><h4><strong>Vragenlijst</strong><br/></h4>
							<span class="">Niet (volledig) ingevuld </span><br/>
							<a href="#modal-qlist-application" class="todo" data-toggle="modal" data-target="#modal-qlist-application">Vragenlijst invullen</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
							<i class="fa fa-2x fa-2x fa-question pull-right"></i><h4><strong>Vragenlijst</strong><br/></h4>
							<span>Ingevuld</span>&nbsp;(<a href="#modal-qlist-application" class="todo" data-toggle="modal" data-target="#modal-qlist-application">wijzigen</a>)
						</div>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${ enrollmentsStatus.value != 'OK' }">
						<c:choose>
							<c:when test="${ enrollmentsStatus.errorCode == 'APPLICATION_NO_ENROLLMENTS' }">
								<div class="col-sm-12 alert alert-warning">
									<i class="fa fa-2x fa-2x fa-users pull-right"></i><h4><strong>Deelnemer(s)</strong><br/></h4>
									<span class="">Nog geen deelnemers toegevoegd</span><br/>
									<a href="#modal-participant-${application.uuid}" class="todo" data-toggle="modal" data-target="#modal-participant-${application.uuid}">Deelnemer toevoegen</a>
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
										<c:forEach items="${enrollmentsStatus.object}" var="enrollmentStatus">
											<c:set var="enrollment" value="${enrollmentStatus.object}"/>
											<div class="row">
												<div class="col-sm-5 alert ${enrollmentStatus.value == 'OK' ? 'alert-success' : ( fn:startsWith( enrollmentStatus.errorCode, 'PARTICIPANT_DATA_' ) ? 'alert-danger' : 'alert-success' ) }">
													<i class="fa fa-user"></i>&nbsp;<span class="x">${enrollment.deelnemers[0].voorNaam}&nbsp;${enrollment.deelnemers[0].familieNaam}</span>&nbsp;(<a href="#modal-participant-${enrollment.uuid}" class="todo" data-toggle="modal" data-target="#modal-participant-${enrollment.uuid}">wijzigen</a>)&nbsp;&nbsp;<c:if test="${enrollment.reference != null }">(<a href="javascript:void(0);" class="enrollment-delete todo" data-uuid="${enrollment.uuid}" >verwijder</a>)&nbsp;&nbsp;</c:if>
												</div>
												<div class="col-sm-1">
												</div>
												<div class="col-sm-5 alert ${enrollmentStatus.value == 'OK' ? 'alert-success' : ( fn:startsWith( enrollmentStatus.errorCode, 'PARTICIPANT_MEDIC' ) ? 'alert-danger' : 'alert-success' ) }">
													<i class="fa fa-3 fa-medkit"></i>&nbsp;&nbsp;<a href="#modal-participant-${enrollment.uuid}-medical" class="todo" data-toggle="modal" data-target="#modal-participant-${enrollment.uuid}-medical">Medische fiche invullen/aanpassen</a>
												</div>
											</div>
										</c:forEach><br/>
										<a href="javascript:addParticipant('${application.uuid}');" class="todo">Deelnemer toevoegen</a>									
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<div class="col-sm-12 alert alert-success">
							<i class="fa fa-2x fa-2x fa-users pull-right"></i><h4><strong>Deelnemer(s)</strong><br/></h4>
							<div class="container">
								<c:forEach items="${enrollmentsStatus.object}" var="enrollmentStatus">
									<c:set var="enrollment" value="${enrollmentStatus.object}"/>
									<div class="row">
										<div class="col-sm-4 alert ${enrollmentStatus.value == 'OK' ? 'alert-success' : ( fn:startsWith( enrollmentStatus.errorCode, 'PARTICIPANT_DATA_' ) ? 'alert-danger' : 'alert-success' ) }">
											<i class="fa fa-user"></i>&nbsp;<span class="x">${enrollment.deelnemers[0].voorNaam}&nbsp;${enrollment.deelnemers[0].familieNaam}</span>&nbsp;(<a href="#modal-participant-${enrollment.uuid}" class="todo" data-toggle="modal" data-target="#modal-participant-${enrollment.uuid}">Wijzig</a>)&nbsp;&nbsp;<c:if test="${enrollment.reference != null }">(<a href="javascript:void(0);" class="enrollment-delete todo" data-uuid="${enrollment.uuid}" >Verwijder</a>)</c:if>&nbsp;&nbsp;
										</div>
										<div class="col-sm-2 alert alert-success">
											<strong><spring:message code="enrollment.status.${enrollment.status.value}"/></strong>
										</div>
										<div class="col-sm-4 alert ${enrollmentStatus.value == 'OK' ? 'alert-success' : ( fn:startsWith( enrollmentStatus.errorCode, 'PARTICIPANT_MEDIC' ) ? 'alert-danger' : 'alert-success' ) }">
											<i class="fa fa-3 fa-medkit"></i>&nbsp;&nbsp;<a href="#modal-participant-${enrollment.uuid}-medical" class="todo" data-toggle="modal" data-target="#modal-participant-${enrollment.uuid}-medical">Medische fiche invullen/aanpassen</a>
										</div>
									</div>
								</c:forEach><br/>
								<a href="javascript:addParticipant('${application.uuid}');" class="todo">Deelnemer toevoegen</a>									
							</div>
						</div>
					</c:otherwise>
				</c:choose>
					
				
			<c:choose>
					<c:when test="${applicationStatus.value =='DRAFT'}">
						<div class="col-sm-12 alert alert-info">
							<i class="fa fa-2x fa-envelope pull-right"></i><h4><strong>Indienen</strong><br/></h4>
							<c:choose>
								<c:when test="${(applicationHolidaysResult.value == 'OK') && (applicationContactResult.value == 'OK') && (applicationQuestionListResult.value == 'OK') && (enrollmentsStatus.value == 'OK') }">
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
								<c:forEach items="${application.vakanties}" var="vakantie">	
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
							<button type="button" id="vakantie-save" class="btn btn-primary"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Sla op</button>
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
								Geef hier de gegevens op van de persoon bij uw organisatie die ons secretariaat eventueel kan contacteren i.v.m deze application.
							</p>
						</div>
						<div class="modal-body">
							<form class="form-horizontal">
							<div class="form-group">
								<label for="contact-naam" class="col-sm-4 control-label">Naam (*)</label>
									<div class="col-sm-6">	
										<input id="contact-name" type="text" class="form-control" value="${application.contactGegevens.name}"></input>
									</div>
							</div>
							<div class="form-group">
								<label for="contact-naam" class="col-sm-4 control-label">Telefoon (*)</label>
									<div class="col-sm-4">	
										<input id="contact-phone" type="text" class="form-control" value="${application.contactGegevens.phone}"></input>
									</div>
							</div>
							<div class="form-group">
								<label for="contact-naam" class="col-sm-4 control-label">E-mail (*)</label>
									<div class="col-sm-3">	
										<input id="contact-email" type="email" class="form-control" value="${application.contactGegevens.email}"></input>
									</div>
							</div>
							</form>
						</div>
						<div class="modal-footer">
							<div class="form-group">
								<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
								<button type="button" id="contact-save" class="btn btn-primary"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Sla op</button>
								<span id ="contact-status"></span>
							</div>
						</div>
					</div>
				</div>
			</div>
					
			<div id="modal-qlist-application" class="modal fade" tabindex="-1" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
			
							<h2>Vragenlijst</h2>
						</div>
					<div class="modal-body">
						<form class="form-horizontal">
						<c:forEach items="${application.vragen}" var="vraag">
								<c:if test="${vraag.tag eq 'application'}">
									<div class="form-group">
										<label class="col-sm-6 control-label">${vraag.vraag} (*)</label>
										<c:choose>
											<c:when test="${vraag.type eq 'YesNo'}">
												<div class="col-sm-6">
													<div class="checkbox">
														<label>
															<input type="radio" name="${vraag.uuid}" class="q" data-tag="${vraag.tag}" value="Y" ${vraag.antwoord eq 'Y' ? "checked='checked'" : ""}>&nbsp;Ja
														</label>
														&nbsp;&nbsp;&nbsp;
														<label>
															<input type="radio" name="${vraag.uuid}" class="q" data-tag="${vraag.tag}" value="N" ${vraag.antwoord eq 'N' ? "checked='checked'" : ""}>&nbsp;Neen
														</label>
													</div>
												</div>
											</c:when>
											<c:when test="${vraag.type eq 'Text'}">
												<div class="col-sm-3">
													<input id="${vraag.uuid}" type="text" class="form-control q" data-tag="${vraag.tag}" value="${vraag.antwoord}"></input>
												</div>
											</c:when>
											<c:when test="${vraag.type eq 'Area'}">
												<div class="col-sm-6">
													<textarea id="${vraag.uuid}" class="form-control q" rows="10" cols="64" data-tag="${vraag.tag}">${vraag.antwoord}</textarea>
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
							<button type="button" id="q-save-application" class="btn btn-primary"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Sla op</button>
							<span id ="q-status-application"></span>
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
			
						<c:if test="${application.uuid != enrollment.uuid}">
						
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
								<c:forEach items="${application.vragen}" var="vraag">
								<c:if test="${vraag.tag eq 'history'}">
									<div class="form-group">
										<label class="col-sm-6 control-label">${vraag.vraag} (*)</label>
										<c:choose>
											<c:when test="${vraag.type eq 'YesNo'}">
												<div class="col-sm-6">
													<div class="checkbox">
														<label>
															<input type="radio" name="${vraag.uuid}" class="q" data-tag="${vraag.tag}" value="Y" ${vraag.antwoord eq 'Y' ? "checked='checked'" : ""}>&nbsp;Ja
														</label>
														&nbsp;&nbsp;&nbsp;
														<label>
															<input type="radio" name="${vraag.uuid}" class="q" data-tag="${vraag.tag}" value="N" ${vraag.antwoord eq 'N' ? "checked='checked'" : ""}>&nbsp;Neen
														</label>
													</div>
												</div>
											</c:when>
											<c:when test="${vraag.type eq 'Text'}">
												<div class="col-sm-3">
													<input id="${vraag.uuid}" type="text" class="form-control q" data-tag="${vraag.tag}" value="${vraag.antwoord}"></input>
												</div>
											</c:when>
											<c:when test="${vraag.type eq 'Area'}">
												<div class="col-sm-6">
													<textarea id="${vraag.uuid}" class="form-control q" rows="10" cols="64" data-tag="${vraag.tag}">${vraag.antwoord}</textarea>
												</div>
											</c:when>
										</c:choose>
										</div>
								</c:if>
						</c:forEach>
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
								<button type="button" id="participant-save-${enrollment.uuid}" class="btn btn-primary participant-save" data-uuid="${enrollment.uuid}"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Sla op</button>
								<span id ="participant-status-${enrollment.uuid}"></span>
							</div>
						</div>
						
						</form>
			
				</div>
			</div>
			</div>
		</div>
		
		<!-- Medical file -->
		<div id="modal-participant-${enrollment.uuid}-medical" class="modal fade" tabindex="-1" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
			
							<h2>Medische fiche ${enrollment.deelnemers[0].voorNaam}&nbsp;${enrollment.deelnemers[0].familieNaam}</h2>
						</div>
					<div class="modal-body">
					
						<div class="alert alert-info" role="alert">
							<strong>Opgelet</strong>: alle vragen moeten beantwoord worden om de inschrijving te kunnen versturen. Indien de vraag niet van toepassing is, vul dan "NVT" in.
						</div>
					
						<form class="form-horizontal">
							<c:forEach items="${enrollment.vragen}" var="vraag">
								<c:if test="${vraag.tag eq 'medic'}">
									<div class="form-group">
										<label class="col-sm-6 control-label">${vraag.vraag} (*)</label>
										<c:choose>
											<c:when test="${vraag.type eq 'YesNo'}">
												<div class="col-sm-6">
													<div class="checkbox">
														<label>
															<input type="radio" name="${vraag.uuid}" class="q" value="Y" data-tag="medic" ${vraag.antwoord eq 'Y' ? "checked='checked'" : ""}>&nbsp;Ja
														</label>
														&nbsp;&nbsp;&nbsp;
														<label>
															<input type="radio" name="${vraag.uuid}" class="q" value="N" data-tag="medic" ${vraag.antwoord eq 'N' ? "checked='checked'" : ""}>&nbsp;Neen
														</label>
													</div>
												</div>
											</c:when>
											<c:when test="${vraag.type eq 'Text'}">
												<div class="col-sm-3">
													<input id="${vraag.uuid}" type="text" class="form-control q" data-tag="medic" value="${vraag.antwoord != null ? vraag.antwoord : 'NVT'}"></input>
												</div>
											</c:when>
											<c:when test="${vraag.type eq 'Area'}">
												<div class="col-sm-6">
													<textarea id="${vraag.uuid}" class="form-control q" rows="10" cols="64" data-tag="medic">${vraag.antwoord != null ? vraag.antwoord : 'NVT'}</textarea>
												</div>
											</c:when>
										</c:choose>
										</div>
								</c:if>
						</c:forEach>
					
					<div class="modal-footer">
						<div class="form-group">
							<button type="button" class="btn btn-default" data-dismiss="modal">Annuleer</button>
							<button type="button" class="btn btn-primary btn-save-medic" data-uuid="${enrollment.uuid}"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Sla op</button>
							<span id ="q-status-medic"></span>
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
		
		var saveParticipantAddress = function( id ) {
			var selector = id;
			var a = new Adres( $jq("#adres-zipcode-"+selector).val(), $jq("#adres-gemeente-"+selector).val(), $jq("#adres-straat-"+selector).val(), $jq("#adres-nummer-"+selector).val() );
			putAddress ( id, a, $jq("#participant-save-"+selector ),$jq("#participant-status-"+selector ), saveParticipantList );
		};
		
		var saveParticipantList = function( id ) {
			return saveQList( id, "history" );
		};
		
		var saveQList = function( id, tag ) {
			var list
				= [];
			$jq( "input:checked.q[data-tag='" + tag + "']" ).each( function( index, element ) {
				list.push( new Vraag( element.name, tag, element.attributes["data-q"], element.value ) );
			});
			$jq( "input[type='text'].q[data-tag='" + tag + "']" ).each( function( index, element ) {
				list.push( new Vraag( element.id, tag, element.attributes["data-q"], element.value ) );
			});
			$jq( "textarea.q[data-tag='" + tag + "']" ).each( function( index, element ) {
				list.push( new Vraag( element.id, tag, element.attributes["data-q"], element.value ) );
			});
			putVragen ( id, list, $jq("#q-save-" + tag ),$jq("#q-status-" + tag ), refresh );
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
		
		var cancel = function( id ) {
			saveStatus( id, "CANCELLED", $jq("#status-comment-text").val() );
		};
		
		var deleteDraftEnrollment = function( id ) {
			
			deleteEnrollment( id, $jq("#enrollment-delete" ),$jq("#delete-status" ), refresh );
			
		};
		
		var refresh = function( ) {
			window.location.hash="";
			window.location.reload();
		};
    	
		$jq("#vakantie-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveVakanties( "${application.uuid}" );
			
		});
		
		$jq("#contact-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveContact( "${application.uuid}" );
			
		});
		
		$jq("#q-save-application").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveQList( "${application.uuid}", "application" );
			
		});
		
		$jq(".btn-save-medic").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveQList( $jq(this).attr("data-uuid"), "medic" );
			
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
