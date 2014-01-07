function initTabs(){
	$("#tabs").tabs();
	
	//disable tabs 2-5
	$("#tabs").tabs({disabled: [1,2,3,4,5]});
	$(":input[type=submit]").attr("hidden", true);
}

function navigate(tabIndex){
	$("#tabs").tabs('enable',tabIndex);
	$("#tabs").tabs('select',tabIndex);
	
	if (tabIndex === 5) {
		$(":input[type=submit]").attr("hidden", false);
	}
}

function editFields(stapName){
	
	var buttonText = $("#" + stapName + " #editButton").val();
	
	if (buttonText == "Wijzigen") {
		$("#" + stapName + " input").attr("readonly", false);
		$("#" + stapName + " #editButton").attr("hidden", true);
		$("#" + stapName + " .okButton").attr("hidden", false);
	}else if (buttonText == "Opslaan") {
		$("#" + stapName + " input:text").attr("readonly", true);
		$("#" + stapName + " #editButton").attr("hidden", false);
		$("#" + stapName + " .okButton").attr("hidden", false);
	}
}