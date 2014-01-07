<div>
	<!-- 
	<h4>Samenvatting</h4>
	<h2 style="color: #cc4a0a">Bekijk de gegevens en bevestig onderaan.</h2>
	 -->
	
	<div class="smallDiv">
		<h5 class="header-bg">Gegevens doorverwijzer</h5>
		
		<table class="imageAlignTable">
			<tr>
				<td><img src="${pageContext.request.contextPath}/resources/images/user.png"/></td>
				<td><label>${command.aanvraagInschrijving.contactpersoon.dienst.naam}</label></td>
			</tr>
			<tr>
				<td><img src="${pageContext.request.contextPath}/resources/images/home.png"/></td>
				<td>
					<label id="dienst_adres_straat">${command.aanvraagInschrijving.contactpersoon.dienst.adres.straat}</label>
					<label id="dienst_adres_nummer">${command.aanvraagInschrijving.contactpersoon.dienst.adres.nummer}</label><br/>
					<label id="dienst_adres_postcode">${command.aanvraagInschrijving.contactpersoon.dienst.adres.postcode}</label>
					<label id="dienst_adres_gemeente">${command.aanvraagInschrijving.contactpersoon.dienst.adres.gemeente}</label>
				</td>
			</tr>
			<tr>
				<td><img src="${pageContext.request.contextPath}/resources/images/contact.png"/></td>
				<td><label id="dienst_tel">${command.aanvraagInschrijving.contactpersoon.dienst.telefoonnummer}</label></td>
			</tr>
		</table>
		
	</div>
	
	<div class="smallDiv">
		<h5 class="header-bg">Gegevens contactpersoon</h5>
		
		
		<table class="imageAlignTable">
			<tr>
				<td><img src="${pageContext.request.contextPath}/resources/images/user.png"/></td>
				<td>
					<label id="contactpersoon_voornaam">${command.aanvraagInschrijving.contactpersoon.voornaam}</label>
					<label id="contactpersoon_naam">${command.aanvraagInschrijving.contactpersoon.familienaam}</label>
				</td>
			</tr>
			<tr>
				<td><img src="${pageContext.request.contextPath}/resources/images/case.png"/></td>
				<td><label id="contactpersoon_functie">${command.aanvraagInschrijving.contactpersoon.functie}</label></td>
			</tr>
			<tr>
				<td><img src="${pageContext.request.contextPath}/resources/images/mail.png"/></td>
				<td><label id="contactpersoon_email">${command.aanvraagInschrijving.contactpersoon.email}</label></td>
			</tr>
			<tr>
				<td><img src="${pageContext.request.contextPath}/resources/images/contact.png"/></td>
				<td><label id="contactpersoon_tel">${command.aanvraagInschrijving.contactpersoon.telefoonnr}</label></td>
			</tr>
		</table>
	</div>
	
	<div class="largeDiv">
		<h5 class="header-bg">Vakantieproject en deelnemers</h5>
		<label id="vakantieproject"></label><br/><br/>
		<label id="inschrijving_straat">${command.aanvraagInschrijving.deelnemersAdres.straat}</label>
		<label id="inschrijving_nummer">${command.aanvraagInschrijving.deelnemersAdres.nummer}</label>,
		<label id="inschrijving_postcode">${command.aanvraagInschrijving.deelnemersAdres.postcode}</label>
		<label id="inschrijving_gemeente">${command.aanvraagInschrijving.deelnemersAdres.gemeente}</label>
		
		<table id="summaryParticipantTable">
			<tr>
				<th>Voornaam</th>
				<th>Naam</th>
				<th>Rijksregisternummer</th>
				<th>Geboortedatum</th>
				<th>m/v</th>
			</tr>
		</table>
	</div>
	<div class="largeDiv">
		<h5>Opmerkingen</h5>
		Verder contact: <input type="text" readonly="readonly" name="contacttype"><br/><br/>
		<textarea readonly="readonly" cols="128" rows="5" id="aanvraag_opmerkingen">
			${command.aanvraagInschrijving.opmerkingen}
		</textarea>
	</div>

	<div id="tabFooterButtons_noMargin">
		<input type="submit" value="Aanvraag versturen" class="button" />
	</div>
</div>