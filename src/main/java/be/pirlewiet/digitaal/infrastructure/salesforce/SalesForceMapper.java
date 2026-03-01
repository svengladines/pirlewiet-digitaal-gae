package be.pirlewiet.digitaal.infrastructure.salesforce;

import be.occam.utils.timing.Timing;
import be.pirlewiet.digitaal.model.*;
import be.pirlewiet.digitaal.model.Address;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

import static be.pirlewiet.digitaal.domain.q.QIDs.*;

public class SalesForceMapper {

    protected static final Map<String,String> qnaMapping = new HashMap<>();
    static {
        qnaMapping.put(QID_MEDIC_REMARKS, "Aandachtspunten__c");

        qnaMapping.put(QID_SHARED_BILL, "Wie_betaalt_de_factuur__c");
        qnaMapping.put(QID_SHARED_BILL_DETAIL, "Naam_betaler__c");
        qnaMapping.put(QID_SHARED_BILL, "Wie_betaalt_de_factuur__c");
        qnaMapping.put(QID_SHARED_BILL_DETAIL, "Naam_betaler__c");
        qnaMapping.put(QID_SHARED_CONTACT,"Naam_contactpersoon__c");
        qnaMapping.put(QID_SHARED_CONTACT_PHONE, "Telefoonnummer_contactpersoon__c");
        qnaMapping.put(QID_SHARED_PHOTO, "Foto_s__c");
        qnaMapping.put(QID_HISTORY, "Eerder_mee_geweest__c");
        qnaMapping.put(QID_MEDIC_DUTCH, "Nederlands__c");
        qnaMapping.put(QID_FAMILY_CAR, "Vervoer__c");
        // VOV
        qnaMapping.put(QID_ADULTERY_WITH,"VOV_met_vriend_in__c");
        qnaMapping.put(QID_ADULTERY_WITH_WHO,"VOV_naam_vriend_in__c");
    }

    public static HashMap<String,String> map(QuestionAndAnswer qna) {
        HashMap<String,String> map = new HashMap<>();
        if (qnaMapping.containsKey(qna.getQid())) {
            map.put(qnaMapping.get(qna.getQid()), switch (qna.getType()) {
                case YesNo -> "Y".equalsIgnoreCase(qna.getAnswer()) ? "Ja" : "Neen";
                case MC ->  {
                    // temporary mismatch due to options changed in Questionsheet | TODO: remove
                    if (qna.getAnswer().toLowerCase().contains("auto")) {
                        yield "Auto";
                    }
                    else if (qna.getAnswer().toLowerCase().contains("trein")) {
                        yield "Openbaar vervoer";
                    }
                    else {
                        yield qna.getAnswer();
                    }
                }
                default -> qna.getAnswer();
            });
        }
        return map;
    }

    public static Map<String,String> map(List<QuestionAndAnswer> qnaList) {
        HashMap<String,String> map = new HashMap();
        qnaList.stream().forEach( qna -> {
            map.putAll(SalesForceMapper.map(qna));
        });
        return map;
    }

    public static Map<String,String> mapOrganisation(Organisation organisation) {
        HashMap<String,String> map = new HashMap();
        // map organisation
        map.put("Naam_doorverwijzer__c", organisation.getName());
        map.put("Inschrijving__c",switch(organisation.getType()) {
            case NON_PROFIT -> "Doorverwijzer";
            case INDIVIDUAL -> "Iedereen Verdient Vakantie";
            default -> null;});
        return map;
    }

    public static Map<String,String> mapApplication(Application application, Person applicant) {
        HashMap<String,String> map = new HashMap();
        // map organisation
        map.put("Datum_inschrijving__c", date(application.getSubmitted()));
        map.put("Naam_aanvrager__c", "%s %s".formatted(applicant.getGivenName(), applicant.getFamilyName()));
        map.put("Telefoon_aanvrager__c", applicant.getPhone());
        map.put("E_mail_aanvrager__c", applicant.getEmail());
        return map;
    }

    public static Map<String,String> mapEnrollment(Enrollment enrollment, Holiday holiday) {
        HashMap<String,String> map = new HashMap();
        // map holiday
        map.put("Type_vakantie__c", switch(holiday.getName()) {
           case "PaasKIKA" -> "PaasKika";
           case "PaasGEZINS" -> "PaasGezins";
           case "VOV 1" -> "VOV1";
           case "CAVASOL" -> "CAVASOL";
           case "Gezins 1" -> "Gezins1";
           case "Gezins 2" -> "Gezins2";
           case "Kika" -> "Kika";
           case "Tika" -> "Tika";
           case "VOV 2" -> "VOV2";
           default -> null;
        });
        // map status
        map.put("Deelname__c", switch(enrollment.getStatus().getValue()) {
            case EnrollmentStatus.Value.ACCEPTED -> "Inschrijving bevestigd";
            case EnrollmentStatus.Value.CANCELLED -> "Geannuleerd";
            case EnrollmentStatus.Value.REJECTED -> "Geweigerd";
            case EnrollmentStatus.Value.WAITINGLIST ->  "Wachtlijst";
            default -> null;
        });
        // contact
        return map;
    }

    public static void removeNulls(Map<String,String> map) {
        List<String> toRemove = new ArrayList<>();
        for (String key : map.keySet()) {
            if (map.get(key) == null) {
                toRemove.add(key);
            }
        }
        toRemove.forEach(map::remove);
    }

    public static String date(Date date) {
        if (date == null) {
            return null;
        }
        return Timing.date(date,"yyyy-MM-dd");
    }

    protected String concat(String... values) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(values).iterator().forEachRemaining(value -> {
            sb.append(value);
            sb.append(";");
        });
        if (sb.length() > 0) {
            return sb.toString().substring(0, sb.length()-1);
        }
        return sb.toString();
    }
}
