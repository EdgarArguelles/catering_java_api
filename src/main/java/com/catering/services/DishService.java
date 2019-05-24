package com.catering.services;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.models.Dish;

public interface DishService {

    /**
     * Retrieves a Dish by its id.
     *
     * @param id value to search.
     * @return the Dish with the given id.
     * @throws CateringDontFoundException if Dish not found.
     */
    Dish findById(String id) throws CateringDontFoundException;
}