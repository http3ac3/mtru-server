package com.vlsu.inventory.dto.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum UserDto {;
    interface Id { Long getId(); }
    interface Username { String getUsername(); }
    interface Password { String getPassword(); }
    interface Responsible { ResponsibleDto.Response.WithoutDepartment getResponsible(); }
    interface Roles { List<RoleDto.Response.Default> getRoles(); }
    public enum Response {;
        @AllArgsConstructor @Getter
        public static class Public implements Id, Username, Responsible, Roles {
            Long id;
            String username;
            ResponsibleDto.Response.WithoutDepartment responsible;
            List<RoleDto.Response.Default> roles;
        }

        @AllArgsConstructor @Getter
        public static class WithoutResponsible implements Id, Username, Roles {
            Long id;
            String username;
            List<RoleDto.Response.Default> roles;
        }
    }
}
