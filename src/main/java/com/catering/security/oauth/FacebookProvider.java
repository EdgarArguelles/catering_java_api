package com.catering.security.oauth;

import com.catering.models.AuthProvider;
import com.catering.repositories.AuthProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
public class FacebookProvider implements OAuthProvider {

    private static OAuth2Operations oAuthOperations;

    private static AuthProvider authProvider;

    @Autowired
    private AuthProviderRepository authProviderRepository;

    @Override
    public String getApiKey() {
        return getAuthProvider().getAuthKey();
    }

    @Override
    public String getApiSecret() {
        return getAuthProvider().getAuthSecret();
    }

    @Override
    public String getAccessCode() {
        try {
            String accessCode = getAuthProvider().getAccessCode();
            Map<String, Object> body = new RestTemplate()
                    .getForObject("https://graph.facebook.com/v4.0/me/accounts?access_token=" + accessCode, Map.class);
            var data = (List<LinkedHashMap<String, Object>>) body.get("data");
            return data.get(0).get("access_token").toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAuthorizeUrl(String callback, String state) {
        OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();
        oAuth2Parameters.setRedirectUri(callback);
        // scope could be email,user_birthday,user_gender,user_friends, etc
        // but facebook will warn users your application is trying to access sensitive
        // information or post for you
        // oAuth2Parameters.setScope("email,user_birthday,user_gender");
        oAuth2Parameters.setScope("email");
        oAuth2Parameters.setState(state);
        return getOAuthOperations().buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
    }

    @Override
    public AccessGrant getAccessGrant(String callback, String authorizationCode) {
        return getOAuthOperations().exchangeForAccess(authorizationCode, callback, null);
    }

    private OAuth2Operations getOAuthOperations() {
        if (oAuthOperations == null) {
            FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(getApiKey(), getApiSecret());
            oAuthOperations = connectionFactory.getOAuthOperations();
        }

        return oAuthOperations;
    }

    private AuthProvider getAuthProvider() {
        if (authProvider == null) {
            authProvider = authProviderRepository.findByName("FACEBOOK");
        }

        return authProvider;
    }
}