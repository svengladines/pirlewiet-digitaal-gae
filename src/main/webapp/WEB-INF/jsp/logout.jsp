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
					<h1>Uitloggen</h1>
					<p>
						Je komt naar hier om uit te loggen.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>

	<div class="container">
	
		<br/>
		
		<div class="row">
		
			<div id="inprogress" class="well">
				Bezig met uitloggen...
			</div>
			
			<div id="done" class="well hidden">
				Je bent nu uitgelogd. Ga naar de <a href="/code.htm">inlogpagina</a> om opnieuw in te loggen.
			</div>
		</div>
			
	</div><!-- container -->
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
	
    <script>
    	var $jq = jQuery.noConflict();
    	
    	$jq.ajax( {
   				type: "delete",
   				url:"/rs/codes",
   				success: function( ) {
   						$jq("#inprogress").removeClass("show").addClass("hidden");
   						$jq("#done").removeClass("hidden").addClass("show");
   				},
   				error: function(  jqXHR, textStatus, errorThrown ) {
   				}
		});
    	
    </script>
  </body>
</html>
