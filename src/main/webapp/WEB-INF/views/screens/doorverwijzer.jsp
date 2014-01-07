<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test='${screen == "Doorverwijzer"}'>	
		<div id="doorverwijzerTable"></div>
		<p id="filter"></p>

		<div id="nieuweDienstDialog">
			<jsp:include page="/WEB-INF/views/forms/DienstFormulier.jsp"></jsp:include>
		</div>
		<div id="bevestigDialog"></div>
	<div id="bevestigMessage"></div>
</c:if>