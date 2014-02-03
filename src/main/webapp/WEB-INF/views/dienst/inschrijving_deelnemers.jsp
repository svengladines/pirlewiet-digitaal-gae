<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
	<h3>Deelnemer</h3>
	
	<div>
		<form:form class="form-horizontal form-query" method="post" action="${pageContext.request.contextPath}/deelnemers" modelAttribute="command">
			
			<div class="form-group">
			    <label for="q" class="col-sm-2 control-label">Rijksregisternummer</label>
			    <div class="col-sm-4">
			      <input type="text" class="form-control" id="rrn" name="rrn" placeholder="">
			    </div>
			 </div>
			 <div class="form-group">
			    <label for="q" class="col-sm-2 control-label"></label>
			    <div class="col-sm-4">
			      <span>of</span>
			    </div>
			 </div>
			 <div class="form-group">
			    <label for="family" class="col-sm-2 control-label">Familienaam</label>
			    <div class="col-sm-4">
			      <input type="text" class="form-control" id="family" name="family" placeholder="">
			    </div>
			  </div>
			 
			  <div class="form-group">
			  	 <label for="family" class="col-sm-2 control-label"></label>
			    <div class="col-sm-4">
			      <button type="button" class="btn btn-primary btn-query">Zoek</button>
			    </div>
			  </div>
		</form:form>
		
		<div>
			<form:form class="form form-form" method="post" action="${pageContext.request.contextPath}/inschrijvingen/${inschrijving.id}/deelnemers" modelAttribute="command">
				
				<div id="inschrijving" class="form-group">
					
				 </div>
				 <div class="form-group">
				 	<button id="vakantiegekozen" type="button" class="btn btn-primary btn-next">Gekozen</button>
				  </div>
			</form:form>
		</div>
		
	</div>