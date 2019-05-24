package com.catering.repositories;

import com.catering.repositories.mysql.MySQLVersionRepository;

public interface VersionRepository extends MySQLVersionRepository {

    //generic query not depends of mongo or sql
}