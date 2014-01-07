<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<c:if test="${duplicate == 'no'}">succes
</c:if>


<c:if test="${duplicate == 'yes'}">
<p>Er bestaat al een dienst met deze naam. Bent u zeker dat u de
	nieuwe dienst wil toevoegen?</p>

<table>
	<tr>
		<td>Naam</td>
		<td>Straat</td>
		<td>Nummer</td>
		<td>Postcode</td>
		<td>Gemeente</td>
		<td>Telefoon</td>
		<td>Fax</td>
		<td>Email</td>
	</tr>

	<c:forEach var="dienst" items="${diensten}">
		<tr>
			<td>${dienst.naam}</td>
			<td>${dienst.adres.straat}</td>
			<td>${dienst.adres.nummer}</td>
			<td>${dienst.adres.postcode}</td>
			<td>${dienst.adres.gemeente}</td>
			<td>${dienst.telefoonnummer}</td>
			<td>${dienst.faxnummer}</td>
			<td>${dienst.emailadres}</td>
			<td>${dienst.afdeling}</td>
		</tr>
	</c:forEach>
</table>
</c:if>

