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

	<div class="container">
	
		<div class="row">
	
			<br/>	
			<p>
		    	<a id="xls" href="${pageContext.request.contextPath}/rs/inschrijvingen/download" class="btn btn-primary" type="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" download="excel.xlsx" data-loading-text="Even geduld...">Download</a>
			</p>
		
		</div>
	
		<div class="row mandatory">
		
			<h2>NIEUW</h2>
			
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
			
			
			<c:forEach items="${submitted}" var="inschrijving">
				
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.inschrijvingsdatum}" var="date"></fmt:formatDate>
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>
							
				<tr>
					<td>
						<c:forEach items="${inschrijving.vakanties}" var="vakantie">
							${vakantie.naam}<br/>
						</c:forEach>
					</td>
					<td>${inschrijving.deelnemers[0].voorNaam}</td>
					<td>${inschrijving.deelnemers[0].familieNaam}</td>
					<td>${gd}</td>
					<td>${inschrijving.status}</td>
					<td><a href="./inschrijvingen/${inschrijving.uuid}.html"><i class="fa fa-edit"></i></td>
				</tr>
			</c:forEach>
			
			</table>
			
		</div>	
		
		<div class="row mandatory">
		
			<h2>IN BEHANDELING</h2>
			
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
			
			
			<c:forEach items="${transit}" var="inschrijving">
				
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.inschrijvingsdatum}" var="date"></fmt:formatDate>
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>
							
				<tr>
					<td>
						<c:forEach items="${inschrijving.vakanties}" var="vakantie">
							${vakantie.naam}<br/>
						</c:forEach>
					</td>
					<td>${inschrijving.deelnemers[0].voorNaam}</td>
					<td>${inschrijving.deelnemers[0].familieNaam}</td>
					<td>${gd}</td>
					<td>${inschrijving.status}</td>
					<td><a href="./inschrijvingen/${inschrijving.uuid}.html"><i class="fa fa-edit"></i></td>
				</tr>
			</c:forEach>
			
			</table>
			
		</div>
		
		<div class="row mandatory">
		
			<h2>WACHTLIJST</h2>
			
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
			
			
			<c:forEach items="${waiting}" var="inschrijving">
				
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.inschrijvingsdatum}" var="date"></fmt:formatDate>
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>
							
				<tr>
					<td>
						<c:forEach items="${inschrijving.vakanties}" var="vakantie">
							${vakantie.naam}<br/>
						</c:forEach>
					</td>
					<td>${inschrijving.deelnemers[0].voorNaam}</td>
					<td>${inschrijving.deelnemers[0].familieNaam}</td>
					<td>${gd}</td>
					<td>${inschrijving.status}</td>
					<td><a href="./inschrijvingen/${inschrijving.uuid}.html"><i class="fa fa-edit"></i></td>
				</tr>
			</c:forEach>
			
			</table>
			
		</div>	
		
		<div class="row mandatory">
		
			<h2>AANVAARD</h2>
			
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
			
			
			<c:forEach items="${accepted}" var="inschrijving">
				
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.inschrijvingsdatum}" var="date"></fmt:formatDate>
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>
							
				<tr>
					<td>
						<c:forEach items="${inschrijving.vakanties}" var="vakantie">
							${vakantie.naam}<br/>
						</c:forEach>
					</td>
					<td>${inschrijving.deelnemers[0].voorNaam}</td>
					<td>${inschrijving.deelnemers[0].familieNaam}</td>
					<td>${gd}</td>
					<td>${inschrijving.status}</td>
					<td><a href="./inschrijvingen/${inschrijving.uuid}.html"><i class="fa fa-edit"></i></td>
				</tr>
			</c:forEach>
			
			</table>
			
		</div>	
		
		<div class="row mandatory">
		
			<h2>GEWEIGERD</h2>
			
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
			
			
			<c:forEach items="${rejected}" var="inschrijving">
				
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.inschrijvingsdatum}" var="date"></fmt:formatDate>
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>
							
				<tr>
					<td>
						<c:forEach items="${inschrijving.vakanties}" var="vakantie">
							${vakantie.naam}<br/>
						</c:forEach>
					</td>
					<td>${inschrijving.deelnemers[0].voorNaam}</td>
					<td>${inschrijving.deelnemers[0].familieNaam}</td>
					<td>${gd}</td>
					<td>${inschrijving.status}</td>
					<td><a href="./inschrijvingen/${inschrijving.uuid}.html"><i class="fa fa-edit"></i></td>
				</tr>
			</c:forEach>
			
			</table>
			
		</div>	
		
		<div class="row mandatory">
		
			<h2>GEANNULEERD</h2>
			
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
			
			
			<c:forEach items="${cancelled}" var="inschrijving">
				
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.inschrijvingsdatum}" var="date"></fmt:formatDate>
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${inschrijving.deelnemers[0].geboorteDatum}" var="gd"></fmt:formatDate>
							
				<tr>
					<td>
						<c:forEach items="${inschrijving.vakanties}" var="vakantie">
							${vakantie.naam}<br/>
						</c:forEach>
					</td>
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
	
	<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
	
    <script>
    	var $jq = jQuery.noConflict();
    	
    </script>
  </body>
</html>
