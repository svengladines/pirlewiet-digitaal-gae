package be.pirlewiet.digitaal.infrastructure.salesforce;

import be.pirlewiet.digitaal.model.QuestionAndAnswer;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import static be.pirlewiet.digitaal.domain.q.QIDs.*;

public class SalesForceMapper {

    protected static final Map<String,String> qnaMapping = new HashMap<>();
    static {
        qnaMapping.put(QID_MEDIC_REMARKS, "Aandachtspunten__c");
        qnaMapping.put(QID_SHARED_PHOTO, "Foto_s__c");
        qnaMapping.put(QID_SHARED_CONTACT_PHONE, "Telefoonnummer_contactpersoon__c");
        qnaMapping.put(QID_SHARED_BILL, "Wie_betaalt_de_factuur__c");
        qnaMapping.put(QID_SHARED_CONTACT_PHONE, "Telefoonnummer_contactpersoon__c");
        qnaMapping.put(QID_SHARED_BILL_DETAIL, "Naam_betaler__c");
        qnaMapping.put(QID_FAMILY_CAR, "Vervoer__c");
        qnaMapping.put(QID_HISTORY, "Eerder_mee_geweest__c");

    }

    public static HashMap<String,String> map(QuestionAndAnswer qna) {
        HashMap<String,String> map = new HashMap<>();
        if (qnaMapping.containsKey(qna.getQid())) {
            map.put(qnaMapping.get(qna.getQid()), switch (qna.getType()) {
                case YesNo -> "Y".equalsIgnoreCase(qna.getAnswer()) ? "Ja" : "Neen";
                default -> qna.getAnswer();
            });
        }
        return map;
    }
}
