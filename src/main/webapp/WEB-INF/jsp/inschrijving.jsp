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
						Maak of beheer een inschrijving.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>
	
	<div class="container">
	
			<br/>
			
			<div class="container alert alert-info">
				<div class="row">
					<div class="col-sm-4">
						Vakantie(s)
					</div>
					<div class="col-sm-8">
					<c:choose>
					<c:when test="${empty inschrijving.vakanties}">
						<a href="javascript:show('div-vakantie');" class="todo">Vakantie selecteren</a>
					</c:when>
					<c:otherwise>
						<c:forEach items="${inschrijving.vakanties}" var="vakantie">
							<a href="javascript:show('div-vakantie');" class="done">${vakantie.naam}</a><br/>
						</c:forEach>
					</c:otherwise>
					</c:choose>
					
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4">
						Contactpersoon bij doorverwijzer
					</div>
					<div class="col-sm-8">
					<c:choose>
						<c:when test="${inschrijving.contactGegevens.name == null}">
							<a href="javascript:show('div-contact');" class="todo">Contactpersoon ingeven</a>
						</c:when>
						<c:otherwise>
							<a href="javascript:show('div-contact');" class="done">${inschrijving.contactGegevens.name}</a><br/>
						</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4">
						Vragenlijst
					</div>
					<div class="col-sm-8">
					<c:choose>
						<c:when test="${not areAllMandatoryQuestionsAnswered}">
							<a href="javascript:show('div-various');" class="todo">Antwoorden ingeven</a>
						</c:when>
						<c:otherwise>
							<a href="javascript:show('div-various');" class="done">Antwoorden aanpassen</a><br/>
						</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<br/>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4">
						Deelnemer(s)
					</div>
					<div class="col-sm-8">
						<a href="javascript:show('div-participant-empty');">Deelnemer toevoegen</a>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<br/>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4">
						Status
					</div>
					<div class="col-sm-8">
						<strong>Concept</strong>
					</div>
				</div>
							
			</div>
			
						
			<div id="div-vakantie" class="panel hidden">
				<h2>Vakantie(s)</h2>
				<p>
					Selecteer de vakantie(s) waarvoor je wil inschrijven.
				</p>
				<div>
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
				<div class="">
					<button type="button" id="vakantie-save" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;&nbsp;Sla op</button>
					<span id ="vakantie-status"></span>
				</div>
			</div>	
			<div id="div-contact" class="panel hidden">
				<h2>Contactgegevens doorverwijzer</h2>
				
				<p>
					Geef hier de gegevens op van de persoon bij uw organisatie die ons secretariaat eventueel kan contacteren i.v.m deze inschrijving.
				</p>
				
				<form class="form-horizontal">
				<div class="form-group">
					<label for="contact-naam" class="col-sm-4 control-label">Naam (*)</label>
						<div class="col-sm-3">	
							<input id="contact-name" type="text" class="form-control" value="${inschrijving.contactGegevens.name}"></input>
						</div>
				</div>
				<div class="form-group">
					<label for="contact-naam" class="col-sm-4 control-label">Telefoon (*)</label>
						<div class="col-sm-2">	
							<input id="contact-phone" type="text" class="form-control" value="${inschrijving.contactGegevens.phone}"></input>
						</div>
				</div>
				<div class="form-group">
					<label for="contact-naam" class="col-sm-4 control-label">E-mail (*)</label>
						<div class="col-sm-3">	
							<input id="contact-email" type="email" class="form-control" value="${inschrijving.contactGegevens.email}"></input>
						</div>
				</div>
				<div class="form-group">
					<button type="button" id="contact-save" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;&nbsp;Sla op</button>
					<span id ="contact-status"></span>
				</div>
				</form>
			</div>
			
			<div id="div-various" class="panel hidden">
			
				<h2>Vragenlijst</h2>
				
				<form class="form-horizontal">
				<c:forEach items="${inschrijving.vragen}" var="vraag">
						<c:if test="${vraag.tag eq 'various' or vraag.tag eq 'fotos'}">
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
				<div class="form-group">
					<button type="button" id="q-save" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;&nbsp;Sla op</button>
					<span id ="q-status"></span>
				</div>
				</form>
			</div>
			
			<div id="div-participant-empty" class="panel hidden">
				
				<h2>Deelnemer</h2>
				
				<p>
					Geef hier de gegevens op van de deelnemer.
				</p>
				
				<form class="form-horizontal">
				
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
							<c:when test="${inschrijving.deelnemers[0].geslacht eq 'F'}">
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
				<div class="form-group">
					<label for="participant-save" class="col-sm-4 control-label"></label>
					<div class="col-sm-4">
						<button type="button" id="participant-save" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;&nbsp;Sla op</button>
						<span id ="participant-status"></span>
					</div>
				</div>
				
				</form>
			
			</div>

				
			
		<div id="div-submit" class="panel hidden">
	
			<c:choose>
			<c:when test="${inschrijving.status.value eq 'DRAFT'}">
			
				<h2>Indienen</h2>
				<p>
					Controleer de gegevens in het formulier en klik op 'Verstuur' om de inschrijving in te dienen.<br/>
				</p>
			</c:when>
			<c:otherwise>
				
				<h2>Gewijzigd ?</h2>
				<p>
					Klik op 'Verstuur' om de wijzigingen door te sturen.
				</p>
			</c:otherwise>
			</c:choose>
			
			<div>
					<label class="col-sm-4 control-label">
					</label>
					<div class="col-sm-6">
						<span id="x-status" class="status"></span>
					</div>
			</div>
							
			<div id="status-comment" class="form-group" class="form-group">
					<label class="col-sm-4 control-label">Opmerking<br/>
					<span class="text-info">Deze opmerking is bestemd voor de secretariaatsmedewerkers die de inschrijving zullen behandelen.</span><br/>
					<span class="">Indien je een inschrijving wijzigde, licht dan hier kort toe wat er veranderd is.</span><br/>
					<span class="text-info">Deze opmerking mag maximaal 500 karakters bevatten.</span>
					</label>
					
					
					<div class="col-sm-6">
						<textarea id="status-comment-text" class="form-control" rows="10" cols="64"></textarea>
					</div>
			</div>
							
				<input type="hidden" name="vak" class="q-email" value="true"/>
				<div class="form-group">
					<label class="col-sm-4 control-label">
					</label>
					<div class="col-sm-6">
						<span id="x-status" class="status"></span>
					</div>
				</div>
				<div id="create-confirm" class="form-group">
					<label class="col-sm-4 control-label">
					</label>
					<div class="col-sm-4">
						<button type="button" id="enrollment-save" class="btn btn-primary"><i class="fa fa-save"></i>&nbsp;&nbsp;Verstuur</button>
						<button type="button" id="enrollment-cancel" class="btn btn-warning" data-vakantie="1" data-loading-text="Even geduld..."><i class="fa fa-trash-o"></i>&nbsp;&nbsp;Annuleer</button>
					</div>
				</div>
							
		</div>
	</form>
	
	</div>
	
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
			putVakanties ( id, list, $jq("#vakantie-save"),$jq("#vakantie-status" ) );
		};
		
		var saveContact = function( id ) {
			var c = new Contact( $jq("#contact-name").val(), $jq("#contact-phone").val(), $jq("#contact-email").val() );
			putContact ( id, c, $jq("#contact-save" ),$jq("#contact-status" ) );
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
			putVragen ( id, list, $jq("#q-save" ),$jq("#q-status" ) );
		};
		
		var saveStatus = function( id ) {
			var comment = $jq("#status-comment-text").val();
			var status = new Status ( "AUTO", comment ,true );
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
		
		$jq("#enrollment-cancel").click( function( event ) {
			
			window.location.reload();
			
		});
		
    </script>
  </body>
</html>
