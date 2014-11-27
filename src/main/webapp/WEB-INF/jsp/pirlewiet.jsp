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
            <li class="active"><a href="organisation.html">PIRLEWIET</a></li>
            <li>
            	<c:choose>
            	<c:when test="${incomplete == false}">
            		<a href="/rs/inschrijvingen.html">INSCHRIJVINGEN</a>
            	</c:when>
            	<c:otherwise>
            		<a href="#" class="pop" data-toggle="popover" title="Opgelet" data-content="Je kan pas inschrijvingen beheren als je hieronder alle verplichte velden hebt ingevuld." data-placement="bottom" data-trigger="focus" tabindex="0">INSCHRIJVINGEN</a>
            	</c:otherwise>
            	</c:choose>
            </li>
            <li><a href="help.html">HELP</a></li>
            <li><a id="logout" title="Uitloggen" href="#"><i class="fa fa-sign-out"></i></a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

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
	
	<div id="f" class="centered">
		<a href="#"><i class="fa fa-twitter"></i></a><a href="#"><i class="fa fa-facebook"></i></a><a href="#"><i class="fa fa-dribbble"></i></a>
	</div>
	
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
