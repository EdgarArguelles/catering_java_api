package com.catering.repositories.mysql;

import com.catering.models.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLAuthProviderRepository extends JpaRepository<AuthProvider, String> {

    //custom mysql query for AuthProvider
}