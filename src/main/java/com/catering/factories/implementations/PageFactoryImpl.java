package com.catering.factories.implementations;

import com.catering.factories.PageFactory;
import com.catering.pojos.pages.PageDataRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PageFactoryImpl implements PageFactory {

    @Override
    public PageRequest pageRequest(PageDataRequest pageDataRequest) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        if (pageDataRequest.sort() != null && !pageDataRequest.sort().isEmpty()) {
            Sort.Direction direction = pageDataRequest.direction() != null ? getDirection(pageDataRequest.direction())
                    : null;
            sort = Sort.by(Objects.requireNonNull(direction), pageDataRequest.sort().toArray(new String[0]));
        }

        return PageRequest.of(pageDataRequest.page(), pageDataRequest.size(), sort);
    }

    /**
     * Parse Sort Direction
     *
     * @param direction page sort direction (could be ASC or DESC)
     * @return Sort Direction or null if value was invalid
     */
    private Sort.Direction getDirection(PageDataRequest.SORT_DIRECTION direction) {
        return switch (direction) {
            case ASC -> Sort.Direction.ASC;
            case DESC -> Sort.Direction.DESC;
        };
    }
}