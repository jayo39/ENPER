package com.jnjnetwork.CUHelper.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RecaptchaService {

    @Value("${recaptcha.secret}")
    private String secret;

    private final RestTemplate rest = new RestTemplate();

    public boolean isHuman(String responseToken, String remoteIp) {
        var params = new LinkedMultiValueMap<String, String>();
        params.add("secret", secret);
        params.add("response", responseToken);
        params.add("remoteip", remoteIp);

        RecaptchaReply reply = rest.postForObject(
                "https://www.google.com/recaptcha/api/siteverify",
                params,
                RecaptchaReply.class);

        return reply != null && reply.success();
    }

    // DTO for JSON mapping
    record RecaptchaReply(
            boolean success,
            @JsonProperty("error-codes") List<String> errorCodes) {}
}
