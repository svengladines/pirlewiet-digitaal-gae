package be.pirlewiet.digitaal.dev;

import be.pirlewiet.digitaal.application.config.SalesforceConfig;
import be.pirlewiet.digitaal.infrastructure.salesforce.Contact;
import be.pirlewiet.digitaal.infrastructure.salesforce.SalesforceClient;
import be.pirlewiet.digitaal.infrastructure.salesforce.SalesforceTokenClient;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@Component
public class RunSalesforceClient_Dev {

    final protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final SalesforceClient salesforceClient;

    public RunSalesforceClient_Dev(SalesforceClient salesforceClient) {
        this.salesforceClient = salesforceClient;
    }

    @PostConstruct
    public void run() {
        //Optional<Contact> contact = salesforceClient.findContact("003gK00000KQ3JhQAL");
        Contact toCreate = new Contact()
                .firstName("Lisa")
                .lastName("Simpson")
                .gender("Vrouw")
                .birthDate("1990-01-01")
                .mobilePhone("0499/290671");
        salesforceClient.createContact(toCreate).ifPresentOrElse(contact -> {
            logger.info("contact created: {}", contact);
        }, () -> {
            logger.error("contact NOT created: {}");
        });

    }

    public static void main(final String[] args) {
        try {
            new SpringApplicationBuilder()
                .profiles("dev")
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .sources(SalesforceConfigForDev.class, SalesforceConfig.class, SalesforceClient.class, SalesforceTokenClient.class, RunSalesforceClient_Dev.class)            .run(args);
        }
        catch(Throwable ex) {
            ex.printStackTrace();
        }
    }

}
