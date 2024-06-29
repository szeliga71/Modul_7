package org.example;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="Author_of_Book")
public class Author {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private Genre favouriteGenre;

    @OneToMany(mappedBy = "author")
    private List<Book> books=new ArrayList<>();

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
    private String showBooksOfAuthor(List<Book>books){
        if(books==null|| books.isEmpty()){
            return "No Books Found";
        }else{
            StringBuilder sb=new StringBuilder();
            for(Book book:books){
                sb.append(book.getTitle()).append(book.getNumberOfPages()).append('\n');
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
              //" books=" + books +
                '}';
    }
}
