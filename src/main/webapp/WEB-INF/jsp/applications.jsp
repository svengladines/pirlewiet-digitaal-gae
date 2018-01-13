<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

	<jsp:include page="/WEB-INF/jsp/menu.jsp">
		<jsp:param name="active" value="application"/>
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
		
			<c:choose>
				<c:when test="${false || ('sven.gladines@gmail.com' == organisation.email) || ('anke.wulteputte@hotmail.com' == organisation.email )}">
					<form class="form-horizontal">
						
						<div class="form-group form-group-lg">
							<div>
								<button type="button" id="create" class="btn btn-primary btn"><i class="fa fa-plus"></i>&nbsp;&nbsp;Nieuwe inschrijving</button>
							</div>
						</div>
						
					</form>
				</c:when>
				
				<c:otherwise>
						<div>
							<strong>Inschrijvingen zijn pas mogelijk vanaf 15 januari 2018. Nog even geduld!</strong> <br/><br/>
						</div>
				</c:otherwise>
			</c:choose>
		</div>
	
		<div class="row mandatory">
		
			<c:choose>
			
			<c:when test="${applicationsResult.value == 'OK'}">
			
					<c:forEach items="${applicationsResult.object}" var="individualResult">
					
						<c:set var="application" value="${individualResult.object}" /> 
					
						<table class="table table-bordered">
						
						<thead>
							<tr class="bg-success">
								<th scope="row" class="th-row">Dossier</th>
								<td colspan="2">
									<a title="${application.uuid}" href="/application-${application.uuid}.html" class="btn btn-primary pull-right">Wijzig</a>
								</td>
							</tr>
						</thead>
							
						<tbody>
						
							<tr>
								<th scope="row" class="bg-info">Status</th>
								<td colspan="2" class="bg-info">
									<fmt:message key="application.status.${application.status.value}"/>
								</td>
							</tr>
						
						<!-- 
							<tr>
								<th scope="row" class="th-row">Referentie</th>
								<td colspan="2">
									${application.reference}
								</td>
							</tr>
						-->
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
								<c:if test="${application.status.value != 'DRAFT'}">
									<span class="pull-right text-success"><strong><fmt:message key="enrollment.status.${enrollment.status.value}"/></strong></span>
								</c:if>
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
			
			busyButton( $jq("#create") );
			
			postApplication( new Application(), viewApplication );
			
    	});
		
    </script>
  </body>
</html>
