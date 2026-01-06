package be.pirlewiet.digitaal.dev;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class SalesforceConfigForDev {

    @Bean
    public RestTemplateBuilder restTemplateBuilder(@Value("${salesforce.api.baseUrl}") String baseUrl) {
        return new RestTemplateBuilder().rootUri(baseUrl); // bv. WireMock
    }

}
