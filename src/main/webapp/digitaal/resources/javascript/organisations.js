var Organisatie = function ( email ) {
	this.email = email;
};

var Organisation = function ( id, name, telephone, gsm, email ) {
	
	this.uuid = id;
	this.naam = name;
	this.telefoonNummer = telephone;
	this.gsmNummer = gsm;
	this.email = email;
	
};

var CodeRequest = function ( email ) {
	this.email = email;
};

var postOrganisation = function ( organisation, button, statusElement, callback ) {

	$jq.ajax( {
		type: "post",
		url: base() + "/rs/organisations",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( organisation ),
		success: function( jqXHR ) {
			success( button, statusElement );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, statusElement, jqXHR.responseText );
		}
	});
	
};

var putOrganisation = function ( organisation, button, errorElement, callback, callbackParam ) {

	$jq.ajax( {
		type: "put",
		url: base() + "/rs/organisation",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( organisation ),
		success: function( jqXHR ) {
			callback( organisation );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement );
		}
	});
	
};

var putOrganisationAddress = function ( organisation, adres, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url: base() + "/rs/organisation/adres",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( adres ),
		success: function( returned ) {
			success( button, statusElement );
			if ( callback ) {
				callback();
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, statusElement, jqXHR.responseText );
		}
	});
	
};