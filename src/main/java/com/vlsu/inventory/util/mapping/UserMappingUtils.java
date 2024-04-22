package com.vlsu.inventory.util.mapping;

import com.vlsu.inventory.dto.model.UserDto;
import com.vlsu.inventory.model.Role;
import com.vlsu.inventory.model.User;

import java.util.List;

public class UserMappingUtils {
    public static User fromDto(UserDto.Response.Public dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setResponsible(ResponsibleMappingUtils.fromDto(dto.getResponsible()));

        List<Role> userRoles = dto.getRoles().stream().map(RoleMappingUtils::fromDto).toList();
        user.setRoles(userRoles);
        return user;
    }

    public static User fromDtoWithoutResponsible(UserDto.Response.WithoutResponsible dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        List<Role> userRoles = dto.getRoles().stream().map(RoleMappingUtils::fromDto).toList();
        user.setRoles(userRoles);
        return user;
    }

    public static UserDto.Response.WithoutResponsible toDtoWithoutResponsible(User user) {
        return new UserDto.Response.WithoutResponsible(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream().map(RoleMappingUtils::toDto).toList()
        );
    }
}
