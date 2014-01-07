<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
	$(document).ready(
					function() {
						initNieuweDienstBtn();
					});
	
</script>
<div>	
	<button type="button" id="nieuweDienstBtn" class="button">Nieuwe dienst toevoegen</button>
	<table class="overviewTable" id="dienstenTable">
		<thead>
		<tr class="ui-widget-header">
			<th>Naam</th>
			<th class="centerAlign">Straat</th>
			<th class="centerAlign">Nummer</th>
			<th class="centerAlign">Postcode</th>
			<th class="centerAlign">Gemeente</th>
			<th class="centerAlign">Telefoon</th>
			<th class="centerAlign">Fax</th>
			<th class="centerAlign">Email</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${diensten}" var="dienst">
			<tr>
				<td>${dienst.naam}</td>
				<td class="centerAlign">${dienst.adres.straat}</td>
				<td class="centerAlign">${dienst.adres.nummer}</td>
				<td class="centerAlign">${dienst.adres.postcode}</td>
				<td class="centerAlign">${dienst.adres.gemeente}</td>
				<td class="centerAlign">${dienst.telefoonnummer}</td>
				<td class="centerAlign">${dienst.faxnummer}</td>
				<td class="centerAlign">${dienst.emailadres}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
<div id="nieuweDienstDialog">
	<jsp:include page="/WEB-INF/views/forms/DienstFormulier.jsp"></jsp:include>
</div>
<div id="bevestigDialog">
</div>
<div id="bevestigMessage">
</div>

