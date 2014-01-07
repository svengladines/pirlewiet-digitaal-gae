<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						initTabs();

						initDeelnemersLijst();
						initCrudButtonsDeelnemersTabel();
						initNieuweContactpersoonButton();
						selectContactType();
						$(".datepicker").datepicker({
							clickInput : true,
							dateFormat : 'yy-mm-dd'
						});

						$(".datepicker").keypress(function(event) {
							event.preventDefault();
						});

						$("#resultMessage").dialog({
							autoOpen : false,
							modal : true
						});


						$( "#newGeboortedatum" ).datepicker({
							yearRange: "-60: +0",
							changeMonth: true,
							changeYear: true,
							dateFormat: "dd/mm/yy",
							maxDate: "+0d"
						});
					});
	
	function changeLabelText(labelID, text){
		document.getElementById(labelID).innerHTML = text;
	}
</script>
<h2>Inschrijving</h2>

<form:form method="post" action="model/inschrijving/add"
	modelAttribute="command">
	<div id="tabs">
		<ul>
			<li><a href="#stap1">Doorverwijzer</a></li>
			<li><a href="#stap2">Contactpersoon</a></li>
			<li><a href="#stap3">Vakantieproject</a></li>
			<li><a href="#stap4">Deelnemers</a></li>
			<li><a href="#stap5">Opmerkingen</a></li>
			<li><a href="#stap6">Samenvatting</a></li>
		</ul>

		<div id="stap1" class="tab tab-minHeight">
		<h5 style="margin-top: 20px;">Gegevens doorverwijzer</h5>
			<table style="margin-top: 45px;">

				<tr >
					<td>Naam buurtwerk/instelling/dienst:</td>
					<td><label>${command.aanvraagInschrijving.contactpersoon.dienst.naam}</label></td>
				</tr>
				<tr>
					<td>Straat:</td>
					<td><form:input
							path="aanvraagInschrijving.contactpersoon.dienst.adres.straat"
							size="50" readonly="true"
							onchange="changeLabelText('dienst_adres_straat',this.value)"/></td>
				</tr>
				<tr>
					<td>Huisnummer/bus:</td>
					<td><form:input
							path="aanvraagInschrijving.contactpersoon.dienst.adres.nummer"
							size="7" readonly="true"
							onchange="changeLabelText('dienst_adres_nummer',this.value)" /></td>
				</tr>
				<tr>
					<td>Postcode</td>
					<td><form:input
							path="aanvraagInschrijving.contactpersoon.dienst.adres.postcode"
							size="7" readonly="true"
							onchange="changeLabelText('dienst_adres_postcode',this.value)" /></td>
				</tr>
				<tr>
					<td>Gemeente:</td>
					<td><form:input
							path="aanvraagInschrijving.contactpersoon.dienst.adres.gemeente"
							size="50" readonly="true"
							onchange="changeLabelText('dienst_adres_gemeente',this.value)" /></td>
				</tr>
				<tr>
					<td>Tel:</td>
					<td><form:input
							path="aanvraagInschrijving.contactpersoon.dienst.telefoonnummer"
							readonly="true" onchange="changeLabelText('dienst_tel',this.value)" /></td>
				</tr>
			</table>

			<div id="tabFooterButtons">
				<input type="button" value="Wijzigen" onclick="editFields('stap1')"
					class="button" id="editButton" /> <input type="button"
					value="Volgende" onclick="navigate(1)" class="button" />
				<input type="submit" value="Aanvraag versturen" class="button" />
			</div>
		</div>
		<div id="stap2" class="tab tab-minHeight">
			<h5 style="margin-top: 20px;">Gegevens contactpersoon</h5>
			<p>
				<form:select id="contactpersoonSelect"
					path="aanvraagInschrijving.contactpersoon.id">
					<form:option value="0">Selecteer een contactpersoon</form:option>
					<form:options items="${contactpersonen}" itemValue="id" />
				</form:select>
				<button type="button" id="nieuweContactpersoonKnop" class="button">Nieuwe contactpersoon</button>
			<table id="inputveldenContactpersoon" style="margin-top: 30px;">
				<tr>
					<td><label>Naam:</label></td>
					<td><label id="aanvraagInschrijving.contactpersoon.voornaam"></label>
					 <label
						id="aanvraagInschrijving.contactpersoon.familienaam"></label></td>
				</tr>
				<tr>
					<td><form:label
							path="aanvraagInschrijving.contactpersoon.telefoonnr">Tel:</form:label></td>
					<td><form:input
							path="aanvraagInschrijving.contactpersoon.telefoonnr"
							readonly="true"
							onchange="changeLabelText('contactpersoon_tel',this.value)" /></td>
				</tr>
				<tr>
					<td><form:label
							path="aanvraagInschrijving.contactpersoon.functie">Functie:</form:label></td>
					<td><form:input
							path="aanvraagInschrijving.contactpersoon.functie"
							readonly="true"
							onchange="changeLabelText('contactpersoon_functie',this.value)" /></td>
				</tr>
				<tr>
					<td><form:label
							path="aanvraagInschrijving.contactpersoon.email">E-mail:</form:label></td>
					<td><form:input
							path="aanvraagInschrijving.contactpersoon.email" readonly="true"
							onchange="changeLabelText('contactpersoon_email',this.value)" /></td>
				</tr>

			</table>

			<div id="tabFooterButtons">
				<input type="button" value="Wijzigen" onclick="editFields('stap2')"
					class="button" id="editButton" /> <input type="button"
					value="Volgende" onclick="navigate(2)" class="okButton button" />
				<input type="submit" value="Aanvraag versturen" class="button" />
			</div>
		</div>
		<div id="stap3" class="tab tab-minHeight">
		<h5 style="margin-top: 15px;">Keuze vakantieproject</h5>
			<table id="stap3TableWrapper">

				<tr>
				<td> <form:select
							path="aanvraagInschrijving.vakantieproject.id"
							onchange="selectProject()" 
							id="vakantieProjectSelect">
							<form:option value="0">Selecteer een vakantieproject</form:option>
							<form:options items="${vakanties}" itemValue="id"/>
						</form:select></td>
					<td>
						<label id="inschrijvingLabel" hidden="true"></label>
					</td>
				</tr>
			</table>
			<div id="vakantie_info">
			</div>
			<div id="tabFooterButtons">
				<input type="button" value="Volgende" onclick="navigate(3)"
					class="okButton button ui-state-disabled" disabled="true"/>
				<input type="submit" value="Aanvraag versturen" class="button" />
			</div>
		</div>
		<div id="stap4" class="tab tab-minHeight">
		<h5 style="margin-top: 15px">Adres</h5>
						<table>
							<tr>
								<td>Straat:</td>
								<td><form:input path="aanvraagInschrijving.deelnemersAdres.straat"
								onchange="changeLabelText('inschrijving_straat',this.value)"/></td>
														<td>Nr/Bus:</td>
								<td><form:input path="aanvraagInschrijving.deelnemersAdres.nummer"
										size="7" onchange="changeLabelText('inschrijving_nummer',this.value)"/></td>
							</tr>
							<tr>
								<td>Postcode</td>
								<td><form:input path="aanvraagInschrijving.deelnemersAdres.postcode"
										size="7" onchange="changeLabelText('inschrijving_postcode',this.value)"/></td>
																		<td>Gemeente:</td>
								<td><form:input path="aanvraagInschrijving.deelnemersAdres.gemeente"
										size="" onchange="changeLabelText('inschrijving_gemeente',this.value)"/></td>
							</tr>

						</table>
						<table id="participantTable">
							<tr>
								<th>Voornaam</th>
								<th>Naam</th>
								<th>Rijksregisternummer</th>
								<th>Geboortedatum</th>
								<th>m/v</th>
								<th></th>
							</tr>
							<tr style="height: 40px">
								<td><input type="text" id="newVoornaam"/></td>
								<td><input type="text" id="newFamilienaam"/></td>
								<td><input type="text" id="newRijksregisterNummer"/></td>
								<td><input type="text" id="newGeboortedatum" readonly="readonly"/></td>
								<td><select id="newGeslacht">
									<c:forEach items="${geslachten}" var="geslacht">
										<option value="${geslacht}">${geslacht.indicator}</option>
									</c:forEach>
								</select></td>
								<td><input type="button" value="Toevoegen"
									class="button" onclick="addDeelnemer()"/></td>
							</tr>
						</table>
			<div id="tabFooterButtons_addDeelnemers" class="pos_absolute">
				<input type="button" value="Volgende" onclick="navigate(4)" class="okButton button ui-state-disabled" disabled="true" /> 
				<input type="submit" value="Aanvraag versturen" class="button" />
			</div>
		</div>
		<div id="stap5" class="tab tab-minHeight">
			<table>
				<tr>
					<td><p>Verder contact via: <form:select
							path="aanvraagInschrijving.contactType"
							onchange="selectContactType()" 
							id="contacttypeSelect">
							<form:option value="">-</form:option>
							<form:options items="${contacttypes}" />
						</form:select></p></td>
				</tr>

				<tr>
					<td colspan="2"><h4>Opmerkingen</h4></td>
				</tr>
				<tr>
					<td><form:textarea rows="10" cols="126"
							path="aanvraagInschrijving.opmerkingen"
							onchange="changeLabelText('aanvraag_opmerkingen',this.value)" /></td>
				</tr>
			</table>
			<div id="tabFooterButtons">
				<input type="button" value="Volgende" onclick="navigate(5)"
					class="button" />
				<input type="submit" value="Aanvraag versturen" class="button" />
			</div>
		</div>
		<div id="stap6" class="tab tab-minHeight">
			<jsp:include page="/WEB-INF/views/dienst/aanvraagSummary.jsp"></jsp:include>
		</div>
	</div>
</form:form>

<div id="nieuweDeelnemerDialog">
</div>
<div id="nieuweContactpersoonDialog">
	<jsp:include page="/WEB-INF/views/forms/Contactpersoonformulier.jsp"></jsp:include>
</div>


<div id="resultMessage"></div>