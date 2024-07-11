package org.example.dao;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.sessions.AuthorSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Comparator;
import java.util.List;

public class AuthorDAO {

    private final SessionFactory sessionFactory = AuthorSessionFactory.getAuthorSessionFactory();

    public void saveAuthor(Author author, List<Book> books) {
        if (author == null || !author.isValid() || books == null) {
            throw new IllegalArgumentException("Provided Author or Author credentials or list of books are incorrect or empty");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(author);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save author :" + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        saveBook(author.getName(), books);
    }

    public void saveBook(String authorName, List<Book> books) {
        if (authorName == null || authorName.isEmpty() || books == null) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Author author = findAuthorByName(authorName);
            if (author == null) {
                throw new IllegalArgumentException("Author not found with name :" + authorName);
            }
            for (Book book : books) {
                book.setAuthor(author);
            }
            books.forEach(session::merge);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save book :" + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Author findAuthorByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }
        Session session = sessionFactory.openSession();

        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Author> authorQuery = criteriaBuilder.createQuery(Author.class);
            Root<Author> root = authorQuery.from(Author.class);
            authorQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
            return session.createQuery(authorQuery).getSingleResult();

        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    public void changeAuthorAge(String authorName, int age) {
        if (authorName == null || authorName.isEmpty() || age <= 0) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty or provided aga cannot be less or equal to zero");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Author author = findAuthorByName(authorName);
            author.setAge(age);
            session.merge(author);// dopisanie
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to change Author age :" + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteAuthor(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            deleteBooksOfAuthor(authorName);
            Author author = findAuthorByName(authorName);
            session.remove(author);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed delete Author :" + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteBook(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            List<Book> books = findBooksByTitle(title);
            if (books.isEmpty()) {
                throw new IllegalArgumentException("Book not found with title :" + title);
            }
            books.forEach(book -> {
                System.out.println("Deleting book :" + book.getTitle());
                session.remove(book);
            });
            //session.remove(books);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                System.out.println("Transaction is rolled back due to an error");
            }
            throw new RuntimeException("Failed to delete book(s) with title: " + title, e);
        } finally {
            session.close();
        }
    }


    public List<Book> findBooksByTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> bookQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = bookQuery.from(Book.class);
            bookQuery.select(root).where(criteriaBuilder.equal(root.get("title"), title));
            return session.createQuery(bookQuery).getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    public Book findBookByTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> bookQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = bookQuery.from(Book.class);
            bookQuery.select(root).where(criteriaBuilder.equal(root.get("title"), title));
            return session.createQuery(bookQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    public List<Book> getBooksOfAuthor(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> bookQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = bookQuery.from(Book.class);
            bookQuery.select(root).where(criteriaBuilder.equal(root.get("author").get("name"), authorName));
            List<Book> books = session.createQuery(bookQuery).getResultList();
            books.sort(Comparator.comparing(Book::getNumberOfPages));
            return books;
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    public void deleteBooksOfAuthor(String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = criteriaQuery.from(Book.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("author").get("name"), authorName));
            List<Book> books = session.createQuery(criteriaQuery).getResultList();
            for (Book book : books) {
                session.remove(book);
            }
            transaction.commit();
        } catch (NoResultException e) {
            System.out.println("The person wit name " + authorName + " is not the author of any book.");
        } finally {
            session.close();
        }
    }

    public List<Author> getAllAuthors() {
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Author> authorQuery = criteriaBuilder.createQuery(Author.class);
            Root<Author> root = authorQuery.from(Author.class);
            authorQuery.select(root);
            return session.createQuery(authorQuery).getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    public List<Book> getAllBooks() {
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Book> bookQuery = criteriaBuilder.createQuery(Book.class);
            Root<Book> root = bookQuery.from(Book.class);
            bookQuery.select(root);
            return session.createQuery(bookQuery).getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    public void addBookToAuthor(Book book, String authorName) {
        if (authorName == null || authorName.isEmpty()) {
            throw new IllegalArgumentException("Provided name of Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Author author = findAuthorByName(authorName);
            book.setAuthor(author);
            session.merge(book);
            transaction.commit();
        } catch (NoResultException e) {
            //=================================
        } finally {
            session.close();
        }
    }

    public void addAuthor(Author author) {
        if (author == null || !author.isValid()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(author);
            transaction.commit();
        } catch (NoResultException e) {
//===============================================
        } finally {
            session.close();
        }
    }


    public void getAllBooksAndAuthors() {
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Author> authorQuery = criteriaBuilder.createQuery(Author.class);
            Root<Author> root = authorQuery.from(Author.class);

            root.fetch("books", JoinType.LEFT);
            authorQuery.select(root).distinct(true);
            List<Author> authors = session.createQuery(authorQuery).getResultList();

            for (Author author : authors) {
                System.out.println(author);
                System.out.println(author.getBooks());
            }
        } catch (NoResultException e) {
//===============================================
        } finally {
            session.close();
        }
    }


}
