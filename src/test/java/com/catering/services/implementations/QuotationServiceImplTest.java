package com.catering.services.implementations;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.factories.PageFactory;
import com.catering.models.*;
import com.catering.pojos.pages.PageDataRequest;
import com.catering.repositories.CourseRepository;
import com.catering.repositories.DishRepository;
import com.catering.repositories.MenuRepository;
import com.catering.repositories.QuotationRepository;
import com.catering.security.pojos.LoggedUser;
import com.catering.security.services.SecurityService;
import com.catering.services.QuotationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuotationServiceImplTest {

    @Autowired
    private QuotationService quotationService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private QuotationRepository quotationRepository;

    @MockBean
    private MenuRepository menuRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private DishRepository dishRepository;

    @MockBean
    private PageFactory pageFactory;

    @Before
    public void setup() {
        final LoggedUser user = new LoggedUser();
        user.setPermissions(Set.of("MY_DATA"));
        final List<GrantedAuthority> authorities = user.getPermissions().stream().map(p -> (GrantedAuthority) () -> "ROLE_" + p).collect(Collectors.toList());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, authorities));
    }

    /**
     * Should throw ConstraintViolationException when invalid
     */
    @Test(expected = ConstraintViolationException.class)
    public void pageMineInvalid() {
        quotationService.pageMine(new PageDataRequest());
    }

    /**
     * Should call findAll function
     */
    @Test
    public void pageMineAllData() {
        final PageDataRequest pageDataRequest = new PageDataRequest(0, 1, null, null);
        final PageRequest pageRequest = PageRequest.of(0, 1);
        final Page<Quotation> quotationsMocked = new PageImpl<>(List.of(new Quotation("ID1"), new Quotation("ID2")));
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "FN", "I", "R", Set.of("VIEW_ALL_DATA")));
        given(pageFactory.pageRequest(pageDataRequest)).willReturn(pageRequest);
        given(quotationRepository.findAll(pageRequest)).willReturn(quotationsMocked);

        final Page<Quotation> quotationsExpected = new PageImpl<>(List.of(new Quotation("ID1"), new Quotation("ID2")));

        final Page<Quotation> quotationsResult = quotationService.pageMine(pageDataRequest);

        assertSame(quotationsMocked, quotationsResult);
        assertNotSame(quotationsExpected, quotationsResult);
        assertEquals(quotationsExpected, quotationsResult);
        verify(securityService, times(1)).getLoggedUser();
        verify(pageFactory, times(1)).pageRequest(pageDataRequest);
        verify(quotationRepository, times(1)).findAll(pageRequest);
    }

    /**
     * Should call findByPerson function
     */
    @Test
    public void pageMine() {
        final Person person = new Person("P1");
        final PageDataRequest pageDataRequest = new PageDataRequest(0, 1, null, null);
        final PageRequest pageRequest = PageRequest.of(0, 1);
        final Page<Quotation> quotationsMocked = new PageImpl<>(List.of(new Quotation("ID1"), new Quotation("ID2")));
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "R"));
        given(pageFactory.pageRequest(pageDataRequest)).willReturn(pageRequest);
        given(quotationRepository.findByPerson(person, pageRequest)).willReturn(quotationsMocked);

        final Page<Quotation> quotationsExpected = new PageImpl<>(List.of(new Quotation("ID1"), new Quotation("ID2")));

        final Page<Quotation> quotationsResult = quotationService.pageMine(pageDataRequest);

        assertSame(quotationsMocked, quotationsResult);
        assertNotSame(quotationsExpected, quotationsResult);
        assertEquals(quotationsExpected, quotationsResult);
        verify(securityService, times(1)).getLoggedUser();
        verify(pageFactory, times(1)).pageRequest(pageDataRequest);
        verify(quotationRepository, times(1)).findByPerson(person, pageRequest);
    }

    /**
     * Should throw CateringDontFoundException
     */
    @Test(expected = CateringDontFoundException.class)
    public void findMineByIdWhenDontFoundAllData() {
        final String ID = "id";
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "FN", "I", "R", Set.of("VIEW_ALL_DATA")));
        given(quotationRepository.findById(ID)).willReturn(Optional.empty());

        quotationService.findMineById(ID);
    }

    /**
     * Should throw CateringDontFoundException
     */
    @Test(expected = CateringDontFoundException.class)
    public void findMineByIdWhenDontFound() {
        final String ID = "id";
        final Person person = new Person("P1");
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "R"));
        given(quotationRepository.findByIdAndPerson(ID, person)).willReturn(Optional.empty());

        quotationService.findMineById(ID);
    }

    /**
     * Should call findById function
     */
    @Test
    public void findMineByIdAllData() {
        final String ID = "ID";
        final Quotation quotationMocked = new Quotation(ID);
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "FN", "I", "R", Set.of("VIEW_ALL_DATA", "extra")));
        given(quotationRepository.findById(ID)).willReturn(Optional.of(quotationMocked));

        final Quotation quotationExpected = new Quotation(ID);

        final Quotation quotationResult = quotationService.findMineById(ID);

        assertSame(quotationMocked, quotationResult);
        assertNotSame(quotationExpected, quotationResult);
        assertEquals(quotationExpected, quotationResult);
        verify(securityService, times(1)).getLoggedUser();
        verify(quotationRepository, times(1)).findById(ID);
    }

    /**
     * Should call findByIdAndPerson function
     */
    @Test
    public void findMineById() {
        final String ID = "ID";
        final Person person = new Person("P1");
        final Quotation quotationMocked = new Quotation(ID);
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "R"));
        given(quotationRepository.findByIdAndPerson(ID, person)).willReturn(Optional.of(quotationMocked));

        final Quotation quotationExpected = new Quotation(ID);

        final Quotation quotationResult = quotationService.findMineById(ID);

        assertSame(quotationMocked, quotationResult);
        assertNotSame(quotationExpected, quotationResult);
        assertEquals(quotationExpected, quotationResult);
        verify(securityService, times(1)).getLoggedUser();
        verify(quotationRepository, times(1)).findByIdAndPerson(ID, person);
    }

    /**
     * Should throw ConstraintViolationException when invalid
     */
    @Test(expected = ConstraintViolationException.class)
    public void saveInvalid() {
        quotationService.save(new Quotation());
    }

    /**
     * Should return a Quotation
     */
    @Test
    public void save() {
        final String D1 = "D1";
        final String D2 = "D2";
        final String D3 = "D3";
        final Course course1 = new Course(1, new CourseType("CT1"), new Menu("FAKE"),
                Set.of(new Dish(D1), new Dish(D2)));
        final Course course2 = new Course(1, new CourseType("CT1"), new Menu("FAKE"),
                Set.of(new Dish(D1)));
        final Course course3 = new Course(2, new CourseType("CT2"), new Menu("FAKE"),
                Set.of(new Dish(D2), new Dish(D3)));

        final Menu menu1 = new Menu("M1", 10, new Quotation("FAKE"));
        menu1.setCourses(List.of(course1));
        final Menu menu2 = new Menu("M2", 20, new Quotation("FAKE2"));
        menu2.setCourses(List.of(course2, course3));

        final Quotation quotation = new Quotation("Q1", 10.5F, new Person("FAKE"));
        quotation.setMenus(List.of(menu1, menu2));

        final Person person = new Person("P1");
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "FN", "I", "R", Set.of("VIEW_ALL_DATA", "extra")));
        given(dishRepository.findById(D1)).willReturn(Optional.of(new Dish(null, null, null, 100.1F, null, null, null)));
        given(dishRepository.findById(D2)).willReturn(Optional.of(new Dish(null, null, null, 200.2F, null, null, null)));
        given(dishRepository.findById(D3)).willReturn(Optional.of(new Dish(null, null, null, 300.3F, null, null, null)));

        final Quotation quotationExpected = new Quotation("Q1", 15015F, person);
        quotationExpected.setMenus(List.of(menu1, menu2));

        final Quotation quotationResult = quotationService.save(quotation);

        assertNotSame(quotationExpected, quotationResult);
        assertEquals(quotationExpected, quotationResult);
        verify(securityService, times(1)).getLoggedUser();
        verify(dishRepository, times(2)).findById(D1);
        verify(dishRepository, times(2)).findById(D2);
        verify(dishRepository, times(1)).findById(D3);
        verify(quotationRepository, times(1)).save(quotation);
        verify(menuRepository, times(1)).save(menu1);
        verify(menuRepository, times(1)).save(menu2);
        verify(courseRepository, times(1)).save(course1);
        verify(courseRepository, times(1)).save(course2);
        verify(courseRepository, times(1)).save(course3);
    }

    /**
     * Should throw ConstraintViolationException when invalid
     */
    @Test(expected = ConstraintViolationException.class)
    public void updateMineInvalid() {
        quotationService.updateMine(new Quotation());
    }

    /**
     * Should throw CateringDontFoundException when quotation doesn't exist
     */
    @Test(expected = CateringDontFoundException.class)
    public void updateMineDontFound() {
        final String ID = "ID";
        final Quotation quotation = new Quotation("Name 1", 500F, new Person("FAKE"));
        quotation.setId(ID);
        quotation.setMenus(Collections.emptyList());

        final Person person = new Person("P1");
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "FN", "I", "R", Set.of("VIEW_ALL_DATA", "extra")));
        given(quotationRepository.findByIdAndPerson(ID, person)).willReturn(Optional.empty());

        quotationService.updateMine(quotation);
    }

    /**
     * Should return a Quotation
     */
    @Test
    public void updateMineNoChanges() {
        final String ID = "ID";
        final Quotation quotation = new Quotation("Name 1", 500F, new Person("FAKE"));
        quotation.setId(ID);
        quotation.setMenus(Collections.emptyList());

        final Quotation original = new Quotation("Name 1", 0F, new Person("ORIGINAL"));
        original.setId("O");
        original.setMenus(Collections.emptyList());

        final Quotation quotationExpected = new Quotation("Name 1", 0F, new Person("ORIGINAL"));
        quotationExpected.setId("O");

        final Person person = new Person("P1");
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "FN", "I", "R", Set.of("VIEW_ALL_DATA", "extra")));
        given(quotationRepository.findByIdAndPerson(ID, person)).willReturn(Optional.of(original));

        final Quotation quotationResult = quotationService.updateMine(quotation);

        assertNotSame(quotationExpected, quotationResult);
        assertEquals(quotationExpected, quotationResult);
        verify(securityService, times(1)).getLoggedUser();
        verify(quotationRepository, times(1)).findByIdAndPerson(ID, person);
        verify(quotationRepository, never()).save(any());
        verify(menuRepository, never()).save(any());
        verify(menuRepository, never()).delete(any());
        verify(courseRepository, never()).save(any());
        verify(courseRepository, never()).delete(any());
    }

    /**
     * Should return a Quotation
     */
    @Test
    public void updateMineEmptyMenus() {
        final String ID = "ID";
        final Quotation quotation = new Quotation("Name 2", 500F, new Person("FAKE"));
        quotation.setId(ID);
        quotation.setMenus(Collections.emptyList());

        final Quotation original = new Quotation("Name 1", 400F, new Person("ORIGINAL"));
        original.setId("O");
        original.setMenus(Collections.emptyList());

        final Quotation quotationExpected = new Quotation("Name 2", 0F, new Person("ORIGINAL"));
        quotationExpected.setId("O");

        final Person person = new Person("P1");
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "FN", "I", "R", Set.of("VIEW_ALL_DATA", "extra")));
        given(quotationRepository.findByIdAndPerson(ID, person)).willReturn(Optional.of(original));

        final Quotation quotationResult = quotationService.updateMine(quotation);

        assertNotSame(quotationExpected, quotationResult);
        assertEquals(quotationExpected, quotationResult);
        verify(securityService, times(1)).getLoggedUser();
        verify(quotationRepository, times(1)).findByIdAndPerson(ID, person);
        verify(quotationRepository, times(1)).save(quotationExpected);
        verify(menuRepository, never()).save(any());
        verify(menuRepository, never()).delete(any());
        verify(courseRepository, never()).save(any());
        verify(courseRepository, never()).delete(any());
    }

    /**
     * Should return a Quotation
     */
    @Test
    public void updateMine() {
        final String ID = "ID";
        final Course newCourse0 = new Course(1, new CourseType(), null, Set.of(new Dish("D1")));
        final Course newCourse = new Course(1, new CourseType(), null, Set.of(new Dish("D1")));
        final Course course1 = new Course(2, new CourseType(), null, Set.of(new Dish("D1")));
        course1.setId("C1");

        final Menu newMenu = new Menu("New Menu", 10, null);
        newMenu.setCourses(List.of(newCourse0));
        final Menu menu1 = new Menu("Menu 1.1", 20, null);
        menu1.setId("M1");
        menu1.setCourses(List.of(newCourse, course1));
        final Quotation quotation = new Quotation("Name", 500F, new Person("FAKE"));
        quotation.setId(ID);
        quotation.setMenus(List.of(newMenu, menu1));

        final Course course1Original = new Course(3, null, null, Collections.emptySet());
        course1Original.setId("C1");
        final Course course2 = new Course(4, null, null, null);
        course2.setId("C2");

        final Quotation original = new Quotation("Name", 0F, new Person("ORIGINAL"));
        original.setId("O");
        final Menu menu1Original = new Menu("Menu 1", 19, null);
        menu1Original.setId("M1");
        menu1Original.setCourses(List.of(course1Original, course2));
        final Menu menu2 = new Menu("Menu 2", 5, null);
        menu2.setId("M2");
        menu2.setCourses(Collections.emptyList());
        original.setMenus(List.of(menu1Original, menu2));

        final Quotation quotationExpected = new Quotation("Name", 500F, new Person("ORIGINAL"));
        quotationExpected.setId("O");

        final Person person = new Person("P1");
        given(dishRepository.findById("D1")).willReturn(Optional.of(new Dish("N", "D", "P", 10F, 0, null, null)));
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "FN", "I", "R", Set.of("VIEW_ALL_DATA", "extra")));
        given(quotationRepository.findByIdAndPerson(ID, person)).willReturn(Optional.of(original));

        final Quotation quotationResult = quotationService.updateMine(quotation);

        assertNotSame(quotationExpected, quotationResult);
        assertEquals(quotationExpected, quotationResult);
        verify(dishRepository, times(3)).findById("D1");
        verify(securityService, times(1)).getLoggedUser();
        verify(quotationRepository, times(1)).findByIdAndPerson(ID, person);
        verify(quotationRepository, times(1)).save(original);
        verify(menuRepository, times(1)).save(newMenu);
        verify(menuRepository, times(1)).delete(menu2);
        verify(courseRepository, times(1)).save(newCourse);
        verify(courseRepository, times(1)).delete(course2);
        verify(courseRepository, times(1)).save(course1Original);
        verify(menuRepository, times(1)).save(menu1Original);
    }

    /**
     * Should throw CateringDontFoundException when quotation doesn't exist
     */
    @Test(expected = CateringDontFoundException.class)
    public void deleteMineDontFound() {
        final String ID = "ID";
        final Person person = new Person("P1");
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "FN", "I", "R", Set.of("VIEW_ALL_DATA", "extra")));
        given(quotationRepository.findByIdAndPerson(ID, person)).willReturn(Optional.empty());

        quotationService.deleteMine(ID);
    }

    /**
     * Should return a Quotation
     */
    @Test
    public void deleteMine() {
        final String ID = "ID";
        final Menu menu1Original = new Menu("MO1");
        menu1Original.setCourses(List.of(new Course("C1")));
        final Menu menu2Original = new Menu("MO2");
        menu2Original.setCourses(List.of(new Course("C2"), new Course("C3")));
        final Quotation original = new Quotation("O");
        original.setMenus(List.of(menu1Original, menu2Original));
        final Quotation quotationExpected = new Quotation("O");
        final Person person = new Person("P1");
        given(securityService.getLoggedUser()).willReturn(new LoggedUser("P1", "FN", "I", "R", Set.of("VIEW_ALL_DATA", "extra")));
        given(quotationRepository.findByIdAndPerson(ID, person)).willReturn(Optional.of(original));

        final Quotation quotationResult = quotationService.deleteMine(ID);

        assertNotSame(quotationExpected, quotationResult);
        assertEquals(quotationExpected, quotationResult);
        verify(securityService, times(1)).getLoggedUser();
        verify(quotationRepository, times(1)).findByIdAndPerson(ID, person);
        verify(courseRepository, times(1)).delete(new Course("C1"));
        verify(courseRepository, times(1)).delete(new Course("C2"));
        verify(courseRepository, times(1)).delete(new Course("C3"));
        verify(menuRepository, times(1)).delete(menu1Original);
        verify(menuRepository, times(1)).delete(menu2Original);
        verify(quotationRepository, times(1)).delete(original);
    }

    /**
     * Should call findByQuotation function
     */
    @Test
    public void getMenusWhenNull() {
        final Quotation quotation = new Quotation();
        final List<Menu> menusMocked = List.of(new Menu("M1"), new Menu("M2"));
        given(menuRepository.findByQuotation(quotation)).willReturn(menusMocked);

        final List<Menu> menusExpected = List.of(new Menu("M1"), new Menu("M2"));

        final List<Menu> menusResult = quotationService.getMenus(quotation);

        assertSame(menusMocked, menusResult);
        assertNotSame(menusExpected, menusResult);
        assertEquals(menusExpected, menusResult);
        verify(menuRepository, times(1)).findByQuotation(quotation);
    }

    /**
     * Should not call findByQuotation function
     */
    @Test
    public void getMenus() {
        final Quotation quotation = new Quotation();
        quotation.setMenus(List.of(new Menu("M1"), new Menu("M2")));

        final List<Menu> menusExpected = List.of(new Menu("M1"), new Menu("M2"));

        final List<Menu> menusResult = quotationService.getMenus(quotation);

        assertNotSame(menusExpected, menusResult);
        assertEquals(menusExpected, menusResult);
        verify(menuRepository, never()).findByQuotation(quotation);
    }

    /**
     * Should call findByMenu function
     */
    @Test
    public void getCoursesWhenNull() {
        final Menu menu = new Menu();
        final List<Course> coursesMocked = List.of(new Course("C1"), new Course("C2"));
        given(courseRepository.findByMenu(menu)).willReturn(coursesMocked);

        final List<Course> coursesExpected = List.of(new Course("C1"), new Course("C2"));

        final List<Course> coursesResult = quotationService.getCourses(menu);

        assertSame(coursesMocked, coursesResult);
        assertNotSame(coursesExpected, coursesResult);
        assertEquals(coursesExpected, coursesResult);
        verify(courseRepository, times(1)).findByMenu(menu);
    }

    /**
     * Should not call findByMenu function
     */
    @Test
    public void getCourses() {
        final Menu menu = new Menu();
        menu.setCourses(List.of(new Course("C1"), new Course("C2")));

        final List<Course> coursesExpected = List.of(new Course("C1"), new Course("C2"));

        final List<Course> coursesResult = quotationService.getCourses(menu);

        assertNotSame(coursesExpected, coursesResult);
        assertEquals(coursesExpected, coursesResult);
        verify(courseRepository, never()).findByMenu(menu);
    }
}