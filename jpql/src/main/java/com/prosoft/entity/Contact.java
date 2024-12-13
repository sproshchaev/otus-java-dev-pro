package com.prosoft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "contacts",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"type", "student_id"})})
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Contact {

    @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "value", nullable = false, length = 70)
    private String value;

    @Column(name = "type", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private ContactType type;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

}
