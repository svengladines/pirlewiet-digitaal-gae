<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

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

	<form class="form-horizontal" role="form">

	<div class="container">
	
	<div class="row">
	
			<br/>
	
			<div class="form-group">
				<label class="col-sm-4 control-label">Status</label>
				<div class="col-sm-2">
					<select id="status" class="form-control">
 							<option value="SUBMITTED" ${inschrijving.status.value eq 'SUBMITTED' ? 'selected="selected"' : "" }>Ingediend</option>
 							<option value="TRANSIT" ${inschrijving.status.value eq 'TRANSIT' ? 'selected="selected"' : "" }>In behandeling</option>
 							<option value="WAITINGLIST" ${inschrijving.status.value eq 'WAITINGLIST' ? 'selected="selected"' : "" }>Wachtlijst</option>
 							<option value="ACCEPTED" ${inschrijving.status.value eq 'ACCEPTED' ? 'selected="selected"' : "" }>Aanvaard</option>
 							<option value="REJECTED" ${inschrijving.status.value eq 'REJECTED' ? 'selected="selected"' : "" }>Geweigerd</option>
 							<option value="CANCELLED" ${inschrijving.status.value eq 'CANCELLED' ? 'selected="selected"' : "" }>Geannuleerd</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label">Opmerking</label>
				<div class="col-sm-6">
					<p class="form-control-static">${inschrijving.status.comment}</p>
				</div>
			</div>
			
			<div id="status-comment" class="form-group hidden" class="form-group">
				<label class="col-sm-4 control-label">Opmerking bij nieuwe status<br/><span class="text-warning">De doorverwijzer ziet deze opmerking</span>
				</label>
				<div class="col-sm-6">
					<textarea id="status-comment-text" class="form-control" rows="10" cols="64"></textarea>
				</div>
			</div>
			
			<div id="status-email" class="form-group hidden">
					<label for="contact-naam" class="col-sm-4 control-label">Verwittig contactpersoon per e-mail</label>
					<div class="col-sm-6">
						<div class="checkbox">
							<label>
								<input type="checkbox" name="vak" class="q-email" value="true">Ja</input>
							</label>
						</div>
					</div>
				</div>
			
			<div id="status-confirm" class="form-group hidden">
				<label class="col-sm-4 control-label">
				</label>
				<div class="col-sm-2 bg-info">
					<button type="button" id="status-confirmBtn" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;&nbsp;Verstuur</button>
				</div>
			</div>
			
		</div>
		
		<hr/>
			
		<div class="row">
			
			<h2>Vakantie</h2>
			
			<p>
				Selecteer de vakantie(s) waarvoor je wil inschrijven.
			</p>
			
				<div class="form-group">
					<label for="contact-naam" class="col-sm-4 control-label">Vakantie</label>
					<div class="col-sm-6">
						<c:if test="${empty vakanties}">
							<span>Er is momenteel geen vakantie waar men voor kan inschrijven</span>
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
				</div>
		</div>
				
		<div class="row">
		
			<h2>Deelnemer</h2>
			
			<p>
				<span id="deelnemer-error" class="error text-danger hidden"></span>
			</p>
			
			<input id="deelnemer-id" type="hidden" value="${inschrijving.deelnemers[0].uuid}"></input>
			<div class="form-group">
				<label for="deelnemer-voor" class="col-sm-4 control-label">Voornaam</label>
				<div class="col-sm-3">	
					<p class="form-control-static">${inschrijving.deelnemers[0].voorNaam}</p>
				</div>
			</div>
			<div class="form-group">
				<label for="deelnemer-familie" class="col-sm-4 control-label">Familienaam</label>
				<div class="col-sm-3">	
						<p class="form-control-static">${inschrijving.deelnemers[0].familieNaam}</p>
				</div>
			</div>
			<div class="form-group">
				<label for="deelnemer-geslacht" class="col-sm-4 control-label">Geslacht</label>
				<div class="col-sm-3">
					<c:choose>
					<c:when test="${inschrijving.deelnemers[0].geslacht eq 'V'}">
							<p class="form-control-static">Vrouw</p>
					</c:when>
					<c:otherwise>
							<p class="form-control-static">Man</p>
						
					</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="form-group">
					<label for="deelnemer-geboorte" class="col-sm-4 control-label">Geboortedatum</label>
					<div class="col-sm-2">
						<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>	
						<p class="form-control-static">${gd}</p>
					</div>
			</div>
			<div class="form-group">
					<label for="deelnemer-telefoon" class="col-sm-4 control-label">Telefoonnummer</label>
					<div class="col-sm-2">
						<p class="form-control-static">${inschrijving.deelnemers[0].telefoonNummer}</p>
					</div>
			</div>
			<div class="form-group">
					<label for="deelnemer-gsm" class="col-sm-4 control-label">GSM-nummer</label>
					<div class="col-sm-2">
						<p class="form-control-static">${inschrijving.deelnemers[0].mobielNummer}</p>
					</div>
			</div>
			<div class="form-group">
					<label for="deelnemer-email" class="col-sm-4 control-label">E-mail</label>
					<div class="col-sm-3">
						<p class="form-control-static">${inschrijving.deelnemers[0].email}</p>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-gemeente" class="col-sm-4 control-label">Gemeente</label>
					<div class="col-sm-2">
						<p class="form-control-static">${inschrijving.adres.gemeente}</p>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-straat" class="col-sm-4 control-label">Straat</label>
					<div class="col-sm-3">
						<p class="form-control-static">${inschrijving.adres.straat}</p>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-nummer" class="col-sm-4 control-label">Huisnummer</label>
					<div class="col-sm-2">
						<p class="form-control-static">${inschrijving.adres.nummer}</p>
					</div>
			</div>
		</div>
		
		<div class="row">
		
			<h2>Varia</h2>
			
			<c:forEach items="${inschrijving.vragen}" var="vraag">
					<c:if test="${vraag.tag eq 'various'}">
						<div class="form-group">
							<label class="col-sm-4 control-label">${vraag.vraag}</label>
							<div class="col-sm-8">
								<c:choose>
									<c:when test="${vraag.type eq 'YesNo'}">
										
											<p class="form-control-static">${vraag.antwoord eq 'Y' ? 'Ja' : 'Nee'}</p>
	
									</c:when>
									<c:when test="${vraag.type eq 'Text'}">
										<div class="col-sm-8">
											<p class="form-control-static">${vraag.antwoord}</p>
										</div>
									</c:when>
									<c:when test="${vraag.type eq 'Area'}">
										<div class="col-sm-8">
											<p class="form-control-static">${vraag.antwoord}</p>
										</div>
									</c:when>
								</c:choose>
							</div>
						</div>
					</c:if>
			</c:forEach>
			
		</div>
		
		<div class="row">
		
			<h2>Medische gegevens</h2>
			
			<c:forEach items="${inschrijving.vragen}" var="vraag">
					<c:if test="${vraag.tag eq 'medic'}">
						<div class="form-group">
							<label class="col-sm-4 control-label">${vraag.vraag}</label>
							<div class="col-sm-8">
								<c:choose>
									<c:when test="${vraag.type eq 'YesNo'}">
										
											<p class="form-control-static">${vraag.antwoord eq 'Y' ? 'Ja' : 'Nee'}</p>
	
									</c:when>
									<c:when test="${vraag.type eq 'Text'}">
										<div class="col-sm-8">
											<p class="form-control-static">${vraag.antwoord}</p>
										</div>
									</c:when>
									<c:when test="${vraag.type eq 'Area'}">
										<div class="col-sm-8">
											<p class="form-control-static">${vraag.antwoord}</p>
										</div>
									</c:when>
								</c:choose>
							</div>
						</div>
					</c:if>
			</c:forEach>
			
		</div>
		
		<div class="row">
		
			<h2>Foto's</h2>
			
			<c:forEach items="${inschrijving.vragen}" var="vraag">
					<c:if test="${vraag.tag eq 'fotos'}">
						<div class="form-group">
							<label class="col-sm-4 control-label">${vraag.vraag}</label>
							<div class="col-sm-8">
								<c:choose>
									<c:when test="${vraag.type eq 'YesNo'}">
										
											<p class="form-control-static">${vraag.antwoord eq 'Y' ? 'Ja' : 'Nee'}</p>
	
									</c:when>
									<c:when test="${vraag.type eq 'Text'}">
										<div class="col-sm-8">
											<p class="form-control-static">${vraag.antwoord}</p>
										</div>
									</c:when>
									<c:when test="${vraag.type eq 'Area'}">
										<div class="col-sm-8">
											<p class="form-control-static">${vraag.antwoord}</p>
										</div>
									</c:when>
								</c:choose>
							</div>
						</div>
					</c:if>
			</c:forEach>
			
		</div>
		
		<div class="row">
		
			<h2>Opmerkingen</h2>
			
			<div class="form-group">
				<label class="col-sm-4 control-label"></label>
				<div class="col-sm-6">
					<p class="form-control-static">${inschrijving.opmerking}</p>
				</div>
			</div>
			
		</div>
		
		<div class="row">
		
			<h2>Organisatie</h2>
			
			<p>
				<span id="contact-error" class="error text-danger hidden"></span>
			</p>
			
			<div class="form-group">
				<label for="contact-naam" class="col-sm-4 control-label">Naam</label>
					<div class="col-sm-8">
						<p class="form-control-static">${inschrijving.organisatie.naam}</p>
					</div>
			</div>
			
		</div>
		
		<div class="row">
		
			<h2>Contactgegevens</h2>
			
			<p>
				<span id="contact-error" class="error text-danger hidden"></span>
			</p>
			
			<div class="form-group">
				<label for="contact-naam" class="col-sm-4 control-label">Naam</label>
					<div class="col-sm-8">
						<p class="form-control-static">${inschrijving.contactGegevens.naam}</p>
					</div>
			</div>
			<div class="form-group">
				<label for="contact-naam" class="col-sm-4 control-label">Telefoon</label>
					<div class="col-sm-2">	
						<p class="form-control-static">${inschrijving.contactGegevens.telefoonNummer}</p>
					</div>
			</div>
			<div class="form-group">
				<label for="contact-naam" class="col-sm-4 control-label">E-mail</label>
					<div class="col-sm-3">	
						<p class="form-control-static">${inschrijving.contactGegevens.email}</p>
					</div>
			</div>
			
		</div>
		
		
		</form>
		
	</div><!-- container -->
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
	
    <script>
    	var $jq = jQuery.noConflict();
    	
		var saveStatus = function( id ) {
			var status
				= new Status( $jq("option:selected").val(), $jq("#status-comment-text").val(), $jq("input:checked.q-email").val() );
			putStatus ( id, status, $jq("#status-confirmBtn" ),$jq("#status-error" ) );
		};
		
		$jq("#status-confirmBtn").click( function( event ) {
			saveStatus( "${inschrijving.uuid}" );
		});
    	
		$jq("#status").change( function( event ) {
			
			$jq("#status-comment").removeClass("hidden").addClass("show");
			$jq("#status-email").removeClass("hidden").addClass("show");
			$jq("#status-confirm").removeClass("hidden").addClass("show");
			
		});
    	
    </script>
  </body>
</html>
