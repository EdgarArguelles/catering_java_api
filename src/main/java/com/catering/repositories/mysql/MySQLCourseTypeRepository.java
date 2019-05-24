package com.catering.repositories.mysql;

import com.catering.models.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLCourseTypeRepository extends JpaRepository<CourseType, String> {

    //custom mysql query for CourseType
}