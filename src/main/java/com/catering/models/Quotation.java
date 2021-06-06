package com.catering.models;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@ToString(callSuper = true, of = "name")
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "quotation")
@GraphQLType(description = "Quotation's information")
public class Quotation extends Model {

    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    @Getter
    @Setter
    @GraphQLQuery(description = "Quotation's name")
    private String name;

    @Column(nullable = false)
    @Getter
    @Setter
    @GraphQLQuery(description = "Quotation's price")
    private Float price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    @DBRef // all foreign keys need @DBRef to notify Mongo about relationship and ownership
    @Getter
    @Setter
    @GraphQLIgnore
    private Person person;

    // this entity doesn't have the ownership
    @Valid
    @OneToMany(mappedBy = "quotation", fetch = FetchType.LAZY)
    @Getter
    @Setter
    private List<Menu> menus;

    public Quotation(String id) {
        this.id = id;
    }

    public Quotation(String name, Float price, Person person) {
        this.name = name;
        this.price = price;
        this.person = person;
    }
}