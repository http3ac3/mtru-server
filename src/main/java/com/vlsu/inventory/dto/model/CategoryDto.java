package com.vlsu.inventory.dto.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum CategoryDto {;
    interface Id { Long getId(); }
    interface Name { String getName(); }

    public enum Request {;
        @Getter @Setter @NoArgsConstructor
        public static class Create implements Name {
            String name;
        }

        @Getter @Setter @NoArgsConstructor @AllArgsConstructor
        public static class Update implements Id, Name {
            Long id;
            String name;
        }
    }

    public enum Response {;
        @Getter @Setter @NoArgsConstructor @AllArgsConstructor
        public static class Default implements Id, Name {
            Long id;
            String name;
        }
    }
}
