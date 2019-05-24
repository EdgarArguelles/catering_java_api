package com.catering.security.services;

import com.catering.security.pojos.LoggedUser;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.google.api.impl.GoogleTemplate;

/**
 * Deals with parsing oauth profile process
 */
public interface OAuthService {

    /**
     * Parse Facebook profile to LoggedUser instance
     *
     * @param template instance of FacebookTemplate generated for Facebook API
     * @return LoggedUser instance associated with Facebook profile
     */
    LoggedUser parseFacebookUser(FacebookTemplate template);

    /**
     * Parse Google profile to LoggedUser instance
     *
     * @param template instance of GoogleTemplate generated for Google API
     * @return LoggedUser instance associated with Google profile
     */
    LoggedUser parseGoogleUser(GoogleTemplate template);
}