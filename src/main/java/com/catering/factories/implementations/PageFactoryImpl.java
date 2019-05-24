package com.catering.factories.implementations;

import com.catering.factories.PageFactory;
import com.catering.pojos.pages.PageDataRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageFactoryImpl implements PageFactory {

    @Override
    public PageRequest pageRequest(PageDataRequest pageDataRequest) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");

        if (pageDataRequest.getSort() != null && !pageDataRequest.getSort().isEmpty()) {
            Sort.Direction direction = pageDataRequest.getDirection() != null ? getDirection(pageDataRequest.getDirection()) : null;
            sort = new Sort(direction, pageDataRequest.getSort());
        }

        return PageRequest.of(pageDataRequest.getPage(), pageDataRequest.getSize(), sort);
    }

    /**
     * Parse Sort Direction
     *
     * @param direction page sort direction (could be ASC or DESC)
     * @return Sort Direction or null if value was invalid
     */
    private Sort.Direction getDirection(PageDataRequest.SORT_DIRECTION direction) {
        switch (direction) {
            case ASC:
                return Sort.Direction.ASC;
            case DESC:
                return Sort.Direction.DESC;
            default:
                return null;
        }
    }
}