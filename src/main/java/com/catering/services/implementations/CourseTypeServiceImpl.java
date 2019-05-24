package com.catering.services.implementations;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.models.CourseType;
import com.catering.models.Dish;
import com.catering.repositories.CourseTypeRepository;
import com.catering.repositories.DishRepository;
import com.catering.services.CourseTypeService;
import io.leangen.graphql.annotations.*;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@GraphQLApi
@Service
public class CourseTypeServiceImpl implements CourseTypeService {

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private DishRepository dishRepository;

    @Override
    @GraphQLQuery(name = "activeCourseTypes", description = "Find all active course types")
    public List<CourseType> findAllActive() {
        return courseTypeRepository.findByStatusOrderByPosition(CourseType.STATUS.ACTIVE);
    }

    @Override
    @GraphQLQuery(name = "courseType", description = "Find a course type by ID")
    public CourseType findById(@GraphQLId @GraphQLNonNull @GraphQLArgument(name = "id", description = "Course type's ID") String id) throws CateringDontFoundException {
        return courseTypeRepository.findById(id).orElseThrow(() -> new CateringDontFoundException("Data don't found."));
    }

    @Override
    @GraphQLQuery(name = "activeDishes", description = "Active dishes where this CourseType is present")
    public List<Dish> getActiveDishes(@GraphQLContext CourseType courseType) {
        if (courseType.getAllowedDishes() == null) {
            courseType.setAllowedDishes(dishRepository.findByCourseType(courseType));
        }

        return courseType.getAllowedDishes()
                .stream().filter(dish -> dish.getStatus().equals(Dish.STATUS.ACTIVE)).collect(Collectors.toList());
    }
}