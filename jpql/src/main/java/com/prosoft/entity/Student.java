package com.prosoft.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "student_name", nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "student")
    private List<Contact> contacts;

    @ManyToMany(mappedBy = "students")
    private List<Course> courses;
}
