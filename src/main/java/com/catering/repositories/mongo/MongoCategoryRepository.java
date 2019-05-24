package com.catering.repositories.mongo;

import com.catering.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCategoryRepository extends MongoRepository<Category, String> {

    //custom mongo query for Category
}