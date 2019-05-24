package com.catering.factories;

import com.catering.pojos.pages.PageDataRequest;
import org.springframework.data.domain.PageRequest;

/**
 * Create Page instances
 */
public interface PageFactory {

    /**
     * Create a PageRequest from a PageDataRequest
     *
     * @param pageDataRequest PageDataRequest data
     * @return PageRequest created
     */
    PageRequest pageRequest(PageDataRequest pageDataRequest);
}