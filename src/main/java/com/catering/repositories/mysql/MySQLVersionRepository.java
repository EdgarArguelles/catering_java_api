package com.catering.repositories.mysql;

import com.catering.models.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLVersionRepository extends JpaRepository<Version, Long> {

    //custom mysql query for Version
}