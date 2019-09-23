package com.catering.security.services;

import com.catering.security.pojos.LoggedUser;
import io.leangen.graphql.annotations.GraphQLEnumValue;

import java.io.IOException;

/**
 * Deals with security process
 */
public interface SecurityService {

    enum SOCIAL_MEDIA {
        @GraphQLEnumValue(description = "use Facebook") FACEBOOK,
        @GraphQLEnumValue(description = "use Google") GOOGLE,
    }

    /**
     * Creates a new LoggedUser instance with the requested role
     *
     * @param roleId requested role id.
     * @return new LoggedUser instance
     * @throws IOException if error generating token
     */
    LoggedUser changeRole(String roleId) throws IOException;

    /**
     * Gets info from logged user (LoggedUser instance)
     *
     * @return LoggedUser instance
     */
    LoggedUser getLoggedUser();

    /**
     * Gets access code required by social media API
     *
     * @param social social media name
     * @return access code
     */
    String getAccessCode(SOCIAL_MEDIA social);
}