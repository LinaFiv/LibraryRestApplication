package org.example.servlets.DTO;

import java.util.Objects;

public class AuthorRequestDTO {
    private String fullName;

    public AuthorRequestDTO() {
    }

    public AuthorRequestDTO(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorRequestDTO that = (AuthorRequestDTO) o;
        return Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName);
    }
}
