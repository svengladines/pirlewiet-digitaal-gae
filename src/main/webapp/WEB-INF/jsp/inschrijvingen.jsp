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
					<h1>Inschrijvingen</h1>
					<p>
						Beheer hier de inschrijvingen van jouw organisatie.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>

	<div class="container">
	
		<div class="row">
		
			<br/>
		
			<form class="form-horizontal">
				
				<div class="form-group form-group-lg">
					<div>
						<button type="button" id="nieuw" class="btn btn-primary btn-lg" data-vakantie="1"><i class="fa fa-plus"></i>&nbsp;&nbsp;Nieuwe inschrijving</button>
					</div>
				</div>
				
			</form>
		
		</div>
	
		<div class="row mandatory">
		
			<h2>Inschrijvingen</h2>
			
			<table class="table table-bordered table-striped">
			
			<thead>
				<tr>
					<th>Vakantie(s)</th>
					<th>Voornaam</th>
					<th>Familienaam</th>
					<th>Geboortedatum</th>
					<th>Status</th>
					<th>Beheer</th>
				</tr>
			</thead>
			
			
			<c:forEach items="${inschrijvingen}" var="inschrijving">
				
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.inschrijvingsdatum}" var="date"></fmt:formatDate>
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>
							
				<tr>
					<td>
						<c:forEach items="${inschrijving.vakanties}" var="vakantie">
							${vakantie.naam}<br/>
						</c:forEach>
					</td>
					<td>${inschrijving.deelnemers[0].voorNaam}</td>
					<td>${inschrijving.deelnemers[0].familieNaam}</td>
					<td>${gd}</td>
					<td>${inschrijving.status}</td>
					<td class="text-center"><a href="./inschrijvingen/${inschrijving.uuid}.html"><i class="fa fa-edit"></i></td>
				</tr>
			</c:forEach>
			
			</table>
			
		</div>	
		
	</div><!-- container -->
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>

    <script>
    	var $jq = jQuery.noConflict();
    	
    	var inschrijving = null;
    	
    	retrieveInschrijving( "${inschrijving.uuid}" );
    	
		$jq("#nieuw").click( function( event ) {
			
			event.preventDefault();
			
			postInschrijving( new Inschrijving() );
			
    	});
		
    </script>
  </body>
</html>
