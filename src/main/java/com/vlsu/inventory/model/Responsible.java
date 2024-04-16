package com.vlsu.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "responsible")
public class Responsible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "phone_number", length = 11, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "is_financially_responsible", nullable = false)
    private boolean isFinanciallyResponsible;

    @ManyToOne()
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @OneToMany(mappedBy = "responsible")
    @JsonIgnore
    private List<Equipment> equipment;

    @OneToMany(mappedBy = "responsible")
    @JsonIgnore
    private List<Rent> rents;

    @OneToOne(mappedBy = "responsible", fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    public Responsible(String firstName, String lastName, String patronymic, String position, String phoneNumber, boolean isFinanciallyResponsible) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.isFinanciallyResponsible = isFinanciallyResponsible;
    }
}
