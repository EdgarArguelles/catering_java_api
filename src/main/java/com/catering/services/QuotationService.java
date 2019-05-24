package com.catering.services;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.models.Course;
import com.catering.models.Menu;
import com.catering.models.Quotation;
import com.catering.pojos.pages.PageDataRequest;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface QuotationService {

    /**
     * Retrieves all yours Quotations.
     *
     * @param pageDataRequest Page data.
     * @return list of Quotations with metadata.
     */
    Page<Quotation> pageMine(@Valid PageDataRequest pageDataRequest);

    /**
     * Retrieves one of your Quotation by its id.
     *
     * @param id value to search.
     * @return the Quotation with the given id.
     * @throws CateringDontFoundException if Quotation not found.
     */
    Quotation findMineById(String id) throws CateringDontFoundException;

    /**
     * Create a Quotation.
     *
     * @param quotation quotation to be created.
     * @return the Quotation created.
     */
    Quotation save(@Valid Quotation quotation);

    /**
     * Update one of your Quotation.
     *
     * @param quotation quotation to be updated.
     * @return the Quotation updated.
     */
    Quotation updateMine(@Valid Quotation quotation);

    /**
     * Delete one of your Quotation.
     *
     * @param id quotation id to be deleted.
     * @return the Quotation deleted.
     */
    Quotation deleteMine(String id);

    /**
     * GraphQL function to load Quotation's Menus (only needed with mongo or jpa which doesn't implement bi-directional relationship)
     *
     * @param quotation quotation where related data is loaded.
     * @return Quotation's Menus list.
     */
    List<Menu> getMenus(Quotation quotation);

    /**
     * GraphQL function to load Menu's Courses (only needed with mongo or jpa which doesn't implement bi-directional relationship)
     *
     * @param menu menu where related data is loaded.
     * @return Menu's Courses list.
     */
    List<Course> getCourses(Menu menu);
}