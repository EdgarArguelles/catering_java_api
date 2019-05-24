package com.catering.services.implementations;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.models.Version;
import com.catering.repositories.VersionRepository;
import com.catering.services.VersionService;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@GraphQLApi
@Service
public class VersionServiceImpl implements VersionService {

    @Autowired
    private VersionRepository versionRepository;

    @Override
    @GraphQLQuery(name = "version", description = "Get data version")
    public Version get() throws CateringDontFoundException {
        List<Version> versions = versionRepository.findAll();

        if (versions.isEmpty()) {
            throw new CateringDontFoundException("Data don't found.");
        }

        return versions.get(0);
    }
}