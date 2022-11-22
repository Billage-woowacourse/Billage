package sogorae.billage.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import sogorae.billage.dto.SlackFindClientIdResponse;

@Service
@Transactional(readOnly = true)
public class SlackApiService {

    @Value("${slack.token}")
    private String slackToken;

    private static final String url = "https://slack.com/api/chat.postMessage";
    private static final String findIdUrl = "https://slack.com/api/users.lookupByEmail";

    public Object sendMessage(String message, String email) {
        String id = findClientIdByEmail(email);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + slackToken);
        Map<String, String> body = new HashMap<>();
        body.put("channel", id);
        body.put("text", message);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, request, Object.class);
    }

    private String findClientIdByEmail(String email) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = getHttpEntity();
        URI targetUrl = UriComponentsBuilder
            .fromUriString(findIdUrl)
            .queryParam("email", email)
            .build()
            .encode(StandardCharsets.UTF_8)
            .toUri();
        try {
            SlackFindClientIdResponse response = restTemplate.exchange(targetUrl, HttpMethod.POST, httpEntity,
                SlackFindClientIdResponse.class).getBody();
            return response.getUserIdToString();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Slack채널에 해당 Email 이용자가 없습니다.");
        }
    }

    private HttpEntity<String> getHttpEntity() { //헤더에 인증 정보 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + slackToken);
        return new HttpEntity<>(httpHeaders);
    }
}
