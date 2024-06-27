package org.example;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

    private final SessionFactory sessionFactory=AuthorSessionFactory.getAuthorSessionFactory();

    public void saveAuthor(Author author, List<Book> books){
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        session.merge(author);
        transaction.commit();
        session.close();

        saveBook(books, author.getName());
    }

    public void saveBook(List<Book>books,String authorName){
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        Author author=findAuthorByName(authorName);
        for(Book book:books){
            book.setAuthor(author);
        }
        books.forEach(session::merge);
        transaction.commit();
        session.close();
    }

    public Author findAuthorByName(String name){
        Session session=sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder=session.getCriteriaBuilder();
        CriteriaQuery<Author>authorQuery=criteriaBuilder.createQuery(Author.class);
        Root<Author> root=authorQuery.from(Author.class);
        authorQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
        return session.createQuery(authorQuery).getSingleResult();
    }
    public void changeAuthorAge(String authorName,int age){
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        Author author=findAuthorByName(authorName);
        author.setAge(age);
        session.merge(author);// dopisanie
        transaction.commit();
        session.close();
    }
    public void deleteAuthor(String authorName){
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        Author author=findAuthorByName(authorName);
        //deleteBooksOfAuthor(authorName);
        session.remove(author);
        transaction.commit();
        session.close();
    }

    public List<Book> getBooksOfAuthor(String authorName){
        Session session=sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder=session.getCriteriaBuilder();
        CriteriaQuery<Book>bookQuery=criteriaBuilder.createQuery(Book.class);
        Root<Book>root=bookQuery.from(Book.class);
        bookQuery.select(root).where(criteriaBuilder.equal(root.get("author").get("name"),authorName));
        List<Book>books=session.createQuery(bookQuery).getResultList();
        return books;
    }
    public void deleteBooksOfAuthor(String authorName){
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        Author author=findAuthorByName(authorName);
        // Znalezienie i usunięcie książek tego autora
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("author").get("name"), authorName));
        List<Book> books = session.createQuery(criteriaQuery).getResultList();

        for(Book book:books){
            session.delete(book);
        }
        transaction.commit();
        session.close();
    }
    public void addBookToAuthor(String authorName,Book book){}

    public List<Author> getAllAuthors(){
        return new ArrayList<>();
    }
    public List<Book> getAllBooks(){
        return new ArrayList<>();
    }
   // public List<List<T>> getAllBooksAndAuthors(){
     //   return new ArrayList<>();
    //}

    public void addAuthor(Author author){

    }
    public void deleteBook(String title){

    }
    public void deleteAuthor1(String authorName){

    }

}
