package com.catering.repositories.mysql;

import com.catering.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLPersonRepository extends JpaRepository<Person, String> {

    //custom mysql query for Person
}