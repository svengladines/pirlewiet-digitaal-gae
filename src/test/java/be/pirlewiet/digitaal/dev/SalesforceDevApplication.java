package be.pirlewiet.digitaal.dev;

import be.pirlewiet.digitaal.application.config.SalesforceConfig;
import be.pirlewiet.digitaal.infrastructure.salesforce.SalesforceClient;
import be.pirlewiet.digitaal.infrastructure.salesforce.SalesforceTokenClient;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
public class SalesforceDevApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
            .profiles("dev")
            .bannerMode(Banner.Mode.OFF)
            .sources(SalesforceDevApplication.class,SalesforceConfigForDev.class, SalesforceConfig.class, SalesforceClient.class, SalesforceTokenClient.class)
            .run(args);
    }
}
