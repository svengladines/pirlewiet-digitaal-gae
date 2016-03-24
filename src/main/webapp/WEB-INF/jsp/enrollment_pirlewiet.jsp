<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib uri = "http://www.springframework.org/tags" prefix = "spring"%>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>
	
	<fmt:bundle basename="pirlewiet-messages">

    <jsp:include page="/WEB-INF/jsp/menu.jsp">
    	<jsp:param name="active" value="enrollments"/>
    </jsp:include>

	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Inschrijving</h1>
					<p>
						Beslis over de inschrijving van een deelnemer.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>
	
	<div class="container">
	
		<br/>
		<div class="row">
			
			<div class="col-sm-12 alert alert-info">
				<h4><strong>Status</strong><br/></h4>
				<p>
					<i><spring:message code="enrollment.status.${enrollment.status.value}"/></i><br/>
					<spring:message code="enrollment.status.${enrollment.status.value}.description"/> <br/>
				</p>
			</div>
			
		</div>
		<div class="row">
			<div class="col-sm-12 alert alert-${ fn:length(enrollment.vakanties) == 1 ? 'success' : 'warning'}">
				<i class="fa fa-2x fa-2x fa-calendar pull-right"></i><h4><strong>Vakantie</strong><br/></h4>
				<p>Selecteer een vakantie</p>
					<c:forEach items="${enrollment.vakanties}" var="vakantie">	
						<div class="radio">
							<label>
								<input type="radio" name="vak" class="vakantie" value="${vakantie.uuid}" checked="${ fn:length(enrollment.vakanties) == 1 ? "checked" : "false" }">&nbsp;${vakantie.naam}
							</label>
						</div>
					</c:forEach>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 alert alert-success">
				<div class="row">
					<div class="col-sm-12"><i class="fa fa-2x fa-2x fa-user pull-right"></i><h4><strong>Deelnemer</strong><br/></h4>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4 media-middle max-height">
					<span>${enrollment.deelnemers[0].voorNaam}&nbsp;${enrollment.deelnemers[0].familieNaam}</span>
				</div>
			</div>
						
			</div>
				
		</div>
		
		<div class="row">
			<div class="col-sm-12 alert alert-${ enrollment.status.value == 'SUBMITTED' ? 'warning' : 'success'}">
				<div class="row">
					<div class="col-sm-12"><i class="fa fa-2x fa-check pull-right"></i><h4><strong>Beslissing</strong></h4>
					<p>Neem een beslissing over de inschrijving</p>
				</div>
			</div>
			<div class="row">
				<div class="radio col-sm-12">
					<label>
						<input type="radio" name="vak" class="vakantie" value="ACCEPTED" ${ enrollment.status.value == 'ACCEPTED' ? "checked=\"checked\"" : ""}><spring:message code="enrollment.status.ACCEPTED"/><br/>
					</label>
				</div>
				<div class="radio col-sm-12">
					<label>
						<input type="radio" name="vak" class="vakantie" value="REJECTED" ${ enrollment.status.value == 'REJECTED' ? "checked=\"checked\"" : ""}><spring:message code="enrollment.status.REJECTED"/><br/>
					</label>
				</div>
				<div class="radio col-sm-12">
					<label>
						<input type="radio" name="vak" class="vakantie" value="WAITINGLIST" ${ enrollment.status.value == 'WAITINGLIST' ? "checked=\"checked\"" : ""}><spring:message code="enrollment.status.WAITINGLIST"/><br/>
					</label>
				</div>
			</div>
			
			</div>
				
		</div>
		
		<div class="row">
			<div class="col-sm-12 alert alert-info">
				<div class="row">
					<div class="col-sm-12"><h4><strong>Uitleg bij beslissing</strong></h4>
						<p>Voorzie enige uitleg bij de beslissing.<br/>
						<strong>Opgelet:</strong> De doorverwijzer kan deze uitleg zien.</p>
					</div>
				</div>
				<div class="row">
					<div class="radio col-sm-12">
						<textarea class="form-control" rows="10" cols="64">
							
						</textarea>
					</div>
				</div>
			</div>
		</div>
			
		<div class="row">
			<div class="col-sm-12 alert alert-info">
				<div class="row">
					<div class="col-sm-12"><h4><strong>Opslaan</strong><br/></h4>
				</div>
			</div>
				<div class="row">
					<div class="checkbox col-sm-12">
						<label>
							<input type="checkbox" name="send-email" checked="checked">&nbsp;Verstuur e-mail naar doorverwijzer.
						</label>
					</div>
				</div>
			<div class="row">
				<div class="radio col-sm-12">
					<button type="button" id="enrollment-submit" class="btn btn-primary"><i class="fa fa-3 fa-envelope"></i>&nbsp;&nbsp;Verstuur</button>
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
    	
		var saveVakanties = function( id ) {
			var list
				= "";
			$jq( ".vakantie:checked" ).each( function( index, element ) {
				if ( list.length > 0 ) {
					list = list.concat(",");
				}
				list = list.concat( element.value );	
			});
			putVakanties ( id, list, $jq("#vakantie-save"),$jq("#vakantie-status" ), refresh );
		};
		
		var saveStatus = function( id, value ) {
			var comment = $jq("#status-comment-text").val();
			if ( value ) {
				var sx = new Status (value, comment ,true );
				putStatus ( id, sx, $jq("#enrollment-save" ),$jq("#x-status" ), refresh );
			}
			else {
				var sx = new Status ( "AUTO", comment ,true );
				putStatus ( id, sx, $jq("#enrollment-save" ),$jq("#x-status" ), refresh );	
			}
			
			
		};
		
		var deleteEnrollment = function( id ) {
			
			deleteEnrollment( id, $jq("#enrollment-delete" ),$jq("#delete-status" ), refresh );
			
		};
		
		var refresh = function( ) {
			window.location.hash="";
			window.location.reload();
		};
    	
		$jq("#vakantie-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveVakanties( "${enrollment.uuid}" );
			
		});
		
		$jq("#contact-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveContact( "${enrollment.uuid}" );
			
		});
		
		$jq("#q-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveVragen( "${enrollment.uuid}" );
			
		});
		

		$jq(".participant-save").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveParticipant( $jq(this).attr("data-uuid"), false );
			
		});
		
		$jq("#participant-save-new").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveParticipant( $jq(this).attr("data-uuid"), true );
			
		});
		
		$jq("#enrollment-submit").click( function( event ) {
			
			clearStatus();
			$jq(this).button('Even geduld...');
			
			saveStatus( "${enrollment.uuid}" );
			
		});
		
		$jq(".enrollment-set-status").click( function( event ) {

			clearStatus();
			$jq(this).button('Even geduld...');
			
			var uuid = event.currentTarget.attributes["data-uuid"].value;
			var status = event.currentTarget.attributes["data-status"].value;
			
			saveStatus( uuid, status, $jq("#status-comment-text").val() );
			
		});
		
		$jq("#enrollment-delete").click( function( event ) {
			
			var uuid 
				= event.currentTarget.attributes["data-uuid"].value;
			
			$jq(this).button('Even geduld...');
			
			deleteEnrollment( uuid );
			
		});
		
		$jq("#submit-show").click( function( event ) {
			
			show('div-submit');	
			
		});
		
		$jq( document ).ready(function() {
			
			if ( window.location.hash ) {
				$jq( "#" + window.location.hash.substring(1) ).modal();
			}
		    
		});
		
    </script>
  </body>
</html>
