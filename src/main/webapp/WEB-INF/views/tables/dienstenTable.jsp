<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>

<table class="overviewTable" id="dienstenTable">
	<thead>

		<tr class="ui-widget-header">
			<th>Naam</th>
			<th class="centerAlign">Straat</th>
			<th class="centerAlign">Nummer</th>
			<th class="centerAlign">Gemeente</th>
			<th class="centerAlign">Postcode</th>
			<th class="centerAlign">Telefoon</th>
			<th class="centerAlign">Fax</th>
			<th class="centerAlign">Email</th>
			<th class="centerAlign">Afdeling</th>
			<th class="centerAlign">Actief</th>
		</tr>
	</thead>

	<tbody>

		<c:forEach var="dienst" items="${diensten}">

			<tr>
				<td class='centerAlign'>
					<s:url var="dienstOverzichtLink" value="/secretariaat/diensten/dienst-${dienst.id}" />
					<a href="${dienstOverzichtLink}">${dienst.naam}</a>
				</td>
				<td class='centerAlign'>${dienst.adres.straat}</td>
				<td class='centerAlign'>${dienst.adres.nummer}</td>
				<td class='centerAlign'>${dienst.adres.gemeente}</td>
				<td class='centerAlign'>${dienst.adres.postcode}</td>
				<td class='centerAlign'>${dienst.telefoonnummer}</td>
				<td class='centerAlign'>${dienst.faxnummer}</td>
				<td class='centerAlign'>${dienst.emailadres}</td>
				<td class='centerAlign'>${dienst.afdeling}</td>
				
				<!-- If enabled == true checkbox as checked -->
				<c:if test="${dienst.credentials.enabled == true}">
					<td class='centerAlign'><input type='checkbox' onchange="activeerDeactiveerDienst(${dienst.id})"
					id='checkbox_${dienst.id}' checked /></td>
				</c:if>
				
				<!-- If enabled == false checkbox as unchecked -->
				<c:if test="${dienst.credentials.enabled != true}">
					<td class='centerAlign'><input type='checkbox' onchange="activeerDeactiveerDienst(${dienst.id})"
					id='checkbox_${dienst.id}' /></td>
				</c:if>

					
					

			</tr>
		</c:forEach>

	</tbody>
</table>





