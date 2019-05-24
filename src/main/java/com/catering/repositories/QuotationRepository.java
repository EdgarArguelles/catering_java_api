package com.catering.repositories;

import com.catering.models.Person;
import com.catering.models.Quotation;
import com.catering.repositories.mysql.MySQLQuotationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuotationRepository extends MySQLQuotationRepository {

    //generic query not depends of mongo or sql

    /**
     * Find all Quotations associated with the Person
     *
     * @param person   value to search.
     * @param pageable pageable attributes.
     * @return associated quotations list
     */
    Page<Quotation> findByPerson(Person person, Pageable pageable);

    /**
     * Retrieves an entity by its id and person.
     *
     * @param id     entry id.
     * @param person value to search.
     * @return the entity with the given id and person or null if none found
     */
    Optional<Quotation> findByIdAndPerson(String id, Person person);
}