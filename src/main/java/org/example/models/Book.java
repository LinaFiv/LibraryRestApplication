package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private int id;
    private String title;
    private int year;
    private Person owner;
    private List<Author> authors = new ArrayList<>();

    public Book() {

    }

    public Book(String title, int year) {
        this.title = title;
        this.year = year;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
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

    public Person getOwner() {
        return owner;
    }
    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public List<Author> getAuthors() {
        return authors;
    }
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
