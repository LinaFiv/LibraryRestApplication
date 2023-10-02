package org.example.servlets.DTO;

import java.util.Objects;
import java.util.UUID;

public class BookResponseDTO {

    private UUID id;
    private String title;
    private int year;

    public BookResponseDTO() {
    }

    public BookResponseDTO(UUID id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        BookResponseDTO that = (BookResponseDTO) o;
        return year == that.year && Objects.equals(id, that.id) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, year);
    }
}
