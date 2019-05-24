package com.catering.repositories;

import com.catering.models.Role;
import com.catering.repositories.mysql.MySQLRoleRepository;

public interface RoleRepository extends MySQLRoleRepository {

    //generic query not depends of mongo or sql

    /**
     * Retrieves an entity by its name (name is an unique value).
     *
     * @param name value to search.
     * @return the entity with the given name or null if none found
     */
    Role findByName(String name);
}