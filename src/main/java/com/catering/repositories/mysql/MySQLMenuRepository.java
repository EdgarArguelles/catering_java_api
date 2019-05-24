package com.catering.repositories.mysql;

import com.catering.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLMenuRepository extends JpaRepository<Menu, String> {

    //custom mysql query for Menu
}