function listen( context, next ){
	
	$(".btn-form").click( function() {
		laadFormulier( "deelnemers", "nieuw", "add" );
	});
	
	$('.btn-start').click(
			function() {
				$.ajax({
					type : "POST",
					cache : false,
					url: $(".form-form").attr("action"),
					data : $(".form-form").serialize(),
					success : function(inschrijving) {
						window.location.href = next + "/" + inschrijving.id;
					}
				});
				return false;
			}
	);
	
	$('.btn-next').click(
			function() {
				$.ajax({
					type : "POST",
					cache : false,
					url: $(".form-form").attr("action"),
					data : $(".form-form").serialize(),
					success : function(inschrijving) {
						// $(".result").addClass("alert-success");
						// $(".result-message").text(response);
						if ( next ) {
							window.location.href = next;
						}
						else {
							window.location.href = window.location.href + "/" + inschrijving.id;
						}
					}
				});
				return false;
			}
	);
	
	$('.btn-query').click(
			function() {
				$.ajax({
					type : "GET",
					cache : false,
					url: $(".form-query").attr("action"),
					data : $(".form-query").serialize(),
					success : function(jsp) {
						$("#" + context).html(jsp);
					}
				});
				return false;
			}
	);
	
}

function query( context ) {
	$.ajax({
		type : "GET",
		cache : false,
		url: $(".form-query").attr("action"),
		data : $(".form-query").serialize(),
		success : function(jsp) {
			$("#" + context).html(jsp);
		}
	});
	return false;
}