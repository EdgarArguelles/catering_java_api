package com.catering.repositories;

import com.catering.models.CourseType;
import com.catering.repositories.mysql.MySQLCourseTypeRepository;

import java.util.List;

public interface CourseTypeRepository extends MySQLCourseTypeRepository {

    //generic query not depends of mongo or sql

    /**
     * Find all CourseTypes associated with the Status.
     *
     * @param status value to search.
     * @return associated courseTypes list
     */
    List<CourseType> findByStatusOrderByPosition(Integer status);
}