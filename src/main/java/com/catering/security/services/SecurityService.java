package com.catering.security.services;

import com.catering.security.pojos.LoggedUser;

import java.io.IOException;

/**
 * Deals with security process
 */
public interface SecurityService {

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
}