<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<div class="row">
	
		<!--
		<h1>Inschrijving</h1>
		<c:out value="${inschrijving.status}"/>
		-->
		
		<c:choose>
		
			<c:when test="${inschrijving.status == 'NIEUW'}">
				<jsp:include page="/WEB-INF/views/dienst/inschrijving_vakantie.jsp"/>
			</c:when>
			<c:when test="${inschrijving.status == 'VAKANTIEGEKOZEN'}">
				<jsp:include page="/WEB-INF/views/dienst/inschrijving_deelnemer.jsp"/>
			</c:when>
			<c:when test="${inschrijving.status == 'DEELNEMERTOEGEVOEGD'}">
				<jsp:include page="/WEB-INF/views/dienst/inschrijving_opmerkingen.jsp"/>
			</c:when>
			<c:when test="${inschrijving.status == 'OPMERKINGENINGEVULD'}">
				<jsp:include page="/WEB-INF/views/dienst/inschrijving_contact.jsp"/>
			</c:when>
			<c:when test="${inschrijving.status == 'CONTACTBEVESTIGD'}">
				<jsp:include page="/WEB-INF/views/dienst/inschrijving_samenvatting.jsp"/>
			</c:when>
			<c:when test="${inschrijving.status == 'INGEDIEND'}">
				<jsp:include page="/WEB-INF/views/dienst/inschrijving_ingediend.jsp"/>
			</c:when>
			<c:otherwise>
				<jsp:include page="/WEB-INF/views/dienst/inschrijving_samenvatting.jsp"/>
			</c:otherwise>
		
		</c:choose>
			
	</div>
	
	<div class="footer">
	
	</div>
	

<script type="text/javascript">

	listen( "inschrijving", "${pageContext.request.contextPath}/dienst/inschrijving/${inschrijving.id}" );
	
</script>