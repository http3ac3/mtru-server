package com.vlsu.inventory.dto.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum PlacementDto {;
    interface Id { Long getId(); }
    interface Name { String getName(); }

    public enum Request {;
        @Getter @Setter @AllArgsConstructor
        public static class Create implements Name {
            String name;
        }

        @Getter @Setter @AllArgsConstructor
        public static class Update implements Id, Name {
            Long id;
            String name;
        }
    }

    public enum Response {;
        @Getter @Setter @AllArgsConstructor
        public static class Default implements Id, Name {
            Long id;
            String name;
        }
    }
}
