package be.pirlewiet.digitaal.infrastructure.salesforce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Component
public class SalesforceClient {

    static protected final String SALESFORCE_API_VERSION = "65.0";

    private final Logger logger = LoggerFactory.getLogger(SalesforceClient.class);
    private final RestTemplate salesforceRestTemplate;

    public SalesforceClient(RestTemplate salesforceRestTemplate) {
        this.salesforceRestTemplate = salesforceRestTemplate;
    }

    public String describeContact() {
        return salesforceRestTemplate.getForObject(path("Contact"), String.class);
    }

    public Optional<Contact> findContact(String id) {
        return Optional.of(salesforceRestTemplate.getForObject(path("Contact", id), Contact.class));
    }

    public Optional<String> findContactAsString(String id) {
        return Optional.of(salesforceRestTemplate.getForObject(path("Contact", id), String.class));
    }

    public Optional<Contact> createContact(Contact toCreate ) {
        try {
            return Optional.of(salesforceRestTemplate.postForObject(path("Contact"), toCreate, Contact.class));
        }
        catch(Exception e) {
            logger.error("contact creation failed with exception", e);
            return Optional.empty();
        }
    }

    public Optional<Contact> updateContact(String id,Contact toUpdate) {
        try {
            salesforceRestTemplate.patchForObject(path("Contact",id), toUpdate, Contact.class);
            return Optional.of(toUpdate);
        }
        catch(Exception e) {
            logger.error("contact update failed with exception", e);
            return Optional.empty();
        }
    }

    protected String path(String object) {
        return "/services/data/v%s/sobjects/%s".formatted(SALESFORCE_API_VERSION,object);
    }

    protected String path(String object, String... subs) {
        StringBuilder path = new StringBuilder("/services/data/v%s/sobjects/%s".formatted(SALESFORCE_API_VERSION,object));
        Arrays.stream(subs).forEach(sub -> {path.append("/%s".formatted(sub));});
        return path.toString();
    }

}
