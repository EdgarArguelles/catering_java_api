package com.catering.repositories;

import com.catering.models.Authentication;
import com.catering.repositories.mysql.MySQLAuthenticationRepository;

public interface AuthenticationRepository extends MySQLAuthenticationRepository {

    //generic query not depends of mongo or sql

    /**
     * Retrieves an entity by its username (username is an unique value).
     *
     * @param username value to search.
     * @return the entity with the given username or null if none found
     */
    Authentication findByUsername(String username);
}