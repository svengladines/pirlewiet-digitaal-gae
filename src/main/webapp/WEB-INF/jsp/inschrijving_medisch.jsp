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
					<h1>Medische fiche</h1>
					<p>
						Vul hier de medische gegevens in van een deelnemer.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>
	
	<form class="form-horizontal" role="form">		

	<div class="container">
	
			<p class="text-info">
					Alle vragen zijn verplicht te beantwoorden.
			</p>
	
			<h2>Vragenlijst</h2>
			
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
			
	</div><!-- container -->
	
	<div class="lg">
	
		<div class="container">
	
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
			
			<div class="form-group">
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
		
		$jq("#enrollment-cancel").click( function( event ) {
			
			window.location.reload();
			
		});
		
    </script>
  </body>
</html>
