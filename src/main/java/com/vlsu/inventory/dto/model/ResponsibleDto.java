package com.vlsu.inventory.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ResponsibleDto {;
    interface Id { Long getId(); }
    interface FirstName { String getFirstName(); }
    interface LastName { String getLastName(); }
    interface Patronymic { String getPatronymic(); }
    interface Position { String getPosition(); }
    interface PhoneNumber { String getPhoneNumber(); }
    interface IsFinanciallyResponsible { boolean isFinanciallyResponsible(); }
    interface Department { DepartmentDto.Response.Default getDepartment(); }

    public enum Request {;
        @Getter @Builder
        public static class Create implements
                FirstName, LastName, Patronymic, Position, PhoneNumber, IsFinanciallyResponsible, Department {
            String lastName;
            String firstName;
            String patronymic;
            String position;
            String phoneNumber;
            @JsonProperty(value = "financiallyResponsible")
            boolean isFinanciallyResponsible;
            DepartmentDto.Response.Default department;
        }

        @Getter @Builder
        public static class Update implements
                Id, FirstName, LastName, Patronymic, Position, PhoneNumber, IsFinanciallyResponsible, Department {
            Long id;
            String lastName;
            String firstName;
            String patronymic;
            String position;
            String phoneNumber;
            @JsonProperty(value = "financiallyResponsible")
            boolean isFinanciallyResponsible;
            DepartmentDto.Response.Default department;
        }
    }

    public enum Response {;
        @Getter @Builder
        public static class Default implements
                Id, FirstName, LastName, Patronymic, Position, PhoneNumber, IsFinanciallyResponsible, Department {
            Long id;
            String lastName;
            String firstName;
            String patronymic;
            String position;
            String phoneNumber;
            boolean isFinanciallyResponsible;
            DepartmentDto.Response.Default department;
        }

        @Getter @Builder
        public static class WithoutDepartment implements
                Id, FirstName, LastName, Patronymic, Position, PhoneNumber, IsFinanciallyResponsible {
            Long id;
            String lastName;
            String firstName;
            String patronymic;
            String position;
            String phoneNumber;
            boolean isFinanciallyResponsible;
        }
    }
}
