package com.catering.repositories;

import com.catering.repositories.mysql.MySQLPersonRepository;

public interface PersonRepository extends MySQLPersonRepository {

    //generic query not depends of mongo or sql
}