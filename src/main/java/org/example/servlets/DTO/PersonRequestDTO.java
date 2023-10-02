package org.example.servlets.DTO;

import java.util.Objects;

public class PersonRequestDTO {

    public PersonRequestDTO() {
    }

    public PersonRequestDTO(String fullName, int yearOfBirth) {
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
    }

    private String fullName;
    private int yearOfBirth;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonRequestDTO that = (PersonRequestDTO) o;
        return yearOfBirth == that.yearOfBirth && Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, yearOfBirth);
    }
}
