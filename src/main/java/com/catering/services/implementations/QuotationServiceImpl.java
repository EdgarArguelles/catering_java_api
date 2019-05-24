package com.catering.services.implementations;

import com.catering.exceptions.CateringDontFoundException;
import com.catering.factories.PageFactory;
import com.catering.models.Course;
import com.catering.models.Menu;
import com.catering.models.Person;
import com.catering.models.Quotation;
import com.catering.pojos.pages.PageDataRequest;
import com.catering.repositories.CourseRepository;
import com.catering.repositories.DishRepository;
import com.catering.repositories.MenuRepository;
import com.catering.repositories.QuotationRepository;
import com.catering.security.pojos.LoggedUser;
import com.catering.security.services.SecurityService;
import com.catering.services.QuotationService;
import io.leangen.graphql.annotations.*;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@GraphQLApi
@Service
public class QuotationServiceImpl implements QuotationService {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private PageFactory pageFactory;

    @Override
    @PreAuthorize("hasRole('MY_DATA')")
    @GraphQLComplexity("180 + childScore")
    @GraphQLQuery(name = "quotationPage", description = "Page all quotations")
    public Page<Quotation> pageMine(@GraphQLNonNull @GraphQLArgument(name = "pageDataRequest", description = "Filter, limit and sort data") PageDataRequest pageDataRequest) {
        LoggedUser loggedUser = securityService.getLoggedUser();
        Page<Quotation> quotations = loggedUser.getPermissions() != null && loggedUser.getPermissions().contains("VIEW_ALL_DATA")
                ? quotationRepository.findAll(pageFactory.pageRequest(pageDataRequest))
                : quotationRepository.findByPerson(new Person(loggedUser.getId()), pageFactory.pageRequest(pageDataRequest));
        return quotations;
    }

    @Override
    @PreAuthorize("hasRole('MY_DATA')")
    @GraphQLQuery(name = "quotation", description = "Find a quotation by ID")
    public Quotation findMineById(@GraphQLId @GraphQLNonNull @GraphQLArgument(name = "id", description = "Quotation's ID") String id) throws CateringDontFoundException {
        LoggedUser loggedUser = securityService.getLoggedUser();
        Quotation quotation = loggedUser.getPermissions() != null && loggedUser.getPermissions().contains("VIEW_ALL_DATA")
                ? quotationRepository.findById(id).orElseThrow(() -> new CateringDontFoundException("Data don't found."))
                : quotationRepository.findByIdAndPerson(id, new Person(loggedUser.getId())).orElseThrow(() -> new CateringDontFoundException("Data don't found."));
        return quotation;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('MY_DATA')")
    @GraphQLMutation(name = "createQuotation", description = "Create a new quotation")
    public Quotation save(@GraphQLNonNull @GraphQLArgument(name = "quotation", description = "New quotation") Quotation quotation) {
        LoggedUser loggedUser = securityService.getLoggedUser();
        double price = quotation.getMenus().stream().mapToDouble(m -> getMenuPrice(m)).sum();
        quotation.setPrice((float) price);
        quotation.setPerson(new Person(loggedUser.getId()));

        quotationRepository.save(quotation);
        saveMenus(quotation);
        return quotation;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('MY_DATA')")
    @GraphQLMutation(name = "updateQuotation", description = "Update a quotation")
    public Quotation updateMine(@GraphQLNonNull @GraphQLArgument(name = "quotation", description = "Quotation's new values") Quotation quotation) {
        Quotation original = findById(quotation.getId());
        List<String> menuIds = quotation.getMenus().stream().filter(m -> m.getId() != null).map(m -> m.getId()).collect(Collectors.toList());
        List<Menu> originalMenusToDelete = original.getMenus().stream().filter(m -> m.getId() != null && !menuIds.contains(m.getId())).collect(Collectors.toList());
        List<Menu> originalMenusToUpdate = original.getMenus().stream().filter(m -> m.getId() != null && menuIds.contains(m.getId())).collect(Collectors.toList());

        saveMenus(quotation);
        deleteMenus(originalMenusToDelete);
        originalMenusToUpdate.forEach(originalMenu -> {
            Menu menu = quotation.getMenus().stream().filter(m -> m.getId() != null && m.getId().equals(originalMenu.getId())).findFirst().get();
            updateMenu(menu, originalMenu);
        });

        double price = quotation.getMenus().stream().mapToDouble(m -> getMenuPrice(m)).sum();
        boolean edit = false;
        if (!quotation.getName().equals(original.getName())) {
            original.setName(quotation.getName());
            edit = true;
        }

        if (price != original.getPrice()) {
            original.setPrice((float) price);
            edit = true;
        }

        original.setMenus(null);
        if (edit) {
            quotationRepository.save(original);
        }

        return original;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('MY_DATA')")
    @GraphQLMutation(name = "deleteQuotation", description = "Delete a quotation")
    public Quotation deleteMine(@GraphQLId @GraphQLNonNull @GraphQLArgument(name = "id", description = "Quotation's ID") String id) {
        Quotation quotation = findById(id);
        deleteMenus(quotation.getMenus());
        quotationRepository.delete(quotation);
        quotation.setMenus(null);
        return quotation;
    }

    @Override
    @PreAuthorize("hasRole('MY_DATA')")
    @GraphQLComplexity("20")
    @GraphQLQuery(name = "menus", description = "Menus where this Quotation is present")
    public List<Menu> getMenus(@GraphQLContext Quotation quotation) {
        if (quotation.getMenus() == null) {
            quotation.setMenus(menuRepository.findByQuotation(quotation));
        }

        return quotation.getMenus();
    }

    @Override
    @PreAuthorize("hasRole('MY_DATA')")
    @GraphQLQuery(name = "courses", description = "Courses where this Menu is present")
    public List<Course> getCourses(@GraphQLContext Menu menu) {
        if (menu.getCourses() == null) {
            menu.setCourses(courseRepository.findByMenu(menu));
        }

        return menu.getCourses();
    }

    private Quotation findById(String id) throws CateringDontFoundException {
        LoggedUser loggedUser = securityService.getLoggedUser();
        Quotation quotation = quotationRepository.findByIdAndPerson(id, new Person(loggedUser.getId())).orElseThrow(() -> new CateringDontFoundException("Data don't found."));

        // Load related data (only used with mongo or jpa which doesn't implement bi-directional relationship)
        if (quotation.getMenus() == null || (!quotation.getMenus().isEmpty() && quotation.getMenus().get(0).getId() == null)) {
            quotation.setMenus(menuRepository.findByQuotation(quotation));
            quotation.getMenus().forEach(m -> {
                if (m.getCourses() == null || (!m.getCourses().isEmpty() && m.getCourses().get(0).getId() == null)) {
                    m.setCourses(courseRepository.findByMenu(m));
                }
            });
        }

        return quotation;
    }

    /**
     * Calculate Menu's price
     *
     * @param menu menu to calculate
     * @return calculated price
     */
    private double getMenuPrice(Menu menu) {
        double menuPrice = menu.getCourses().stream().mapToDouble(c -> getCoursePrice(c)).sum();
        return menuPrice * menu.getQuantity();
    }

    /**
     * Calculate Course's price
     *
     * @param course course to calculate
     * @return calculated price
     */
    private double getCoursePrice(Course course) {
        return course.getDishes().stream().mapToDouble(d -> dishRepository.findById(d.getId()).orElse(null).getPrice()).sum();
    }

    /**
     * Save all Quotation's Menus and its Courses
     *
     * @param quotation quotation with all Menus to save
     */
    private void saveMenus(Quotation quotation) {
        quotation.getMenus().stream().filter(m -> m.getId() == null).forEach(m -> {
            m.setQuotation(quotation);
            menuRepository.save(m);
            saveCourses(m);
        });
    }

    /**
     * Save all Menu's Courses
     *
     * @param menu menu with all Courses to save
     */
    private void saveCourses(Menu menu) {
        menu.getCourses().stream().filter(c -> c.getId() == null).forEach(c -> {
            c.setMenu(menu);
            courseRepository.save(c);
        });
    }

    /**
     * Update a Menu
     *
     * @param menu     menu to update
     * @param original current menu stored in data base
     */
    private void updateMenu(Menu menu, Menu original) {
        List<String> coursesIds = menu.getCourses().stream().filter(c -> c.getId() != null).map(c -> c.getId()).collect(Collectors.toList());
        List<Course> originalCoursesToDelete = original.getCourses().stream().filter(c -> c.getId() != null && !coursesIds.contains(c.getId())).collect(Collectors.toList());
        List<Course> originalCoursesToUpdate = original.getCourses().stream().filter(c -> c.getId() != null && coursesIds.contains(c.getId())).collect(Collectors.toList());

        saveCourses(menu);
        deleteCourses(originalCoursesToDelete);
        originalCoursesToUpdate.forEach(originalCourse -> {
            Course course = menu.getCourses().stream().filter(c -> c.getId() != null && c.getId().equals(originalCourse.getId())).findFirst().get();
            updateCourse(course, originalCourse);
        });

        boolean edit = false;
        if (!menu.getName().equals(original.getName())) {
            original.setName(menu.getName());
            edit = true;
        }

        if (menu.getQuantity() != original.getQuantity()) {
            original.setQuantity(menu.getQuantity());
            edit = true;
        }

        // mongo validation that avoid adding authentications list to Person table
        if (menuRepository instanceof MongoRepository) {
            original.setCourses(menu.getCourses());
            edit = true;
        }

        if (edit) {
            menuRepository.save(original);
        }
    }

    /**
     * Update a Course
     *
     * @param course   course to update
     * @param original current course stored in data base
     */
    private void updateCourse(Course course, Course original) {
        boolean edit = false;
        if (course.getPosition() != original.getPosition()) {
            original.setPosition(course.getPosition());
            edit = true;
        }

        if (edit) {
            courseRepository.save(original);
        }
    }

    /**
     * Delete Menus in list
     *
     * @param menus list of Menus to be deleted
     */
    private void deleteMenus(List<Menu> menus) {
        menus.forEach(m -> {
            deleteCourses(m.getCourses());
            menuRepository.delete(m);
        });
    }

    /**
     * Delete Courses in list
     *
     * @param courses list of Courses to be deleted
     */
    private void deleteCourses(List<Course> courses) {
        courses.forEach(c -> courseRepository.delete(c));
    }
}