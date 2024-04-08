package com.vlsu.inventory.dto;

import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.model.Role;

import java.util.List;

public class RegisterRequest {
    private String username;
    private String password;
    private List<Role> roles;
    private Responsible responsible;

    public RegisterRequest(String username, String password, List<Role> roles, Responsible responsible) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.responsible = responsible;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Responsible getResponsible() {
        return responsible;
    }

    public void setResponsible(Responsible responsible) {
        this.responsible = responsible;
    }
}
