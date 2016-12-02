<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

      <jsp:include page="/WEB-INF/jsp/menu_public.jsp">
    	<jsp:param name="active" value="organisation"/>
    </jsp:include>

	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Registreer organisatie</h1>
					<p>
						Registreer jouw organisatie als doorverwijzer.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>

	<div class="container">
	
	<br/>
	
	<div class="row">
	
		<form class="form-horizontal" role="form">
		
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
					<label for="adres-city" class="col-sm-4 control-label">Gemeente (*)</label>
					<div class="col-sm-2">
						<input id="organisation-city" type="text" class="form-control" value="${address.city}"></input>
					</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label"></label>
				<div class="col-sm-8">
					<button type="button" id="organisation-save" class="btn btn-primary" data-loading-text="Even geduld..."><i class="fa fa-save"></i>&nbsp;&nbsp;Verstuur</button>
					<span id="organisation-status" class="error hidden">
					</span>
				</div>
			</div>
			
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
						$jq("#organisation-email").val(),
						$jq("#organisation-city").val()
						);
			
			postOrganisation( organisation, $jq("#organisation-save" ),$jq("#organisation-status" ) );	
			
		};
		    	
		$jq("#organisation-save").click( function( event ) {
			
			saveOrganisation();
			
		});
    	
    </script>
  </body>
</html>
