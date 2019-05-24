package com.catering.repositories.mysql;

import com.catering.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLDishRepository extends JpaRepository<Dish, String> {

    //custom mysql query for Dish
}