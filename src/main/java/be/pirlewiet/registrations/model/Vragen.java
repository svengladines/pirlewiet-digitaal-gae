package be.pirlewiet.registrations.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.pirlewiet.registrations.model.Vraag.Type;

public class Vragen {
	
	protected Map<String, List<Vraag>> vragen; 
		
	public Vragen() {
		
		vragen = new HashMap<String, List<Vraag>>();
		
		Vraag[] medics
			= new Vraag[] {
			
				new Vraag( Type.Text, Tags.TAG_MEDIC,"Naam huisarts" ),
				new Vraag( Type.Text,Tags.TAG_MEDIC,  "Telefoon huisarts" ),
				new Vraag( Type.Label,  Tags.TAG_MEDIC, "Mag de deelnemer deelnemen aan:" ),
				new Vraag( Type.YesNo,  Tags.TAG_MEDIC, "Sport" ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC,  "Spel" ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC,  "Wandelen" ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC,  "Fietsen" ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC,  "Zwemmen" ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC, "Voor tieners (14-16 jaar): mag uw kind roken?" ),
				new Vraag( Type.Area, Tags.TAG_MEDIC, "Zijn er specifieke aandachtspunten waar de kampleiding rekening mee dient te houden? Bv. angsten, eetgewoontes, ADHD, autisme, mentale of fysieke beperking, bedplassen, allergieën, astma, epilepsie, vroegere ziektes/operaties, gedrag, ..." ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC, "Moet de deelnemer geneesmiddelen innemen tijdens het kamp of de vakantie?" ),
				new Vraag( Type.Area, Tags.TAG_MEDIC, "Indien ja, welke geneesmiddelen en welke dosering?" )
		};
		
		Vraag[] fotos
			= new Vraag[] {
			new Vraag( Type.YesNo, Tags.TAG_FOTOS, "Mogen wij foto’s gebruiken waar de deelnemer herkenbaar op staat?" ),
		};
		
		vragen.put( Tags.TAG_MEDIC, Arrays.asList( medics ) );
		vragen.put( Tags.TAG_FOTOS, Arrays.asList( fotos ) );
	}

	public Map<String, List<Vraag>> getVragen() {
		return vragen;
	}
	
}
