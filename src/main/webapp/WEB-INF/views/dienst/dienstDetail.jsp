<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	$(document).ready(function() {
		initTabs();
	});
	
	function changeLabelText(labelID, text){
		document.getElementById(labelID).innerHTML = text;
	}
</script>

<h2>Doorverwijzer</h2>

<form:form method="post" action="model/inschrijving/add" modelAttribute="dienst">
	<div id="tabs">
		<ul>
			<li><a href="#stap1">Doorverwijzer</a></li>
			<li><a href="#stap2">Contactpersoon</a></li>
		</ul>
		<div id="stap1" class="tab tab-minHeight">
		<h5 style="margin-top: 20px;">Gegevens doorverwijzer</h5>
			<table style="margin-top: 45px;">
				<tr >
					<td>Naam buurtwerk/instelling/dienst:</td>
					<td><label>${dienst.naam}</label></td>
				</tr>
				<tr>
					<td>Straat:</td>
					<td><form:input
							path="adres.straat"
							size="50" readonly="true"
							onchange="changeLabelText('dienst_adres_straat',this.value)"/></td>
				</tr>
				<tr>
					<td>Huisnummer/bus:</td>
					<td><form:input
							path="adres.nummer"
							size="7" readonly="true"
							onchange="changeLabelText('dienst_adres_nummer',this.value)" /></td>
				</tr>
				<tr>
					<td>Postcode</td>
					<td><form:input
							path="adres.postcode"
							size="7" readonly="true"
							onchange="changeLabelText('dienst_adres_postcode',this.value)" /></td>
				</tr>
				<tr>
					<td>Gemeente:</td>
					<td><form:input
							path="adres.gemeente"
							size="50" readonly="true"
							onchange="changeLabelText('adres.gemeente',this.value)" /></td>
				</tr>
				<tr>
					<td>Tel:</td>
					<td><form:input
							path="telefoonnummer"
							readonly="true" onchange="changeLabelText('dienst_tel',this.value)" /></td>
				</tr>
			</table>

			<div id="tabFooterButtons">
				<input type="button" value="Wijzigen" onclick="editFields('stap1')" class="button" id="editButton" />
				<input type="button" value="Volgende" onclick="navigate(1)" class="button" />
				<input type="submit" value="Aanvraag versturen" class="button" />
			</div>
		</div>
		<div id="stap2" class="tab tab-minHeight">
	 			<h5 style="margin-top: 20px;">Gegevens contactpersonen</h5>
	 			<table id="contactPersonenTabel">
	 					<tr>
	 						<th>voornaam</th>
	 						<th>familienaam</th>
	 						<th>telefoonnummer</th>
	 						<th>functie</th>
	 						<th>email</th>
	 					</tr>
	 					
	 				<c:forEach items="${dienst.contactpersonen}" var="contact" varStatus="row">
	 					<tr>
	 						<td>${contact.voornaam}</td>
	 						<td>${contact.familienaam}</td>
	 						<td>${contact.telefoonnr}</td>
	 						<td>${contact.functie}</td>
	 						<td>${contact.email}</td>
	 					</tr>
	 				</c:forEach>
	 				
	 			</table>
		</div> 


 	</div>
</form:form>



<div id="resultMessage"></div>