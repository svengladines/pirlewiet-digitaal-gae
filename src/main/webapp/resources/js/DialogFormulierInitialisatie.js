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

function laadVakanties(){
	
	$(".btn-add").click( function() {
		laadFormulier( "vakanties", "nieuw", "add" );
	});
	
	$.ajax({
		type : "GET",
		cache : false,
		url : "vakanties",
		success : function(data) {
			
			$("#vakanties").append(data);
			
			$(".btn-edit").click( function() {
				laadFormulier( "vakanties", $(this).attr("data-id"), "edit" );
			});
			
			$(".btn-delete").click( function() {
				laadFormulier( "vakanties", $(this).attr("data-id"), "delete" );
			});
		},
		error : function (){
			console.log("error");
		}
	});
}

function laadFormulier( context, id, action ) {
	
	$(".modal-form-" + action ).load( context + "/" + id + "/formulier", function() {
		
		$(".modal-" + action).modal("toggle");
		
		$('.btn-action-add').click(
				function() {
					$.ajax({
						type : "POST",
						cache : false,
						url: context,
						data : $(".form-form").serialize(),
						success : function(response) {
							$(".result").addClass("alert-success");
							$(".result-message").text(response);
							laadVakanties();
						}
					});
					return false;
				}
		);
		
		$('.btn-action-edit').click(
				function() {
					$.ajax({
						type : "PUT",
						cache : false,
						url: context + "/" + id,
						data : $(".form-form").serialize(),
						success : function(response) {
							$(".result").addClass("alert-success");
							$(".result-message").text(response);
							laadVakanties();
						}
					});
					return false;
				}
		);
		
		$('.btn-action-remove').click(
				function() {
					$.ajax({
						type : "DELETE",
						cache : false,
						url: context + "/" + id,
						data : $(".form-form").serialize(),
						success : function(response) {
							$(".result").addClass("alert-success");
							$(".result-message").text(response);
							laadVakanties();
						}
					});
					return false;
				}
		);
		
		$('.btn-cancel').click(
			function() {
				$(this).parents(".modal").modal("toggle");
			}
		);
		
		$('.btn-action').click(
				function() {
					$(this).attr("disabled","disabled");
				}
		);
		
		$('.btn-close').click(
			function() {
				$(this).parents(".modal").modal("toggle");
			}
		);
		
	});

}

function initVakantieProjectFormulier () {
	
	initFields();
	
	$('#nieuwVakantieProjectFormBtn').click(
			function() {
				$.ajax({
					type : "POST",
					cache : false,
					url : "vakantieprojecten",
					data : $("#nieuwVakantieProjectForm").serialize(),
					success : function(response) {
						$(".result").addClass("alert-success");
						$("#resultMessage").text(response);
						laadVakanties();
					}
				});
				return false;
			}
	);
	
	$('.btn-cancel').click(
		function() {
			$(this).parents(".modal").modal("toggle");
		}
	);
	
	$('.btn-action').click(
			function() {
				$(this).attr("disabled","disabled");
			}
	);
	
	$('.btn-close').click(
		function() {
			$(this).parents(".modal").modal("toggle");
		}
	);

}
//after submitting a new Vakantie, reset the values of the form
function resetVakantieForm(){
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