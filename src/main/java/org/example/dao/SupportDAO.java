package org.example.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.Author;
import org.example.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class SupportDAO {

    protected SessionFactory sessionFactory;

    protected SupportDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Book> collectBooksOfAuthor(String authorName) {
        Session session = sessionFactory.openSession();
        Author author = findAuthorByName(authorName);
        if (author == null) {
            throw new IllegalArgumentException("No Author found with name : " + authorName);
        }
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> bookQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> bookRoot = bookQuery.from(Book.class);
        bookQuery.select(bookRoot).where(criteriaBuilder.equal(bookRoot.get("author").get("id"), author.getId()));
        List<Book> books = session.createQuery(bookQuery).getResultList();
        session.close();
        return books;
    }


    public Author findAuthorByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Author> authorQuery = criteriaBuilder.createQuery(Author.class);
        Root<Author> root = authorQuery.from(Author.class);
        authorQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
        Author author = session.createQuery(authorQuery).getSingleResultOrNull();
        session.close();
        return author;
    }

    public void deleteBooksOfAuthor(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<Book> books = collectBooksOfAuthor(authorName);
        for (Book book : books) {
            session.remove(book);
        }
        transaction.commit();
        session.close();
    }

    public Book findBookByTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Provided title of Book cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> bookQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = bookQuery.from(Book.class);
        bookQuery.select(root).where(criteriaBuilder.equal(root.get("title"), title));
        Book book = session.createQuery(bookQuery).getSingleResultOrNull();
        session.close();
        return book;
    }
}