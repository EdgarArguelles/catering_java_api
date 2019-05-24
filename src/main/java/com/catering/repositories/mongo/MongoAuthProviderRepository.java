package com.catering.repositories.mongo;

import com.catering.models.AuthProvider;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoAuthProviderRepository extends MongoRepository<AuthProvider, String> {

    //custom mongo query for AuthProvider
}