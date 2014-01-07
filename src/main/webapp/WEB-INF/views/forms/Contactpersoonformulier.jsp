<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form commandName="contactpersoon" id="nieuwcontactpersoonformulier" action="contactpersoon/add" method="POST">
	<table>
		<tr>
			<td><form:label path="voornaam">Voornaam:</form:label></td>
			<td><form:input path="voornaam" required="required"/></td>
		</tr>
		<tr>
			<td><form:label path="familienaam">Familienaam:</form:label></td>
			<td><form:input path="familienaam" required="required"/></td>
		</tr>
		<tr>
			<td><form:label path="telefoonnr">Tel.:</form:label></td>
			<td><form:input path="telefoonnr" /></td>
		</tr>
		<tr>
			<td><form:label path="functie">Functie:</form:label></td>
			<td><form:input path="functie" /></td>
		</tr>
				<tr>
			<td><form:label path="email">E-mail:</form:label></td>
			<td><form:input path="email" /></td>
		</tr>
		<tr><td><input type="submit" value="Voeg toe" class="button"/></td></tr>
	</table>
</form:form>