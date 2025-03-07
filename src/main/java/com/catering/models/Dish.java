package com.catering.models;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@ToString(callSuper = true, of = "name")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dish")
@GraphQLType(description = "Dish's information, a dish can belong to one or more courses into a menu")
public class Dish extends Model {

    public interface STATUS {
        Integer INACTIVE = 0;
        Integer ACTIVE = 1;
    }

    @Column(nullable = false)
    @Getter
    @Setter
    @GraphQLQuery(description = "Dish's name")
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    @Getter
    @Setter
    @GraphQLQuery(description = "Dish's description")
    private String description;

    @Column(nullable = false)
    @Getter
    @Setter
    @GraphQLQuery(description = "Dish's picture path")
    private String picture;

    @Column(nullable = false)
    @Getter
    @Setter
    @GraphQLQuery(description = "Dish's price")
    private Float price;

    @Column(nullable = false, columnDefinition = "smallint")
    @Getter
    @Setter
    @GraphQLQuery(description = "Dish's status [0 or 1]")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_type_id", nullable = false)
    @DBRef // all foreign keys need @DBRef to notify Mongo about relationship and ownership
    @Getter
    @Setter
    @GraphQLIgnore
    private CourseType courseType;

    // in @ManyToMany the Owner Entity must use Set to notify MySQL that new
    // relational table will have a combine Primary Key
    // if List is used instead the new relational table won't have a combine Primary
    // key so data could be duplicated
    @ManyToMany(fetch = FetchType.LAZY)
    @DBRef // all foreign keys need @DBRef to notify Mongo about relationship and ownership
    @Getter
    @Setter
    @GraphQLQuery(description = "Dish's categories")
    private Set<Category> categories;

    @ManyToMany(mappedBy = "dishes", fetch = FetchType.LAZY)
    @Getter
    @Setter
    @GraphQLIgnore
    private List<Course> courses;

    public Dish(String id) {
        this.id = id;
    }

    public Dish(String name, String description, String picture, Float price, Integer status, CourseType courseType,
            Set<Category> categories) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.price = price;
        this.status = status;
        this.courseType = courseType;
        this.categories = categories;
    }
}