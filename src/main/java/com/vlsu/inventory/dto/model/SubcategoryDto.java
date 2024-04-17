package com.vlsu.inventory.dto.model;


import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum SubcategoryDto {;
    interface Id { Long getId(); }
    interface Name { String getName(); }
    interface Category { CategoryDto.Response.Default getCategory(); }

    public enum Request {;
        @Getter @Setter @AllArgsConstructor
        public static class Create implements Name, Category {
            String name;
            CategoryDto.Response.Default category;
        }

        @Getter @Setter @AllArgsConstructor
        public static class Update implements Id, Name, Category {
            Long id;
            String name;
            CategoryDto.Response.Default category;
        }
    }

    public enum Response {;
        @Getter @Setter @AllArgsConstructor
        public static class Default implements Id, Name, Category {
            Long id;
            String name;
            CategoryDto.Response.Default category;
        }

        @Getter @Setter @AllArgsConstructor
        public static class WithoutCategory implements Id, Name {
            Long id;
            String name;
        }
    }

}
