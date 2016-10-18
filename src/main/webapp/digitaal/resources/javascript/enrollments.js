var Inschrijving = function ( reference ) {

	if ( reference != null ) {
		this.reference = reference;
	}
	
};

var Contact = function ( naam, tel, email ) {
	
	this.name = naam;
	this.phone = tel;
	this.email = email;
	
};

var Address = function ( zipcode, city, street, number ) {
	
	this.zipCode = zipcode;
	this.city = city;
	this.street = street;
	this.number = number;
	
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

var Vraag = function ( id, tag, vraag, antwoord ) {
	
	this.uuid = id;
	this.tag = tag;
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

var postEnrollment = function ( rx, reference, callback ) {

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
					if ( callback ) {
						callback( inschrijving.uuid, inschrijving.deelnemers[0].uuid );
					}
					else {
						window.location.href = "/rs/inschrijvingen/" + reference + ".html";
					}
				}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			// alert( errorThrown );
		}
	});
	
};

var putDeelnemer = function ( inschrijving, dx, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/participant",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(dx),
		success: function( deelnemer ) {
				if ( callback ) {
					callback( inschrijving );
				}
				else {
					success( button, statusElement, "Opgeslagen" );
				}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, statusElement, jqXHR.responseText );
		}
	});
	
};

var putContact = function ( inschrijving, contact, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/contact",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( contact ),
		success: function( returned ) {
			if ( callback ) {
				callback( inschrijving );
			}
			else {
				success( button, statusElement, "Opgeslagen" );
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, statusElement, jqXHR.responseText );
		}
	});
	
};

var putAddress = function ( inschrijving, adres, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/adres",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( adres ),
		success: function( returned ) {
			if ( callback ) {
				callback( inschrijving );
			}
			else {
				success( button, statusElement, "Opgeslagen" );
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, statusElement, jqXHR.responseText );
		}
	});
	
};

var putVakanties = function ( inschrijving, vakanties, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/vakanties",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( vakanties ),
		success: function( returned ) {
			if ( callback ) {
				callback( inschrijving );
			}
			else {
				success( button, statusElement, "Opgeslagen" );
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, statusElement, jqXHR.responseText );
		}
	});
	
};

var putVragen = function ( inschrijving, vragen, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/qlist",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( vragen ),
		success: function( returned ) {
			if ( callback ) {
				callback( inschrijving );
			}
			else {
				success( button, statusElement, "Opgeslagen" );
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, statusElement, jqXHR.responseText );
		}
	});
	
};

var putOpmerking = function ( inschrijving, opmerking, formstatusElement ) {

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

var putStatus = function ( inschrijving, status, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/rs/inschrijvingen/" + inschrijving + "/status",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( status ),
		success: function( ox ) {
			if ( callback ) {
				callback( ox );
			}
			else {
				success( button, statusElement, "Opgeslagen" );
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			error( button, statusElement, jqXHR.responseText );
		}
	});
	
};

var deleteEnrollment = function ( inschrijving, button, statusElement, callback ) {

	$jq.ajax( {
		type: "delete",
		url:"/rs/inschrijvingen/" + inschrijving,
		success: function( ox ) {
			if ( callback ) {
				callback( inschrijving );
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
