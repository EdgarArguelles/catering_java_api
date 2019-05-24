package com.catering.repositories.mysql;

import com.catering.models.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLQuotationRepository extends JpaRepository<Quotation, String> {

    //custom mysql query for Quotation
}