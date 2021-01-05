package com.catering.services.implementations;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.models.Dish;
import com.catering.repositories.DishRepository;
import com.catering.services.DishService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DishServiceImplTest {

    @Autowired
    private DishService dishService;

    @MockBean
    private DishRepository dishRepository;

    /**
     * Should throw CateringDontFoundException
     */
    @Test
    public void findByIdWhenDontFound() {
        final String ID = "id";
        given(dishRepository.findById(ID)).willReturn(Optional.empty());

        assertThrows(CateringDontFoundException.class, () -> dishService.findById(ID));
    }

    /**
     * Should call findById function
     */
    @Test
    public void findById() {
        final String ID = "ID";
        final Dish dishMocked = new Dish(ID);
        given(dishRepository.findById(ID)).willReturn(Optional.of(dishMocked));

        final Dish dishExpected = new Dish(ID);

        final Dish dishResult = dishService.findById(ID);

        assertSame(dishMocked, dishResult);
        assertNotSame(dishExpected, dishResult);
        assertEquals(dishExpected, dishResult);
        verify(dishRepository, times(1)).findById(ID);
    }
}