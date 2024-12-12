package com.prosoft.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID) <- @GeneratedValue не использовать!
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Course> courseSet;

    public Category(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }
}
