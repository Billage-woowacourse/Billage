package sogorae.billage.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sogorae.billage.dto.SearchBookResponse;

@Service
public class NaverSearchService {

    private final static String baseUrl = "https://openapi.naver.com/v1/search/book.json";

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public SearchBookResponse search(String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = getHttpEntity();
        URI targetUrl = UriComponentsBuilder
          .fromUriString(baseUrl)
          .queryParam("query", keyword)
          .build()
          .encode(StandardCharsets.UTF_8)
          .toUri();
        return restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, SearchBookResponse.class).getBody();
    }

    private HttpEntity<String> getHttpEntity() { //헤더에 인증 정보 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Naver-Client-Id", clientId);
        httpHeaders.set("X-Naver-Client-Secret", clientSecret);
        return new HttpEntity<>(httpHeaders);
    }
}
