package org.example;

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

        books.add(book1);
        books.add(book2);


        authorDAO.saveAuthor(author1,books);

        books.clear();
        books.add(book3);

        authorDAO.saveAuthor(author2,books);

        System.out.println("=============================");

        System.out.println(authorDAO.findAuthorByName("Kowalski"));

       System.out.println(authorDAO.findAuthorByName("Nowak"));

        authorDAO.changeAuthorAge("Kowalski", 29);
        authorDAO.changeAuthorAge("Nowak", 45);

        System.out.println(authorDAO.findAuthorByName("Kowalski"));
        System.out.println(authorDAO.findAuthorByName("Nowak"));
        System.out.println("=============================");

        System.out.println(authorDAO.getBooksOfAuthor("Kowalski"));

       // authorDAO.deleteBooksOfAuthor("Kowalski");
       authorDAO.deleteAuthor("Kowalski");

    }
}