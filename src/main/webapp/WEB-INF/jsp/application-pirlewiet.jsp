<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib uri = "http://www.springframework.org/tags" prefix = "spring"%>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<%@ page import="be.pirlewiet.digitaal.model.EnrollmentStatus.Value"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>
	
	<fmt:bundle basename="pirlewiet-messages">

    <jsp:include page="/WEB-INF/jsp/menu-pirlewiet.jsp">
    	<jsp:param name="active" value="enrollments"/>
    </jsp:include>

	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Dossier</h1>
					<p>
						Beslis over de inschrijving van de deelnemer(s).
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>
	
	<div class="container">
	
		<c:set var="application" value="${applicationResult.object}" />
	
		<br/>
		<div class="row">
			
			<div class="col-sm-2 col-sm-offset-10 right-wing">
				<a href="/application-${enrollment.applicationUuid}.html">Naar het dossier</a>
			</div>
			<br/>
		</div>
		<div class="row">
			
			<div class="col-sm-12 alert alert-info">
				<h4><strong>Status</strong><br/></h4>
                <p>
                    <i><fmt:message key="application.status.${application.status.value}"/></i><br/>
                    <fmt:message key="application.status.${application.status.value}.description"/> <br/>
                </p>
			</div>
			
		</div>
		<div class="row">
			<div class="col-sm-12 alert alert-${ 'OK' == holidaysResult.value ? 'success' : 'warning'}">
				<c:set var="holidays" value="${holidaysResult.object}"/>
				<i class="fa fa-2x fa-2x fa-calendar pull-right"></i><h4><strong>Vakantie</strong><br/></h4>
				<p>Selecteer de vakantie</p>
					<c:forEach items="${holidays}" var="holiday">	
						<div class="checkbox">
							<label>
								<c:choose>
								<c:when test="${holiday.isApplicationHoliday}">
										<input type="checkbox" class="holiday" value="${holiday.uuid}">
										&nbsp;${holiday.name}
									</c:when>
									<c:otherwise>
										<input type="checkbox" class="holiday" value="${holiday.uuid}" checked="checked">
										${holiday.name}
									</c:otherwise>
									</c:choose>
							</label>
						</div>
					</c:forEach>
			</div>
		</div>
		<div class="row">
        			<div class="col-sm-12 alert alert-success">
        				<div class="row">
        					<div class="col-sm-12"><i class="fa fa-2x fa-2x fa-user pull-right"></i><h4><strong>Deelnemer(s)</strong><br/></h4>
        				</div>
        			</div>
        			<div class="row">
        			    <br/>
        			    <table class="table table-bordered">
        				<c:forEach items="${application.enrollments}" var="enrollment">
                        <tr>
                            <td>
                                ${enrollment.participantName}
                            </td>
                            <td colspan="2">
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
                        </table>
        			</div>
        	</div>
        </div>

		<div id="comment" class="row hidden">
			<div class="col-sm-12 alert alert-info">
				<div class="row">
					<div class="col-sm-12"><h4><strong>Uitleg bij beslissing</strong></h4>
						<p>Voorzie eventueel enige uitleg bij de beslissing.<br/>
						<strong>Opgelet:</strong> De doorverwijzer krijgt een e-mail met deze uitleg.</p>
					</div>
				</div>
				<div class="row">
					<div class="radio col-sm-12">
						<textarea id="decision-comment-text" class="form-control" rows="8" cols="64"></textarea>
					</div>
				</div>
			</div>
		</div>
			
		<div class="row">
			<div class="col-sm-12 alert alert-info">
				<div class="row">
					<div class="col-sm-12"><h4><strong>Versturen</strong><br/></h4>
				</div>
			</div>
				
			<div id="enrollment-status" class="col-sm-12 alert alert-warning hidden"></div>
			<div class="row">
				<div class="radio col-sm-12">
					<button type="button" id="enrollment-submit" class="btn btn-primary"><i class="fa fa-3 fa-save"></i>&nbsp;&nbsp;Verstuur</button>
				</div>
			</div>
		</div>	
	</div>
				
	</div><!-- container -->
	
	</fmt:bundle>
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
	
    <script>
    	var $jq = jQuery.noConflict();
    	
    	var show = function( id ) {
    		$jq(".panel" ).removeClass("show").addClass("hidden");
    		$jq("#" + id ).removeClass("hidden").addClass("show");
    	};
    	
    	var saveHolidays = function( id ) {
			var holidays
				= new Array();
			$jq( ".holiday:checked" ).each( function( index, element ) {
				holidays.push( new Holiday( element.value ) );
			});
			putEnrollmentHolidays ( "${enrollment.applicationUuid}", id, holidays, $jq("#enrollment-submit"),$jq("#enrollment-status" ), saveStatus );
		};
		
		var saveStatus = function( id ) {
			var comment = $jq("#decision-comment-text").val();
			var decision = $jq( "input[name=decision]:checked" ).val();
			if ( decision != null ) {
				var sx = new Status (decision, comment ,true );
				putEnrollmentStatus ( "${enrollment.applicationUuid}", id, sx, $jq("#enrollment-submit" ),$jq("#enrollment-status" ), refresh );		
			}
			
		};
		
		$jq("#enrollment-submit").click( function( event ) {
			
			clearStatus();
			saveHolidays( "${enrollment.uuid}" );
			
		});
		
		$jq("input[name='decision']").change( function( event ) {
			
		});

        $jq(".status-select").change( function() {
            $jq("#comment").removeClass("hidden").addClass("show");
            var selectId = $jq(this).attr("id");
            var applicationUuid = $jq(this).attr("data-application-uuid");
            var enrollmentUuid = $jq(this).attr("data-enrollment-uuid");
            $jq( "#" + selectId + " option:selected" ).each( function() {
            } );
        });
		
		$jq( document ).ready(function() {
			
			buttons();
		    
		});
		
    </script>
  </body>
</html>
