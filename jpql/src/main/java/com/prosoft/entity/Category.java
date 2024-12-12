package com.prosoft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@NamedEntityGraph(
        name = "category-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("courseSet")
        }
)
@Getter
@Setter
public class Category {

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID) <- @GeneratedValue не использовать!
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    // @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "category")
    // @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private Set<Course> courseSet;

    public Category(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
