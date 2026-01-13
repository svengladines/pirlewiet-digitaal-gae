package be.pirlewiet.digitaal.application.config;


import be.pirlewiet.digitaal.infrastructure.salesforce.SalesforceTokenClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class SalesforceConfig {

    @Bean
    public RestTemplate salesforceRestTemplate(
            RestTemplateBuilder builder,
            SalesforceTokenClient tokenClient,
            @Value("${salesforce.api.baseUrl}") String apiBaseUrl) {

        ClientHttpRequestInterceptor auth = (req, body, exec) -> {
            String token = tokenClient.getAccessToken();
            req.getHeaders().setBearerAuth(token);
            return exec.execute(req, body);
        };

        return builder
                .rootUri(apiBaseUrl)
                .additionalInterceptors(auth)
                .build();
    }
}

