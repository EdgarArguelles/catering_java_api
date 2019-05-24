package com.catering.repositories.mysql;

import com.catering.models.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLAuthenticationRepository extends JpaRepository<Authentication, String> {

    //custom mysql query for Authentication
}