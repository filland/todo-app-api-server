package com.kurbatov.todoapp.security.oauth2;

import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.security.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.kurbatov.todoapp.security.oauth2.user.OAuth2UserInfo;
import com.kurbatov.todoapp.security.oauth2.user.OAuth2UserInfoFactory;
import com.kurbatov.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userRepository;

    @Autowired
    private OAuth2UserInfoFactory oAuth2UserInfoFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        // todo maybe move API from OAuth2UserInfoFactory to here

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.createOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                new HashMap<>(oAuth2User.getAttributes()),
                oAuth2UserRequest.getAccessToken().getTokenValue()
        );

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException(ErrorType.AUTH_OAUTH2_EMAIL_NOT_FOUND);
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(
                    AuthProvider.toEnum(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
            ) {
                throw new OAuth2AuthenticationProcessingException(
                        ErrorType.AUTH_OAUTH2_WRONG_AUTH_MECHANISM,
                        user.getProvider().name()
                );
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setFirstName(oAuth2UserInfo.getName());
        user.setUsername((String) oAuth2UserInfo.getAttributes().get("login"));
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setProvider(AuthProvider.toEnum(
                oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase())
        );
        user.setActive(true);
        user.setEmailConfirmed(false);
        return userRepository.saveUser(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName(oAuth2UserInfo.getName());
        return userRepository.saveUser(existingUser);
    }

}
