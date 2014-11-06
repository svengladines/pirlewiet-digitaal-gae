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

var putOrganisation = function ( organisation, formErrorElement ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/organisation",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( organisation ),
		success: function( jqXHR ) {
			$jq("#organisation-save").addClass("ok");
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			formErrorElement.html(textStatus);
		}
	});
	
};

var putOrganisationAdres = function ( organisation, adres, errorElement, formErrorElement ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/organisation/adres",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( adres ),
		success: function( returned ) {
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( formErrorElement, errorElement, jqXHR.responseText );
		}
	});
	
};