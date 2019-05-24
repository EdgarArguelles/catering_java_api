package com.catering.repositories;

import com.catering.models.Menu;
import com.catering.models.Quotation;
import com.catering.repositories.mysql.MySQLMenuRepository;

import java.util.List;

public interface MenuRepository extends MySQLMenuRepository {

    //generic query not depends of mongo or sql

    /**
     * Find all Menus associated with the Quotation (not needed with JPA because SQL allows bi-directional relationship).
     *
     * @param quotation value to search.
     * @return associated menus list
     */
    List<Menu> findByQuotation(Quotation quotation);
}