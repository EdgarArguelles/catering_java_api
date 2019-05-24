package com.catering.repositories.mongo;

import com.catering.models.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoPersonRepository extends MongoRepository<Person, String> {

    //custom mongo query for Person
}