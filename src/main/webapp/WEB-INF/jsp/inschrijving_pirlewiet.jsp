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
		
			<h2>Status</h2>
			
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
				<label class="col-sm-4 control-label">Opmerking bij laatste wijziging</label>
				<div class="col-sm-4">
					<p>${inschrijving.status.comment}</p>
				</div>
			</div>
			
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
				
		<h2>Contactgegevens</h2>
			
			<p>
				Geef hier de gegevens op van de persoon die Pirlewiet kan contacteren in verband met deze inschrijving.
			</p>
			
			<p>
				<span id="contact-error" class="error text-danger hidden"></span>
			</p>
			
			<div class="form-group">
				<label for="contact-naam" class="col-sm-4 control-label">Naam (*)</label>
					<div class="col-sm-3">	
						<input id="contact-naam" type="text" class="form-control" value="${inschrijving.contactGegevens.naam}"></input>
					</div>
			</div>
			<div class="form-group">
				<label for="contact-naam" class="col-sm-4 control-label">Telefoon (*)</label>
					<div class="col-sm-2">	
						<input id="contact-telefoon" type="text" class="form-control" value="${inschrijving.contactGegevens.telefoonNummer}"></input>
					</div>
			</div>
			<div class="form-group">
				<label for="contact-naam" class="col-sm-4 control-label">E-mail (*)</label>
					<div class="col-sm-3">	
						<input id="contact-email" type="email" class="form-control" value="${inschrijving.contactGegevens.email}"></input>
					</div>
			</div>
			
			<h2>Deelnemer</h2>
			
			<p>
				Geef hier de gegevens op van de deelnemer.
			</p>
			
			<p>
				<span id="deelnemer-error" class="error text-danger hidden"></span>
			</p>
			
			<input id="deelnemer-id" type="hidden" value="${inschrijving.deelnemers[0].uuid}"></input>
			<div class="form-group">
				<label for="deelnemer-voor" class="col-sm-4 control-label">Voornaam (*)</label>
				<div class="col-sm-3">	
					<input id="deelnemer-voor" type="text" class="form-control" value="${inschrijving.deelnemers[0].voorNaam}"></input>
				</div>
			</div>
			<div class="form-group">
				<label for="deelnemer-familie" class="col-sm-4 control-label">Familienaam (*)</label>
				<div class="col-sm-3">	
						<input id="deelnemer-familie" type="text" class="form-control" value="${inschrijving.deelnemers[0].familieNaam}"></input>
				</div>
			</div>
			<div class="form-group">
				<label for="deelnemer-geslacht" class="col-sm-4 control-label">Geslacht (*)</label>
				<div class="col-sm-3">
					<c:choose>
					<c:when test="${inschrijving.deelnemers[0].geslacht eq 'V'}">
						<div class="checkbox">
							<label>
								<input type="radio" name="gender" class="deelnemer-geslacht" value="V" checked="checked">&nbsp;Vrouw
							</label>
						</div>
						<div class="checkbox">
							<label>
								<input type="radio" name="gender" class="deelnemer-geslacht" value="M">&nbsp;Man
							</label>
						</div>
					</c:when>
					<c:when test="${inschrijving.deelnemers[0].geslacht eq 'M'}">
						<div class="checkbox">
							<label>
								<input type="radio" name="gender" class="deelnemer-geslacht" value="V">&nbsp;Vrouw
							</label>
						</div>
						<div class="checkbox">
							<label>
								<input type="radio" name="gender" class="deelnemer-geslacht" value="M" checked="checked">&nbsp;Man
							</label>
						</div>
					</c:when>
					<c:otherwise>
					<div class="checkbox">
							<label>
								<input type="radio" name="gender" class="deelnemer-geslacht" value="V">&nbsp;Vrouw
							</label>
						</div>
						<div class="checkbox">
							<label>
								<input type="radio" name="gender" class="deelnemer-geslacht" value="M">&nbsp;Man
							</label>
						</div>
					</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="form-group">
					<label for="deelnemer-geboorte" class="col-sm-4 control-label">Geboortedatum (*)</label>
					<div class="col-sm-2">
						<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>	
						<input id="deelnemer-geboorte" type="text" class="form-control" value="${gd}" placeholder="28/08/1977"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="deelnemer-telefoon" class="col-sm-4 control-label">Telefoonnummer (*)</label>
					<div class="col-sm-2">
						<input id="deelnemer-telefoon" type="tel" class="form-control" value="${inschrijving.deelnemers[0].telefoonNummer}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="deelnemer-gsm" class="col-sm-4 control-label">GSM-nummer</label>
					<div class="col-sm-2">
						<input id="deelnemer-gsm" type="tel" class="form-control" value="${inschrijving.deelnemers[0].mobielNummer}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="deelnemer-email" class="col-sm-4 control-label">E-mail (*)</label>
					<div class="col-sm-3">
						<input id="deelnemer-email" type="tel" class="form-control" value="${inschrijving.deelnemers[0].email}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-zipcode" class="col-sm-4 control-label">PostCode (*)</label>
					<div class="col-sm-2">
						<input id="adres-zipcode" type="tel" class="form-control" value="${inschrijving.adres.zipCode}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-gemeente" class="col-sm-4 control-label">Gemeente (*)</label>
					<div class="col-sm-2">
						<input id="adres-gemeente" type="tel" class="form-control" value="${inschrijving.adres.gemeente}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-straat" class="col-sm-4 control-label">Straat (*)</label>
					<div class="col-sm-3">
						<input id="adres-straat" type="tel" class="form-control" value="${inschrijving.adres.straat}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-nummer" class="col-sm-4 control-label">Huisnummer (*)</label>
					<div class="col-sm-2">
						<input id="adres-nummer" type="tel" class="form-control" value="${inschrijving.adres.nummer}"></input>
					</div>
			</div>
			
			<h2>Varia</h2>
			
			<c:forEach items="${inschrijving.vragen}" var="vraag">
					<c:if test="${vraag.tag eq 'various'}">
						<div class="form-group">
							<label class="col-sm-4 control-label">${vraag.vraag} (*)</label>
							<c:choose>
								<c:when test="${vraag.type eq 'YesNo'}">
									<div class="col-sm-2">
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
		
			<h2>Medische gegevens</h2>
			
			<c:forEach items="${inschrijving.vragen}" var="vraag">
					<c:if test="${vraag.tag eq 'medic'}">
						<div class="form-group">
							<label class="col-sm-4 control-label">${vraag.vraag}</label>
							<c:choose>
								<c:when test="${vraag.type eq 'YesNo'}">
									<div class="col-sm-2">
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
			
			<h2>Foto's</h2>
			
			<p>
				Pirlewiet gebruikt leuke foto’s van op alle kampen en vakanties in zijn folders, op de website en in andere publicaties waarin Pirlewiet aan bod komt.
			</p>
			
			<c:forEach items="${inschrijving.vragen}" var="vraag">
					<c:if test="${vraag.tag eq 'fotos'}">
						<div class="form-group">
							<label class="col-sm-4 control-label">${vraag.vraag} (*)</label>
							<c:choose>
								<c:when test="${vraag.type eq 'YesNo'}">
									<div class="col-sm-2">
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
										<input id="${vraag.uuid}" type="text" class="form-control q" value="${vraag.antwoord}" data-q="${vraag.vraag}"></input>
									</div>
								</c:when>
								<c:when test="${vraag.type eq 'Area'}">
									<div class="col-sm-6">
										<textarea id="${vraag.uuid}" class="form-control q" rows="10" cols="64" data-q="${vraag.vraag}">${vraag.antwoord}</textarea>
									</div>
								</c:when>
							</c:choose>
							</div>
					</c:if>
			</c:forEach>
			
	</div><!-- container -->
			
	<div class="lg">
	
		<div class="container">
		
			<div class="row">
	
				<h2>Gewijzigd ?</h2>
				
					<div id="status-comment" class="form-group" class="form-group">
						<label class="col-sm-4 control-label">Opmerking bij wijzigingen<br/><span class="text-warning">De doorverwijzer ziet deze opmerking</span><br/>
						<span class="text-info">Deze commentaar mag maximaal 500 karakters bevatten.</span>
						</label>
						
						
						<div class="col-sm-6">
							<textarea id="status-comment-text" class="form-control" rows="10" cols="64"></textarea>
						</div>
					</div>
					
					<div id="status-email" class="form-group">
							<label for="contact-naam" class="col-sm-4 control-label">Verwittig contactpersoon per e-mail</label>
							<div class="col-sm-6">
								<div class="checkbox">
									<label>
										<input type="checkbox" name="vak" class="q-email" value="true">Ja</input>
									</label>
								</div>
							</div>
						</div>
					<div class="form-group">
						<label class="col-sm-4 control-label">
						</label>
						<div class="col-sm-6">
							<span id="x-status" class="status"></span>
						</div>
					</div>
					<div id="status-confirm" class="form-group">
						<label class="col-sm-4 control-label">
						</label>
						<div class="col-sm-2 bg-info">
							<button type="button" id="enrollment-save" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;&nbsp;Verstuur</button>
						</div>
					</div>
					
				</div>
			</div>
		</div>	
			
		</form>
		
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
	
    <script>
    	var $jq = jQuery.noConflict();
    	
		var save = function( id ) {
			var list
				= "";
			$jq( ".vakantie:checked" ).each( function( index, element ) {
				if ( list.length > 0 ) {
					list = list.concat(",");
				}
				list = list.concat( element.value );	
			});
			putVakanties ( id, list, $jq("#enrollment-save"),$jq("#x-status" ), saveContact );
		};
		
		var saveContact = function( id ) {
			var c = new Contact( $jq("#contact-naam").val(), $jq("#contact-telefoon").val(), $jq("#contact-email").val() );
			putContact ( id, c, $jq("#enrollment-save" ),$jq("#x-status" ), saveDeelnemer );
		};
		
		var saveDeelnemer = function( id ) {
			
			var date = $jq("#deelnemer-geboorte").val() != "" ? moment.utc( $jq("#deelnemer-geboorte").val(), "DD/MM/YYYY") : null;
			
			var deelnemer
				= new Deelnemer( 
						$jq("#deelnemer-id").val(),
						$jq("#deelnemer-voor").val(),
						$jq("#deelnemer-familie").val(),
						$jq(".deelnemer-geslacht:checked").val(),
						date,
						$jq("#deelnemer-telefoon").val(),
						$jq("#deelnemer-gsm").val(),
						$jq("#deelnemer-email").val()
						);
			
			
			
			putDeelnemer( id, deelnemer, $jq("#enrollment-save" ),$jq("#x-status" ), saveAddress );
			
		};
		
		var saveAddress = function( id ) {
			var a = new Adres( $jq("#adres-zipcode").val(), $jq("#adres-gemeente").val(), $jq("#adres-straat").val(), $jq("#adres-nummer").val() );
			putAddress ( id, a, $jq("#enrollment-save" ),$jq("#x-status" ), saveVragen );
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
			putVragen ( id, list, $jq("#enrollment-save" ),$jq("#x-status" ), saveStatus );
		};
		
		var saveStatus = function( id ) {
			var status
				= new Status( $jq("option:selected").val(), $jq("#status-comment-text").val(), $jq("input:checked.q-email").val() );
			putStatus ( id, status, $jq("#enrollment-save" ),$jq("#x-status" ) );
		};
		
		var cancel = function( id ) {
			saveStatus( id, "CANCELLED", $jq("#status-comment-text").val() );
		};
    	
		$jq("#enrollment-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			save( "${inschrijving.uuid}" );
			
		});
		
		$jq("#enrollment-cancel").click( function( event ) {
			
			clearError();
			$jq(this).button('loading');
			
			cancel( "${inschrijving.uuid}" );
			
		});
		
		$jq("#enrollment-cancel").hover( function( event ) {
			
			$jq("#cancel-warning").removeClass("hidden").addClass("show");
			
		}, function( event ) {
			
			$jq("#cancel-warning").removeClass("show").addClass("hidden");
			
		});
		
		$jq("#enrollment-delete").click( function( event ) {
			
			$jq(this).button('loading');
			
			deleteInschrijving( "${inschrijving.uuid}", $jq("#enrollment-delete" ),$jq("#status-error" )  );
			
		});
    	
    </script>
  </body>
</html>