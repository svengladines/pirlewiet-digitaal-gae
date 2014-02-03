<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<div>
		<form:form class="form form-form" method="post" action="${pageContext.request.contextPath}/inschrijvingen/${inschrijving.id}/vakantie" modelAttribute="command">
			
			<div id="breadcrumb">Kies vakantie</div>
			
			<div id="vakantie">	
				<h3>Keuze vakantie</h3>
				
					<div class="form-group">
					 		<c:forEach items="${vakanties}" var="vakantie">
								<div id="vakantieradio" class="radio">
						  			<label>
						    			<input type="radio" name="vakantieId" value="${vakantie.id}">${vakantie}
						  			</label>
								</div>
							</c:forEach>
					 </div>
					 <div class="form-group">
					 	<button id="vakantiegekozen" type="button" class="btn btn-primary btn-next">Gekozen</button>
					  </div>
			</div>
			
		</form:form>
			
	</div>