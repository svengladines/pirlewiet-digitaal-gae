<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:forEach items="${vakanties}" var="vakantie">
	<tr>
		<td>${vakantie.vakantietype.displaynaam}</td>
		<td><fmt:formatDate pattern="d MMM" value="${vakantie.beginDatum}"/> - <fmt:formatDate pattern="d MMM" value="${vakantie.eindDatum}"/></td>
		<td><fmt:formatDate pattern="d MMM" value="${vakantie.beginInschrijving}"/> - <fmt:formatDate pattern="d MMM" value="${vakantie.eindInschrijving}"/></td>
		<td>${vakantie.maxDeelnemers}</td>
		<td>${vakantie.minLeeftijd}</td>
		<td>${vakantie.maxLeeftijd}</td>
		<td>
			<button type="button" class="btn btn-warning btn-sm btn-edit" data-id="${vakantie.id}">
  				<span class="glyphicon glyphicon-pencil"></span>
			</button>
			<button type="button" class="btn btn-danger btn-sm btn-delete" data-id="${vakantie.id}">
  				<span class="glyphicon glyphicon-trash"></span>
			</button>
		</td>
	</tr>
</c:forEach>






