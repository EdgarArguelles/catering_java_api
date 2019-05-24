package com.catering.services;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.models.CourseType;
import com.catering.models.Dish;

import java.util.List;

public interface CourseTypeService {

    /**
     * Retrieves all active CourseTypes.
     *
     * @return list of active courseTypes.
     */
    List<CourseType> findAllActive();

    /**
     * Retrieves a CourseType by its id.
     *
     * @param id value to search.
     * @return the CourseType with the given id.
     * @throws CateringDontFoundException if CourseType not found.
     */
    CourseType findById(String id) throws CateringDontFoundException;

    /**
     * GraphQL function to load Active CourseType's Dishes (only needed with mongo or jpa which doesn't implement bi-directional relationship)
     *
     * @param courseType courseType where related data is loaded.
     * @return Active CourseType's Dishes list.
     */
    List<Dish> getActiveDishes(CourseType courseType);
}