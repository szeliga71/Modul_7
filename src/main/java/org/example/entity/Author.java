package org.example.entity;

import jakarta.persistence.*;
import org.example.entitySupport.Genre;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Author_of_Book")
public class Author {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private Genre favouriteGenre;

    @OneToMany(mappedBy = "author")//,fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public Author() {
    }

    public Author(long id, String name, Integer age, Genre favouriteGenre) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.favouriteGenre = favouriteGenre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Genre getFavouriteGenre() {
        return favouriteGenre;
    }

    public void setFavouriteGenre(Genre favouriteGenre) {
        this.favouriteGenre = favouriteGenre;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    private String showBooksOfAuthor(List<Book> books) {
        if (books == null || books.isEmpty()) {
            return "No Books Found";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Book book : books) {
                sb.append(book.getTitle()).append(" ").append(book.getNumberOfPages()).append(" ").append('\n');
            }
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", favouriteGenre='" + favouriteGenre + '\'' +
                '}';
    }

    public boolean isValid() {
        if (name == null || name.isEmpty()) {
            System.out.println("Validation failed: name is null or empty.");
            return false;
        }
        if (favouriteGenre == null) {
            System.out.println("Validation failed: favorite genre is null.");
            return false;
        }
        if (age <= 0) {
            System.out.println("Validation failed: age is less than or equal to zero.");
            return false;
        }

        return true;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author author)) return false;

        return getName().equals(author.getName()) && getAge().equals(author.getAge()) && getFavouriteGenre() == author.getFavouriteGenre();
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(getId());
        result = 31 * result + getName().hashCode();
        result = 31 * result + getAge().hashCode();
        result = 31 * result + getFavouriteGenre().hashCode();
        return result;
    }
}