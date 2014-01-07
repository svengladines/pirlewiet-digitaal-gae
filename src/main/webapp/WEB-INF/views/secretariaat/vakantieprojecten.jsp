<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						initNieuweVakantieprojectBtn();
					});
</script>

<div>	
	<button type="button" id="nieuwVakantieProjectBtn" class="button">Nieuw vakantieproject toevoegen</button>
	<table class="overviewTable" id="vakantieprojectenTable">
		<thead>
		<tr class="ui-widget-header">
			<th>Type</th>
			<th class="centerAlign">Vakantie periode</th>
			<th class="centerAlign">Inschrijving periode</th>
			<th class="centerAlign">Max. deelnemers</th>
			<th class="centerAlign">Min. leeftijd</th>
			<th class="centerAlign">Max. leeftijd</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${vakanties}" var="vakantie">
			<tr>
				<td>${vakantie.vakantietype.displaynaam}</td>
				<td class="centerAlign"><fmt:formatDate pattern="d MMM" value="${vakantie.beginDatum}"/> - <fmt:formatDate pattern="d MMM" value="${vakantie.eindDatum}"/></td>
				<td class="centerAlign"><fmt:formatDate pattern="d MMM" value="${vakantie.beginInschrijving}"/> - <fmt:formatDate pattern="d MMM" value="${vakantie.eindInschrijving}"/></td>
				<td class="centerAlign">${vakantie.maxDeelnemers}</td>
				<td class="centerAlign">${vakantie.minLeeftijd}</td>
				<td class="centerAlign">${vakantie.maxLeeftijd}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>

<div id="nieuwVakantieprojectDialog">
	<jsp:include page="/WEB-INF/views/forms/VakantieprojectFormulier.jsp"></jsp:include>
</div>
<div id="resultMessage"></div>