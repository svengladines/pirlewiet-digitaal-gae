var Application = function ( ) {

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

var Participant = function ( givenName, familyName, gender, birthDay, phone, email, stateNumber, id ) {
	
	this.uuid = id;
	
	this.givenName = givenName;
	this.familyName = familyName;
	this.gender = gender;
	this.birthDay = birthDay;
	this.phone = phone;
	this.email = email;
	this.stateNumber = stateNumber;
	
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

var postApplication = function( application,callback ) {

	$jq.ajax( {
		type: "post",
		url:"/api/applications",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(application),
		success: function( createdResult ) {
			if ( callback ) {
				callback( createdResult.object );
			}
		},
		error: function(  jqXHR, textStatus, errorThrown ) {
			// alert( errorThrown );
		}
	});
	
};

var viewApplicationOrganisation = function( application ) {

	window.location.href = "/organisation/application-" + application.uuid + ".html";
	
};

var viewApplicationOrganisation = function( application ) {

	window.location.href = "/referenced/application-" + application.uuid + ".html";

};

var postEnrollment = function ( applicationUuid, enrollment, callback ) {

	$jq.ajax( {
		type: "post",
		url:"/api/applications/" + applicationUuid + "/enrollments",
		dataType: "json",
		contentType: "application/json",
	    processData: false,
		data: JSON.stringify(enrollment),
		success: function( result ) {
				if ( result.value == "OK") {
					if ( callback ) {
						callback( );
					}
				}
				else {
					error( $jq(".btn-primary"), $jq("#error"), result.message );
				}
		},
		error: function( jqXHR, textStatus, errorThrown ) {
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

var putEnrollmentHolidays = function ( applicationUuid, enrollmentUuid, holidays, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + applicationUuid + "/enrollments/" + enrollmentUuid + "/holidays",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( holidays ),
		success: function( returned ) {
			if ( callback ) {
				callback( enrollmentUuid );
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

var putEnrollmentStatus = function ( applicationUuid, enrollmentUuid, status, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + applicationUuid + "/enrollments/" + enrollmentUuid + "/status",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( status ),
		success: function( returned ) {
			if ( callback ) {
				callback( enrollmentUuid );
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

var putStatus = function ( applicationUuid, status, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + applicationUuid + "/status",
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

var putEnrollmentQList = function ( applicationUuid, enrollmentUuid, qList, button, statusElement, callback ) {

	$jq.ajax( {
		type: "put",
		url:"/api/applications/" + applicationUuid + "/enrollments/" + enrollmentUuid + "/qlist",
		dataType: "json",
		contentType: "application/json;charset=\"utf-8\"",
	    processData: false,
		data: JSON.stringify( qList ),
		success: function( returned ) {
			if ( callback ) {
				callback( );
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

var deleteEnrollment = function ( applicationUuid, enrollmentUuid, button, statusElement, callback ) {

	$jq.ajax( {
		type: "delete",
		url:"/api/applications/" + applicationUuid + "/enrollments/" + enrollmentUuid,
		success: function( ox ) {
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
