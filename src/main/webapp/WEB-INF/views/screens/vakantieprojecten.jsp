<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<c:if test='${screen == "Vakantieprojecten"}'>

		<div id="vakantieprojectTable"></div>

		<div id="nieuwVakantieprojectDialog">
			<jsp:include page="/WEB-INF/views/forms/VakantieprojectFormulier.jsp"></jsp:include>
		</div>

</c:if>