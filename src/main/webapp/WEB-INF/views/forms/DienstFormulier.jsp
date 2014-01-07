<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Voeg nieuwe dienst toe</h2>

<form:form commandName="dienst_object" id="nieuwDienstForm" action="addNewDienst" method="POST">
	<table>
		<tr>

			<td>Naam: </td>
			<td><form:input path="naam" required="required"/></td>
				
			
		</tr>
		<tr>
			<td>Straat: </td>
			<td><form:input path="adres.straat" id="straat"/></td>
		</tr>
		<tr>
			<td>Nummer: </td>
			<td><form:input path="adres.nummer" id="nummer"/></td>
		</tr>
		<tr>
			<td>Postcode: </td>
			<td><form:input path="adres.postcode" id="postcode"/></td>
		</tr>
		<tr>
			<td>Gemeente: </td>
			<td><form:input path="adres.gemeente" id="gemeente"/></td>
		</tr>

		<tr>
			<td>Telefoonnummer: </td>
			<td><form:input path="telefoonnummer"/></td>
		</tr>
		<tr>
			<td>Faxnummer: </td>
			<td><form:input path="faxnummer"/></td>
		</tr>
		<tr>
			<td>Emailadres: </td>
			<td><form:input path="emailadres"/></td>
		</tr>
		<tr>
			<td>Afdeling: </td>
			<td><form:input path="afdeling"/></td>
		</tr>
	</table>
	
	<input type="submit" value="Voeg toe" name="voeg_toe" class="button"/>
</form:form>


