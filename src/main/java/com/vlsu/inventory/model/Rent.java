package com.vlsu.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rent")
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

    @ManyToOne()
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipment equipment;

    @ManyToOne()
    @JoinColumn(name = "responsible_id", referencedColumnName = "id")
    private Responsible responsible;

    @ManyToOne()
    @JoinColumn(name = "placement_id", referencedColumnName = "id")
    private Placement placement;

    public Rent() {};

    public Rent(LocalDateTime createDate, LocalDateTime endDate) {
        this.createDateTime = createDate;
        this.endDateTime = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDate) {
        this.createDateTime = createDate;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDate) {
        this.endDateTime = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Responsible getResponsible() {
        return responsible;
    }

    public void setResponsible(Responsible responsible) {
        this.responsible = responsible;
    }

    public Placement getPlacement() {
        return placement;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", createDate=" + createDateTime +
                ", endDate=" + endDateTime +
                ", description=" + description +
                '}';
    }
}
