<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

    <jsp:include page="/WEB-INF/jsp/menu-pirlewiet.jsp">
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

	<fmt:bundle basename="pirlewiet-messages">
	<div class="container content">
	
		<div class="row">
		
			<br/>
		
			<c:if test="${ false }">
				<form class="form-horizontal">
					
					<div class="form-group form-group-lg">
						<div>
							<button type="button" id="nieuw" class="btn btn-primary btn-lg" data-vakantie="1"><i class="fa fa-plus"></i>&nbsp;&nbsp;Nieuwe inschrijving</button>
						</div>
					</div>
					
				</form>
			</c:if>
			
			<c:if test="${ true }">
					<div>
						<strong>Inschrijvingen zijn pas mogelijk vanaf 15 januari 2017. Nog even geduld!</strong> <br/><br/>
					</div>
			</c:if>
		</div>
	
		<div class="row mandatory">
		
			<c:choose>
			
			<c:when test="${applicationsResult.value == 'OK'}">
			
					<c:forEach items="${applicationsResult.object}" var="individualResult">
					
						<c:set var="application" value="${individualResult.object}" /> 
					
						<table class="table table-bordered">
						
						<thead>
							<tr>
								<th scope="row" class="th-row">Dossier</th>
								<td colspan="2">
									<a title="${application.uuid}" href="/application.html?uuid=${application.uuid}" class="btn btn-primary pull-right">Wijzig</a>
								</td>
							</tr>
						</thead>
							
						<tbody>
						
							<tr>
								<th scope="row" class="th-row">Referentie</th>
								<td colspan="2">
									${application.reference}
								</td>
							</tr>
							
							<tr>
								<th scope="row" class="th-row">Vakantie(s)</th>
								<td colspan="2">
									${application.holidayNames}
								</td>
							</tr>
							
							<tr>
								<th scope="row" class="th-row">Contactpersoon</th>
								<td colspan="2">
									${application.contactPersonName}
								</td>
							</tr>
							
							<c:forEach items="${application.enrollments}" var="enrollment">
							
							<tr>
								<th scope="row" class="th-row">Deelnemer</th>
								<td colspan="2">
									<span>${enrollment.participantName}</span>
									<div class="pull-right">
										<span class="text-info"><strong><fmt:message key="enrollment.status.${enrollment.status.value}"/></strong></span>&nbsp;&nbsp;
										<a title="${application.uuid}" href="/enrollment-${enrollment.uuid}-pirlewiet.html" class="btn btn-default">Beheer</a>
									</div>
								</td>
							</tr>
							
							</c:forEach>
							
						</tbody>
						</table>			
					</c:forEach>
			
				
			</c:when>
			
			<c:otherwise>
			
				<span>${applications.result.errorCode}</span>
			
			</c:otherwise>
			
			</c:choose>
			
		</div>	
		
	</div><!-- container -->
	
	</fmt:bundle>
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>

    <script>
    	var $jq = jQuery.noConflict();
    	
		$jq("#nieuw").click( function( event ) {
			
			event.preventDefault();
			
			postEnrollment( new Inschrijving() );
			
    	});
		
    </script>
  </body>
</html>
