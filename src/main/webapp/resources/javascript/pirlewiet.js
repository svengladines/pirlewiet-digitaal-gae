function error( button, element, message ) {
	
	button.button('reset');
	button.removeClass("btn-success");
	button.addClass("btn-danger");
	if ( message != undefined ) {
		element.html( message )	;
	}
	element.removeClass("hidden").addClass("show");
	
};

function success( button, element, message ) {
	
	button.button('reset');
	button.removeClass("btn-danger");
	button.addClass("btn-success");
	
	if ( message != undefined ) {
		element.html( message )	;
	}
	
	if ( element != undefined ) {
		element.removeClass("hidden").addClass("show");
	}
	
};

function clearError() {
	
	$jq(".error").removeClass("show").addClass("hidden");
	
}