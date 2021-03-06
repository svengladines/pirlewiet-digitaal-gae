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
          <a class="navbar-brand" href="#">PIRLEWIET DIGITAAL</i></a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li><a href="/index.htm">START</a></li>
            <li><a href="organisation.html">ORGANISATIE</a></li>
            <li class="active"><a href="/rs/inschrijvingen.html">INSCHRIJVINGEN</a></li>
            <li><a href="help.html">HELP</a></li>
            <li><a data-toggle="modal" data-target="#myModal" href="#myModal"><i class="fa fa-envelope-o"></i></a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>
	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Inschrijvingen</h1>
					<p>
						Beheer je inschrijvingen.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>

	<div class="container">
	
		<div class="row">
		
			<br/>
		
			<form class="form-horizontal">
				
				<div class="form-group form-group-lg">
					<div>
						<button type="button" id="nieuw" class="btn btn-primary" data-vakantie="1"><i class="fa fa-plus"></i>&nbsp;&nbsp;Nieuwe inschrijving</button>
					</div>
				</div>
				
			</form>
		
		</div>
	
		<div class="row mandatory">
		
			<h2>Actuele inschrijvingen</h2>
			
			<table class="table table-bordered">
			
			<thead>
				<tr>
					<th>Vakantie(s)</th>
					<th>Voornaam</th>
					<th>Familienaam</th>
					<th>Geboortedatum</th>
					<th>Status</th>
					<th>Beheer</th>
				</tr>
			</thead>
			
			
			<c:forEach items="${inschrijvingen}" var="inschrijving">
				
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.inschrijvingsdatum}" var="date"></fmt:formatDate>
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>
							
				<tr>
					<td>${inschrijving.vakantieDetails}</td>
					<td>${inschrijving.deelnemers[0].voorNaam}</td>
					<td>${inschrijving.deelnemers[0].familieNaam}</td>
					<td>${gd}</td>
					<td>${inschrijving.status}</td>
					<td><a href="./inschrijvingen/${inschrijving.uuid}.html"><i class="fa fa-edit"></i></td>
				</tr>
			</c:forEach>
			
			</table>
			
		</div>	
		
	</div><!-- container -->
	
	<!-- FOOTER -->
	<div id="f">
		<div class="container">
			<div class="row centered">
				<a href="#"><i class="fa fa-twitter"></i></a><a href="#"><i class="fa fa-facebook"></i></a><a href="#"><i class="fa fa-dribbble"></i></a>
		
			</div><!-- row -->
		</div><!-- container -->
	</div><!-- Footer -->

    <script>
    	var $jq = jQuery.noConflict();
    	
    	var inschrijving = null;
    	
    	retrieveInschrijving( "${inschrijving.uuid}" );
    	
		$jq("#nieuw").click( function( event ) {
			
			event.preventDefault();
			
			postInschrijving( new Inschrijving() );
			
    	});
		
    </script>
  </body>
</html>
