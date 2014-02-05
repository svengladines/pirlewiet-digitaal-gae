<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="row">
	<div class="pull-left">
		<p class="bg-info">
			Hier vind je de actuele toestand van je inschrijvingen
		</p>
	</div>
	
	<div class="well welldone pull-right">
		<jsp:include page="/WEB-INF/views/dienst/dienst.jsp"/>
	</div>
	
	<div class="well welldone pull-right">
		<jsp:include page="/WEB-INF/views/dienst/gebruiker.jsp"/>
	</div>

</div>

<div class="row">

	<div id="inschrijvingen-contact">
		<form class="form-horizontal form-query" method="post" action="${pageContext.request.contextPath}/inschrijvingen">
			
	      <input type="hidden" id="contact" name="contact" value="${gebruiker.id}"/>
	      <input type="hidden" id="context" name="context" value="dienst"/>
	      
	     </form>
	     
	</div>
	
</div>

<div class="row">
	
	<form:form class="form form-form" method="post" action="${pageContext.request.contextPath}/inschrijvingen" modelAttribute="command">
		<div class="form-group">
			 <input type="hidden" id="contact" name="contact" value="${gebruiker.id}"/>
		</div>
		<div class="form-group">
			<a href="inschrijving" class="btn btn-primary btn-start">Nieuwe inschrijving</a>
		</div>
	</form:form>
	
</div>

<script type="text/javascript">

	listen( "inschrijving", "${pageContext.request.contextPath}/dienst/inschrijving" );
	
	query( "inschrijvingen-contact" );

</script>