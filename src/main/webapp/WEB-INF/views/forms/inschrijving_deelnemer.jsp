<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<c:forEach items="${deelnemers}" var="deelnemer">
		<div id="vakantieradio" class="radio">
  			<label>
    			<input type="radio" name="deelnemerId" value="${deelnemer.id}">${deelnemer.familienaam} ${deelnemer.voornaam},  ${deelnemer.geboortedatum}
  			</label>
		</div>
	</c:forEach>