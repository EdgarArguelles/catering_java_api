package com.catering.repositories;

import com.catering.models.Course;
import com.catering.models.Menu;
import com.catering.repositories.mysql.MySQLCourseRepository;

import java.util.List;

public interface CourseRepository extends MySQLCourseRepository {

    //generic query not depends of mongo or sql

    /**
     * Find all Courses associated with the Menu (not needed with JPA because SQL allows bi-directional relationship).
     *
     * @param menu value to search.
     * @return associated courses list
     */
    List<Course> findByMenu(Menu menu);
}