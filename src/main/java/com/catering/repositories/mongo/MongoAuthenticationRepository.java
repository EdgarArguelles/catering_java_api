package com.catering.repositories.mongo;

import com.catering.models.Authentication;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoAuthenticationRepository extends MongoRepository<Authentication, String> {

    //custom mongo query for Authentication
}