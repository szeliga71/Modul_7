package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        System.out.println(books);

        authorDAO.saveAuthor(author2,books);

        System.out.println("=============================");

        System.out.println(authorDAO.findAuthorByName("Kowalski"));

       System.out.println(authorDAO.findAuthorByName("Nowak"));

        authorDAO.changeAuthorAge("Kowalski", 29);
        authorDAO.changeAuthorAge("Nowak", 45);

        System.out.println(authorDAO.findAuthorByName("Kowalski"));
        System.out.println(authorDAO.findAuthorByName("Nowak"));
        System.out.println("=============================");

        //System.out.println(authorDAO.getBooksOfAuthor("Kowalski"));
        //System.out.println(authorDAO.getAllAuthors());
        //System.out.println(authorDAO.getAllBooks());


        Book book4 = new Book();
        book4.setAuthor(author1);
        book4.setTitle("C++");
        book4.setGenre(Genre.CRIME);
        book4.setNumberOfPages(664);

        authorDAO.addBookToAuthor(book4,"Kowalski");

        Author author3 = new Author();
        author3.setName("Malinowski");
        author3.setAge(21);
        author3.setFavouriteGenre(Genre.ADVENTURE);

        authorDAO.addAuthor(author3);
        authorDAO.addBookToAuthor(book4,"Malinowski");


       // authorDAO.deleteAuthor("Malinowski");
        //authorDAO.deleteBook("C++");

       // System.out.println(authorDAO.getAllBooksAndAuthors().get(2).getBooks());

        //authorDAO.getAllBooksAndAuthors();

        //authorDAO.getAllBooksAndAuthors().stream().forEach(System.out::println);

        //authorDAO.getAllBooks().stream().sorted(Comparator.comparing(Book::getNumberOfPages)).forEach(book->System.out.println(book));

        List<Book> books2 = authorDAO.getAllBooks();
        Collections.sort(books2);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println(books2);
    }
}