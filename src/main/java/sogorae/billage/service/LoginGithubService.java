package sogorae.billage.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import sogorae.billage.domain.Member;
import sogorae.billage.dto.MemberSignUpRequest;
import sogorae.billage.service.dto.LoginGithubResponse;

@Service
public class LoginGithubService {

    private static final String AUTH_BASE_URL = "https://github.com/login/oauth/access_token";
    private static final String GITHUB_USER_BASE_URL = "https://api.github.com/user";

    private final MemberService memberService;

    @Value("${github.client.id}")
    private String clientID;

    @Value("${github.client.secret}")
    private String clientSecret;

    public LoginGithubService(MemberService memberService) {
        this.memberService = memberService;
    }

    public Member login(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(null);
        URI targetUrl = UriComponentsBuilder
            .fromUriString(AUTH_BASE_URL)
            .queryParam("client_id", clientID)
            .queryParam("client_secret", clientSecret)
            .queryParam("code", code)
            .build()
            .encode(StandardCharsets.UTF_8)
            .toUri();
        LoginGithubResponse tokenResponse = restTemplate.exchange(targetUrl, HttpMethod.POST, httpEntity,
            LoginGithubResponse.class).getBody();

        HttpEntity<String> memberHttpEntity =
            getHttpEntity(Objects.requireNonNull(tokenResponse).getAccess_token());

        MemberGithubResponse memberGithubResponse = restTemplate.exchange(GITHUB_USER_BASE_URL, HttpMethod.GET,
            memberHttpEntity,
            MemberGithubResponse.class).getBody();

        return getMemberByResponse(memberGithubResponse);
    }

    private Member getMemberByResponse(MemberGithubResponse response) {
        String email = Objects.requireNonNull(response).getEmail();
        try {
            return memberService.findByEmail(email);
        } catch (RuntimeException e) {
            String nickname = response.getName();
            memberService.save(
                new MemberSignUpRequest(email, nickname));
            return new Member(email, nickname);
        }
    }

    private HttpEntity<String> getHttpEntity(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "token " + token);
        return new HttpEntity<>(httpHeaders);
    }
}
