package org.example;

import org.example.dao.AuthorDAO;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entitySupport.Genre;
import org.example.sessions.AuthorSessionFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        AuthorDAO authorDAO = new AuthorDAO(AuthorSessionFactory.getAuthorSessionFactory());

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
        author3.setFavouriteGenre(Genre.FANTASY);

        Author author4 = new Author();
        author4.setName("Kowalski");
        author4.setAge(40);
        author4.setFavouriteGenre(Genre.ADVENTURE);


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


        authorDAO.addAuthor(author1);


        authorDAO.addBookToAuthor(book1,"Kowalski");
        authorDAO.addBookToAuthor(book2,"Kowalski");

        authorDAO.addAuthor(author2);
        authorDAO.deleteBooksOfAuthor("Kowalski");
        authorDAO.addBookToAuthor(book4,"Kowalski");
        authorDAO.addBookToAuthor(book3,"Nowak");

        System.out.println(authorDAO.getAllBooks());

        System.out.println(authorDAO.getBooksOfAuthor("Nowak"));

        authorDAO.deleteBooksOfAuthor("Nowak");
        System.out.println(authorDAO.getBooksOfAuthor("Nowak"));





    }
}