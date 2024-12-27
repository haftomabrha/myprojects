package com.muluneh.MUCSchool.dto;

public enum Role {
    ADMIN,
    STUDENT,
    TEACHER,
    REGISTRAR,
    PARENT;

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No role found for: " + role);
    }
}
