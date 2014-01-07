//secretariaat/diensten init method
function initNieuweDienstBtn(){
	
	//Dialog to add a new dienst
	$("#nieuweDienstDialog").dialog({
		autoOpen : false,
		modal : true,
		resizable : false,
		title : 'Nieuwe Dienst',
		draggable : false,
		width :  'auto'
	});
	
	//Dialog showing all dienst object with the same name. The user can confirm of cancel.
	$("#bevestigDialog").dialog({
		autoOpen : false,
		modal : true,
		resizable : false,
		title : 'Nieuwe Dienst',
		draggable : false,
		width : 'auto',
		buttons : {
			"Bevestig" : function() {
				$(this).dialog("close");
				
				$.ajax({
					type : "POST",
					cache : false,
					data : $("#nieuwDienstForm").serialize(),
					url : "bevestig",
					success : function(response) {						
						showMessage("Nieuwe dienst werd met succes toegevoegd.","info");
						initDienstenPage();
						resetValues();
					},
				});
			},"Annuleer" : function() {
				$(this).dialog("close");
				
				resetValues();
			}
		}
	});
	//Dialog informing the user that the new dienst object was persisted successfully
	$("#bevestigMessage").dialog({
		autoOpen : false,
		modal : true,
		resizable : false,
		title : 'Dienst toegevoegd',
		draggable : false,
		width :  'auto',
		buttons : {
			"Ok" : function() {
				$(this).dialog("close");
				
				initDienstenPage();
			}
		}
	});
	
	$("#nieuweDienstBtn").click(function() {
		$("#nieuweDienstDialog").dialog('open');
	});
	
	//User clicked 'Voeg Toe'
	$('#nieuwDienstForm').submit(function(){
		console.log("Submit clicked !!");
		$("#nieuweDienstDialog").dialog('close');
		

		$.ajax({
			type : "POST",
			cache : false,
			url : "addNewDienst",
			data : $("#nieuwDienstForm").serialize(),
			success : function(data) {
				
				if($.trim(data) == "succes"){
					//reload the diensten table
					initDienstenPage();
					showMessage("Dienst was succesvol toegevoegd.","info");
					resetValues();
				}else{
					$("#bevestigDialog").html(data);
					$("#bevestigDialog").dialog('open');
				}
			},
			error: function(jqXHR, exception) {

	             alert("An error occured." + jqXHR + exception);
	            
	        }
		});
		return false;
		
		
	});
}

//After submitting reset all values
function resetValues(){
	$("#naam").val("");
	$("#straat").val("");
	$("#nummer").val("");
	$("#postcode").val("");
	$("#gemeente").val("");
	$("#telefoonnummer").val("");
	$("#faxnummer").val("");
	$("#emailadres").val("");
	$("#afdeling").val("");
}
function initDienstenPage(){
	
	$.ajax({
		type : "GET",
		cache : false,
		url : "getDienstenTable",
		success : function(data) {
			$("#doorverwijzerTable").html(data);
			
		}
	});
	
}
function initVakentieProjectPage(){
	console.log("init vakantie");
	$.ajax({
		type : "GET",
		cache : false,
		url : "getAllVakanties",
		success : function(data) {
			$("#vakantieprojectTable").html(data);
		},
		error : function (){
			console.log("error");
		}
	});
}
function initNieuweVakantieprojectBtn() {
	initFields();
	
	$("#nieuwVakantieprojectDialog").dialog({
		autoOpen : false,
		modal : true,
		resizable : false,
		title : 'Nieuw vakantieproject',
		draggable : false,
		width : 'auto'
	});

	$("#nieuwVakantieProjectBtn").click(function() {
		$("#nieuwVakantieprojectDialog").dialog('open');
	});

	
	$('#nieuwVakantieProjectForm').submit(
			function() {
				$("#nieuwVakantieprojectDialog").dialog('close');

				$.ajax({
					type : "POST",
					cache : false,
					url : "vakantieprojecten/add",
					data : $("#nieuwVakantieProjectForm").serialize(),
					success : function(data) {						
						$("#vakantieprojectTable").html(data);
						
						showMessage("Vakantieproject werd toegevoegd", "info");
						resetVakantieValues();
	
					}
				});
				return false;
			}
	);
	

}
//after submitting a new Vakantie, reset the values of the form
function resetVakantieValues(){
	$("#vakatieDropDown").val("Paaskinderkamp");
	$("#beginDatum").val("");
	$("#eindDatum").val("");
	$("#beginInschrijving").val("");
	$("#eindInschrijving").val("");
	$("#maxDeelnemers").val("");
	$("#minLeeftijd").val("");
	$("#maxLeeftijd").val("");
	
}
function initFields(){
	$(".datepicker").datepicker({
		clickInput : true,
		dateFormat : 'd/mm/yy'
	});

	$(".datepicker").keypress(function(event) {
		event.preventDefault();
	});
}