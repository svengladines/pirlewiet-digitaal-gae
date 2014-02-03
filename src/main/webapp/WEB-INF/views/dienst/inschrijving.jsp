<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<div class="row">
	
		<h1>Inschrijving</h1>
		
		<c:out value="${inschrijving.status}"/>
		
		<c:choose>
		
			<c:when test="${inschrijving.status == 'VAKANTIEGEKOZEN'}">
				
				<jsp:include page="/WEB-INF/views/dienst/inschrijving_deelnemers.jsp"/>
			</c:when>
			<c:otherwise>
				<jsp:include page="/WEB-INF/views/dienst/inschrijving_vakantie.jsp"/>
			</c:otherwise>
		
		</c:choose>
			
	</div>

<script type="text/javascript">

	listen( "inschrijving", "${pageContext.request.contextPath}/dienst/inschrijving/${inschrijving.id}" );
	
</script>