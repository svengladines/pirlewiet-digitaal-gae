<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
	<h3>Inschrijving</h3>
	
	<div>
			
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
			      <p class="form-control-static"><span class="glyphicon glyphicon-user"></span>&nbsp;${inschrijving.deelnemer.voornaam} ${inschrijving.deelnemer.familienaam}</p>
			      <p class="form-control-static"><span class="glyphicon glyphicon-calendar"></span>&nbsp;<span>${dienst.naam}</span>${inschrijving.deelnemer.geboortedatum}</p>
			      <p class="form-control-static"><span class="glyphicon glyphicon-tag">${inschrijving.deelnemer.rijksregisternr}</p>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Opmerkingen</label>
			    <div class="col-sm-10">
			      <p class="form-control-static">${inschrijving.opmerkingen}</p>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Contactpersoon</label>
			    <div class="col-sm-10">
			      <p class="form-control-static">${inschrijving.contactpersoon.voornaam} ${inschrijving.deelnemer.familienaam}</p>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Doorverwijzer</label>
			    <div class="col-sm-10">
			      <p class="form-control-static">${inschrijving.contactpersoon.dienst.naam}</p>
			    </div>
			  </div>
		</form>
		
		<form:form class="form-horizontal form-form" method="post" action="${pageContext.request.contextPath}/inschrijvingen/${inschrijving.id}/status" modelAttribute="command">
			
			<div class="form-group">
			    <label class="col-sm-2 control-label">Status</label>
			    <div class="col-sm-10">
			      <select name="status" class="form-control">
					  <option value="INBEHANDELING">In behandeling</option>
					  <option value="HUISBEZOEK">Huisbezoek gepland</option>
					  <option value="WACHTLIJST">Wachtlijst</option>
					  <option value="GEWEIGERD">Geweigerd</option>
					  <option value="BEVESTIGD">Bevestigd</option>
					</select>
			    </div>
			 </div>
			  <div class="form-group">
			  	 <label for="btn" class="col-sm-2 control-label"></label>
			    <div class="col-sm-10">
			      <button type="button" class="btn btn-primary btn-next">Aanpassen</button>
			    </div>
			  </div>
		</form:form>
		
	</div>
	
<script type="text/javascript">

	listen( "inschrijving", "${pageContext.request.contextPath}/secretariaat/inschrijving/${inschrijving.id}" );
	
</script>