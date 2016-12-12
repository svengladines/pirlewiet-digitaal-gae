<!DOCTYPE html>
<html>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
	<jsp:include page="/WEB-INF/jsp/head.jsp"/>
	
	<body>

      <c:choose>
	  <c:when test="${isPirlewiet}">
	  		<c:set var="zee" value="de"/>
	      	<jsp:include page="/WEB-INF/jsp/menu-pirlewiet.jsp">
	    		<jsp:param name="active" value="organisation"/>
	   		</jsp:include>
	   </c:when>
	   <c:otherwise>
	   		<c:set var="zee" value="jouw"/>
	   		<jsp:include page="/WEB-INF/jsp/menu-public.jsp">
	    		<jsp:param name="active" value="organisation"/>
	   		</jsp:include>
	   </c:otherwise>
	   </c:choose>
    
	<div class="banner">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-12">
					<h1>Organisaties</h1>
					<p>
						Overzicht van de geregistreerde organisaties.
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
					<th><a href="/organisations.html?order=name">Naam</a></th>
					<th><a href="/organisations.html?order=city">Gemeente</a></th>
					<th>E-mail</th>
				</tr>
			</thead>
			
			<c:set var="organisations" value="${organisationsResult.object}"/>
			
			
			<c:forEach items="${organisations}" var="organisationResult">
			
			<c:set var="organisation" value="${organisationResult.object}"/>
				
				<tr>
					<td>${organisation.name}</td>
					<td class="nowrap">${organisation.city}</td>
					<td class="nowrap">${organisation.email}</td>
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
