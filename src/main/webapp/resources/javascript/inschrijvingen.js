var Inschrijving = function ( id ) {

	if ( id != null ) {
		this.uuid = id;
	}
	
};

var Contact = function ( naam, tel, email ) {
	
	this.naam = naam;
	this.telefoonNummer = tel;
	this.email = email;
	
};

var Adres = function ( zipcode, gemeente, straat, nummer ) {
	
	this.zipCode = zipcode;
	this.gemeente = gemeente;
	this.straat = straat;
	this.nummer = nummer;
	
};

var Deelnemer = function ( id, voor, familie, geslacht, geboorte, telefoon, gsm, email ) {
	
	this.uuid = id;
	this.voorNaam = voor;
	this.familieNaam = familie;
	this.geslacht = geslacht;
	this.geboorteDatum = geboorte;
	this.telefoonNummer = telefoon;
	this.mobielNummer = gsm;
	this.email = email;
	
};

var Vraag = function ( id, vraag, antwoord ) {
	
	this.uuid = id;
	this.vraag = vraag;
	this.antwoord = antwoord;
	
};

var Status = function ( value, comment, email ) {
	
	this.value = value;
	if ( comment != undefined ) {
		this.comment = comment;
	}
	
	if ( email != undefined ) {
		this.emailMe = email;
	}
	
};


var viewInschrijving = function( index, inschrijving ) {
	
	var vakantie = inschrijving.vakantie;
	
	var html = _.template( contemplate("inschrijving-row"), inschrijving );
	// var html = "x";
	
	$jq("tbody[data-vakantie$='" + vakantie.uuid + "']" ).append( html );
	
};

var retrieveInschrijving = function ( id ) {

	$jq.ajax( {
		type: "get",
		url:"/rs/inschrijvingen/" + id,
		dataType: "json",
		success: function( ix ) {
				inschrijving = ix;
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			//alert( errorThrown );
		}
	});
	
};

var retrieveInschrijvingen = function ( ) {

	$jq.ajax( {
		type: "get",
		url:"/rs/inschrijvingen",
		dataType: "json",
		success: function( inschrijvingen ) {
				$jq.each( inschrijvingen, viewInschrijving );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			// alert( errorThrown );
		}
	});
	
};

var postInschrijving = function ( rx, reference ) {

	$jq.ajax( {
		type: "post",
		url:"/rs/inschrijvingen",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(rx),
		success: function( inschrijving ) {
			if ( reference == null ) {
				window.location.href = "/rs/inschrijvingen/" + inschrijving.uuid + ".html";
			}
			else {
				window.location.href = "/rs/inschrijvingen/" + inschrijving.reference + ".html";
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			// alert( errorThrown );
		}
	});
	
};

var putDeelnemer = function ( inschrijving, dx, button, errorElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/deelnemers/" + dx.uuid,
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(dx),
		success: function( deelnemer ) {
				dx.uuid = deelnemer.uuid;
				callback( inschrijving );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement, jqXHR.responseText );
		}
	});
	
};

var putContact = function ( inschrijving, contact, button, errorElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/contact",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( contact ),
		success: function( returned ) {
			callback( inschrijving );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement, jqXHR.responseText );
		}
	});
	
};

var putAddress = function ( inschrijving, adres, button, errorElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/adres",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( adres ),
		success: function( returned ) {
			callback( inschrijving );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement, jqXHR.responseText );
		}
	});
	
};

var putVakanties = function ( inschrijving, vakanties, button, errorElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/vakanties",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( vakanties ),
		success: function( returned ) {
			callback( inschrijving );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement, jqXHR.responseText );
		}
	});
	
};

var putVragen = function ( inschrijving, vragen, button, errorElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/vragen",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( vragen ),
		success: function( returned ) {
			callback( inschrijving );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement, jqXHR.responseText );
		}
	});
	
};

var putOpmerking = function ( inschrijving, opmerking, formErrorElement ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving.uuid + "/opmerking",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( opmerking ),
		success: function( ox ) {
			$jq("#opmerking-save").addClass("ok");
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			$jq("#opmerking-save").addClass("ok");
			$jq("#opmerking-error").html(textStatus);
		}
	});
	
};

var putStatus = function ( inschrijving, status, button, errorElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/status",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( status ),
		success: function( ox ) {
			success( button, errorElement );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement, jqXHR.responseText );
		}
	});
	
};

var deleteInschrijving = function ( inschrijving, button, errorElement, callback ) {

	$jq.ajax( {
		type: "delete",
		url:"/rs/inschrijvingen/" + inschrijving,
		success: function( ox ) {
			window.location.href = "/rs/inschrijvingen.html";
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, errorElement, jqXHR.responseText );
		}
	});
	
};

function getParameter(url, key) {
    var sURLVariables = url.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == key) 
        {
            return sParameterName[1];
        }
    }
};
