package org.example.servlets.DTO;

import java.util.Objects;

public class BookRequestDTO {

    private String title;
    private int year;

    public BookRequestDTO() {
    }

    public BookRequestDTO(String title, int year) {
        this.title = title;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookRequestDTO that = (BookRequestDTO) o;
        return year == that.year && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }
}
