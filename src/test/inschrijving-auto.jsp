<!DOCTYPE html>
<html>
	
	<body>
	
		<script>
		
			var $jq = jQuery.noConflict();
		
			var vakantie = new Vakantie( "1" );
		
			var inschrijving = new Inschrijving( vakantie );
			inschrijving.id = "${inschrijving.id}";
		
			var contact = new Contact( "Abe Simpson", "+123456789", "abe.simspon@springfield.net" );
			
			putContact ( inschrijving, contact );
			
			var callbackOne = function( dxOne ) {
				
				dxOne.voorNaam = "Bart";
				dxOne.familieNaam = "Simpson";
				putDeelnemer( inschrijving, dxOne );
				
			};
			
			var callbackTwo = function( dxTwo ) {
				
				dxTwo.voorNaam = "Lisa";
				dxTwo.familieNaam = "Simpson";
				putDeelnemer( inschrijving, dxTwo );
				
			};
			
			var bart = new Deelnemer();
			
			postDeelnemer( inschrijving, bart, callbackOne );
			
			var lisa = new Deelnemer();
			
			postDeelnemer( inschrijving, lisa, callbackTwo );
			
			var adres = new Adres("Springfield","Evergreen Terrace", "741");
			
			putAdres( inschrijving, adres );
			
			var vragen = new Array();
			vragen[0] = new Vraag("who");
			vragen[1] = new Vraag("why");
			
			vragen[0].antwoord = "mister X";
			vragen[1].antwoord = "no reason";
			
			postVraag( inschrijving, vragen[0] );
			postVraag( inschrijving, vragen[1] );
			
			var opmerking = "good job, guys";
			
			putOpmerking( inschrijving, opmerking );
			
		
		</script>
	
	</body>

</html>