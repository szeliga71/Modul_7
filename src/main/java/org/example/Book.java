package org.example;

import jakarta.persistence.*;

@Entity
public class Book implements Comparable<Book> {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private Integer numberOfPages;
    @ManyToOne
    private Author author;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
    @Override
    public int compareTo(Book other) {
        if (this.numberOfPages == null || other.numberOfPages == null) {
            throw new NullPointerException("NumberOfPages cannot be null for comparison.");
        }
        return this.numberOfPages.compareTo(other.numberOfPages);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", author=" + author.getName() +
                '}';
    }
}
