package com.catering.models;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, of = "type")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "course")
@GraphQLType(description = "Course which is part of a menu")
public class Course extends Model {

    @NotNull
    @Min(1)
    @Column(nullable = false, columnDefinition = "smallint")
    @Getter
    @Setter
    @GraphQLQuery(description = "Course's position > 0")
    private Integer position;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_type_id", nullable = false)
    @DBRef // all foreign keys need @DBRef to notify Mongo about relationship and ownership
    @Getter
    @Setter
    @GraphQLQuery(description = "Course's type")
    private CourseType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @DBRef // all foreign keys need @DBRef to notify Mongo about relationship and ownership
    @Getter
    @Setter
    @GraphQLIgnore
    private Menu menu;

    // in @ManyToMany the Owner Entity must use Set to notify MySQL that new
    // relational table will have a combine Primary Key
    // if List is used instead the new relational table won't have a combine Primary
    // key so data could be duplicated
    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)
    @DBRef // all foreign keys need @DBRef to notify Mongo about relationship and ownership
    @Getter
    @Setter
    @GraphQLQuery(description = "Course's dishes")
    private Set<Dish> dishes;

    public Course(String id) {
        this.id = id;
    }
}