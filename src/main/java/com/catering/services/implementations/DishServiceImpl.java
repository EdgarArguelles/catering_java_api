package com.catering.services.implementations;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.models.Dish;
import com.catering.repositories.DishRepository;
import com.catering.services.DishService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLId;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@GraphQLApi
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishRepository dishRepository;

    @Override
    @GraphQLQuery(name = "dish", description = "Find a dish by ID")
    public Dish findById(@GraphQLId @GraphQLNonNull @GraphQLArgument(name = "id", description = "Dish's ID") String id) throws CateringDontFoundException {
        return dishRepository.findById(id).orElseThrow(() -> new CateringDontFoundException("Data don't found."));
    }
}