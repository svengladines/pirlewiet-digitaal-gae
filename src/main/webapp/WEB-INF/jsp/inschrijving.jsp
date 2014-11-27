<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

    <!-- Fixed navbar -->
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">PIRLEWIET DIGITAAL</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="/index.htm">START</a></li>
            <li><a href="/rs/organisation.html">ORGANISATIE</a></li>
            <li class="active"><a href="/rs/inschrijvingen.html">INSCHRIJVINGEN</a></li>
            <li><a href="help.html">HELP</a></li>
            <li><a id="logout"><i class="fa fa-sign-out"></i></a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

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
	
			<form class="form-horizontal" role="form">
			
			<c:if test="${inschrijving.status.value eq 'DRAFT'}">
			
				<p>
					Vul het onderstaande formulier in en klik op 'verstuur' om een inschrijving aan te vragen. <br/>
					Veldjes met een (*) zijn verplicht in te vullen.
				</p>
			
			</c:if>
			
			<div class="form-group">
				<label class="col-sm-4 control-label">Status</label>
					<div class="col-sm-4">	
						<p class="form-control-static text-info"><strong>${inschrijving.status}</strong></p>
					</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label">Opmerking</label>
				<div class="col-sm-6">
					<p class="form-control-static">${inschrijving.status.comment}</p>
				</div>
			</div>
			
			<c:choose>
			<c:when test="${inschrijving.status.value eq 'DRAFT'}">
				<div class="form-group">
					<label class="col-sm-4 control-label">Klaar ?</label>
					<div class="col-sm-2">
						<button type="button" id="enrollment-save" class="btn btn-primary" data-vakantie="1"><i class="fa fa-save"></i>&nbsp;&nbsp;Verstuur</button>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label"></label>
					<div class="col-sm-8">
						<span id="enrollment-error" class="error text-danger hidden">
								Er zijn fouten of onvolkomendheden in het formulier. Controleer de gegevens en probeer opnieuw AUB. 
							</span>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">Bedacht ?</label>
					<div class="col-sm-2">
						<button type="button" id="enrollment-delete" class="btn btn-warning" data-vakantie="1"><i class="fa fa-trash-o"></i>&nbsp;&nbsp;Verwijder</button>
					</div>
				</div>
			</c:when>
			<c:when test="${inschrijving.status.value eq 'CANCELLED'}">
			</c:when>
			<c:when test="${inschrijving.status.value eq 'REJECTED'}">
			</c:when>
			<c:otherwise>
				<div class="form-group">
					<label class="col-sm-4 control-label">Wijzigingen ?</label>
					<div class="col-sm-2">
						<button type="button" id="enrollment-save" class="btn btn-primary" data-vakantie="1"><i class="fa fa-save"></i>&nbsp;&nbsp;Verstuur</button>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">Inschrijving annuleren ?</label>
					<div class="col-sm-2">
						<button type="button" id="enrollment-cancel" class="btn btn-warning" data-vakantie="1"><i class="fa fa-times-circle"></i>&nbsp;&nbsp;Annuleer</button>
					</div>
				</div>
			</c:otherwise>
			</c:choose>
			
			<hr/>
		
			<h2>Vakantie</h2>
			
			<p>
				Selecteer de vakantie(s) waarvoor je wil inschrijven.
			</p>
			
				<div class="form-group">
					<label for="contact-naam" class="col-sm-4 control-label">Vakantie (*)</label>
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
									<c:when test="${vakantie.id eq vk.id}">
										<c:set var="contains" value="true" />
									</c:when>
								</c:choose>
								</c:forEach>
								<div class="checkbox">
									<label>
										<input type="checkbox" name="vak" class="vakantie" value="${vk.id}" ${contains == true ? "checked='checked'" : ""}>&nbsp;${vk.naam}&nbsp;&nbsp;&nbsp;(${start} t.e.m. ${end})
									</label>
								</div>
						</c:forEach>
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
			
			<input id="deelnemer-id" type="hidden" value="${inschrijving.deelnemers[0].id}"></input>
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
		
			<h2>Medische gegevens</h2>
			
			<c:forEach items="${inschrijving.vragen}" var="vraag">
					<c:if test="${vraag.tag eq 'medic'}">
						<div class="form-group">
							<label class="col-sm-4 control-label">${vraag.vraag} (*)</label>
							<c:choose>
								<c:when test="${vraag.type eq 'YesNo'}">
									<div class="col-sm-2">
										<div class="checkbox">
											<label>
												<input type="radio" name="${vraag.id}" class="q" value="Y" ${vraag.antwoord eq 'Y' ? "checked='checked'" : ""}>&nbsp;Ja
											</label>
											&nbsp;&nbsp;&nbsp;
											<label>
												<input type="radio" name="${vraag.id}" class="q" value="N" ${vraag.antwoord eq 'N' ? "checked='checked'" : ""}>&nbsp;Neen
											</label>
										</div>
									</div>
								</c:when>
								<c:when test="${vraag.type eq 'Text'}">
									<div class="col-sm-3">
										<input id="${vraag.id}" type="text" class="form-control q" value="${vraag.antwoord}"></input>
									</div>
								</c:when>
								<c:when test="${vraag.type eq 'Area'}">
									<div class="col-sm-6">
										<textarea id="${vraag.id}" class="form-control q" rows="10" cols="64">${vraag.antwoord}</textarea>
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
												<input type="radio" name="${vraag.id}" class="q" value="Y" ${vraag.antwoord eq 'Y' ? "checked='checked'" : ""}>&nbsp;Ja
											</label>
											&nbsp;&nbsp;&nbsp;
											<label>
												<input type="radio" name="${vraag.id}" class="q" value="N" ${vraag.antwoord eq 'N' ? "checked='checked'" : ""}>&nbsp;Neen
											</label>
										</div>
									</div>
								</c:when>
								<c:when test="${vraag.type eq 'Text'}">
									<div class="col-sm-3">
										<input id="${vraag.id}" type="text" class="form-control q" value="${vraag.antwoord}" data-q="${vraag.vraag}"></input>
									</div>
								</c:when>
								<c:when test="${vraag.type eq 'Area'}">
									<div class="col-sm-6">
										<textarea id="${vraag.id}" class="form-control q" rows="10" cols="64" data-q="${vraag.vraag}">${vraag.antwoord}</textarea>
									</div>
								</c:when>
							</c:choose>
							</div>
					</c:if>
			</c:forEach>
			
			<h2>Opmerkingen</h2>
			
			<p>
				Indien gewenst, kan je hier nog opmerkingen rond deze inschrijving kwijt.
			</p>
			
			<div class="form-group">
				<label class="col-sm-4 control-label"></label>
				<div class="col-sm-6">
					<textarea id="opmerking-tekst" class="form-control" rows="10" cols="64">${inschrijving.opmerking}</textarea>
				</div>
			</div>
			
		</form>
		
	</div><!-- container -->
	
	<div id="f" class="centered">
		<a href="#"><i class="fa fa-twitter"></i></a><a href="#"><i class="fa fa-facebook"></i></a><a href="#"><i class="fa fa-dribbble"></i></a>
	</div>
	
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
			putVakanties ( id, list, $jq("#enrollment-save"),$jq("#enrollment-error" ), saveContact );
		};
		
		var saveContact = function( id ) {
			var c = new Contact( $jq("#contact-naam").val(), $jq("#contact-telefoon").val(), $jq("#contact-email").val() );
			putContact ( id, c, $jq("#enrollment-save" ),$jq("#enrollment-error" ), saveDeelnemer );
		};
		
		var saveDeelnemer = function( id ) {
			
			var date = $jq("#deelnemer-geboorte").val() != "" ? moment( $jq("#deelnemer-geboorte").val(), "DD/MM/YYYY") : null;
			
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
			
			
			
			putDeelnemer( id, deelnemer, $jq("#enrollment-save" ),$jq("#enrollment-error" ), saveAddress );
			
		};
		
		var saveAddress = function( id ) {
			var a = new Adres( $jq("#adres-gemeente").val(), $jq("#adres-straat").val(), $jq("#adres-nummer").val() );
			putAddress ( id, a, $jq("#enrollment-save" ),$jq("#enrollment-error" ), saveVragen );
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
			putVragen ( id, list, $jq("#enrollment-save" ),$jq("#enrollment-error" ), saveStatus );
		};
		
		var saveStatus = function( id ) {
			var status = new Status ( "SUBMITTED" );
			putStatus ( id, status, $jq("#enrollment-save" ),$jq("#status-error" ) );
		};
    	
		$jq("#enrollment-save").click( function( event ) {
			
			clearError();
			
			save( "${inschrijving.id}" );
			
		});
    	
    </script>
  </body>
</html>
