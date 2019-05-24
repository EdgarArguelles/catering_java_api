package com.catering.repositories.mongo;

import com.catering.models.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCourseRepository extends MongoRepository<Course, String> {

    //custom mongo query for Course
}