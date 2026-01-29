package be.pirlewiet.digitaal.dev;

import be.pirlewiet.digitaal.application.config.SalesforceConfig;
import be.pirlewiet.digitaal.infrastructure.salesforce.Contact;
import be.pirlewiet.digitaal.infrastructure.salesforce.SalesforceClient;
import be.pirlewiet.digitaal.infrastructure.salesforce.SalesforceTokenClient;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RunSalesforceClient_Production {

    final protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final SalesforceClient salesforceClient;

    public RunSalesforceClient_Production(SalesforceClient salesforceClient) {
        this.salesforceClient = salesforceClient;
    }

    @PostConstruct
    public void run() {
        //Optional<Contact> contact = salesforceClient.findContact("003Tb00000LLi91IAD");
        Optional<String> contact = salesforceClient.findContactAsString("003Tb00000uHzd7IAC");
        logger.info("contact: {}", contact);
    }

    public static void main(final String[] args) {
        try {
            new SpringApplicationBuilder()
                .profiles("dev")
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .sources(SalesforceConfigForDev.class, SalesforceConfig.class, SalesforceClient.class, SalesforceTokenClient.class, RunSalesforceClient_Production.class).run(args);
        }
        catch(Throwable ex) {
            ex.printStackTrace();
        }
    }

}
