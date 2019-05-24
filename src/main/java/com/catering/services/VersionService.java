package com.catering.services;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.models.Version;

public interface VersionService {

    /**
     * Retrieves a Version.
     *
     * @return the Version found.
     * @throws CateringDontFoundException if Version not found.
     */
    Version get() throws CateringDontFoundException;
}