<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<ol class="breadcrumb">
	  <li><a href="#">Inschrijving</a></li>
	  <li>Vakantie</li>
	  <li>Deelnemer</li>
	  <li>Opmerkingen</li>
	  <li>Contact</li>
	  <li class="active">Samenvatting</li>
	</ol>
	
	<h3>Bevestiging</h3>
	
	<div>
		<p>
			Je inschrijving werd ingediend.
		</p>
		<p>
			Klik <a href="${pageContext.request.contextPath}/dienst/home?gebruiker=${inschrijving.contactpersoon.id}&dienst=${inschrijving.contactpersoon.dienst.id}">hier</a> om terug te gaam naar je overzicht.
		</p>
	</div>