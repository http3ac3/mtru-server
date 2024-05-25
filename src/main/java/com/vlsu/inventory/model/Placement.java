package com.vlsu.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "placement")
@NamedEntityGraph(
        name = "Placement.equipment.rents",
        attributeNodes = {
                @NamedAttributeNode("equipment"),
                @NamedAttributeNode("rents")
        }
)
public class Placement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "placement")
    @JsonIgnore
    private List<Equipment> equipment;

    @OneToMany(mappedBy = "placement", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Rent> rents;
}
