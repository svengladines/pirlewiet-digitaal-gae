<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="table table-bordered">
	<thead>
		<tr>
			<th>ID</th>
			<th>Vakantie</th>
			<th>Familienaam</th>
			<th>Voornaam</th>
			<th>Geboortedatum</th>
			<th>Doorverwijzer</th>
			<th>Status</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${inschrijvingen}" var="inschrijving">
			<tr>
				<td><a href="${pageContext.request.contextPath}/${context}/inschrijving/${inschrijving.id}">${inschrijving.id}</a></td>
				<td>${inschrijving.vakantieproject}</td>
				<td>${inschrijving.deelnemer.familienaam}</td>
				<td>${inschrijving.deelnemer.voornaam}</td>
				<td>${inschrijving.deelnemer.geboortedatum}</td>
				<td>${inschrijving.contactpersoon.dienst.naam}</td>
				<td>${inschrijving.status.displayString}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>