package com.catering.repositories.mongo;

import com.catering.models.Quotation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoQuotationRepository extends MongoRepository<Quotation, String> {

    //custom mongo query for Quotation
}