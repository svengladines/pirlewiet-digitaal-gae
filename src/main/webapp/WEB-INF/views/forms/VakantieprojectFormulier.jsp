<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<form:form commandName="vakantieproject" id="nieuwVakantieProjectForm" action="vakantieprojecten/add" method="POST">
	<table>
		<tr>
			<td>Type:</td>
			<td><form:select path="vakantietype.id" id="vakatieDropDown">
					<c:forEach items="${vakantieTypes}" var="vakantieType">
						<form:option value="${vakantieType.id}">${vakantieType.displaynaam}</form:option>
					</c:forEach>
				</form:select>
			</td>
		</tr>
		<tr>
			<td>Datum vertrek:</td>
			<td><form:input path="beginDatum" class="datepicker" /></td>
		</tr>
		<tr>
			<td>Datum terugkomst:</td>
			<td><form:input path="eindDatum" class="datepicker" /></td>
		</tr>
		<tr>
			<td>Datum start inschrijving:</td>
			<td><form:input path="beginInschrijving" class="datepicker" /></td>
		</tr>
		<tr>
			<td>Datum einde inschrijving:</td>
			<td><form:input path="eindInschrijving" class="datepicker" /></td>
		</tr>
		<tr>
			<td>Max. aantal deelnemers:</td>
			<td><form:input path="maxDeelnemers" class="smallField"/></td>
		</tr>
		<tr>
			<td>Min. leeftijd:</td>
			<td><form:input path="minLeeftijd" class="smallField"/></td>
		</tr>
		<tr>
			<td>Max. leeftijd:</td>
			<td><form:input path="maxLeeftijd" class="smallField"/></td>
		</tr>
	</table>
	
	<input type="submit" value="Voeg toe" class="button"/>
</form:form>