<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri = "http://www.springframework.org/tags" prefix = "spring"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>

	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
<body>

	<div class="container">
	
		<div class="row">
		
		<form id="multipleForm" action="api/organisations" method="post" class="form-horizontal" enctype="multipart/form-data">
	
			<div id="upload-div" class="row">
		
					<fieldset>
												
						<br/>
						<div class="control-group">
							<div class="controls">
								<input id="fileInput" type="file" required/>
								<br/>
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<input id="submitButton" type="button" class="btn btn-primary" value="Verstuur" disabled="disabled"/>
							</div>
						</div>
		    			
		    		</fieldset>
		    			
			</div>
			
		
			<div id="success-div" class="row hide">
			
				<div id="success" class="alert alert-success">
					
   				</div>
   				
   				<div id="proceed">
					<p><fmt:message key="result.proceed.1"/></p>
					<p><fmt:message key="result.proceed.2"/></p>
				
					
   				</div>
   				
			</div>
			
			<div id="error-div" class="row hide">
				<div id="error" class="alert alert-error">
					<span id="error"></span>
   				</div>
			</div>
			
			</form>
				
		</div>
		
	</div>
	
	<script>
	
	var $jq = jQuery.noConflict();
	
	$jq("#fileInput").change( function() {
		$jq("#submitButton").prop( "disabled", false );
	});
	
	$jq("#submitButton").click( function() {
		
			$jq("#submitButton").prop( "disabled", "disabled" );
		
			var type = "json";

			var url = "/api/organisations";

			var fdata = new FormData();

			var fields = $jq("#frm").serializeArray();

			$jq.each(fields, function(i, field) {

				fdata.append(field.name, field.value);

			});

			fdata.append("file", $jq('#fileInput')[0].files[0]);

			$jq.ajax({
				type : "post",
				url : url,
				dataType : type,
				contentType : false,
				processData : false,
				data : fdata,
				success : function( created ) {
					
					$jq("#error-div").hide();
					accounts.reset( created );
					$jq("#multipleForm")[0].reset();
					
				},
				error : function( data ) {

					$jq("#error").text(data.responseText);
					$jq("#error-div").show();
					$jq("#multipleForm")[0].reset();
				}
			});
			
			return false;
	});
	
	</script>
	
</body>