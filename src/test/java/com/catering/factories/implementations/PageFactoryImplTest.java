package com.catering.factories.implementations;

import com.catering.factories.PageFactory;
import com.catering.pojos.pages.PageDataRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PageFactoryImplTest {

    @Autowired
    private PageFactory pageFactory;

    /**
     * Should use default sort when sort list is null
     */
    @Test
    public void pageRequestWhenSortListNull() {
        final Integer PAGE = 2;
        final Integer SIZE = 5;

        final List<String> SORT = null;
        final PageDataRequest pageDataRequest = new PageDataRequest(PAGE, SIZE, PageDataRequest.SORT_DIRECTION.DESC, SORT);
        final PageRequest pageRequestExpected = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "id"));

        final PageRequest pageRequestResult = pageFactory.pageRequest(pageDataRequest);

        assertNotSame(pageRequestExpected, pageRequestResult);
        assertEquals(pageRequestExpected, pageRequestResult);
    }

    /**
     * Should use default sort when sort list is empty
     */
    @Test
    public void pageRequestWhenSortListEmpty() {
        final Integer PAGE = 2;
        final Integer SIZE = 5;

        final List<String> SORT = Collections.emptyList();
        final PageDataRequest pageDataRequest = new PageDataRequest(PAGE, SIZE, PageDataRequest.SORT_DIRECTION.DESC, SORT);
        final PageRequest pageRequestExpected = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "id"));

        final PageRequest pageRequestResult = pageFactory.pageRequest(pageDataRequest);

        assertNotSame(pageRequestExpected, pageRequestResult);
        assertEquals(pageRequestExpected, pageRequestResult);
    }

    /**
     * Should use sort when sort list is present
     */
    @Test
    public void pageRequestWhenSortListDESC() {
        final Integer PAGE = 2;
        final Integer SIZE = 5;
        final List<String> SORT = List.of("sort1", "sort2");
        final PageDataRequest pageDataRequest = new PageDataRequest(PAGE, SIZE, PageDataRequest.SORT_DIRECTION.DESC, SORT);

        final PageRequest pageRequestExpected = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.DESC, SORT.toArray(new String[0])));

        final PageRequest pageRequestResult = pageFactory.pageRequest(pageDataRequest);

        assertNotSame(pageRequestExpected, pageRequestResult);
        assertEquals(pageRequestExpected, pageRequestResult);
    }

    /**
     * Should use sort when sort list is present
     */
    @Test
    public void pageRequestWhenSortListASC() {
        final Integer PAGE = 2;
        final Integer SIZE = 5;
        final List<String> SORT = List.of("sort1", "sort2");
        final PageDataRequest pageDataRequest = new PageDataRequest(PAGE, SIZE, PageDataRequest.SORT_DIRECTION.ASC, SORT);

        final PageRequest pageRequestExpected = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, SORT.toArray(new String[0])));

        final PageRequest pageRequestResult = pageFactory.pageRequest(pageDataRequest);

        assertNotSame(pageRequestExpected, pageRequestResult);
        assertEquals(pageRequestExpected, pageRequestResult);
    }
}