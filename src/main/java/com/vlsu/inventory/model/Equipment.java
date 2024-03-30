package com.vlsu.inventory.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "image_data", nullable = false)
    private byte[] imageData;

    @Column(name = "description")
    private String description;

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

    @OneToMany(mappedBy = "equipment")
    private List<Rent> rents;

    public Equipment() {}

    public Equipment(String inventoryNumber, String name, byte[] imageData,
                     String description, LocalDate commissioningDate, String commissioningActNumber,
                     LocalDate decommissioningDate, String decommissioningActNumber) {
        this.inventoryNumber = inventoryNumber;
        this.name = name;
        this.imageData = imageData;
        this.description = description;
        this.commissioningDate = commissioningDate;
        this.commissioningActNumber = commissioningActNumber;
        this.decommissioningDate = decommissioningDate;
        this.decommissioningActNumber = decommissioningActNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCommissioningDate() {
        return commissioningDate;
    }

    public void setCommissioningDate(LocalDate commissioningDate) {
        this.commissioningDate = commissioningDate;
    }

    public String getCommissioningActNumber() {
        return commissioningActNumber;
    }

    public void setCommissioningActNumber(String commissioningActNumber) {
        this.commissioningActNumber = commissioningActNumber;
    }

    public LocalDate getDecommissioningDate() {
        return decommissioningDate;
    }

    public void setDecommissioningDate(LocalDate decommissioningDate) {
        this.decommissioningDate = decommissioningDate;
    }

    public String getDecommissioningActNumber() {
        return decommissioningActNumber;
    }

    public void setDecommissioningActNumber(String decommissioningActNumber) {
        this.decommissioningActNumber = decommissioningActNumber;
    }

    public Responsible getResponsible() {
        return responsible;
    }

    public void setResponsible(Responsible responsible) {
        this.responsible = responsible;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public Placement getPlacement() {
        return placement;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public void setRents(List<Rent> rents) {
        this.rents = rents;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", inventoryNumber='" + inventoryNumber + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", commissioningDate=" + commissioningDate +
                ", commissioningActNumber='" + commissioningActNumber + '\'' +
                ", decommissioningDate=" + decommissioningDate +
                ", decommissioningActNumber='" + decommissioningActNumber + '\'' +
                '}';
    }
}
