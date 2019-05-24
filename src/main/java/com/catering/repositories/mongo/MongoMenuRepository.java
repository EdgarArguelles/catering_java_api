package com.catering.repositories.mongo;

import com.catering.models.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoMenuRepository extends MongoRepository<Menu, String> {

    //custom mongo query for Menu
}