package com.catering.repositories.mysql;

import com.catering.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLCategoryRepository extends JpaRepository<Category, String> {

    //custom mysql query for Category
}