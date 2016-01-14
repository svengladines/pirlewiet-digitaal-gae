<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

    <jsp:include page="/WEB-INF/jsp/menu_pirlewiet.jsp">
    	<jsp:param name="active" value="organisations"/>
    </jsp:include>
    
	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Organisaties</h1>
					<p>
						Overzicht van de geregistreerde doorverwijzers.
					</p>
				</div>
			</div><!-- row -->
		</div><!-- container -->
	</div>

	<div class="container">
	
		<div class="row mandatory">
		
			<br/>
		
			<table class="table table-bordered table-condensed">
			
			<thead>
				<tr>
					<th><a href="/rs/organisations.html?order=name">Naam</a></th>
					<th><a href="/rs/organisations.html?order=street">Adres</a></th>
					<th><a href="/rs/organisations.html?order=city">Gemeente</a></th>
					<th>Telefoon</th>
					<th>GSM</th>
					<th>E-mail</th>
					<th>Code</th>
				</tr>
			</thead>
			
			
			<c:forEach items="${organisations}" var="organisation">
				
				<fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${organisation.updated}" var="updated"></fmt:formatDate>
				<tr>
					<td class="nowrap">${organisation.naam}</td>
					<td>${organisation.adres.straat} ${organisation.adres.nummer}</td>
					<td>${organisation.adres.zipCode} ${organisation.adres.gemeente}</td>
					<td>${organisation.telefoonNummer}</td>
					<td>${organisation.gsmNummer}</td>
					<td>${organisation.email}</td>
					<td>${organisation.code}</td>
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
