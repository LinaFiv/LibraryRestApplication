package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private int id;
    private String fullName;
    private List<Book> books = new ArrayList<>();

    public Author() {

    }

    public Author(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
