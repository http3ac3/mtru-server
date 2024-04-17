package com.vlsu.inventory.dto.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum UserDto {;
    interface Id { Long getId(); }
    interface Username { String getUsername(); }
    interface Password { String getPassword(); }


}
