package com.vlsu.inventory.util.mapping;

import com.vlsu.inventory.dto.model.RoleDto;
import com.vlsu.inventory.model.Role;

public class RoleMappingUtils {
    public static Role fromDto(RoleDto.Response.Default dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }

    public static RoleDto.Response.Default toDto(Role role) {
        return new RoleDto.Response.Default(role.getId(), role.getName());
    }

}
