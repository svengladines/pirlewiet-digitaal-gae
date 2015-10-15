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

var postOrganisation = function ( organisation, button, errorElement, callback, callbackParam ) {

	$jq.ajax( {
		type: "post",
		url:"http://localhost:8068/rs/organisations",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( organisation ),
		success: function( jqXHR ) {
			callback( organisation, callbackParam, button, errorElement );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement, jqXHR.responseText );
		}
	});
	
};

var putOrganisation = function ( organisation, button, errorElement, callback, callbackParam ) {

	$jq.ajax( {
		type: "put",
		url:"https://pirlewiet-digitaal.appspot.com/rs/organisation",
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
		url:"https://pirlewiet-digitaal.appspot.com/rs/organisation/adres",
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