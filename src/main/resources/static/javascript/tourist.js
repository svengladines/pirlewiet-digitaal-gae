var Tourist = function ( code, email ) {
	this.code = code;
	this.email = email;
};

var postTourist = function ( tourist, button, statusElement, callback ) {

	$jq.ajax( {
		type: "post",
		url: base() + "/api/tourists",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( organisation ),
		success: function( result ) {
			if ( result.value == 'OK' ) {
				success( button, statusElement );
			}
			else {
				error( button, statusElement, result.message );
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, statusElement, jqXHR.responseText );
		}
	});
	
    };

};