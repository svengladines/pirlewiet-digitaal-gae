

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
>

	<script>
		$(document).ready(function() {
			$(".overzichtdeelnemers tr:even").css('background-color',"whitesmoke");
		});
	</script>

	<h2>Inschrijvingen (${command.size()})</h2>

	<c:if test="${command.size() eq 0}">
        geen lopende inschrijvingen
    </c:if>
	<c:if test="${command.size() ne 0}">
		<c:forEach var="inschrijving" items="${command}">
			<div class="overzichtinschrijving ui-tabs ui-widget ui-widget-content ui-corner-all">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">Aanvraag </div>

				<table class="overzichtdeelnemers">
					<tr>
						<th>Deelnemer</th>
						<th>Geboortedatum</th>
						<th>Geslacht</th>
						<c:set var="theString" value="I am a test String"/>
						<c:if test="${! fn:containsIgnoreCase(inschrijving.vakantieproject.vakantietype, 'kinder')}"><th>telefoon</th></c:if>
					</tr>
					<c:forEach var="deelnemer" items="${inschrijving.deelnemers}">
						<tr class="gekleurd">
							<td>${deelnemer.voornaam} ${deelnemer.familienaam}</td>
							<td>${deelnemer.geboortedatum}</td>
							<td>${deelnemer.geslacht}</td>
							<c:if test="${! fn:containsIgnoreCase(inschrijving.vakantieproject.vakantietype, 'kinder')}"><td>${deelnemer.telefoonnr}</td></c:if>
						</tr>
					</c:forEach>
				</table>

				<div class="extraInfoInschrijving">
					<table>
						<tr>
							<td><b>Vakantieproject:</b></td>
							<td>${inschrijving.vakantieproject}</td>
						</tr>
						<tr>
							<td><b>Thuisadres:</b></td>
							<td>${inschrijving.deelnemersAdres}</td>
						</tr>
						<tr>
							<td><b>Contactpersoon:</b></td>
							<td>${inschrijving.contactpersoon.voornaam}
								${inschrijving.contactpersoon.familienaam}</td>
						</tr>
						<tr>
							<td><b>Contact verloopt via:</b></td>
							<td>${inschrijving.contactType.displayName}</td>
						</tr>
						<tr>
							<td><b>Status:</b></td>
							<td>${inschrijving.status.displayString}</td>
						</tr>
					</table>
				</div>

				<div class="clearbothfix">
					<p></p>
				</div>
			</div>
		</c:forEach>

	</c:if>
</jsp:root>
