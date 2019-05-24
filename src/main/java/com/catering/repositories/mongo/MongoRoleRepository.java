package com.catering.repositories.mongo;

import com.catering.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoRoleRepository extends MongoRepository<Role, String> {

    //custom mongo query for Role
}