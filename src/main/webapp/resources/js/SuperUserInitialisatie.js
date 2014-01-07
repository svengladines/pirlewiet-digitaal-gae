var mijnTabel;
var geselecteerdeDeelnemers = new Array();

var test = "";

//boolean to keep state of the msg div
var inShowMsg = false; 

//get all medewerkers who must be displayed
function initPageSuperUserHome(){
	$.ajax({
	    type : "GET",
	    cache : false,
	    url : "secretariaatsmedewerker/getAllDisplayed",
	    success : function(data) {
	    	$("#medewerkers_table").html(data);

	    }
	});	
}

//Checkbox actief is clicked + show msg
function activeerDeactiveerMedewerker(id) {
    var dataObject = { "secretariaatsmedewerkerID": id };
    var oldcheck = $("#checkbox_"+id).attr('checked');
    var url = "" ;
    if (oldcheck == 'checked') {
    	//Checkbox was off
       url = "secretariaatsmedewerker/enable";
    }else {
    	//Checkbox was on
    	url =  "secretariaatsmedewerker/disable" ;
    }
     $.ajax({
            type : "POST",
            cache : false,
            url : url,
            data : dataObject,
            success : function(response) {
            	showMessage(response.msg, "info");
            }
       });
}

//Show dialog to create a new medewerker object
function initNieuweSecretariaatsMedewerkerButton() {
    $("#NewSecretariaatsmedewerkersDialog").dialog({
        autoOpen : false,
        modal : true,
        resizable : false,
        title : 'Nieuwe SecretariaatsMedewerker',
        draggable : false,
        width : 'auto'
    });

    $("#nieuweSecretariaatsMedewerkerKnop").click(function() {
        $("#NewSecretariaatsmedewerkersDialog").dialog('open');
    });
    
    var textnumsecr = $("#numUsers").text();
    var numsecr = 0;
    if (textnumsecr != null)
        numsecr = parseInt(textnumsecr);
    if (numsecr == 0) {
        $("#secretariaatsCredentialsOverzicht").hide();
        $("#notNoUsersMsg").hide();
    }
    if (numsecr > 0) {
        $("#noUsersMsg").hide();
    }
    $('#NewSecretariaatsmedewerkersDialog').submit(function() {

    		
    		$("#submitMedewerker").addClass("display-none");
    		$("#loading_submit_medewerker").removeClass("display-none");
    	
            // validate fields
            var str = $("#nieuwemailadres").val();
            var at="@" ;
            var dot="." ;
            var lat=str.indexOf(at) ;
            var lstr=str.length ;
            var ldot=str.indexOf(dot) ;
            if ((str.indexOf(at)<1) || str.indexOf(at)==lstr || str.indexOf(dot)<=1 || str.indexOf(dot,(lat+2))==-1 || str.indexOf(" ")!=-1
                || str.indexOf(dot)==lstr  || str.indexOf(at,(lat+1))!=-1 || str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot )
                {
            	showMessage("Ongeldig E-mailadres", "error");
            	
            	$("#submitMedewerker").removeClass("display-none");
	    		$("#loading_submit_medewerker").addClass("display-none");
	    		$("#NewSecretariaatsmedewerkersDialog").dialog("close");
            	
            	return false;
            };

            // go to server
            var dataObject = $("#nieuwsecretariaatsmedewerkerformulier").serialize();

            $.ajax({
                type : "POST",
                cache : false,
                url : "secretariaatsmedewerker/add",
                data : dataObject,
                success : function(data) {              	   	        	
                	$("#medewerkers_table").html(data);
                	
        	        showMessage("De medewerker werd met succes toegevoegd.",'info');
        	        resetMedewerkerValues();
     	    		$("#NewSecretariaatsmedewerkersDialog").dialog("close");
    	    		$("#submitMedewerker").removeClass("display-none");
    	    		$("#loading_submit_medewerker").addClass("display-none");
                },
                error : function (){            	
                	showMessage("Mail kon niet verstuurd worden. Er werd geen medewerker aangemaakt.",'error');
                	resetMedewerkerValues();
    	    		$("#submitMedewerker").removeClass("display-none");
    	    		$("#loading_submit_medewerker").addClass("display-none");
    	    		$("#NewSecretariaatsmedewerkersDialog").dialog("close");
                }
            });
            
            
            return false;
        });
}

function resetMedewerkerValues(){
	$("#voornaam").val("");
	$("#familienaam").val("");
	$("#username").val("");
	$("#nieuwemailadres").val("");
}

function deleteSecretariaatsMedewerker(id){
	console.log("Delete id: " + id);
	var dataObject = { "id": id };
	
	$.ajax({
	    type : "POST",
	    cache : false,
	    url : "secretariaatsmedewerker/delete",
	    data : dataObject,
	    success : function(data) {        	
	     
	        showMessage("De mederwerker werd met succes verwijderd", "info");
	        $("#medewerkers_table").html(data);

	    }
	});
}

//Show a msg at the top of the page
//Include <div id="msg"></div> at the top of your page
//accepts the msg and the type of message.
function showMessage(msg,type) {

		if(type=="info"){
			
			$("#msg").addClass("msgStyle").addClass("msgInfo");
			
			string = "<img src='/PirlewietRegistrations/resources/images/info_message.png'/>";
			string += "<h3 style='display:inline;'>"+msg+"</h3>";
			
			
			
		}else if(type=="error"){
			$("#msg").removeClass("msgInfo");
			$("#msg").addClass("msgStyle").addClass("msgError");
			
			string = "<img src='/PirlewietRegistrations/resources/images/error_message.png'/>";
			string += "<h3 style='display:inline;'>"+msg+"</h3>";
			
		}

		$("#msg").html(string);

		    
		  
}



function resetSecretariaatsMedewerker(id){
	console.log("Reset id: " + id);
	var dataObject = { "id": id };
	

	//hide the closing div
	$("#sendingMail").dialog({
		dialogClass: 'no-close'
	});
	
	$("#sendingMail").dialog('open');
	$.ajax({
	    type : "POST",
	    cache : false,
	    url : "secretariaatsmedewerker/reset",
	    data : dataObject,
	    success : function(response) {
	        showMessage(response.msg, "info");
	        $("#sendingMail").dialog('close');

	    }, 
	    error : function(response){
	    	showMessage("Mail kon niet verstuurd worden. De medewerker werd niet gereset.",'error');
	    	$("#sendingMail").dialog('close');
	    }
	});
}