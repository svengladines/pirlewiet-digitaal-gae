<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="overviewTable">
	<thead>
		<tr class='ui-widget-header'>
			<th class="centerAlign">secretariaatsmedewerker</th>
			<th class="centerAlign">gebruikersnaam</th>
			<th class="centerAlign">email</th>
			<th class='centerAlign'>actief</th>
			<th width='15%'></th>
		</tr>
	</thead>
	<tbody>


	</tbody>
	
		<c:forEach var="medewerker" items="${medewerkers}">

		<tr>
			<td style='padding-left: 8px;'>${medewerker}</td>
			<td style='padding-left: 8px;'>${medewerker.credentials.username}</td>
			<td style='padding-left: 8px;'>${medewerker.credentials.emailadres}</td>

			<!-- If enabled == true checkbox as checked -->
			<c:if test="${medewerker.credentials.enabled == true}">
				<td class='centerAlign'><input type='checkbox'
					onchange="activeerDeactiveerMedewerker(${medewerker.id})"
					id='checkbox_${medewerker.id}' checked />
				</td>
			</c:if>

			<!-- If enabled == false checkbox as unchecked -->
			<c:if test="${medewerker.credentials.enabled != true}">
				<td class='centerAlign'><input type='checkbox'
					onchange="activeerDeactiveerMedewerker(${medewerker.id})"
					id='checkbox_${medewerker.id}' />
				</td>
			</c:if>

			<td><input type='button' value='verzend nieuw wachtwoord'
				class='button'
				onclick='resetSecretariaatsMedewerker(${medewerker.id})' /> <input
				type='button' value='verwijder gebruiker' class='button'
				onclick='showConfirmDialog(${medewerker.id})' /></td>
			</tr>

		</c:forEach>
</table>