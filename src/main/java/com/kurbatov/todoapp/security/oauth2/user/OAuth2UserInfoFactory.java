package com.kurbatov.todoapp.security.oauth2.user;

import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.security.oauth2.AuthProvider;
import com.kurbatov.todoapp.security.oauth2.GithubUserEmailRS;
import com.kurbatov.todoapp.security.oauth2.exception.OAuth2AuthenticationProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

// TODO where to move github's api call ?
@Component
public class OAuth2UserInfoFactory {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @param registrationId
     * @param attributes
     * @param accessToken    is the token for accessing the API of (github or facebook
     *                       or google or whatever API supported by the app
     */
    public OAuth2UserInfo createOAuth2UserInfo(String registrationId,
                                               Map<String, Object> attributes,
                                               String accessToken) {

        if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {

            // the email is not included in the response received after success auth
            // so it is necessary to fetch user's email by another request
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "token " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<List<GithubUserEmailRS>> response = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<GithubUserEmailRS>>() {
                    });
            List<GithubUserEmailRS> list = response.getBody();
            for (GithubUserEmailRS email : list) {
                if (email.getPrimary()) {
                    attributes.put("email", email.getEmail());
                }
            }

            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException(ErrorType.AUTH_OAUTH2_PROVIDER_NOT_SUPPORTED, registrationId);
        }
    }
}
