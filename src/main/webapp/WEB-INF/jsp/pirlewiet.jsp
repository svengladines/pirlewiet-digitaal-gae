<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

   <jsp:include page="/WEB-INF/jsp/menu_pirlewiet.jsp">
    	<jsp:param name="active" value="organisation"/>
    </jsp:include>

	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Pirlewiet</h1>
					<p>
						Beheer hier het profiel van Pirlewiet.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>

	<div class="container">
	
			<form class="form-horizontal" role="form">
		
		<div class="row mandatory">
		
			<h2>Gegevens</h2>
			
			<p>
				Velden met een (*) moet je zeker invullen.
			</p>
			
			<input id="organisation-id" type="hidden" value="${organisation.id}"></input>
			<div class="form-group">
				<label for="organisation-name" class="col-sm-4 control-label">Naam (*)</label>
				<div class="col-sm-3">	
					<input id="organisation-name" type="text" class="form-control" value="${organisation.naam}"></input>
				</div>
			</div>
			<div class="form-group">
					<label for="organisation-telefoon" class="col-sm-4 control-label">Telefoonnummer (*)</label>
					<div class="col-sm-2">
						<input id="organisation-telephone" type="tel" class="form-control" value="${organisation.telefoonNummer}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="organisation-gsm" class="col-sm-4 control-label">GSM-nummer</label>
					<div class="col-sm-2">
						<input id="organisation-gsm" type="tel" class="form-control" value="${organisation.gsmNummer}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="organisation-email" class="col-sm-4 control-label">E-mail (*)</label>
					<div class="col-sm-3">
						<input id="organisation-email" type="tel" class="form-control" value="${organisation.email}"></input>
					</div>
			</div>
			</div>
			<div class="form-group">
					<label for="organisation-alternative-email" class="col-sm-4 control-label">Alternatief e-mailadres</label>
					<div class="col-sm-3">
						<input id="organisation-alternative-email" type="tel" class="form-control" value="${organisation.alternativeEmail}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-straat" class="col-sm-4 control-label">Gemeente (*)</label>
					<div class="col-sm-2">
						<input id="adres-gemeente" type="tel" class="form-control" value="${organisation.adres.gemeente}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-straat" class="col-sm-4 control-label">Straat (*)</label>
					<div class="col-sm-3">
						<input id="adres-straat" type="tel" class="form-control" value="${organisation.adres.straat}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-nummer" class="col-sm-4 control-label">Huisnummer (*)</label>
					<div class="col-sm-2">
						<input id="adres-nummer" type="tel" class="form-control" value="${organisation.adres.nummer}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="organisation-error" class="col-sm-4 control-label"></label>
					<div class="col-sm-8">
						<span id="organisation-ok" class="error text-success hidden">
							Gegevens werden met succes verwerkt. Je kan nu inschrijvingen maken en beheren.
						</span>
						<span id="organisation-error" class="error text-danger hidden">
							Het formulier kon niet worden verwerkt. Controleer de gegevens en probeer opnieuw AUB. 
						</span>
					</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label"></label>
				<div class="col-sm-8">
					<button type="button" id="organisation-save" class="btn btn-primary" data-vakantie="1"><i class="fa fa-save"></i>&nbsp;&nbsp;Verstuur</button>
				</div>
			</div>
			
		</form>
		
	</div><!-- container -->
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
	
    <script>
    	var $jq = jQuery.noConflict();
    	
    	$jq(".pop").popover();
    	
		var saveOrganisation = function() {
			
			var organisation
				= new Organisation( 
						$jq("#organisation-id").val(),
						$jq("#organisation-name").val(),
						$jq("#organisation-telephone").val(),
						$jq("#organisation-gsm").val(),
						$jq("#organisation-email").val(),
						$jq("#organisation-alternative-email").val()
						);
			
			var a = new Adres( $jq("#adres-gemeente").val(), $jq("#adres-straat").val(), $jq("#adres-nummer").val() );
			
			if ( organisation.id == 0 ) {
				postOrganisation( organisation, $jq("#organisation-save" ),$jq("#organisation-error" ), putOrganisationAddress, a );
			}
			else {
				putOrganisation( organisation, $jq("#organisation-save" ),$jq("#organisation-error" ), putOrganisationAddress, a );	
			}
			
		};
		
		    	
		$jq("#organisation-save").click( function( event ) {
			
			clearError();
			
			saveOrganisation();
			
		});
    	
    </script>
  </body>
</html>
