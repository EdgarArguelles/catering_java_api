package com.catering.models;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@ToString(callSuper = true, of = "name")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "menu")
@GraphQLType(description = "Menu's information")
public class Menu extends Model {

    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    @Getter
    @Setter
    @GraphQLQuery(description = "Menu's name")
    private String name;

    @NotNull
    @Min(1)
    @Column(nullable = false, columnDefinition = "smallint")
    @Getter
    @Setter
    @GraphQLQuery(description = "Menu's quantity > 0")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", nullable = false)
    @DBRef // all foreign keys need @DBRef to notify Mongo about relationship and ownership
    @Getter
    @Setter
    @GraphQLIgnore
    private Quotation quotation;

    // this entity doesn't have the ownership
    @NotEmpty
    @Valid
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    @Getter
    @Setter
    private List<Course> courses;

    public Menu(String id) {
        this.id = id;
    }

    public Menu(String name, Integer quantity, Quotation quotation) {
        this.name = name;
        this.quantity = quantity;
        this.quotation = quotation;
    }
}