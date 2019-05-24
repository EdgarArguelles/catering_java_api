package com.catering.repositories.mongo;

import com.catering.models.Version;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoVersionRepository extends MongoRepository<Version, Long> {

    //custom mongo query for Version
}