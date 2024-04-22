package com.vlsu.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "inventory_number", nullable = false, unique = true)
    private String inventoryNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_data")
    private String imageData;

    @Column(name = "description")
    private String description;
    @Column(name = "initial_cost")
    private BigDecimal initialCost;

    @Column(name = "commissioning_date", nullable = false)
    private LocalDate commissioningDate;

    @Column(name = "commissioning_act_number", nullable = false)
    private String commissioningActNumber;

    @Column(name = "decommissioning_date")
    private LocalDate decommissioningDate;

    @Column(name = "decommissioning_act_number")
    private String decommissioningActNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_id", referencedColumnName = "id")
    private Responsible responsible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", referencedColumnName = "id")
    private Subcategory subcategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placement_id", referencedColumnName = "id")
    private Placement placement;

    @OneToMany(mappedBy = "equipment", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Rent> rents = new ArrayList<>();

    public Equipment(String inventoryNumber, String name, String imageData,
                     String description, BigDecimal initialCost, LocalDate commissioningDate, String commissioningActNumber,
                     LocalDate decommissioningDate, String decommissioningActNumber) {
        this.inventoryNumber = inventoryNumber;
        this.name = name;
        this.imageData = imageData;
        this.description = description;
        this.initialCost = initialCost;
        this.commissioningDate = commissioningDate;
        this.commissioningActNumber = commissioningActNumber;
        this.decommissioningDate = decommissioningDate;
        this.decommissioningActNumber = decommissioningActNumber;
    }

    public boolean isAlreadyRented() {
        return rents.stream().anyMatch(r -> r.getEndDateTime() == null);
    }
}
