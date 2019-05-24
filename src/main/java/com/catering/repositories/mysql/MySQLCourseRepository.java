package com.catering.repositories.mysql;

import com.catering.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLCourseRepository extends JpaRepository<Course, String> {

    //custom mysql query for Course
}