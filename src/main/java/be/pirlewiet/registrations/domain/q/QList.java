package be.pirlewiet.registrations.domain.q;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import be.pirlewiet.registrations.model.Tags;
import be.pirlewiet.registrations.model.Vraag;
import be.pirlewiet.registrations.model.Vraag.Type;

public class QList {
	
	protected Map<String, List<Vraag>> vragen; 
		
	public static QList template() {
		
		List<Vraag> templateVragen 
			= new ArrayList<Vraag>();
		
		Vraag[] medics
			= new Vraag[] {
			
				new Vraag( Type.Text, Tags.TAG_MEDIC, QIDs.QID_MEDIC_MEDIC, "Naam huisarts" ),
				new Vraag( Type.Text,Tags.TAG_MEDIC,  QIDs.QID_MEDIC_MEDIC_TEL, "Telefoon huisarts" ),
				new Vraag( Type.Label,  Tags.TAG_MEDIC, QIDs.QID_MEDIC_CAN,  "Mag de deelnemer deelnemen aan:" ),
				new Vraag( Type.YesNo,  Tags.TAG_MEDIC, QIDs.QID_MEDIC_SPORTS, "Sport" ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC,  QIDs.QID_MEDIC_GAME,"Spel" ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_HIKE, "Wandelen" ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_BIKE, "Fietsen" ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_SWIM, "Zwemmen" ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_SMOKE, "Voor tieners (14-16 jaar): mag uw kind roken?" ),
				new Vraag( Type.Area, Tags.TAG_MEDIC, QIDs.QID_MEDIC_REMARKS, "Zijn er specifieke aandachtspunten waar de kampleiding rekening mee dient te houden? Bv. angsten, eetgewoontes, ADHD, autisme, mentale of fysieke beperking, bedplassen, allergieën, astma, epilepsie, vroegere ziektes/operaties, gedrag, ..." ),
				new Vraag( Type.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_MEDICINS, "Moet de deelnemer geneesmiddelen innemen tijdens het kamp of de vakantie?" ),
				new Vraag( Type.Area, Tags.TAG_MEDIC, QIDs.QID_MEDIC_MEDICINS_DETAIL, "Indien ja, welke geneesmiddelen en welke dosering?" )
		};
		
		Vraag[] fotos
			= new Vraag[] {
			
		};
		
		Vraag[] various
			= new Vraag[] {
				new Vraag( Type.Text, Tags.TAG_APPLICATION, QIDs.QID_SHARED_BILL, "Wie betaalt de factuur ?" ),
				new Vraag( Type.YesNo, Tags.TAG_APPLICATION, QIDs.QID_SHARED_CONTACT, "Verdere contacten i.v.m. de vakantie verlopen via de doorverwijzer ?" ),
				new Vraag( Type.YesNo, Tags.TAG_APPLICATION, QIDs.QID_SHARED_PHOTO, "Mogen wij foto’s gebruiken waar de deelnemer herkenbaar op staat?" )
		};
		
		Vraag[] history
			= new Vraag[] {
			new Vraag( Type.YesNo, Tags.TAG_HISTORY, QIDs.QID_HISTORY, "Ging deze deelnemer reeds eerder mee op een Pirlewietvakantie ?" ),
		};
		
		templateVragen.addAll( Arrays.asList( medics ) );
		templateVragen.addAll( Arrays.asList( fotos ) );
		templateVragen.addAll( Arrays.asList( various ) );
		templateVragen.addAll( Arrays.asList( history ) );
		
		return new QList( templateVragen );
		
	}
	
	public QList( List<Vraag> vragen ) {
		
		this.vragen = new HashMap<String, List<Vraag>>();
		
		for ( Vraag v : vragen ) {
			
			List<Vraag> list
				= this.vragen.get( v.getTag() );
			
			if ( list == null ) {
				
				list = new LinkedList<Vraag>();
				this.vragen.put( v.getTag(), list );
				
			}
			
			list.add( v );
			
		}
		
	}

	public Map<String, List<Vraag>> getVragen() {
		return vragen;
	}
	
	public Vraag getVraag( String qid ) {
		
		Vraag vraag
			= null;
		
		for ( List<Vraag> list : vragen.values() ) {
			
			for ( Vraag v : list ) {
				if ( qid.equals( v.getQID() ) ) {
					vraag = v;
					break;
				}
			}
			
			if ( vraag != null ) {
				break;
			}

		}
		
		return vraag;
		
	}
	
}
