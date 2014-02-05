<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<ol class="breadcrumb">
	  <li><a href="#">Inschrijving</a></li>
	  <li>Vakantie</li>
	  <li>Deelnemer</li>
	  <li>Opmerkingen</li>
	  <li class="active">Contact</li>
	  <li>Bevestiging</li>
	</ol>
	
	<h3>Contactpersoon</h3>
	
	<div>
		<p>
			Controleer AUB of je eigen gegevens en die van je dienst (nog) correct zijn en klik op 'verder'.
		</p>
		<c:set var="gebruiker" value="${inschrijving.contactpersoon}" scope="request"/>
		<c:set var="dienst" value="${inschrijving.contactpersoon.dienst}" scope="request"/>
		<div class="well welldone">
			<jsp:include page="/WEB-INF/views/dienst/gebruiker.jsp"/>
		</div>
		<div class="well welldone">
			<jsp:include page="/WEB-INF/views/dienst/dienst.jsp"/>
		</div>
		<form:form class="form-horizontal form-form" method="post" action="${pageContext.request.contextPath}/inschrijvingen/${inschrijving.id}/status" modelAttribute="command">
			
			<div class="form-group">
			   	<input type="hidden" name="status" value="CONTACTBEVESTIGD"/>
			 </div>
			  <div class="form-group">
			  	 <label for="btn" class="col-sm-0 control-label"></label>
			    <div class="col-sm-12">
			      <button type="button" class="btn btn-primary btn-next">Verder</button>
			    </div>
			  </div>
		</form:form>
		
	</div>