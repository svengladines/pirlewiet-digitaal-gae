<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

    <!-- Fixed navbar -->
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">PIRLEWIET DIGITAAL</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="/index.htm">START</a></li>
            <li><a href="help.html">HELP</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

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
				Je bent nu uitgelogd. Ga naar <a href="/index.htm">Start</a> om opnieuw in te loggen.
			</div>
		</div>
			
	</div><!-- container -->
	
	<div id="f" class="centered">
		<a href="#"><i class="fa fa-twitter"></i></a><a href="#"><i class="fa fa-facebook"></i></a><a href="#"><i class="fa fa-dribbble"></i></a>
	</div>
	
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
