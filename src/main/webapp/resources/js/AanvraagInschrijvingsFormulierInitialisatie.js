var mijnTabel;
var geselecteerdeDeelnemers = new Array();

var test = "";
var deelnemerCount = 0;
function initDeelnemersLijst() {
	mijnTabel = $('#deelnemerslijstDatatable')
			.dataTable(
					{
						"sAjaxSource" : 'datatabledatagenerated',
						"sPaginationType" : "full_numbers",
						"fnCreatedRow" : function(nRow, aData, iDataIndex) {

							var newcheckbox = document.createElement('td');
							newcheckbox.innerHTML = '<input type="checkbox" />';
							nRow.appendChild(newcheckbox);

							var newtd = document.createElement('td');
							newtd.innerHTML = '<input type="button" class="edit" value="Edit"/><input type="button" class="delete" value="Delete"/>';
							nRow.appendChild(newtd);

							$("input:checkbox", nRow).live("click", function() {
								$(nRow).toggleClass('row_selected');
								showSelectedDeelnemers();
							});
						},
						"aoColumns" : [ {
							"bSearchable" : false,
							"bVisible" : false
						}, null, null, null, null ],
						"bAutoWidth" : false
					});
};

function doSomething() {
	var lijstGeslecteerdeDeelnemers = new Array();
	for (i = 0; i < fnGetSelected(mijnTabel).length; i++) {
		lijstGeslecteerdeDeelnemers.push(mijnTabel
				.fnGetData(fnGetSelected(mijnTabel)[i])[0]);
	}
	alert(lijstGeslecteerdeDeelnemers);
}

function fnGetSelected(oTableLocal) {
	return oTableLocal.$('tr.row_selected');
}

// CRUD OPERATIONS FOR DATATABLE
function editRow(oTable, nRow) {
	var aData = oTable.fnGetData(nRow);
	var jqTds = $('>td', nRow);
	jqTds[0].innerHTML = '<input type="text" value="' + aData[1] + '">';
	jqTds[1].innerHTML = '<input type="text" value="' + aData[2] + '">';
	jqTds[2].innerHTML = '<input type="text" value="' + aData[3] + '">';
	jqTds[3].innerHTML = '<input type="text" value="' + aData[4] + '">';
	jqTds[5].innerHTML = '<a class="edit" href="">Save</a><a class="delete" href="">delete</a>';
}

function saveRow(oTable, nRow) {
	var jqInputs = $('input', nRow);
	var jqTds = $('>td', nRow);
	oTable.fnUpdate(jqInputs[0].value, nRow, 1, false);
	oTable.fnUpdate(jqInputs[1].value, nRow, 2, false);
	oTable.fnUpdate(jqInputs[2].value, nRow, 3, false);
	oTable.fnUpdate(jqInputs[3].value, nRow, 4, false);
	jqTds[5].innerHTML = '<a class="edit" href="">Edit</a><a class="delete" href="">delete</a>';
	oTable.fnDraw();

	var sData = oTable.fnGetData(nRow);
	alert(sData);
}

function restoreRow(oTable, nRow) {
	var aData = oTable.fnGetData(nRow);
	var jqTds = $('>td', nRow);

	for ( var i = 0, iLen = jqTds.length - 1; i < iLen; i++) {
		oTable.fnUpdate(aData[i], nRow, i, false);
	}
	jqTds[5].innerHTML = '<a class="edit" href="">Edit</a>';

	// oTable.fnDraw();
}

function initCrudButtonsDeelnemersTabel() {
	// FIRST INITIALIZE THE DIALOG (autoOpen on false to hide the content, but
	// wait to open the dialog)
	$("#nieuweDeelnemerDialog").dialog({
		autoOpen : false,
		modal : true,
		resizable : false,
		title : 'Nieuwe deelnemer',
		draggable : false,
		width : 'auto'
	});

	$('#deelnemerslijstDatatable input.edit').live(
			'click',
			function(e) {
				var nRow = $(this).parents('tr')[0];
				var aData = mijnTabel.fnGetData(nRow);
				var deelnemerId = aData[0];
				e.preventDefault();
				$
						.ajax({
							type : "GET",
							url : "deelnemer/get/" + deelnemerId,
							cache : false,
							success : function(response) {
								test = response;
								var formId = "#nieuweDeelnemerDialog";
								$("#voornaam", formId).val(response.voornaam);
								$("#familienaam", formId).val(
										response.familienaam);
								$("#telefoonnr", formId).val(
										response.telefoonnr);
								$("#rijksregisternr", formId).val(
										response.rijksregisternummer);
								$("#geboortedatum", formId).val(
										response.geboortedatum);
								$(
										"#geslacht option:contains('"
												+ response.geslacht + "')")
										.val(response.geslacht);
								$("#nieuweDeelnemerDialog").dialog('option',
										'title', 'Bewerk deelnemer');
								$("#nieuweDeelnemerDialog").data('bewerking',
										'edit');
								$("#nieuweDeelnemerDialog").dialog('open');
							}

						});
			});

	$('#deelnemerslijstDatatable input.delete').live('click', function(e) {
		// e.preventDefault();
		// var nRow = $(this).parents('tr')[0];
		// mijnTabel.fnDeleteRow(nRow);
		alert('delete...');
	});

	// Bind the dialog to clicking on an element with ID = new
	$('#new').click(
			function() {
				$('#nieuwdeelnemerformulier').each(function() {
					this.reset();
				});
				$("#nieuweDeelnemerDialog").dialog('option', 'title',
						'Nieuwe deelnemer');
				$("#nieuweDeelnemerDialog").data('bewerking', 'new');
				$("#nieuweDeelnemerDialog").dialog('open');
			});
}

// Show selected participants in second table
function showSelectedDeelnemers() {
	var selected = fnGetSelected(mijnTabel);

	// Set border if table is visible
	if (selected.length > 0) {
		$("#selectedParticipantTable").css("border-style", "solid");
		$("#selectedParticipantTable").css("border-width", "1px");
	} else {
		$("#selectedParticipantTable").css("border-width", "0px");
	}

	// Empty table
	$("#selectedParticipantTable tr").remove();

	// Fill table with selected participants
	for ( var i = 0; i < selected.length; i++) {
		var rowData = mijnTabel.fnGetData(selected[i]);

		var tableRow = $('<tr id="' + rowData[0] + '"/>').appendTo(
				$("#selectedParticipantTable tbody"));
		$("<td />").text(rowData[1]).appendTo(tableRow);
		$("<td />").text(rowData[2]).appendTo(tableRow);
		$("<td />").text(rowData[3]).appendTo(tableRow);
		$("<td />").text(rowData[4]).appendTo(tableRow);
		$(
				"<td><input type='button' value='X' onclick='removeSelected("
						+ rowData[0] + ")'/></td>").appendTo(tableRow);
	}
}

function openAddDeelnemerForm(){
	
	
}

function addDeelnemer() {
	var voornaam = $("#newVoornaam");
	var familienaam = $("#newFamilienaam");
	var rijksregister = $("#newRijksregisterNummer");
	var geboortedatum = $("#newGeboortedatum");
	var geslacht = $("#newGeslacht");
	var rowID = $("#participantTable tr").length;

	var dataObject = {
		'voornaam' : voornaam.val(),
		'familienaam' : familienaam.val(),
		'rijksregisternr' : rijksregister.val(),
		'geboortedatum' : geboortedatum.val(),
		'geslacht' : geslacht.val()
	};

	$
			.ajax({
				type : "POST",
				url : "deelnemer/add/" + rowID,
				cache : false,
				data : dataObject,
				success : function(response) {
					$('.errorblock').remove();
					if (response === "OK") {
						deelnemerCount ++;
						// Table on 'Deelnemer toevoegen'
						var tableRow = $('<tr id="' + rowID + '"/>').appendTo(
								$("#participantTable tbody"));
						$("<td />").text(voornaam.val()).appendTo(tableRow);
						$("<td />").text(familienaam.val()).appendTo(tableRow);
						$("<td />").text(rijksregister.val())
								.appendTo(tableRow);
						$("<td />").text(geboortedatum.val())
								.appendTo(tableRow);
						$("<td />").text($("#newGeslacht option:selected").text()).appendTo(tableRow);
						$(
								"<td><input type='button' value='Verwijderen' class='button' onclick='deleteDeelnemer("
										+ rowID + ")'/></td>").appendTo(
								tableRow);

						// Table on summary page
						var summaryTableRow = $('<tr id="' + rowID + '"/>')
								.appendTo($("#summaryParticipantTable tbody"));
						$("<td />").text(voornaam.val()).appendTo(
								summaryTableRow);
						$("<td />").text(familienaam.val()).appendTo(
								summaryTableRow);
						$("<td />").text(rijksregister.val()).appendTo(
								summaryTableRow);
						$("<td />").text(geboortedatum.val()).appendTo(
								summaryTableRow);
						$("<td />").text($("#newGeslacht option:selected").text()).appendTo(
								summaryTableRow);

						// Clear fields
						voornaam.val('');
						familienaam.val('');
						rijksregister.val('');
						geboortedatum.val('');
						geslacht.val('Onbekend');
						
						
						if(deelnemerCount > 0){
							$("#stap4 .okButton").attr("disabled", false).removeClass("ui-state-disabled");
						}
						
						/*if (($("#vakantieProjectSelect option:selected").index()) > 0) {
							$("#stap3 .okButton").attr("hidden", false);
						}else{
							$("#stap3 .okButton").attr("hidden", true);
						}*/
						
					}else{
						$("<div class='errorblock'><p>De velden 'Voornaam' en 'Naam' zijn verplicht. Vul deze in en probeer opnieuw</p></div>").insertAfter(
								$("#participantTable"));
					}
				}
			});
	
	//if there are 5 deelnemers static 
	
	if(deelnemerCount > 4){
		$("#tabFooterButtons_addDeelnemers").removeClass("pos_absolute");
	}
}

function deleteDeelnemer(rowIndex) {
	deelnemerCount--;
	
	if(deelnemerCount == 0){
		$("#stap4 .okButton").attr("disabled", true).addClass("ui-state-disabled");
	}else if(deelnemerCount == 5){
		$("#tabFooterButtons_addDeelnemers").addClass("pos_absolute");
	}
	
	$.ajax({
		type : "POST",
		cache : false,
		url : "deelnemer/delete/" + rowIndex,
		success : function() {
			$('.errorblock').remove();
			$('#participantTable tbody #' + rowIndex).remove();
			$('#summaryParticipantTable tbody #' + rowIndex).remove();
			
			if ((($("#vakantieProjectSelect option:selected").index()) == 0) || (($("#participantTable tbody tr").length) <= 2)) {
				$("#stap3 .okButton").attr("hidden", true);
				$("#tabs").tabs({disabled: [3,4]});
				$(":input[type=submit]").attr("hidden", true);
			}else{
				$("#stap3 .okButton").attr("hidden", false);
			}
		}
	});
}
function selectContactType() {
	$('input[name=contacttype]').val(
			$("#contacttypeSelect option:selected").text());
}
function selectProject() {
	var m_names = new Array("Januari", "Februari", "Maart", "April", "Mei",
			"Juni", "Juli", "Augustus", "September", "Oktober", "November",
			"December");

	var labelText = "Inschrijving: vanaf ";
	$("#vakantieProjectSelect option[value='0']").attr("disabled", true);

	$.ajax({
		url : "vakantieproject/get/" + $("#vakantieProjectSelect").val(),
		cache : false,
		success : function(data) {
			var beginInschrijving = new Date(data.beginInschrijving);
			var eindInschrijving = new Date(data.eindInschrijving);
			var beginString = beginInschrijving.getDate() + " "
					+ m_names[beginInschrijving.getMonth()];
			var eindString = eindInschrijving.getDate() + " "
					+ m_names[eindInschrijving.getMonth()] + " "
					+ eindInschrijving.getFullYear();
			
			labelText += beginString + " t/m " + eindString;
			$("#inschrijvingLabel").text(labelText);
			$("#inschrijvingLabel").attr("hidden", false);
			if (eindInschrijving < new Date()) {
				$("#inschrijvingLabel").css("color", "red");
			} else {
				$("#inschrijvingLabel").css("color", "green");
			}
			
			/*if (($("#participantTable tbody tr").length) > 2) {
				$("#stap3 .okButton").attr("hidden", false);
			}else{
				$("#stap3 .okButton").attr("hidden", true);
				$("#tabs").tabs({disabled: [3,4]});
				$(":input[type=submit]").attr("hidden", true);
			}*/
			$('#vakantie_info').html("<p>Hier komt meer info over het geselecteerde project</p>");

			$('#stap5 #vakantieproject').html($("#vakantieProjectSelect option:selected").text());
		}
	});
	
	//if a a project has been selected --> show info about project
	// show net button
	if (($("#vakantieProjectSelect option:selected").index()) > 0) {
		$("#stap3 .okButton").attr("disabled", false).removeClass("ui-state-disabled");
	}else{
		$("#stap3 .okButton").attr("disabled", true);
	}

}

function initNieuwDeelnemersFormulierWithExisting() {
	// #nieuweDeelnemerMessage
}

function initNieuweContactpersoonButton() {
	$("#nieuweContactpersoonDialog").dialog({
		autoOpen : false,
		modal : true,
		resizable : false,
		title : 'Nieuwe contactpersoon',
		draggable : false,
		width : 'auto'
	});

	$('#nieuwdeelnemerformulier').submit(function() {
		$("#nieuweDeelnemerDialog").dialog('close');

		$("#resultMessage").dialog({
			buttons : {
				"Ok" : function() {
					$(this).dialog("close");
				}
			}
		});

		var voornaam = $("input#voornaam").val();
		var familienaam = $("input#familienaam").val();
		var telefoonnr = $("input#telefoonnr").val();
		var rijksregisternnr = $("input#rijksregisternr").val();
		var geboortedatum = $("input#geboortedatum").val();
		var geslacht = $("input#geslacht").val();

		var dataObject = {
			'voornaam' : voornaam,
			'familienaam' : familienaam,
			'telefoonnr' : telefoonnr,
			'rijksregisternr' : rijksregisternr,
			'geboortedatum' : geboortedatum,
			'geslacht' : geslacht
		};
		var url = "";

		if ($("#nieuweDeelnemerDialog").data('bewerking') == 'edit') {
			url = "deelnemer/edit";
		} else {
			url = "deelnemer/add";
		}

		$.ajax({
			type : "POST",
			cache : false,
			url : url,
			data : dataString,
			success : function(response) {
				console.log("Response:" + response);
				$("#resultMessage").html("<p>" + response.resultaat + "</p>");
				$("#resultMessage").dialog('open');
			}
		});
		return false;
	});

	$('#contactpersoonSelect')
			.change(
					function() {
						if ($(this).val() == 0) {
							$('#inputveldenContactpersoon')
									.attr('hidden', true);
						} else {
							$("#contactpersoonSelect option[value='0']").attr(
									"disabled", true);
							$
									.ajax({
										url : "contactpersoon/get/"
												+ $(this).val(),
										cache : false,
										success : function(data) {
											$(
													"#aanvraagInschrijving\\.contactpersoon\\.voornaam")
													.html(data.voornaam);
											$(
													"#aanvraagInschrijving\\.contactpersoon\\.familienaam")
													.html(data.familienaam);
											$(
													"input#aanvraagInschrijving\\.contactpersoon\\.telefoonnr")
													.val(data.telefoonnr);
											$(
													"input#aanvraagInschrijving\\.contactpersoon\\.functie")
													.val(data.functie);
											$(
													"input#aanvraagInschrijving\\.contactpersoon\\.email")
													.val(data.email);
											$('#inputveldenContactpersoon')
													.attr('hidden', false);
											// fill in fields on summary page
											$('#stap5 #contactpersoon_naam').html(data.familienaam);
											$('#stap5 #contactpersoon_voornaam').html(data.voornaam);
											$('#stap5 #contactpersoon_email').html(data.email);
											$('#stap5 #contactpersoon_tel').html(data.telefoonnr);
											$('#stap5 #contactpersoon_functie').html(data.functie);
										}
									});
						}
					});
	$('#inputveldenContactpersoon').attr('hidden', true);

	$("#nieuweContactpersoonKnop").click(function() {
		$("#nieuweContactpersoonDialog").dialog('open');
	});

	$('#nieuweContactpersoonDialog').submit(
			function() {
				$("#nieuweContactpersoonDialog").dialog('close');

				$("#resultMessage").dialog({
					buttons : {
						"Ok" : function() {
							$(this).dialog("close");
						}
					}
				});

				var voornaam = $("input#voornaam",
						"#nieuweContactpersoonDialog").val();
				var familienaam = $("input#familienaam",
						"#nieuweContactpersoonDialog").val();
				var telefoonnr = $("input#telefoonnr",
						"#nieuweContactpersoonDialog").val();
				var functie = $("input#functie", "#nieuweContactpersoonDialog")
						.val();
				var email = $("input#email", "#nieuweContactpersoonDialog")
						.val();
				var dataObject = {
					'voornaam' : voornaam,
					'familienaam' : familienaam,
					'telefoonnr' : telefoonnr,
					'functie' : functie,
					'email' : email
				};
				$.ajax({
					type : "POST",
					cache : false,
					url : "contactpersoon/add",
					data : dataObject,
					success : function(response) {
						console.log("Response:" + response);
						$("#resultMessage").html(
								"<p>" + response.resultaat + "</p>");
						$('#contactpersoonSelect').append($("<option/>", {
							value : response.resultaatId,
							text : voornaam + ' ' + familienaam
						}));
						$('#contactpersoonSelect').val(response.resultaatId);
						$('#contactpersoonSelect').change();
						$("#resultMessage").dialog('open');
					}
				});
				return false;
			});
}