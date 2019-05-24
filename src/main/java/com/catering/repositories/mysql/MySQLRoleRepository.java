package com.catering.repositories.mysql;

import com.catering.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLRoleRepository extends JpaRepository<Role, String> {

    //custom mysql query for Role
}