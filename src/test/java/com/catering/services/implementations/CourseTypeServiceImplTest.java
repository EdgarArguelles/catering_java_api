package com.catering.services.implementations;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.models.CourseType;
import com.catering.models.Dish;
import com.catering.repositories.CourseTypeRepository;
import com.catering.repositories.DishRepository;
import com.catering.services.CourseTypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseTypeServiceImplTest {

    @Autowired
    private CourseTypeService courseTypeService;

    @MockBean
    private CourseTypeRepository courseTypeRepository;

    @MockBean
    private DishRepository dishRepository;

    /**
     * Should call findByStatusOrderByPosition function
     */
    @Test
    public void findAllActive() {
        final List<CourseType> courseTypesMocked = List.of(
                new CourseType("C1"), new CourseType("C2"), new CourseType("C3")
        );
        given(courseTypeRepository.findByStatusOrderByPosition(CourseType.STATUS.ACTIVE)).willReturn(courseTypesMocked);

        final List<CourseType> courseTypesExpected = List.of(
                new CourseType("C1"), new CourseType("C2"), new CourseType("C3")
        );

        final List<CourseType> courseTypesResult = courseTypeService.findAllActive();

        assertSame(courseTypesMocked, courseTypesResult);
        assertNotSame(courseTypesExpected, courseTypesResult);
        assertEquals(courseTypesExpected, courseTypesResult);
        verify(courseTypeRepository, times(1)).findByStatusOrderByPosition(CourseType.STATUS.ACTIVE);
    }

    /**
     * Should throw CateringDontFoundException
     */
    @Test(expected = CateringDontFoundException.class)
    public void findByIdWhenDontFound() {
        final String ID = "id";
        given(courseTypeRepository.findById(ID)).willReturn(Optional.empty());

        courseTypeService.findById(ID);
    }

    /**
     * Should call findById function
     */
    @Test
    public void findById() {
        final String ID = "ID";
        final CourseType courseTypeMocked = new CourseType(ID);
        given(courseTypeRepository.findById(ID)).willReturn(Optional.of(courseTypeMocked));

        final CourseType courseTypeExpected = new CourseType(ID);

        final CourseType courseTypeResult = courseTypeService.findById(ID);

        assertSame(courseTypeMocked, courseTypeResult);
        assertNotSame(courseTypeExpected, courseTypeResult);
        assertEquals(courseTypeExpected, courseTypeResult);
        verify(courseTypeRepository, times(1)).findById(ID);
    }

    /**
     * Should call findByCourseType function
     */
    @Test
    public void getActiveDishesWhenNull() {
        final CourseType courseType = new CourseType();
        final List<Dish> dishesMocked = List.of(new Dish("D1"), new Dish("D2"));
        dishesMocked.get(0).setStatus(Dish.STATUS.ACTIVE);
        dishesMocked.get(1).setStatus(Dish.STATUS.INACTIVE);
        given(dishRepository.findByCourseType(courseType)).willReturn(dishesMocked);

        final List<Dish> dishesExpected = List.of(new Dish("D1"));
        dishesExpected.get(0).setStatus(Dish.STATUS.ACTIVE);

        final List<Dish> dishesResult = courseTypeService.getActiveDishes(courseType);

        assertNotSame(dishesExpected, dishesResult);
        assertEquals(dishesExpected, dishesResult);
        verify(dishRepository, times(1)).findByCourseType(courseType);
    }

    /**
     * Should not call findByCourseType function
     */
    @Test
    public void getActiveDishes() {
        final CourseType courseType = new CourseType();
        courseType.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        courseType.getAllowedDishes().get(0).setStatus(Dish.STATUS.ACTIVE);
        courseType.getAllowedDishes().get(1).setStatus(Dish.STATUS.INACTIVE);

        final List<Dish> dishesExpected = List.of(new Dish("D1"));
        dishesExpected.get(0).setStatus(Dish.STATUS.ACTIVE);

        final List<Dish> dishesResult = courseTypeService.getActiveDishes(courseType);

        assertNotSame(dishesExpected, dishesResult);
        assertEquals(dishesExpected, dishesResult);
        verify(dishRepository, never()).findByCourseType(courseType);
    }
}