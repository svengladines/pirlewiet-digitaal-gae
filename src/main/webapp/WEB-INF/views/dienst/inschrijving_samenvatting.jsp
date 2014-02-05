<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<ol class="breadcrumb">
	  <li><a href="#">Inschrijving</a></li>
	  <li>Vakantie</li>
	  <li>Deelnemer</li>
	  <li>Opmerkingen</li>
	  <li>Contact</li>
	  <li class="active">Samenvatting</li>
	</ol>
	
	<h3>Samenvatting</h3>
	
	<div>
		<p>
			Controleer de gegevens van je inschrijving en klik op 'indienen'.
		</p>
		
		<form class="form-horizontal" role="form">
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Vakantie</label>
			    <div class="col-sm-10">
			      <p class="form-control-static">${inschrijving.vakantieproject}</p>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Deelnemer</label>
			    <div class="col-sm-10">
			      <p class="form-control-static">${inschrijving.deelnemer.voornaam} ${inschrijving.deelnemer.familienaam}</p>
			      <p class="form-control-static">${inschrijving.deelnemer.geboortedatum}</p>
			      <p class="form-control-static">${inschrijving.deelnemer.rijksregisternr}</p>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Opmerkingen</label>
			    <div class="col-sm-10">
			      <p class="form-control-static">${inschrijving.opmerkingen}</p>
			    </div>
			  </div>
		</form>
		
		<form:form class="form-horizontal form-form" method="post" action="${pageContext.request.contextPath}/inschrijvingen/${inschrijving.id}/status" modelAttribute="command">
			
			<div class="form-group">
			   	<input type="hidden" name="status" value="INGEDIEND"/>
			 </div>
			  <div class="form-group">
			  	 <label for="btn" class="col-sm-2 control-label"></label>
			    <div class="col-sm-10">
			      <button type="button" class="btn btn-primary btn-next">Indienen</button>
			    </div>
			  </div>
		</form:form>
		
	</div>