package com.vlsu.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "rent")
@NamedEntityGraph(name = "Rent.equipment.placement.responsible",
    attributeNodes = {
        @NamedAttributeNode(value = "placement"),
            @NamedAttributeNode(value = "equipment"),
            @NamedAttributeNode(value = "responsible", subgraph = "responsible.user")
    },
        subgraphs = {
            @NamedSubgraph(name = "responsible.user", attributeNodes = @NamedAttributeNode("user"))
        }
)
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "create_datetime", nullable = false)
    private LocalDateTime createDateTime;

    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_id", referencedColumnName = "id")
    private Responsible responsible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placement_id", referencedColumnName = "id")
    private Placement placement;
}
