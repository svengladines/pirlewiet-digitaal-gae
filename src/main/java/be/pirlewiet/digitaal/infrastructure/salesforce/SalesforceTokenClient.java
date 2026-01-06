package be.pirlewiet.digitaal.infrastructure.salesforce;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;


@Component
public class SalesforceTokenClient {
    private final RestTemplate restTemplate;
    private final URI tokenUri;
    private final String clientId;
    private final String clientSecret;
    private volatile String cachedToken;
    private volatile Instant expiresAt;

    public SalesforceTokenClient(
            RestTemplateBuilder builder,
            @Value("${salesforce.api.tokenBaseUrl}") String baseUrl,
            @Value("${salesforce.api.clientId}") String clientId,
            @Value("${salesforce.api.clientSecret}") String clientSecret) {
        this.restTemplate = builder.build();
        this.tokenUri = URI.create(baseUrl + "/services/oauth2/token");
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public synchronized String getAccessToken() {
        if (cachedToken != null && expiresAt != null && Instant.now().isBefore(expiresAt.minusSeconds(30))) {
            return cachedToken;
        }
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                tokenUri, new HttpEntity<>(form, headers), TokenResponse.class);

        TokenResponse body = Objects.requireNonNull(response.getBody(), "Empty token response");
        this.cachedToken = body.getAccessToken();
        // Sommige responses bevatten 'expires_in'; als die ontbreekt, kies een veilig korte TTL (bijv. 5 min)
        long ttl = body.getExpiresIn() != null ? body.getExpiresIn() : 300;
        this.expiresAt = Instant.now().plusSeconds(ttl);
        return cachedToken;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenResponse {
        @JsonProperty("access_token") private String accessToken;
        @JsonProperty("token_type")   private String tokenType;
        @JsonProperty("instance_url") private String instanceUrl;
        @JsonProperty("expires_in")   private Integer expiresIn;
        public String getAccessToken() { return accessToken; }
        public Integer getExpiresIn() { return expiresIn; }
    }
}