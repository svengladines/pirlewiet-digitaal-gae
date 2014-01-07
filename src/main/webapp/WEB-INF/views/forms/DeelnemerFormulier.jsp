<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form commandName="deelnemer" id="nieuwdeelnemerformulier" action="deelnemer/add" method="POST">
	<table>
		<tr>
			<td><form:label path="voornaam">Voornaam:</form:label></td>
			<td><form:input path="voornaam" /></td>
		</tr>
		<tr>
			<td><form:label path="familienaam">Familienaam:</form:label></td>
			<td><form:input path="familienaam" /></td>
		</tr>
		<tr>
			<td><form:label path="telefoonnr">Tel.:</form:label></td>
			<td><form:input path="telefoonnr" /></td>
		</tr>
		<tr>
			<td><form:label path="rijksregisternr">Rijksregisternummer:</form:label></td>
			<td><form:input path="rijksregisternr" /></td>
		</tr>
		<tr>
			<td><form:label path="geboortedatum">Geboortedatum:</form:label></td>
			<td><form:input path="geboortedatum" readonly="readonly" class="datepicker" /></td>
		</tr>
		<tr>
			<td><form:label path="geslacht">Geslacht:</form:label></td>
			<td><form:select path="geslacht">
					<form:options items="${geslachten}" />
				</form:select></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="OK" /></td>
		</tr>

	</table>
</form:form>