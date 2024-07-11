import org.example.dao.AuthorDAO;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AuthorDAOTest {

    private final AuthorDAO authorDAO = new AuthorDAO();


    public Author createAuthor() {
        Author testAuthor = new Author();
        testAuthor.setName("testAuthor");
        testAuthor.setAge(88);
        testAuthor.setFavouriteGenre(Genre.HISTORY);
        return testAuthor;
    }

    public Book createBook(Author author) {
        Book testBook = new Book();
        testBook.setAuthor(author);
        testBook.setGenre(Genre.HISTORY);
        testBook.setTitle("testTitle");
        testBook.setNumberOfPages(654);
        return testBook;
    }


    @Test
    void saveAuthorNullAuthorTest() {
        List<Book> books = new ArrayList<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.saveAuthor(null, books));
    }

    @Test
    void saveAuthorNullListBookTest() {
        List<Book> books = null;
        Author testAuthor = createAuthor();
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.saveAuthor(testAuthor, books));
    }

    @Test
    void saveAuthorNullAuthorTest1() {
        Author testAuthor = null;
        List<Book> books = new ArrayList<>();
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.saveAuthor(testAuthor, books));
        String expectedMessage = "Provided Author or Author credentials or list of books are incorrect or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void saveAuthorNullListOfBookTest1() {
        Author testAuthor = createAuthor();
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.saveAuthor(testAuthor, null));
        String expectedMessage = "Provided Author or Author credentials or list of books are incorrect or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void saveAuthorHappyPathTest() {
        Author testAuthor = createAuthor();
        String nameTestAuthor = testAuthor.getName();
        authorDAO.saveAuthor(testAuthor, new ArrayList<>());
        Assertions.assertEquals(nameTestAuthor, authorDAO.findAuthorByName(nameTestAuthor).getName());
        authorDAO.deleteAuthor(nameTestAuthor);
    }

    @Test
    void saveAuthorInvatlidAuthorArgumentTest() {
        Author testAuthor = createAuthor();
        testAuthor.setName(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.saveAuthor(testAuthor, new ArrayList<>()));
    }

    @Test
    void saveBookNullAuthorNameTest() {
        List<Book> books = new ArrayList<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.saveBook(null, books));
    }

    @Test
    void saveBookEmptyAuthorNameTest() {
        List<Book> books = new ArrayList<>();
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.saveBook("", books));
    }

    @Test
    void saveBookListIOfBooksIsNullTest() {
        Author testAuthor = createAuthor();
        List<Book> books = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.saveBook(testAuthor.getName(), books));
    }

    @Test
    void saveBookHappyPathTest() {
        Author testAuthor = createAuthor();
        Book testBook = createBook(testAuthor);
        authorDAO.addAuthor(testAuthor);
        List<Book> books = new ArrayList<>();
        books.add(testBook);
        authorDAO.saveBook(testAuthor.getName(), books);
        Assertions.assertEquals(testBook.getTitle(), authorDAO.findBookByTitle(testBook.getTitle()).getTitle());
        authorDAO.deleteAuthor(testAuthor.getName());
    }

    @Test
    void saveBookHappyPathTest1() {
        Author testAuthor = createAuthor();
        Book testBook = createBook(testAuthor);
        authorDAO.addAuthor(testAuthor);
        List<Book> books = new ArrayList<>();
        books.add(testBook);
        authorDAO.saveBook(testAuthor.getName(), books);
        Assertions.assertEquals(testBook.getAuthor().getName(), authorDAO.findBookByTitle(testBook.getTitle()).getAuthor().getName());
        authorDAO.deleteAuthor(testAuthor.getName());

    }
    @Test
    void faindAuthorByNameNullNameTest(){
        String authorName=null;
        Assertions.assertThrows(IllegalArgumentException.class,()->authorDAO.findAuthorByName(authorName));
    }
    @Test
    void faindAuthorByNameEmptyNameTest(){
        String authorName="";
        Assertions.assertThrows(IllegalArgumentException.class,()->authorDAO.findAuthorByName(authorName));
    }
    @Test
    void findAuthorByNameInvalidArgumentTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.findAuthorByName(null));
        String expectedMessage = "Author name cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void faindAuthorByNameHappyPathTest(){
        Author testAuthor=createAuthor();
        authorDAO.addAuthor(testAuthor);
        Assertions.assertEquals(testAuthor.getName(),authorDAO.findAuthorByName(testAuthor.getName()).getName());
        authorDAO.deleteAuthor(testAuthor.getName());
    }
    @Test
    void faindAuthorByNameHappyPathTest1(){
        Author testAuthor=createAuthor();
        authorDAO.addAuthor(testAuthor);
        Assertions.assertEquals(testAuthor.getAge(),authorDAO.findAuthorByName(testAuthor.getName()).getAge());
        authorDAO.deleteAuthor(testAuthor.getName());
    }
    @Test
    void changeAuthorAgeNullAuthorNameArgumentTest(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.changeAuthorAge(null,26));
        String expectedMessage = "Provided name of Author cannot be null or empty or provided aga cannot be less or equal to zero";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void changeAuthorAgeNullAuthorNameArgumentTest1(){
        Assertions.assertThrows(IllegalArgumentException.class,()->authorDAO.changeAuthorAge(null,56));
    }
    @Test
    void changeAuthorAgeEmptyAuthorNameArgumentTest(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.changeAuthorAge("",26));
        String expectedMessage = "Provided name of Author cannot be null or empty or provided aga cannot be less or equal to zero";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void changeAuthorAgeEmptyAuthorNameArgumentTest1(){
        Assertions.assertThrows(IllegalArgumentException.class,()->authorDAO.changeAuthorAge("",56));
    }
    @Test
    void changeAuthorAgeInvalidAgeArgumentArgumentTest(){
        Author testAuthor=createAuthor();
        Assertions.assertThrows(IllegalArgumentException.class,()->authorDAO.changeAuthorAge(testAuthor.getName(),-5));
    }
    @Test
    void changeAuthorAgeInvalidAgeArgumentArgumentTest1(){
        Author testAuthor=createAuthor();
        Assertions.assertThrows(IllegalArgumentException.class,()->authorDAO.changeAuthorAge(testAuthor.getName(),0));
    }
    @Test
    void changeAuthorAgeHappyPathTest(){
        Author testAuthor=createAuthor();
        authorDAO.addAuthor(testAuthor);
        Integer changedAge=145;
        authorDAO.changeAuthorAge(testAuthor.getName(),changedAge);
        Assertions.assertEquals(145,authorDAO.findAuthorByName(testAuthor.getName()).getAge());
        authorDAO.deleteAuthor(testAuthor.getName());
    }
    @Test
    void deleteAuthorNullAuthorNameTest(){
        Assertions.assertThrows(IllegalArgumentException.class,()->authorDAO.deleteAuthor(null));
    }
    @Test
    void deleteAuthorNullAuthorNameTest1(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteAuthor(null));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void deleteAuthorEmptyAuthorNameTest(){
        Assertions.assertThrows(IllegalArgumentException.class,()->authorDAO.deleteAuthor(""));
    }
    @Test
    void deleteAuthorEmptyAuthorNameTest1() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteAuthor(""));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void deleteAuthorHappyPathTest(){
        Author testAuthor=createAuthor();
        authorDAO.addAuthor(testAuthor);
        Author[]authors=new Author[0];
        authorDAO.deleteAuthor(testAuthor.getName());
        Assertions.assertArrayEquals(authors,authorDAO.getAllAuthors().toArray());
    }
    @Test
    void deleteBookNullTitleTest(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteBook(null));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void deleteBookNullTitleTest1(){
        Assertions.assertThrows(IllegalArgumentException.class,()->authorDAO.deleteBook(null));
    }
    @Test
    void deleteBookEmptyTitleTest(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteBook(""));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void deleteBookEmptyTitleTest1(){
        Assertions.assertThrows(IllegalArgumentException.class,()->authorDAO.deleteBook(""));
    }
    @Test
    void deleteBookHappyPathTest(){
        Author testAuthor=createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book testBook=createBook(testAuthor);
        authorDAO.addBookToAuthor(testBook,testAuthor.getName());
        authorDAO.deleteBook(testBook.getTitle());
        Book[]books=new Book[0];
        Assertions.assertArrayEquals(books,authorDAO.getBooksOfAuthor(testAuthor.getName()).toArray());
    }

}
/*

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
 */