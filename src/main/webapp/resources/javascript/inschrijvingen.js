var Inschrijving = function ( vakantie ) {

	if ( vakantie != null ) {
		this.vakantie = new Object();
		this.vakantie.id = vakantie.id;
	}
	
};

var Contact = function ( naam, tel, email ) {
	
	this.naam = naam;
	this.telefoonNummer = tel;
	this.email = email;
	
};

var Adres = function ( gemeente, straat, nummer ) {
	
	this.gemeente = gemeente;
	this.straat = straat;
	this.nummer = nummer;
	
};

var Deelnemer = function ( id, voor, familie, geslacht, geboorte, telefoon, gsm, email ) {
	
	this.id = id;
	this.voorNaam = voor;
	this.familieNaam = familie;
	this.geslacht = geslacht;
	this.geboorteDatum = geboorte;
	this.telefoonNummer = telefoon;
	this.mobielNummer = gsm;
	this.email = email;
	
};

var Vraag = function ( vraag ) {
	
	this.vraag = vraag;
	
};


function clearError() {
	
	$jq(".error").html( "" );
	
}

var viewInschrijving = function( index, inschrijving ) {
	
	var vakantie = inschrijving.vakantie;
	
	var html = _.template( contemplate("inschrijving-row"), inschrijving );
	// var html = "x";
	
	$jq("tbody[data-vakantie$='" + vakantie.id + "']" ).append( html );
	
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
			alert( errorThrown );
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
			alert( errorThrown );
		}
	});
	
};

var postInschrijving = function ( rx ) {

	$jq.ajax( {
		type: "post",
		url:"/rs/inschrijvingen",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(rx),
		success: function( inschrijving ) {
				window.location.href = "/rs/inschrijvingen/" + inschrijving.id + ".html";
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			alert( errorThrown );
		}
	});
	
};

var postDeelnemer = function ( inschrijving, dx, callback ) {

	$jq.ajax( {
		type: "post",
		url:"/rs/inschrijvingen/" + inschrijving.id + "/deelnemers",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(dx),
		success: function( deelnemer ) {
				dx.id = deelnemer.id;
				callback( dx );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			alert( errorThrown );
		}
	});
	
};

var putDeelnemer = function ( inschrijving, dx ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving.id + "/deelnemers/" + dx.id,
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(dx),
		success: function( deelnemer ) {
				dx.id = deelnemer.id;
				suxxes();
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			alert( errorThrown );
		}
	});
	
};

var putContact = function ( inschrijving, contact ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving.id + "/contact",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( contact ),
		success: function( returned ) {
			$jq("#contact-save").addClass("ok");
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			$jq("#contact-save").addClass("nok");
			$jq("#contact-error").html( jqXHR.responseText );
		}
	});
	
};

var putAdres = function ( inschrijving, adres ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving.id + "/adres",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( adres ),
		success: function( returned ) {
			$jq("#adres-save").addClass("ok");
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			$jq("#adres-save").addClass("nok");
			$jq("#adres-error").html( jqXHR.responseText );
		}
	});
	
};

var putVakantie = function ( inschrijving, vakantie, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving.id + "/vakantie",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( vakantie ),
		success: function( returned ) {
			callback();
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			$jq("#vakantie-error").html( jqXHR.responseText );
		}
	});
	
};

var putAlternatief = function ( inschrijving, alternatief ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving.id + "/alternatief",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( alternatief ),
		success: function( returned ) {
			$jq("#alternatief-save").addClass("ok");
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			$jq("#vakantie-error").html( jqXHR.responseText );
		}
	});
	
};

var postVraag = function ( inschrijving, vraag ) {

	$jq.ajax( {
		type: "post",
		url:"/rs/inschrijvingen/" + inschrijving.id + "/vragen",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(vraag),
		success: function( vx ) {
				vraag.id = vx.id;
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			alert( errorThrown );
		}
	});
	
};

var putOpmerking = function ( inschrijving, opmerking ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving.id + "/opmerking",
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