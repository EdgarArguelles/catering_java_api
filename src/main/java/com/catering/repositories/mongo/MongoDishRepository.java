package com.catering.repositories.mongo;

import com.catering.models.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDishRepository extends MongoRepository<Dish, String> {

    //custom mongo query for Dish
}