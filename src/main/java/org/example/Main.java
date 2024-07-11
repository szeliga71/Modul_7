package org.example;

import org.example.dao.AuthorDAO;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Genre;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        AuthorDAO authorDAO = new AuthorDAO();

        Author author1 = new Author();
        author1.setName("Kowalski");
        author1.setAge(33);
        author1.setFavouriteGenre(Genre.ADVENTURE);

        Author author2 = new Author();
        author2.setName("Nowak");
        author2.setAge(22);
        author2.setFavouriteGenre(Genre.THRILLER);

        Author author3 = new Author();
        author3.setName("Malinowski");
        author3.setAge(21);
        author3.setFavouriteGenre(Genre.ADVENTURE);

        List<Book> books = new ArrayList<>();

        Book book1 = new Book();
        book1.setAuthor(author1);
        book1.setTitle("Java");
        book1.setGenre(Genre.HORROR);
        book1.setNumberOfPages(254);

        Book book2 = new Book();
        book2.setAuthor(author1);
        book2.setTitle("Python");
        book2.setGenre(Genre.BIOGRAPHY);
        book2.setNumberOfPages(789);

        Book book3 = new Book();
        book3.setAuthor(author2);
        book3.setTitle("SQL");
        book3.setGenre(Genre.COMEDY);
        book3.setNumberOfPages(147);

        Book book4 = new Book();
        book4.setAuthor(author1);
        book4.setTitle("C++");
        book4.setGenre(Genre.CRIME);
        book4.setNumberOfPages(664);

//================================================================
        books.add(book1);
        books.add(book2);
        authorDAO.saveAuthor(author1, books);
        books.clear();
        books.add(book3);
        authorDAO.saveAuthor(author2, books);
        books.clear();
        authorDAO.addAuthor(author3);
        authorDAO.addBookToAuthor(book4, "Malinowski");
        //authorDAO.addBookToAuthor(book4,"Kowalski");
        authorDAO.getAllBooksAndAuthors();




    }
}