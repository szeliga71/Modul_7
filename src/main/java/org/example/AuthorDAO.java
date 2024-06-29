package org.example;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AuthorDAO {

    private final SessionFactory sessionFactory = AuthorSessionFactory.getAuthorSessionFactory();

    public void saveAuthor(Author author, List<Book> books) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(author);
        transaction.commit();
        session.close();
        saveBook(books, author.getName());
    }

    public void saveBook(List<Book> books, String authorName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Author author = findAuthorByName(authorName);
        for (Book book : books) {
            book.setAuthor(author);
        }
        books.forEach(session::merge);
        transaction.commit();
        session.close();
    }

    public Author findAuthorByName(String name) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Author> authorQuery = criteriaBuilder.createQuery(Author.class);
        Root<Author> root = authorQuery.from(Author.class);
        authorQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
        Author author = session.createQuery(authorQuery).getSingleResult();
        session.close();
        return author;
    }

    public void changeAuthorAge(String authorName, int age) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Author author = findAuthorByName(authorName);
        author.setAge(age);
        session.merge(author);// dopisanie
        transaction.commit();
        session.close();
    }

    public void deleteAuthor(String authorName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        deleteBooksOfAuthor(authorName);
        Author author = findAuthorByName(authorName);
        session.remove(author);
        transaction.commit();
        session.close();
    }

    public void deleteBook(String title) {

        //  zabezpieczyc jezeli sa dwa takie same tytuly
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //deleteBooksOfAuthor(authorName);
        //Author author=findAuthorByName(authorName);
        Book book = findBookByTitle(title);
        session.remove(book);
        transaction.commit();
        session.close();
    }

    private Book findBookByTitle(String title) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> bookQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = bookQuery.from(Book.class);
        bookQuery.select(root).where(criteriaBuilder.equal(root.get("title"), title));
        Book book = session.createQuery(bookQuery).getSingleResult();
        session.close();
        return book;
    }

    public List<Book> getBooksOfAuthor(String authorName) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> bookQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = bookQuery.from(Book.class);
        bookQuery.select(root).where(criteriaBuilder.equal(root.get("author").get("name"), authorName));
        List<Book> books = session.createQuery(bookQuery).getResultList();
        books.sort(Comparator.comparing(Book::getNumberOfPages));
        return books;
    }

    public void deleteBooksOfAuthor(String authorName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        //Author author=findAuthorByName(authorName);
        // Znalezienie i usunięcie książek tego autora
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("author").get("name"), authorName));
        List<Book> books = session.createQuery(criteriaQuery).getResultList();
        for (Book book : books) {
            session.remove(book);
        }
        transaction.commit();
        session.close();
    }

    public List<Author> getAllAuthors() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Author> authorQuery = criteriaBuilder.createQuery(Author.class);
        Root<Author> root = authorQuery.from(Author.class);
        authorQuery.select(root);
        List<Author> authors = session.createQuery(authorQuery).getResultList();
        session.close();
        return authors;
    }

    public List<Book> getAllBooks() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> bookQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = bookQuery.from(Book.class);
        bookQuery.select(root);
        List<Book> books = session.createQuery(bookQuery).getResultList();
        session.close();
        return books;
    }

    public void addBookToAuthor(Book book, String authorName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Author author = findAuthorByName(authorName);
        book.setAuthor(author);
        session.merge(book);
        transaction.commit();
        session.close();
    }

    public void addAuthor(Author author) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(author);
        transaction.commit();
        session.close();
    }


    public/* List<Author>*/void  getAllBooksAndAuthors() {

        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Author> authorQuery = criteriaBuilder.createQuery(Author.class);
        Root<Author> root = authorQuery.from(Author.class);

        root.fetch("books", JoinType.LEFT);
        authorQuery.select(root).distinct(true);
        List<Author> authors = session.createQuery(authorQuery).getResultList();

        for (Author author:authors){
            System.out.println(author);
            System.out.println(author.getBooks());
        }
        session.close();
        //return authors;
    }


}
