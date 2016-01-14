<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

      <jsp:include page="/WEB-INF/jsp/menu.jsp">
    	<jsp:param name="active" value="organisation"/>
    </jsp:include>

	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Mijn Organisatie</h1>
					<p>
						Beheer het profiel van jouw organisatie.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>

	<div class="container">
	
	<br/>
	
	<div class="row">
	
		<form class="form-horizontal" role="form">
			<c:choose>
			<c:when test="${incomplete == false}">
				<div class="alert alert-success" role="alert">
					<strong>Het profiel van jouw organisatie is in orde.</strong>
				</div>
			</c:when>
			<c:otherwise>
			<div class="alert alert-danger" role="alert">
				<p>
					<strong>Het profiel van je organisatie is niet volledig.</strong><br/>
					Je moet het profiel in orde brengen voordat je inschrijvingen kan aanmaken of beheren.
				</p>
			</div>
			</c:otherwise>
			</c:choose>
		
			<h2>Gegevens</h2>
			
			<p>
				Velden met een (*) moet je zeker invullen.
			</p>
			
			<input id="organisation-id" type="hidden" value="${organisation.uuid}"></input>
			<div class="form-group">
				<label for="organisation-name" class="col-sm-4 control-label">Naam organisatie (*)</label>
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
						<input id="organisation-email" type="email" class="form-control" value="${organisation.email}" required pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="organisation-alternative-email" class="col-sm-4 control-label">Alternatief e-mailadres</label>
					<div class="col-sm-3">
						<input id="organisation-alternative-email" type="email" class="form-control" value="${organisation.alternativeEmail}" pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-zipcode" class="col-sm-4 control-label">Postcode (*)</label>
					<div class="col-sm-2">
						<input id="adres-zipcode" type="tel" class="form-control" value="${organisation.adres.zipCode}"></input>
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
			<c:if test="${!isPirlewiet}">
				<div class="form-group">
					<label class="col-sm-4 control-label"></label>
					<div class="col-sm-8">
						<button type="button" id="organisation-save" class="btn btn-primary" data-loading-text="Even geduld..."><i class="fa fa-save"></i>&nbsp;&nbsp;Verstuur</button>
						<span id="organisation-status" class="error hidden">
						</span>
					</div>
				</div>
			</c:if>
			
		</form>
		
	</div>
		
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
			
			putOrganisation( organisation, $jq("#organisation-save" ),$jq("#organisation-status" ), saveAddress );	
			
		};
		
		var saveAddress = function( organisation ) {
			
			var a = new Adres( $jq("#adres-zipcode").val(), $jq("#adres-gemeente").val(), $jq("#adres-straat").val(), $jq("#adres-nummer").val() );
			
			putOrganisationAddress( organisation, a, $jq("#organisation-save" ),$jq("#organisation-status" ), refresh );	
			
		};
		
		    	
		$jq("#organisation-save").click( function( event ) {
			
			clearError();
			
			$jq(this).button('loading');
			
			saveOrganisation();
			
		});
    	
    </script>
  </body>
</html>
