package com.catering.services.implementations;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.models.Version;
import com.catering.repositories.VersionRepository;
import com.catering.services.VersionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VersionServiceImplTest {

    @Autowired
    private VersionService versionService;

    @MockBean
    private VersionRepository versionRepository;

    /**
     * Should throw CateringDontFoundException
     */
    @Test(expected = CateringDontFoundException.class)
    public void getWhenDontFound() {
        given(versionRepository.findAll()).willReturn(Collections.emptyList());

        versionService.get();
    }

    /**
     * Should call findAll function
     */
    @Test
    public void get() {
        final Long ID = 5L;
        final Version versionMocked = new Version(ID);
        given(versionRepository.findAll()).willReturn(List.of(versionMocked));

        final Version versionExpected = new Version(ID);

        final Version versionResult = versionService.get();

        assertSame(versionMocked, versionResult);
        assertNotSame(versionExpected, versionResult);
        assertEquals(versionExpected, versionResult);
        verify(versionRepository, times(1)).findAll();
    }
}