<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ page import="be.pirlewiet.digitaal.model.EnrollmentStatus.Value"%>
	
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
						Beheer hier de inschrijvingen.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>

	<fmt:bundle basename="pirlewiet-messages">
	<div class="container content">
	
		<div class="row">
		
			<div class="col-xs-12">
			
				<br/>
					
				<c:if test="${ true }">
						<button type="button" id="download" class="btn btn-primary btn"><i class="fa fa-download"></i>&nbsp;&nbsp;Download inschrijvingen</button>
				</c:if>
				
				<br/>
				<br/>
				
			</div>
		</div>
	
		<div class="row">
		
			<div class="col-xs-12">
		
				<c:choose>
				
				<c:when test="${applicationsResult.value == 'OK'}">
				
						<c:if test="${fn:length(applicationsResult.object) == 0}">
							Er zijn nog geen dossiers ingediend.
						</c:if>
				
						<c:forEach items="${applicationsResult.object}" var="individualResult">
						
							<c:set var="application" value="${individualResult.object}" /> 
						
							<table class="table table-bordered">
							
							<thead>
								<tr>
									<th scope="row" class="th-row">
									Dossier
									</th>
									<th colspan="2">
										<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${application.submitted}" var="submitted"></fmt:formatDate>
										<span><strong>${submitted}</strong></span>
										<a title="${application.uuid}" href="/application-${application.uuid}.html" class="btn btn-primary pull-right">Wijzig</a>
									</th>
								</tr>
							</thead>
								
							<tbody>
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
                                            <div class="pull-right">
                                                <select id="${enrollment.uuid}-status" class="form-control status-select" data-application-uuid="${application.uuid}" data-enrollment-uuid="${enrollment.uuid}">
                                                    <c:forEach items="<%=Value.values()%>" var="statusValue">
                                                        <option value="${statusValue}"} ${statusValue == enrollment.status.value ? 'selected' : ''}><strong><fmt:message key="enrollment.status.${statusValue}"/></strong></span></option>
                                                    </c:forEach>
                                                 </select>
                                            </div>
                                        </td>
                                    </tr>

								</c:forEach>

								<tr id="${application.uuid}-status-change-row" class="hidden">
								    <td>
								        Motiveer wijziging
								    </td>
								    <td>
								        <textarea id="${application.uuid}-decision-comment-text" class="form-control" rows="8" cols="64"></textarea>
								    </td>
								    <td>
								        <div class="pull-right">
								            <button type="button" id="update-status-multi" class="btn btn-primary btn">Verzend</button>
								        </div>
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
		</div>
		
	</div><!-- container -->
	
	</fmt:bundle>
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>

    <script>
    	var $jq = jQuery.noConflict();
    	
		$jq("#download").click( function( event ) {
			
			event.preventDefault();
			
			busyButton( $jq( this ) );
			
			window.location.href = "/api/applications/pirlewiet/enrollments/download";
			
    	});

    	$jq(".status-select").change( function() {
    	    var selectId = $jq(this).attr("id");
            var applicationUuid = $jq(this).attr("data-application-uuid");
            var enrollmentUuid = $jq(this).attr("data-enrollment-uuid");
            $jq( "#" + selectId + " option:selected" ).each( function() {
            } );
    	});

    	$jq("#btn-send-status-update").change( function() {
        	    var selectId = $jq(this).attr("id");
                var applicationUuid = $jq(this).attr("data-application-uuid");
                var enrollmentUuid = $jq(this).attr("data-enrollment-uuid");
                $jq( "#" + selectId + " option:selected" ).each( function() {
            	});
		
    </script>
  </body>
</html>
