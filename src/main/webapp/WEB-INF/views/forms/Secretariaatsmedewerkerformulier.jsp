<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>



<form:form commandName="nieuwesecretariaatsmedewerker" id="nieuwsecretariaatsmedewerkerformulier" action="secretariaatsmedewerker/add" method="POST">
	<table>
		<tr>
			<td><label for="vname">Voornaam</label> </td>
			<td><form:input path="voornaam" id="voornaam" class="required" minlength="2"/></td>
		</tr>
		<tr>
			<td><form:label path="familienaam">Familienaam:</form:label> </td>
			<td><form:input path="familienaam" id="familienaam"/></td>
		</tr>
		<tr>
			<td><form:label path="credentials.username">Username:</form:label> </td>
			<td><form:input path="credentials.username" id="username"/></td>
		</tr>
                <tr>
			<td><form:label path="credentials.emailadres">Email:</form:label> </td>
			<td><form:input path="credentials.emailadres" id="nieuwemailadres" /></td>
		</tr>
		<tr><td id="submitMedewerker"><input type="submit" value="Voeg toe" class="button"/></td>
		<td id="loading_submit_medewerker" class="display-none"><img src='/PirlewietRegistrations/resources/images/ajax-loader.gif' /></td></tr>
		
		
	</table>
</form:form>