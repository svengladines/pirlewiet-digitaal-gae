<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

 	<c:choose>
    <c:when test="${isPirlewiet}">
	  		<c:set var="zee" value="de"/>
	      	<jsp:include page="/WEB-INF/jsp/menu-pirlewiet.jsp">
	    		<jsp:param name="active" value="organisation"/>
	   		</jsp:include>
	   </c:when>
	   <c:otherwise>
	   		<c:set var="zee" value="jouw"/>
	   		<jsp:include page="/WEB-INF/jsp/menu.jsp">
	    		<jsp:param name="active" value="organisation"/>
	   		</jsp:include>
	   </c:otherwise>
	   </c:choose>
    
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
		
			<c:if test="${ true }">
				<form class="form-horizontal">
					
					<div class="form-group form-group-lg">
						<div>
							<button type="button" id="create" class="btn btn-primary btn"><i class="fa fa-plus"></i>&nbsp;&nbsp;Nieuwe inschrijving</button>
						</div>
					</div>
					
				</form>
			</c:if>
			
			<c:if test="${ false }">
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
								<th scope="row" class="th-row">Inschrijving</th>
								<td colspan="2">
									<a title="${application.uuid}" href="/application-${application.uuid}.html" class="btn btn-primary pull-right">Beheer</a>
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
								<span class="pull-right text-success"><strong><fmt:message key="enrollment.status.${enrollment.status.value}"/></strong></span>
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
    	
		$jq("#create").click( function( event ) {
			
			event.preventDefault();
			
			postApplication( new Application(), viewApplication );
			
    	});
		
    </script>
  </body>
</html>
