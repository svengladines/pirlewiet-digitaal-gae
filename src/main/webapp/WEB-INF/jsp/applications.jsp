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

	<fmt:bundle basename="pirlewiet-messages">
	<div class="container content">
	
		<div class="row">
		
			<br/>
		
			<c:if test="${ true }">
				<form class="form-horizontal">
					
					<div class="form-group form-group-lg">
						<div>
							<button type="button" id="nieuw" class="btn btn-primary btn-lg" data-vakantie="1"><i class="fa fa-plus"></i>&nbsp;&nbsp;Nieuwe inschrijving</button>
						</div>
					</div>
					
				</form>
			</c:if>
		</div>
	
		<div class="row mandatory">
		
			<span>${applications.value}</span>
		
			<c:choose>
			
			<c:when test="${applications.value == 'OK'}">
			
					<c:forEach items="${applications.object}" var="application">
					
						<table class="table table-bordered table-striped">
						
						<thead>
							<th scope="row" class="th-row">ID</th>
							<td colspan="1">${application.uuid}</td>
						</thead>
							
						<tbody>
						
							<tr>
								<th scope="row" class="th-row">Referentie</th>
								<td colspan="1">
									${application.reference}
								</td>
							</tr>
							
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
    	
    	var inschrijving = null;
    	
    	retrieveInschrijving( "${inschrijving.uuid}" );
    	
		$jq("#nieuw").click( function( event ) {
			
			event.preventDefault();
			
			postEnrollment( new Inschrijving() );
			
    	});
		
    </script>
  </body>
</html>
