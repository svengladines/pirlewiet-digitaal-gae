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
						Beheer hier de inschrijvingen van jouw organisatie (vanaf 15 januari).
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>

	<div class="container content">
	
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
		
			<table>
			<tbody>
			
			<c:forEach items="${enrollments}" var="enrollment">
				
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${enrollment.inschrijvingsdatum}" var="date"></fmt:formatDate>
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${enrollment.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>
				
				<c:if test="${enrollment.reference == null}">
					</tbody>
			
					</table>
					
					<br/>
					
					<table class="table table-bordered table-striped">
			
					<thead>
					</thead>
					<tbody>
					
					<tr>
						<th scope="row">Status</th>
						<td colspan="1">
							<strong>${enrollment.status}</strong>
							<a href="./inschrijvingen/${enrollment.uuid}.html" class="pull-right">bewerk</a>
						</td>
					</tr>
			
					<tr>
						<th scope="row">Vakantie(s)</th>
						<td colspan="1">
							<c:forEach items="${enrollment.vakanties}" var="vakantie">
								${vakantie.naam}<br/>
							</c:forEach>
					</tr>
					<tr>
						<th scope="row">Contactpersoon</th>
						<td colspan="1">
							${enrollment.contactGegevens.name}
						</td>
					</tr>
				</c:if>
							
					<tr>
						<th scope="row">Deelnemer</th>
						<td>${enrollment.deelnemers[0].voorNaam} &nbsp; ${enrollment.deelnemers[0].familieNaam} (${gd})
					</tr>
				
			</c:forEach>
			
			<tbody>
			</tbody>
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
			
			postEnrollment( new Inschrijving() );
			
    	});
		
    </script>
  </body>
</html>
