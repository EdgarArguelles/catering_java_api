package com.catering.repositories.mongo;

import com.catering.models.CourseType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCourseTypeRepository extends MongoRepository<CourseType, String> {

    //custom mongo query for CourseType
}