package org.example.entity;

import jakarta.persistence.*;
import org.example.entitySupport.Genre;

@Entity
public class Book implements Comparable<Book> {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private Integer numberOfPages;
    @ManyToOne
    private Author author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isValid() {
        if (title == null || title.isEmpty()) {
            System.out.println("Validation failed: title is null or empty.");
            return false;
        }
        if (genre == null) {
            System.out.println("Validation failed: genre is null.");
            return false;
        }
        if (numberOfPages <= 0) {
            System.out.println("Validation failed: numberOfPages is less than or equal to zero.");
            return false;
        }
        if (author == null || author.getName() == null || author.getName().isEmpty()) {
            System.out.println("Validation failed: author is null or author's name is null or empty.");
            return false;
        }
        return true;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;

        return getTitle().equals(book.getTitle()) &&
                        getGenre() == book.getGenre() &&
                        getNumberOfPages().equals(book.getNumberOfPages()) &&
                        getAuthor().equals(book.getAuthor());
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(getId());
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getGenre().hashCode();
        result = 31 * result + getNumberOfPages().hashCode();
        result = 31 * result + getAuthor().hashCode();
        return result;
    }
}