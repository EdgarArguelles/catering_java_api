package com.catering.repositories.mongo;

import com.catering.models.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoPermissionRepository extends MongoRepository<Permission, String> {

    //custom mongo query for Permission
}