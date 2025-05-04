package ua.kpi.lab3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.recaptcha")
public record CaptchaProperties(String siteKey, String secretKey) {
}
