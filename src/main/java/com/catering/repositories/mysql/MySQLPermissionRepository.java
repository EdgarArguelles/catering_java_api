package com.catering.repositories.mysql;

import com.catering.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLPermissionRepository extends JpaRepository<Permission, String> {

    //custom mysql query for Permission
}