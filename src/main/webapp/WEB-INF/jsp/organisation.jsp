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
			
			<input id="organisation-uuid" type="hidden" value="${organisation.uuid}"></input>
			<div class="form-group">
				<label for="organisation-name" class="col-sm-4 control-label">Naam organisatie (*)</label>
				<div class="col-sm-3">	
					<input id="organisation-name" type="text" class="form-control" value="${organisation.name}"></input>
				</div>
			</div>
			<div class="form-group">
					<label for="organisation-telefoon" class="col-sm-4 control-label">Telefoonnummer (*)</label>
					<div class="col-sm-2">
						<input id="organisation-phone" type="tel" class="form-control" value="${organisation.phone}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="organisation-email" class="col-sm-4 control-label">E-mail (*)</label>
					<div class="col-sm-3">
						<input id="organisation-email" type="email" class="form-control" value="${organisation.email}" required pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-zipcode" class="col-sm-4 control-label">Postcode (*)</label>
					<div class="col-sm-2">
						<input id="address-zipcode" type="tel" class="form-control" value="${address.zipCode}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-city" class="col-sm-4 control-label">Gemeente (*)</label>
					<div class="col-sm-2">
						<input id="address-city" type="text" class="form-control" value="${address.city}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-street" class="col-sm-4 control-label">Straat (*)</label>
					<div class="col-sm-3">
						<input id="address-street" type="text" class="form-control" value="${address.street}"></input>
					</div>
			</div>
			<div class="form-group">
					<label for="adres-nummer" class="col-sm-4 control-label">Huisnummer (*)</label>
					<div class="col-sm-2">
						<input id="address-number" type="text" class="form-control" value="${address.number}"></input>
					</div>
			</div>
			<c:if test="${!isPirlewiet}">
				<div class="form-group">
					<label class="col-sm-4 control-label"></label>
					<div class="col-sm-8">
						<button type="button" id="organisation-save" class="btn btn-primary" data-loading-text="Even geduld..."><i class="fa fa-save"></i>&nbsp;&nbsp;Verstuur</button>
						<span id="organisation-status"></span>
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
						$jq("#organisation-uuid").val(),
						$jq("#organisation-name").val(),
						$jq("#organisation-phone").val(),
						$jq("#organisation-email").val()
						);
			
			putOrganisation( organisation, $jq("#organisation-save" ),$jq("#organisation-status" ), saveAddress );	
			
		};
		
		var saveAddress = function( organisation ) {
			
			var a = new Address( $jq("#address-zipcode").val(), $jq("#address-city").val(), $jq("#address-street").val(), $jq("#address-number").val() );
			
			putOrganisationAddress( organisation, a, $jq("#organisation-save" ),$jq("#organisation-status" ), refresh );	
			
		};
		
		    	
		$jq("#organisation-save").click( function( event ) {
			
			saveOrganisation();
			
		});
    	
    </script>
  </body>
</html>
