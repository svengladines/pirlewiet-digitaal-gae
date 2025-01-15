var Organisatie = function ( email ) {
	this.email = email;
};

var Organisation = function ( id, name, phone, email,city ) {
	
	this.uuid = id;
	this.name = name;
	this.phone = phone;
	this.email = email;
	this.city = city;
	
};

var Tourist = function ( code, email ) {
	this.code = code;
	this.email = email;
};

var CodeRequest = function ( email ) {
	this.email = email;
};

var postOrganisation = function ( organisation, button, statusElement, callback ) {

	$jq.ajax( {
		type: "post",
		url: base() + "/api/organisations",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( organisation ),
		success: function( result ) {
			if ( result.value == 'OK' ) {
				success( button, statusElement );
				window.location.href = "/start.html";
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

var putOrganisation = function ( organisation, button, errorElement, callback, callbackParam ) {
	
	busyButton( button );

	$jq.ajax( {
		type: "put",
		url: base() + "/api/organisations/" + organisation.uuid,
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( organisation ),
		success: function( updated ) {
			if ( callback ) {
				callback(updated);
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement );
		}
	});
	
};

var putOrganisationAddress = function ( organisation, adres, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url: base() + "/api/organisations/" + organisation.uuid + "/address",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( adres ),
		success: function( result ) {
			if ( result.value == 'OK' ) {
				success( button, statusElement );
				if ( callback ) {
					callback();
				}
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

var deleteOrganisation = function ( organisationUuid, button, statusElement, callback ) {

	$jq.ajax( {
		type: "delete",
		url: base() + "/api/organisations/" + organisationUuid,
		success: function( ) {
			if ( callback ) {
				callback( );
			}
			else {
				success( button, statusElement, "Verwijderd" );
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, statusElement, jqXHR.responseText );
		}
	});
	
};