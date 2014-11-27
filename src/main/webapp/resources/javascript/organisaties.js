var Organisatie = function ( email ) {
	this.email = email;
};

var Organisation = function ( id, name, telephone, gsm, email, altEmail ) {
	
	this.id = id;
	this.naam = name;
	this.telefoonNummer = telephone;
	this.gsmNummer = gsm;
	this.email = email;
	this.alternativeEmail = altEmail;
	
};

var CodeRequest = function ( email ) {
	this.email = email;
};

var postOrganisation = function ( organisation, button, errorElement, callback, callbackParam ) {

	$jq.ajax( {
		type: "post",
		url:"/rs/organisations",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( organisation ),
		success: function( jqXHR ) {
			callback( organisation, callbackParam, button, errorElement );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement );
		}
	});
	
};

var putOrganisation = function ( organisation, button, errorElement, callback, callbackParam ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/organisation",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( organisation ),
		success: function( jqXHR ) {
			callback( organisation, callbackParam, button, errorElement );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement );
		}
	});
	
};

var putOrganisationAddress = function ( organisation, adres, button, errorElement ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/organisation/adres",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( adres ),
		success: function( returned ) {
			success( button, $jq("#organisation-ok" ) );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement, jqXHR.responseText );
		}
	});
	
};