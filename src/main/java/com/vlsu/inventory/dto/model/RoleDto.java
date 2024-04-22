package com.vlsu.inventory.dto.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum RoleDto {;
    interface Id { Long getId(); }
    interface Name { String getName(); }

    public enum Response {;
        @AllArgsConstructor @Getter
        public static class Default implements Id, Name {
            Long id;
            String name;
        }
    }
}
