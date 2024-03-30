package com.vlsu.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "placement")
public class Placement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    public Placement() {}

    public Placement(String name) {
        this.name = name;
    }
}
