package com.catering.repositories;

import com.catering.repositories.mysql.MySQLCategoryRepository;

public interface CategoryRepository extends MySQLCategoryRepository {

    //generic query not depends of mongo or sql
}