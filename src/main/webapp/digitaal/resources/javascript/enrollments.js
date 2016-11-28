var Inschrijving = function ( reference ) {

	if ( reference != null ) {
		this.reference = reference;
	}
	
};

var Holiday = function( uuid ) {
	this.uuid = uuid;
};

var Contact = function ( uuid, givenName, familyName, tel, email ) {
	
	this.uuid = uuid;
	this.givenName = givenName;
	this.familyName = familyName;
	this.phone = tel;
	this.email = email;
	
};

var Address = function ( zipcode, city, street, number ) {
	
	this.zipCode = zipcode;
	this.city = city;
	this.street = street;
	this.number = number;
	
};

var Participant = function ( givenName, familyName, gender, birthDay, phone, email, id ) {
	
	this.uuid = id;
	
	this.givenName = givenName;
	this.familyName = familyName;
	this.gender = gender;
	this.birthDay = birthDay;
	this.phone = phone;
	this.email = email;
	
};

var QuestionAndAnswer = function ( uuid, tag, answer ) {
	
	this.uuid = uuid;
	this.tag = tag;
	this.answer = answer;
	
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

var Enrollment = function ( applicationUuid, participant, address, uuid ) {
	
	this.applicationUuid = applicationUuid;
	this.participant = participant;
	this.address = address;
	this.uuid = uuid;
	
};


var viewInschrijving = function( index, application ) {
	
	var vakantie = application.vakantie;
	
	var html = _.template( contemplate("application-row"), application );
	// var html = "x";
	
	$jq("tbody[data-vakantie$='" + vakantie.uuid + "']" ).append( html );
	
};

var retrieveInschrijving = function ( id ) {

	$jq.ajax( {
		type: "get",
		url:"/api/applications/" + id,
		dataType: "json",
		success: function( ix ) {
				application = ix;
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			//alert( errorThrown );
		}
	});
	
};

var retrieveInschrijvingen = function ( ) {

	$jq.ajax( {
		type: "get",
		url:"/api/applications",
		dataType: "json",
		success: function( applications ) {
				$jq.each( applications, viewInschrijving );
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			// alert( errorThrown );
		}
	});
	
};

var postEnrollment = function ( applicationUuid, enrollment, callback ) {

	$jq.ajax( {
		type: "post",
		url:"/api/applications/" + applicationUuid + "/enrollments",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(enrollment),
		success: function( application ) {
				if ( reference == null ) {
					window.location.href = "/api/applications/" + application.uuid + ".html";
				}
				else {
					if ( callback ) {
						callback( application.uuid, application.deelnemers[0].uuid );
					}
					else {
						window.location.href = "/api/applications/" + reference + ".html";
					}
				}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			// alert( errorThrown );
		}
	});
	
};

var putEnrollment = function ( applicationUuid, enrollment, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + applicationUuid + "/enrollments/" + enrollment.uuid,
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(enrollment),
		success: function( e ) {
				if ( callback ) {
					callback( e );
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

var putContact = function ( application, contact, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + application + "/contact",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify( contact ),
		success: function( returned ) {
			if ( callback ) {
				callback( application );
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

var putAddress = function ( application, adres, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + application + "/adres",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( adres ),
		success: function( returned ) {
			if ( callback ) {
				callback( application );
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

var putHolidays = function ( application, holidays, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + application + "/holidays",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( holidays ),
		success: function( returned ) {
			if ( callback ) {
				callback( application );
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

var putQList = function ( application, qList, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + application + "/qlist",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( qList ),
		success: function( returned ) {
			if ( callback ) {
				callback( application );
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

var putOpmerking = function ( application, opmerking, formstatusElement ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + application.uuid + "/opmerking",
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

var putStatus = function ( application, status, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + application + "/status",
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

var deleteEnrollment = function ( application, button, statusElement, callback ) {

	$jq.ajax( {
		type: "delete",
		url:"/api/applications/" + application,
		success: function( ox ) {
			if ( callback ) {
				callback( application );
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
