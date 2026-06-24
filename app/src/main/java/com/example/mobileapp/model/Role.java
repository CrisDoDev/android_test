package com.example.mobileapp.model;

import com.google.gson.annotations.SerializedName;

public class Role {
    private int idRole;
    private String roleName;
    private String description;

    public Role() {}

    public Role(int idRole, String roleName, String description) {
        this.idRole = idRole;
        this.roleName = roleName;
        this.description = description;
    }

    public int getIdRole() { return idRole; }
    public void setIdRole(int idRole) { this.idRole = idRole; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}