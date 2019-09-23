package com.catering.security.services.implementations;

import com.catering.exceptions.CateringValidationException;
import com.catering.models.Person;
import com.catering.repositories.PersonRepository;
import com.catering.security.factories.LoggedUserFactory;
import com.catering.security.oauth.OAuthProvider;
import com.catering.security.pojos.LoggedUser;
import com.catering.security.services.SecurityService;
import com.catering.security.services.TokenService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@GraphQLApi
@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LoggedUserFactory loggedUserFactory;

    @Autowired
    private TokenService tokenService;

    @Autowired
    @Qualifier("facebookProvider")
    private OAuthProvider facebookProvider;

    @Autowired
    @Qualifier("googleProvider")
    private OAuthProvider googleProvider;

    @Override
    @GraphQLQuery(name = "changeRole", description = "Switch to another user's role")
    public LoggedUser changeRole(@GraphQLNonNull @GraphQLArgument(name = "roleId", description = "New Role's ID") String roleId) throws IOException {
        LoggedUser loggedUser = getLoggedUser();
        if (loggedUser == null) {
            throw new CateringValidationException("There isn't any logged user.");
        }

        Person person = personRepository.findById(loggedUser.getId()).orElse(null);
        loggedUser = loggedUserFactory.loggedUser(person, roleId);
        String token = tokenService.createToken(loggedUser);
        loggedUser = tokenService.getLoggedUser(token);
        loggedUser.setToken(token);

        return loggedUser;
    }

    @Override
    @GraphQLQuery(name = "ping", description = "Ping with server to refresh token")
    public LoggedUser getLoggedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            LoggedUser loggedUser = (LoggedUser) authentication.getPrincipal();
            String newToken = tokenService.createToken(loggedUser);
            loggedUser.setToken(newToken);
            return loggedUser;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @GraphQLQuery(name = "getAccessCode", description = "Gets access code required by social media API")
    public String getAccessCode(@GraphQLNonNull @GraphQLArgument(name = "social", description = "Social media name") SOCIAL_MEDIA social) {
        switch (social) {
            case FACEBOOK:
                return facebookProvider.getAccessCode();
            case GOOGLE:
                return googleProvider.getAccessCode();
        }

        return null;
    }
}