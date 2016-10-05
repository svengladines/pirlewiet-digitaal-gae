package be.pirlewiet.digitaal.domain.q;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import be.pirlewiet.digitaal.model.Tags;
import be.pirlewiet.digitaal.model.QnA;
import be.pirlewiet.digitaal.model.QnA.Type;

public class QList {
	
	protected Map<String, List<QnA>> vragen; 
		
	public static QList template() {
		
		List<QnA> templateVragen 
			= new ArrayList<QnA>();
		
		QnA[] medics
			= new QnA[] {
			
				new QnA( Type.Text, Tags.TAG_MEDIC, QIDs.QID_MEDIC_MEDIC, "Naam huisarts" ),
				new QnA( Type.Text,Tags.TAG_MEDIC,  QIDs.QID_MEDIC_MEDIC_TEL, "Telefoon huisarts" ),
				new QnA( Type.Label,  Tags.TAG_MEDIC, QIDs.QID_MEDIC_CAN,  "Mag de deelnemer deelnemen aan:" ),
				new QnA( Type.YesNo,  Tags.TAG_MEDIC, QIDs.QID_MEDIC_SPORTS, "Sport" ),
				new QnA( Type.YesNo, Tags.TAG_MEDIC,  QIDs.QID_MEDIC_GAME,"Spel" ),
				new QnA( Type.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_HIKE, "Wandelen" ),
				new QnA( Type.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_BIKE, "Fietsen" ),
				new QnA( Type.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_SWIM, "Zwemmen" ),
				new QnA( Type.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_SMOKE, "Voor tieners (14-16 jaar): mag uw kind roken?" ),
				new QnA( Type.Area, Tags.TAG_MEDIC, QIDs.QID_MEDIC_REMARKS, "Zijn er specifieke aandachtspunten waar de kampleiding rekening mee dient te houden? Bv. angsten, eetgewoontes, ADHD, autisme, mentale of fysieke beperking, bedplassen, allergieën, astma, epilepsie, vroegere ziektes/operaties, gedrag, ..." ),
				new QnA( Type.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_MEDICINS, "Moet de deelnemer geneesmiddelen innemen tijdens het kamp of de vakantie?" ),
				new QnA( Type.Area, Tags.TAG_MEDIC, QIDs.QID_MEDIC_MEDICINS_DETAIL, "Indien ja, welke geneesmiddelen en welke dosering?" )
		};
		
		QnA[] fotos
			= new QnA[] {
			
		};
		
		QnA[] various
			= new QnA[] {
				new QnA( Type.Text, Tags.TAG_APPLICATION, QIDs.QID_SHARED_BILL, "Wie betaalt de factuur ?" ),
				new QnA( Type.YesNo, Tags.TAG_APPLICATION, QIDs.QID_SHARED_CONTACT, "Verdere contacten i.v.m. de vakantie verlopen via de doorverwijzer ?" ),
				new QnA( Type.YesNo, Tags.TAG_APPLICATION, QIDs.QID_SHARED_PHOTO, "Mogen wij foto’s gebruiken waar de deelnemer herkenbaar op staat?" )
		};
		
		QnA[] history
			= new QnA[] {
			new QnA( Type.YesNo, Tags.TAG_HISTORY, QIDs.QID_HISTORY, "Ging deze deelnemer reeds eerder mee op een Pirlewietvakantie ?" ),
		};
		
		templateVragen.addAll( Arrays.asList( medics ) );
		templateVragen.addAll( Arrays.asList( fotos ) );
		templateVragen.addAll( Arrays.asList( various ) );
		templateVragen.addAll( Arrays.asList( history ) );
		
		return new QList( templateVragen );
		
	}
	
	public QList( List<QnA> vragen ) {
		
		this.vragen = new HashMap<String, List<QnA>>();
		
		for ( QnA v : vragen ) {
			
			List<QnA> list
				= this.vragen.get( v.getTag() );
			
			if ( list == null ) {
				
				list = new LinkedList<QnA>();
				this.vragen.put( v.getTag(), list );
				
			}
			
			list.add( v );
			
		}
		
	}

	public Map<String, List<QnA>> getVragen() {
		return vragen;
	}
	
	public QnA getVraag( String qid ) {
		
		QnA vraag
			= null;
		
		for ( List<QnA> list : vragen.values() ) {
			
			for ( QnA v : list ) {
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
