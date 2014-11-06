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
          <a class="navbar-brand" href="#">PIRLEWIET</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="/index.htm">HOME</a></li>
            <li class="active"><a href="organisation.html">ORGANISATIE</a></li>
            <li>
            	<c:choose>
            	<c:when test="${incomplete == false}">
            		<a href="/rs/inschrijvingen.html" data-toggle="popover" title="Popover title" data-content="And here's some amazing content. It's very engaging. Right?">INSCHRIJVINGEN</a>
            	</c:when>
            	<c:otherwise>
            		<a href="#" class="pop" data-toggle="popover" title="Opgelet" data-content="Je kan pas inschrijvingen beheren als je hieronder alle verplichte velden hebt ingevuld." data-placement="bottom" data-trigger="focus" tabindex="0">INSCHRIJVINGEN</a>
            	</c:otherwise>
            	</c:choose>
            </li>
            <li><a href="help.html">HELP</a></li>
            <li><a data-toggle="modal" data-target="#myModal" href="#myModal"><i class="fa fa-envelope-o"></i></a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

	<div class="container">
		<div class="row centered">
			<div class="col-lg-12">
				<h1>Mijn Organisatie</h1>
				<p>
					Beheer hier de gegevens van jouw organisatie.
				</p>
			</div>
		</div><!-- row -->
	</div><!-- container -->


	<div class="container">
	
			<form class="form-horizontal" role="form">
		
		<div class="row mandatory">
		
			<h2>Gegevens</h2>
			
			<p>
				Controleer of de gegevens van je organisatie nog in orde zijn. <br/>
				Velden met een (*) moet je zeker invullen.
			</p>
			
			<p>
				<span id="organisation-error" class="error text-danger hidden"></span>
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
			
			
		<div class="row mandatory">
			<h2>Verstuur</h2>
			<p>
				Controleer de ingevulde gegevens en verstuur het formulier
			</p>
			<p>
					<span id="form-error" class="error text-danger hidden">
						Er zijn fouten of onvolkomendheden in het formulier. Controleer de gegevens en probeer opnieuw AUB. 
					</span>
			</p>
			
			<div class="form-group">
				<label class="col-sm-4 control-label"></label>
				<div class="col-sm-2">
					<button type="button" id="submit" class="btn btn-primary" data-vakantie="1"><i class="fa fa-save"></i>&nbsp;&nbsp;Verstuur</button>
				</div>
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
			
			putOrganisation( organisation, $jq("#organisation-error" ),$jq("#form-error" ) );
			
			var a = new Adres( $jq("#adres-gemeente").val(), $jq("#adres-straat").val(), $jq("#adres-nummer").val() );
			
			putOrganisationAdres ( organisation, a, $jq("#organisation-error" ), $jq("#form-error" ) );
			
		};
		
		    	
		$jq("#submit").click( function( event ) {
			
			clearError();
			
			saveOrganisation();
			
		});
    	
    </script>
  </body>
</html>
