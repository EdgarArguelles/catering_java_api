package com.catering.repositories;

import com.catering.models.CourseType;
import com.catering.models.Dish;
import com.catering.repositories.mysql.MySQLDishRepository;

import java.util.List;

public interface DishRepository extends MySQLDishRepository {

    //generic query not depends of mongo or sql

    /**
     * Find all Dishes associated with the CourseType (not needed with JPA because SQL allows bi-directional relationship).
     *
     * @param courseType value to search.
     * @return associated dishes list
     */
    List<Dish> findByCourseType(CourseType courseType);
}