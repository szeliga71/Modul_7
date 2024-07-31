package org.example.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.example.entity.Author;
import org.example.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Comparator;
import java.util.List;

public class AuthorDAO extends SupportDAO {

    public AuthorDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void addAuthor(Author author) {
        if (author == null || !author.isValid()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        if (findAuthorByName(author.getName()) == null) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.merge(author);
            transaction.commit();
            session.close();
        } else {
            throw new IllegalArgumentException("Author with name " + author.getName() + " already exists");
        }
    }

    public void deleteAuthor(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        deleteBooksOfAuthor(authorName);
        Author author = findAuthorByName(authorName);
        if (author == null) {
            throw new IllegalArgumentException("Provided Author cannot be null");
        }
        session.remove(author);
        transaction.commit();
        session.close();
    }

    public void deleteBook(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Book book = findBookByTitle(title);
        if (book == null) {
            throw new IllegalArgumentException("Book not found with title :" + title);
        }
        session.remove(book);
        transaction.commit();
        session.close();
    }

    public List<Book> getBooksOfAuthor(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        List<Book> books = collectBooksOfAuthor(authorName);
        books.sort(Comparator.comparing(Book::getNumberOfPages));
        session.close();
        return books;
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

    public List<Author> getAllAuthorsWithBooks() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Author> cq = cb.createQuery(Author.class);
        Root<Author> root = cq.from(Author.class);
        root.fetch("books", JoinType.LEFT);
        cq.select(root).distinct(true);
        List<Author> authors = session.createQuery(cq).getResultList();
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
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        if (book == null || !book.isValid()) {
            throw new IllegalArgumentException("Book cannot be null or book is not valid");
        }
        if (findBookByTitle(book.getTitle()) == null) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Author author = findAuthorByName(authorName);
            if (author == null || !author.isValid()) {
                throw new IllegalArgumentException("Author not found with name :" + authorName + " or authors data are incorrect");
            }
            book.setAuthor(author);
            session.merge(book);
            transaction.commit();
            session.close();
        } else {
            throw new IllegalArgumentException(" Book with title " + book.getTitle() + " already exists");
        }
    }
}