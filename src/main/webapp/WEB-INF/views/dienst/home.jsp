<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Inschrijving</h1>

	<div id="tabs" class="row">
		<ol>
		</ol>
	</div>
	
	<form:form class="form form-form" method="post" action="${pageContext.request.contextPath}/inschrijvingen" modelAttribute="command">
		<div class="form-group">
			<a href="inschrijving" class="btn btn-primary btn-start">Nieuwe inschrijving</a>
		</div>
	</form:form>
	
<script type="text/javascript">

	listen( "inschrijving", "${pageContext.request.contextPath}/dienst/inschrijving" );

</script>