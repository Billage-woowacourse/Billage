package sogorae.billage.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import sogorae.billage.domain.Member;
import sogorae.billage.dto.MemberSignUpRequest;
import sogorae.billage.service.dto.GithubEmailResponse;
import sogorae.billage.service.dto.LoginGithubResponse;
import sogorae.billage.service.dto.MemberGithubResponse;

@Slf4j
@Service
@Transactional(readOnly = true)
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);
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

        if (Objects.isNull(memberGithubResponse.getEmail())) {
            String email = getPrimaryEmail(
                Objects.requireNonNull(restTemplate.exchange(GITHUB_USER_BASE_URL + "/public_emails", HttpMethod.GET,
                    memberHttpEntity,
                    GithubEmailResponse[].class).getBody()));
            return getMemberByResponse(new MemberGithubResponse(memberGithubResponse.getName(),
                email));
        }
        return getMemberByResponse(memberGithubResponse);
    }

    private String getPrimaryEmail(GithubEmailResponse[] responses) {
        for (GithubEmailResponse emailResponse : responses) {
            if (emailResponse.getPrimary()) {
                return emailResponse.getEmail();
            }
        }

        for (GithubEmailResponse emailResponse : responses) {
            if (!emailResponse.getPrimary()) {
                return emailResponse.getEmail();
            }
        }

        throw new IllegalArgumentException("No Primary Email");
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
        httpHeaders.set("User-Agent", "Login-App");
        return new HttpEntity<>(httpHeaders);
    }
}
