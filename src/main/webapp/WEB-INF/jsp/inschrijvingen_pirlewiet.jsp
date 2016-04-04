<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

    <jsp:include page="/WEB-INF/jsp/menu_pirlewiet.jsp">
    	<jsp:param name="active" value="enrollments"/>
    </jsp:include>
    
	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Inschrijvingen</h1>
					<p>
						Beheer de inschrijvingen.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>

	<fmt:bundle basename="pirlewiet-messages">
	<div class="container">
	
		<div class="row mandatory">
		
			<br/>
		
	    	<a id="xls" href="${pageContext.request.contextPath}/rs/inschrijvingen/download?status=SUBMITTED" class="btn btn-primary" type="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" download="excel.xlsx" data-loading-text="Even geduld..."><i class="fa fa-arrow-circle-o-down"></i>&nbsp;Download</a>
			
			<table class="table table-bordered">
			
			<thead>
			</thead>
			
			
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
						<th scope="row" class="th-row">Inschrijving <span class="pull-right">${date}</span></th>
						<td colspan="3">
							<a href="${pageContext.servletContext.contextPath}/rs/inschrijvingen/${enrollment.uuid}.html" class="pull-left">Details</a>
						</td>
					</tr>
			
					<tr>
						<th scope="row">Vakantie(s)</th>
						<td colspan="3">
							<c:forEach items="${enrollment.vakanties}" var="vakantie">
								${vakantie.naam}<br/>
							</c:forEach>
					</tr>
					<tr>
						<th scope="row">Contactpersoon</th>
						<td colspan="3">
							${enrollment.contactGegevens.name}
						</td>
					</tr>
				</c:if>
							
					<tr>
						<th scope="row">Deelnemer(s)</th>
						<td colspan="1">
							${enrollment.deelnemers[0].voorNaam} &nbsp; ${enrollment.deelnemers[0].familieNaam} (${gd})
						</td>
						<td colspan="1">
							<span><strong><fmt:message key="enrollment.status.${enrollment.status.value}"/></i></strong></span>
						</td>
						<td colspan="1">
							<a href="${pageContext.servletContext.contextPath}/rs/enrollments/${enrollment.uuid}.html" class="btn btn-primary">Beheer</a>
						</td>
					</tr>
				
			</c:forEach>
			
			</table>
			
		</div>	
		
	</div><!-- container -->
	
	</fmt:bundle>
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
	
    <script>
    	var $jq = jQuery.noConflict();
    	
    </script>
  </body>
</html>
