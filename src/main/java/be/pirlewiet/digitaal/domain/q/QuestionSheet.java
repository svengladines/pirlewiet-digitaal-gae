package be.pirlewiet.digitaal.domain.q;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import be.pirlewiet.digitaal.model.QuestionType;
import be.pirlewiet.digitaal.model.Tags;

public class QuestionSheet {
	
	protected Map<String, List<QuestionAndAnswer>> vragen; 
		
	public static QuestionSheet template() {
		
		List<QuestionAndAnswer> templateVragen 
			= new ArrayList<QuestionAndAnswer>();
		
		QuestionAndAnswer[] medics
			= new QuestionAndAnswer[] {
			
				new QuestionAndAnswer( 1,QuestionType.Text, Tags.TAG_MEDIC, QIDs.QID_MEDIC_MEDIC, "Naam huisarts" ),
				new QuestionAndAnswer( 2,QuestionType.Text,Tags.TAG_MEDIC,  QIDs.QID_MEDIC_MEDIC_TEL, "Telefoon huisarts" ),
				new QuestionAndAnswer( 3,QuestionType.Label,  Tags.TAG_MEDIC, QIDs.QID_MEDIC_CAN,  "Mag de deelnemer deelnemen aan:" ),
				new QuestionAndAnswer( 4,QuestionType.YesNo,  Tags.TAG_MEDIC, QIDs.QID_MEDIC_SPORTS, "Sport" ),
				new QuestionAndAnswer( 5,QuestionType.YesNo, Tags.TAG_MEDIC,  QIDs.QID_MEDIC_GAME,"Spel" ),
				new QuestionAndAnswer( 6,QuestionType.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_HIKE, "Wandelen" ),
				new QuestionAndAnswer( 7,QuestionType.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_BIKE, "Fietsen" ),
				new QuestionAndAnswer( 8,QuestionType.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_SWIM, "Zwemmen" ),
				new QuestionAndAnswer( 9,QuestionType.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_SMOKE, "Voor tieners (14-16 jaar): mag uw kind roken?" ),
				new QuestionAndAnswer( 10,QuestionType.Area, Tags.TAG_MEDIC, QIDs.QID_MEDIC_REMARKS, "Zijn er specifieke aandachtspunten waar de kampleiding rekening mee dient te houden? Bv. angsten, eetgewoontes, ADHD, autisme, mentale of fysieke beperking, bedplassen, allergieën, astma, epilepsie, vroegere ziektes/operaties, gedrag, ..." ),
				new QuestionAndAnswer( 11,QuestionType.YesNo, Tags.TAG_MEDIC, QIDs.QID_MEDIC_MEDICINS, "Moet de deelnemer geneesmiddelen innemen tijdens het kamp of de vakantie?" ),
				new QuestionAndAnswer( 12,QuestionType.Area, Tags.TAG_MEDIC, QIDs.QID_MEDIC_MEDICINS_DETAIL, "Indien ja, welke geneesmiddelen en welke dosering?" )
		};
		
		QuestionAndAnswer[] fotos
			= new QuestionAndAnswer[] {
			
		};
		
		QuestionAndAnswer[] various
			= new QuestionAndAnswer[] {
				new QuestionAndAnswer( 13,QuestionType.Text, Tags.TAG_APPLICATION, QIDs.QID_SHARED_BILL, "Wie betaalt de factuur ?" ),
				new QuestionAndAnswer( 14,QuestionType.YesNo, Tags.TAG_APPLICATION, QIDs.QID_SHARED_CONTACT, "Verdere contacten i.v.m. de vakantie verlopen via de doorverwijzer ?" ),
				new QuestionAndAnswer( 15,QuestionType.YesNo, Tags.TAG_APPLICATION, QIDs.QID_SHARED_PHOTO, "Mogen wij foto’s gebruiken waar de deelnemer herkenbaar op staat?" )
		};
		
		QuestionAndAnswer[] history
			= new QuestionAndAnswer[] {
			new QuestionAndAnswer( 16, QuestionType.YesNo, Tags.TAG_HISTORY, QIDs.QID_HISTORY, "Ging deze deelnemer reeds eerder mee op een Pirlewietvakantie ?" ),
		};
		
		templateVragen.addAll( Arrays.asList( medics ) );
		templateVragen.addAll( Arrays.asList( fotos ) );
		templateVragen.addAll( Arrays.asList( various ) );
		templateVragen.addAll( Arrays.asList( history ) );
		
		return new QuestionSheet( templateVragen );
		
	}
	
	public QuestionSheet( List<QuestionAndAnswer> vragen ) {
		
		this.vragen = new HashMap<String, List<QuestionAndAnswer>>();
		
		if ( vragen != null ) {
		
			for ( QuestionAndAnswer v : vragen ) {
				
				List<QuestionAndAnswer> list
					= this.vragen.get( v.getTag() );
				
				if ( list == null ) {
					
					list = new LinkedList<QuestionAndAnswer>();
					this.vragen.put( v.getTag(), list );
					
				}
				
				list.add( v );
				
			}
		}
		
	}

	public Map<String, List<QuestionAndAnswer>> getQuestions() {
		return vragen;
	}
	
	public QuestionAndAnswer getQuestion( String qid ) {
		
		QuestionAndAnswer vraag
			= null;
		
		for ( List<QuestionAndAnswer> list : vragen.values() ) {
			
			for ( QuestionAndAnswer v : list ) {
				if ( qid.equals( v.getQid() ) ) {
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
