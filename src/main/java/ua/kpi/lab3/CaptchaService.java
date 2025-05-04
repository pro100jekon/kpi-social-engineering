package ua.kpi.lab3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class CaptchaService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${google.recaptcha.secret-key}")
    private String secret;

    public boolean verify(String response, String clientIp) {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secret);
        params.add("response", response);
        params.add("remoteip", clientIp);
        log.info("IP: {}", clientIp);
        ResponseEntity<Map> result = restTemplate.postForEntity(url, params, Map.class);
        return (Boolean) result.getBody().get("success");
    }
}
