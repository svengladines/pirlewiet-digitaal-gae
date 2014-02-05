<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
	<ol class="breadcrumb">
	  <li><a href="#">Inschrijving</a></li>
	  <li>Vakantie</li>
	  <li>Deelnemer</li>
	  <li class="active">Opmerkingen</li>
	  <li>Contact</li>
	</ol>
	
	<h3>Opmerkingen</h3>
	
	<div>
		<form:form class="form-horizontal form-form" method="post" action="${pageContext.request.contextPath}/inschrijvingen/${inschrijving.id}/opmerkingen" modelAttribute="command">
			
			<div class="form-group">
			    <label for="opmerking" class="col-sm-0 control-label"></label>
			    <div class="col-sm-12">
			      <textarea class="form-control" id="opmerkingen" name="opmerkingen" rows="10"></textarea>
			    </div>
			 </div>
			  <div class="form-group">
			  	 <label for="btn" class="col-sm-0 control-label"></label>
			    <div class="col-sm-12">
			      <button type="button" class="btn btn-primary btn-next">Verder</button>
			    </div>
			  </div>
		</form:form>
		
	</div>