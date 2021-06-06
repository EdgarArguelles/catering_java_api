package com.catering.models;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@ToString(callSuper = true, of = "name")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "course_type")
@GraphQLType(description = "Course Type which a dish can belong to")
public class CourseType extends Model {

    public interface STATUS {
        Integer INACTIVE = 0;
        Integer ACTIVE = 1;
    }

    @Column(nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(nullable = false)
    @Getter
    @Setter
    private String picture;

    @Column(nullable = false, columnDefinition = "smallint")
    @Getter
    @Setter
    private Integer position;

    @Column(nullable = false, columnDefinition = "smallint")
    @Getter
    @Setter
    private Integer status;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @Getter
    @Setter
    @GraphQLIgnore
    private List<Course> courses;

    @OneToMany(mappedBy = "courseType", fetch = FetchType.LAZY)
    @Getter
    @Setter
    @GraphQLIgnore
    private List<Dish> allowedDishes;

    public CourseType(String id) {
        this.id = id;
    }

    public CourseType(String name, String picture, Integer position, Integer status) {
        this.name = name;
        this.picture = picture;
        this.position = position;
        this.status = status;
    }
}