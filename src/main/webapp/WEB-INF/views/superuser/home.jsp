<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
    $(document).ready(function() {
    	initPageSuperUserHome();
        initNieuweSecretariaatsMedewerkerButton();
       
    	//Dialog to add a new dienst
    	$("#sendingMail").dialog({
    		autoOpen : false,   		
    		modal : true,
    		resizable : false,
    		title : 'Nieuw wachtwoord verzenden',
    		draggable : false,
    		width :  'auto'
    	});

    	//Dialog to add a new dienst
    	$("#confirmdialog").dialog({
    		autoOpen : false,
    		modal : true,
    		resizable : false,
    		title : 'Verwijder Secretariaatsmedewerker',
    		draggable : false,
    		width :  'auto',
    		buttons : {
    			"Ok" : function() {
    				$(this).dialog("close");

    				var id = $(this).data('id');
    				//SuperUserInitialisatie.js
    				
    				deleteSecretariaatsMedewerker(id);
    			},"Annuleer" : function() {
    				$(this).dialog("close");
    			}
    		}
    	});


    });

    function showConfirmDialog(id){
        //send a param to the dialog with the data() method
    	$("#confirmdialog").data('id',id).dialog('open');
     }


</script>

<div id="msg" class="msgStyle infoStyle">
		<h3 style='display:inline;color:#E9E9E9'>test</h3>
</div>


<div id="NewSecretariaatsmedewerkersDialog">
    <jsp:include page="/WEB-INF/views/forms/Secretariaatsmedewerkerformulier.jsp"></jsp:include>
</div>
<div id="resultMessage"></div>
<div id="confirmdialog">
	<p>Bent u zeker dat u deze Secretariaatsmedewerker wilt verwijderen?</p>
</div>
<div id="medewerkers_table"></div>

<div id="medewerkers_table_bottom">
	<button type="button" id="nieuweSecretariaatsMedewerkerKnop" class="button">voeg secretariaatsmedewerker toe</button>
	<div id="aantal_secretariaats_medewerkers">
		<h2 id="notNoUsersMsg">Aantal actieve secretariaatsmedewerkers: <span id="numUsers"></span></h2>
	</div>
</div>

<div id="sendingMail">
<p> Nieuw wachtwoord wordt verstuurd ...</p>
<img style="margin-left: 100px;" src="/PirlewietRegistrations/resources/images/ajax-loader.gif" /> 
</div>
