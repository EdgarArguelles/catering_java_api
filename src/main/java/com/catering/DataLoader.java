package com.catering;

import com.catering.models.*;
import com.catering.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Populates data tables at application start
 */
@Component
public class DataLoader {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private AuthProviderRepository authProviderRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private VersionRepository versionRepository;

    @Value("${data-loader}")
    private Boolean loadData;

    @Value("${test-loader}")
    private Boolean testData;

    private Role chefRole;

    @PostConstruct
    private void setupDatabase() {
        try {
            if (loadData && authProviderRepository.count() == 0) {
                insertData();
            }
            if (testData && courseTypeRepository.count() == 0) {
                insertTest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertData() {
        List<AuthProvider> authProviders = List.of(
                new AuthProvider("FACEBOOK", "Provide access with Facebook account", "489262341460511", "8ff4dae0a47a2e82a75332859350cec0", "EAAG8ZB0QQkh8BAMdZB48YkrLozHY9Ag8jdUhHjd7NJZCn16mQSdfYmugfW8sksDswOr7N6NVwJ6ngIaRUsETPSs5TodFGEGHQXT8FuUIKVZAosD6AsMxkYSPVLjz0bVn6g1ohxyFBETpZA8h1hwZCXjaDtTyYTfka2eQMZAXMOgIX6pquFGSVOMOkL5qBBYUcsZD"),
                new AuthProvider("GOOGLE", "Provide access with Google account", "671405016959-kdd2b8n9rii21drdm6pb9t554s6i5psq.apps.googleusercontent.com", "6lOkkES-_qiw28LMH400JoLH", null)
        );
        authProviderRepository.saveAll(authProviders);

        Set<Permission> allPermissions = Set.of(
                new Permission("VIEW_ALL_DATA", "Allows to see all data."),
                new Permission("MY_DATA", "Allows to see and edit your data.")
        );
        permissionRepository.saveAll(allPermissions);

        Set<Role> allRoles = Set.of(
                new Role("CHEF", "User with chef permissions.", allPermissions),
                new Role("CLIENT", "User with client permissions.", Set.of(permissionRepository.findByName("MY_DATA")))
        );
        roleRepository.saveAll(allRoles);

        chefRole = allRoles.stream().filter(r -> r.getName().equals("CHEF")).findAny().orElse(null);
    }

    private void insertTest() {
        versionRepository.save(new Version((long) (Math.random() * 1000)));

        Person person = new Person("Edgar", "Arguelles", LocalDate.now(), Person.CIVIL_STATUS.MARRIED, Person.SEX.M, "ing.edgar.arguelles@gmail.com", Set.of(chefRole));
        personRepository.save(person);
        Authentication authentication = new Authentication("554301354915404", null, authProviderRepository.findByName("FACEBOOK"), person);
        authenticationRepository.save(authentication);

        List<Category> categories = List.of(
                new Category("Ensaladas"), // E8 PF0 P0
                new Category("Helados"), // E0 PF0 P6
                new Category("Sopas"), // E10 PF0 P0
                new Category("Dulce"), // E3 PF0 P5
                new Category("Guarnición"), // E0 PF3 P0
                new Category("Ave"), // E0 PF3 P0
                new Category("Cerdo"), // E0 PF4 P0
                new Category("Res"), // E0 PF5 P0
                new Category("Pescado"), // E0 PF4 P0
                new Category("Cremoso"), // E0 PF0 P7
                new Category("Pastel"), // E0 PF0 P7
                new Category("Proteina") // E0 PF17 P0
        );
        categoryRepository.saveAll(categories);

        List<CourseType> courseTypes = List.of(
                new CourseType("Baja", "PP1", 2, CourseType.STATUS.INACTIVE),
                new CourseType("Entrada", "1GOLA_5WziAQJ4mZ94xOH7saIVuLXZeMz", 1, CourseType.STATUS.ACTIVE),
                new CourseType("Plato Fuerte", "1cKaMj4u_cfu3RcP-ulNfv7N_d_cZeNQ1", 2, CourseType.STATUS.ACTIVE),
                new CourseType("Postre", "1lQmETC9vtjhsuDQA7VXZFELWKtJHUQUh", 3, CourseType.STATUS.ACTIVE)
        );
        courseTypeRepository.saveAll(courseTypes);

        List<Dish> dishes = List.of(
                new Dish("Plato Baja", "Descripción de platillo dado de baja", "PPP1", 15.5F, Dish.STATUS.INACTIVE,
                        courseTypes.get(1), Set.of(categories.get(0), categories.get(1))),
                new Dish("Plato Inaccesible", "Descripción de platillo activo pero Inaccesible", "PPP1", 15.5F, Dish.STATUS.ACTIVE,
                        courseTypes.get(0), Set.of(categories.get(0), categories.get(1))),

                // Entrada (20)
                new Dish("Tabla de quesos", "Una tabla de quesos (Entrada - sin categoría) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "15oWZdvYxtAkSup3WUkVQqBFDWvOQn4wS",
                        11.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), null),
                new Dish("Cocteleria", "Fina elección de cocteles (Entrada - sin categoría) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1S40I3gLOuUGNBwXPivdWZRpHtwxwLh9V",
                        12.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Collections.emptySet()),
                new Dish("Ensalada de frutas dulces", "Delicioso ensalada con frutos dulces de temporada (Entrada - con 2 categorias) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1Zsa_X9MMhtRzAg0fHqZ88m_-8byhQ6oR",
                        13.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(0), categories.get(3))),
                new Dish("Ensalada Cesar", "Delicioso ensalada Cesar (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "15oWZdvYxtAkSup3WUkVQqBFDWvOQn4wS",
                        14.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(0))),
                new Dish("Ensalada Italiana", "Delicioso ensalada Italiana (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1Zsa_X9MMhtRzAg0fHqZ88m_-8byhQ6oR",
                        15.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(0))),
                new Dish("Ensalada Romana", "Delicioso ensalada Romana (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1S40I3gLOuUGNBwXPivdWZRpHtwxwLh9V",
                        16.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(0))),
                new Dish("Ensalada Mexicana", "Delicioso ensalada Mexicana (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1S40I3gLOuUGNBwXPivdWZRpHtwxwLh9V",
                        17.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(0))),
                new Dish("Ensalada Orejona", "Delicioso ensalada Orejona (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "15oWZdvYxtAkSup3WUkVQqBFDWvOQn4wS",
                        18.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(0))),
                new Dish("Ensalada Dulce", "Delicioso ensalada Dulce (Entrada - con 2 categorias) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1Zsa_X9MMhtRzAg0fHqZ88m_-8byhQ6oR",
                        19.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(0), categories.get(3))),
                new Dish("Ensalada 1", "Delicioso ensalada 1 (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "15oWZdvYxtAkSup3WUkVQqBFDWvOQn4wS",
                        20.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(0))),
                new Dish("Sopa de Elotes", "Delicioso Sopa de Elotes (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1S40I3gLOuUGNBwXPivdWZRpHtwxwLh9V",
                        21.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(2))),
                new Dish("Sopa Dulce", "Delicioso Sopa Dulce (Entrada - con 2 categorias) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "15oWZdvYxtAkSup3WUkVQqBFDWvOQn4wS",
                        22.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(2), categories.get(3))),
                new Dish("Sopa 1", "Delicioso Sopa 1 (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1Zsa_X9MMhtRzAg0fHqZ88m_-8byhQ6oR",
                        23.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(2))),
                new Dish("Sopa 2", "Delicioso Sopa 2 (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1S40I3gLOuUGNBwXPivdWZRpHtwxwLh9V",
                        24.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(2))),
                new Dish("Sopa 3", "Delicioso Sopa 3 (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "15oWZdvYxtAkSup3WUkVQqBFDWvOQn4wS",
                        25.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(2))),
                new Dish("Sopa 4", "Delicioso Sopa 4 (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1Zsa_X9MMhtRzAg0fHqZ88m_-8byhQ6oR",
                        26.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(2))),
                new Dish("Sopa 5", "Delicioso Sopa 5 (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1S40I3gLOuUGNBwXPivdWZRpHtwxwLh9V",
                        27.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(2))),
                new Dish("Sopa 6", "Delicioso Sopa 6 (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1Zsa_X9MMhtRzAg0fHqZ88m_-8byhQ6oR",
                        28.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(2))),
                new Dish("Sopa 7", "Delicioso Sopa 7 (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "15oWZdvYxtAkSup3WUkVQqBFDWvOQn4wS",
                        29.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(2))),
                new Dish("Sopa 8", "Delicioso Sopa 8 (Entrada - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1S40I3gLOuUGNBwXPivdWZRpHtwxwLh9V",
                        30.2F, Dish.STATUS.ACTIVE, courseTypes.get(1), Set.of(categories.get(2))),


                // Plato Fuerte (20)
                new Dish("Lomo de Res", "Jugoso Lomo de Res (Plato Fuerte - sin categoría) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "19VIHX-d2mwGh3zPS4VnxqSlZxshuF0sw",
                        101F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(11))),
                new Dish("Verduras de temporada", "Delicioso Verduras de temporada (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1nZTn6e-pSyQKjjOcbpnaRKEopFTxcad6",
                        102F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(4))),
                new Dish("Verduras 1", "Delicioso Verduras 1 (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1NWqTHBSBU9jH_stw-cHS7syPkicbc4mY",
                        103F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(4))),
                new Dish("Verduras 2", "Delicioso Verduras 2 (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1nZTn6e-pSyQKjjOcbpnaRKEopFTxcad6",
                        104F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(4))),
                new Dish("Pollo a las finas yervas", "Pollo a las finas yervas (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "19VIHX-d2mwGh3zPS4VnxqSlZxshuF0sw",
                        105F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(5), categories.get(11))),
                new Dish("Pato a la Orange", "Pato a la Orange (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "19VIHX-d2mwGh3zPS4VnxqSlZxshuF0sw",
                        106F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(5), categories.get(11))),
                new Dish("Pavo a la mantequilla", "Pavo a la mantequilla (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1NWqTHBSBU9jH_stw-cHS7syPkicbc4mY",
                        107F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(5), categories.get(11))),
                new Dish("Costillitas de cerdo a la BBQ", "Costillitas de cerdo a la BBQ (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1NWqTHBSBU9jH_stw-cHS7syPkicbc4mY",
                        108F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(6), categories.get(11))),
                new Dish("Pierna de cerdo al Chipotle", "Pierna de cerdo al Chipotle (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1nZTn6e-pSyQKjjOcbpnaRKEopFTxcad6",
                        109F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(6), categories.get(11))),
                new Dish("Pierna de cerdo 1", "Pierna de cerdo 1 (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "19VIHX-d2mwGh3zPS4VnxqSlZxshuF0sw",
                        110F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(6), categories.get(11))),
                new Dish("Pierna de cerdo 2", "Pierna de cerdo 2 (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "19VIHX-d2mwGh3zPS4VnxqSlZxshuF0sw",
                        111F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(6), categories.get(11))),
                new Dish("Lomo de Res a la finas llervas", "Lomo de Res a la finas llervas (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1NWqTHBSBU9jH_stw-cHS7syPkicbc4mY",
                        112F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(7), categories.get(11))),
                new Dish("Lomo de Res a la 1", "Lomo de Res a la 1 (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1NWqTHBSBU9jH_stw-cHS7syPkicbc4mY",
                        113F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(7), categories.get(11))),
                new Dish("Lomo de Res a la 2", "Lomo de Res a la 2 (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1nZTn6e-pSyQKjjOcbpnaRKEopFTxcad6",
                        114F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(7), categories.get(11))),
                new Dish("Lomo de Res a la 3", "Lomo de Res a la 3 (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "19VIHX-d2mwGh3zPS4VnxqSlZxshuF0sw",
                        115F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(7), categories.get(11))),
                new Dish("Lomo de Res a la 4", "Lomo de Res a la 4 (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1NWqTHBSBU9jH_stw-cHS7syPkicbc4mY",
                        116F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(7), categories.get(11))),
                new Dish("Coctel de Camarones", "Contel de Camarones (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1nZTn6e-pSyQKjjOcbpnaRKEopFTxcad6",
                        117F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(8), categories.get(11))),
                new Dish("Coctel de Pulpo", "Contel de Pulpo (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "19VIHX-d2mwGh3zPS4VnxqSlZxshuF0sw",
                        118F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(8), categories.get(11))),
                new Dish("Coctel de Ostiones", "Contel de Ostiones (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "19VIHX-d2mwGh3zPS4VnxqSlZxshuF0sw",
                        119F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(8), categories.get(11))),
                new Dish("Coctel de 1", "Contel de 1 (Plato Fuerte - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1nZTn6e-pSyQKjjOcbpnaRKEopFTxcad6",
                        120F, Dish.STATUS.ACTIVE, courseTypes.get(2), Set.of(categories.get(8), categories.get(11))),


                // Postre (20)
                new Dish("Helado de chile", "Delicioso picosito Helado de chile (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1c2SXAKr1IwlntQf7rPpU_1zWo_qXd4hP",
                        201.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(1))),
                new Dish("Helado de Fresa", "Cremoso y dulce Helado sobor a Fresa tropical (Postre - con 2 categorias) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1pkUTHV5Cqhj8lvxjul-QiDMlVWs8h0k0",
                        202.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(1), categories.get(3))),
                new Dish("Helado de Chocolate", "Cremoso y dulce Helado sobor a Chocolate (Postre - con 2 categorias) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1lMkQy2kBknZ88OVJXXOkzqseuQrSjHul",
                        203.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(1), categories.get(3))),
                new Dish("Helado de Vainilla", "Cremoso y dulce Helado sobor a Vainilla (Postre - con 2 categorias) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1pkUTHV5Cqhj8lvxjul-QiDMlVWs8h0k0",
                        204.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(1), categories.get(3))),
                new Dish("Helado de Uva", "Cremoso y dulce Helado sobor a Uva (Postre - con 2 categorias) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1c2SXAKr1IwlntQf7rPpU_1zWo_qXd4hP",
                        205.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(1), categories.get(3))),
                new Dish("Helado de Otro", "Cremoso y dulce Helado sobor a Otro (Postre - con 2 categorias) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1lMkQy2kBknZ88OVJXXOkzqseuQrSjHul",
                        206.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(1), categories.get(3))),
                new Dish("Cremoso de Fresa", "Cremoso de Fresa (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1pkUTHV5Cqhj8lvxjul-QiDMlVWs8h0k0",
                        207.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(9))),
                new Dish("Cremoso de Chocolate", "Cremoso de Chocolate (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1c2SXAKr1IwlntQf7rPpU_1zWo_qXd4hP",
                        208.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(9))),
                new Dish("Cremoso de Vainilla", "Cremoso de Vainilla (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1lMkQy2kBknZ88OVJXXOkzqseuQrSjHul",
                        209.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(9))),
                new Dish("Cremoso de Uva", "Cremoso de Uva (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1pkUTHV5Cqhj8lvxjul-QiDMlVWs8h0k0",
                        210.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(9))),
                new Dish("Cremoso de 1", "Cremoso de 1 (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1c2SXAKr1IwlntQf7rPpU_1zWo_qXd4hP",
                        211.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(9))),
                new Dish("Cremoso de 2", "Cremoso de 2 (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1pkUTHV5Cqhj8lvxjul-QiDMlVWs8h0k0",
                        212.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(9))),
                new Dish("Cremoso de 3", "Cremoso de 3 (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1c2SXAKr1IwlntQf7rPpU_1zWo_qXd4hP",
                        213.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(9))),
                new Dish("Pastel de Fresa", "Pastel de Fresa (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1pkUTHV5Cqhj8lvxjul-QiDMlVWs8h0k0",
                        214.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(10))),
                new Dish("Pastel de Chocolate", "Pastel de Chocolate (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1c2SXAKr1IwlntQf7rPpU_1zWo_qXd4hP",
                        215.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(10))),
                new Dish("Pastel de Vainilla", "Pastel de Vainilla (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1lMkQy2kBknZ88OVJXXOkzqseuQrSjHul",
                        216.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(10))),
                new Dish("Pastel de 1", "Pastel de 1 (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1pkUTHV5Cqhj8lvxjul-QiDMlVWs8h0k0",
                        217.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(10))),
                new Dish("Pastel de 2", "Pastel de 2 (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1c2SXAKr1IwlntQf7rPpU_1zWo_qXd4hP",
                        218.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(10))),
                new Dish("Pastel de 3", "Pastel de 3 (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1pkUTHV5Cqhj8lvxjul-QiDMlVWs8h0k0",
                        219.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(10))),
                new Dish("Pastel de 4", "Pastel de 4 (Postre - con 1 categoria) texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible  texto muy muy largo y dresctiptivo para ocupar el mayor espacio disponible",
                        "1c2SXAKr1IwlntQf7rPpU_1zWo_qXd4hP",
                        220.5F, Dish.STATUS.ACTIVE, courseTypes.get(3), Set.of(categories.get(10)))
        );
        dishRepository.saveAll(dishes);
    }
}