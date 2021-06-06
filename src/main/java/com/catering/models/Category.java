package com.catering.models;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@ToString(callSuper = true, of = "name")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category")
@GraphQLType(description = "Category which a dish can belong to")
public class Category extends Model {

    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    @GraphQLQuery(description = "Category's name")
    private String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    @Getter
    @Setter
    @GraphQLIgnore
    private List<Dish> dishes;

    public Category(String name) {
        this.name = name;
    }
}