import org.example.dao.AuthorDAO;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entitySupport.Genre;
import org.example.sessions.AuthorSessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;


@Testcontainers
public class AuthorDaoWithContainersTest {

    String jdbcUrl = postgres.getJdbcUrl();
    String username = postgres.getUsername();
    String password = postgres.getPassword();
    private AuthorDAO authorDAO = new AuthorDAO(AuthorSessionFactory.getAuthorSessionFactoryTest(jdbcUrl, username, password));

    @BeforeEach
    public void setUp() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
    }

    private Author createAuthor() {
        Author testAuthor = new Author();
        testAuthor.setName("testAuthor");
        testAuthor.setAge(88);
        testAuthor.setFavouriteGenre(Genre.HISTORY);
        return testAuthor;
    }

    private Book createBook(Author author) {
        Book testBook = new Book();
        testBook.setAuthor(author);
        testBook.setGenre(Genre.HISTORY);
        testBook.setTitle("testTitle");
        testBook.setNumberOfPages(654);
        return testBook;
    }

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void faindAuthorByNameHappyPathTest() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Author autTest = authorDAO.findAuthorByName(testAuthor.getName());
        System.out.println(autTest);
        Assertions.assertEquals(testAuthor.getName(), authorDAO.findAuthorByName(testAuthor.getName()).getName());
    }

    @Test
    void faindAuthorByNameHappyPathTestAge() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Assertions.assertEquals(testAuthor.getAge(), authorDAO.findAuthorByName(testAuthor.getName()).getAge());
    }

    @Test
    void faindAuthorByNameNullNameTest() {
        String authorName = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.findAuthorByName(authorName));
    }

    @Test
    void faindAuthorByNameEmptyNameTest() {
        String authorName = "";
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.findAuthorByName(authorName));
    }

    @Test
    void findAuthorByNameInvalidArgumentTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.findAuthorByName(null));
        String expectedMessage = "Author name cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void deleteAuthorNullAuthorNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteAuthor(null));
    }

    @Test
    void deleteAuthorNullAuthorNameTest1() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteAuthor(null));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void deleteAuthorEmptyAuthorNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteAuthor(""));
    }

    @Test
    void deleteAuthorEmptyAuthorNameTest1() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteAuthor(""));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void deleteAuthorHappyPathTest() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Author[] authors = new Author[0];
        authorDAO.deleteAuthor(testAuthor.getName());
        Assertions.assertArrayEquals(authors, authorDAO.getAllAuthors().toArray());
    }

    @Test
    void deleteBookNullTitleTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteBook(null));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void deleteBookNullTitleTest1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteBook(null));
    }

    @Test
    void deleteBookEmptyTitleTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteBook(""));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void deleteBookEmptyTitleTest1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteBook(""));
    }

    @Test
    void deleteBookHappyPathTest() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book testBook = createBook(testAuthor);
        authorDAO.addBookToAuthor(testBook, testAuthor.getName());
        authorDAO.deleteBook(testBook.getTitle());
        Book[] books = new Book[0];
        Assertions.assertArrayEquals(books, authorDAO.getBooksOfAuthor(testAuthor.getName()).toArray());
    }

    @Test
    void findBookByTitleHappyPathTest() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book testBook = createBook(testAuthor);
        authorDAO.addBookToAuthor(testBook, testAuthor.getName());
        Assertions.assertEquals(testBook.getTitle(), authorDAO.findBookByTitle(testBook.getTitle()).getTitle());
    }

    @Test
    void findBookByTitleHappyPathTest1() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book testBook = createBook(testAuthor);
        authorDAO.addBookToAuthor(testBook, testAuthor.getName());
        Assertions.assertEquals(testBook.getNumberOfPages(), authorDAO.findBookByTitle(testBook.getTitle()).getNumberOfPages());
    }

    @Test
    void findBookByTitleNullTitleTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.findBookByTitle(null));
        String expectedMessage = "Provided title of Book cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void findBookByTitleNullTitleTest1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.findBookByTitle(null));
    }

    @Test
    void findBookByTitleEmptyTitleTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.findBookByTitle(""));
        String expectedMessage = "Provided title of Book cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void findBookByTitleEmptyTitleTest1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.findBookByTitle(""));
    }

    @Test
    void getBooksOfAuthorNullAuthorNameTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.getBooksOfAuthor(null));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void getBooksOfAuthorNullAuthorNameTest1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.getBooksOfAuthor(null));
    }

    @Test
    void getBooksOfAuthorEmptyAuthorNameTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.getBooksOfAuthor(""));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void getBooksOfAuthorEmptyAuthorNameTest1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.getBooksOfAuthor(""));
    }

    @Test
    void getBooksOfAuthorHappyPathTest() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book testBook = createBook(testAuthor);
        Book book1 = new Book();
        book1.setTitle("titleTest1");
        book1.setGenre(Genre.HORROR);
        book1.setNumberOfPages(500);
        book1.setAuthor(testAuthor);
        authorDAO.addBookToAuthor(testBook, testAuthor.getName());
        authorDAO.addBookToAuthor(book1, testAuthor.getName());
        Book[] books = new Book[2];
        testBook.setId(2L);
        books[1] = testBook;
        book1.setId(1L);
        books[0] = book1;
        Assertions.assertArrayEquals(books, authorDAO.getBooksOfAuthor(testAuthor.getName()).toArray());
    }

    @Test
    void collectBooksOfAuthorNullAuthorNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.collectBooksOfAuthor(null));
    }

    @Test
    void collectBooksOfAuthorEmptyAuthorNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.collectBooksOfAuthor(""));
        ;
    }

    @Test
    void collectBooksOfAuthorhappyPathTest() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book testBook = createBook(testAuthor);
        Book book1 = new Book();
        book1.setTitle("titleTest1");
        book1.setGenre(Genre.HORROR);
        book1.setNumberOfPages(500);
        book1.setAuthor(testAuthor);
        authorDAO.addBookToAuthor(testBook, testAuthor.getName());
        authorDAO.addBookToAuthor(book1, testAuthor.getName());
        Book[] books = new Book[2];
        testBook.setId(2L);
        books[0] = testBook;
        book1.setId(1L);
        books[1] = book1;
        Assertions.assertArrayEquals(books, authorDAO.collectBooksOfAuthor(testAuthor.getName()).toArray());
    }

    @Test
    void deleteBooksOfAuthorNullAuthorNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteBooksOfAuthor(null));
    }

    @Test
    void deleteBooksOfAuthorNullAuthorNameTest1() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteBooksOfAuthor(null));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void deleteBooksOfAuthorEmptyAuthorNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteBooksOfAuthor(""));
    }

    @Test
    void deleteBooksOfAuthorEmptyAuthorNameTest1() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.deleteBooksOfAuthor(""));
        String expectedMessage = "Provided name of Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void deleteBooksOfAuthorHappyPathTest() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book testBook = createBook(testAuthor);
        Book book1 = new Book();
        book1.setTitle("titleTest1");
        book1.setGenre(Genre.HORROR);
        book1.setNumberOfPages(500);
        book1.setAuthor(testAuthor);
        authorDAO.addBookToAuthor(testBook, testAuthor.getName());
        authorDAO.addBookToAuthor(book1, testAuthor.getName());
        authorDAO.deleteBook("titleTest1");
        authorDAO.deleteBook(testBook.getTitle());
        Book[] books = new Book[0];
        Assertions.assertArrayEquals(books, authorDAO.getBooksOfAuthor(testAuthor.getName()).toArray());
    }

    @Test
    void addAuthorNullAuthorNameTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addAuthor(null));
    }

    @Test
    void addAuthorNullAuthorNameTest1() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addAuthor(null));
        String expectedMessage = "Author cannot be null or empty";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void addAuthorIsNotValidAuthorNameTest() {
        Author testAuthor = createAuthor();
        testAuthor.setAge(0);
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addAuthor(testAuthor));
    }

    @Test
    void addAuthorIsNotValidAuthorNameTest1() {
        Author testAuthor = createAuthor();
        testAuthor.setFavouriteGenre(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addAuthor(testAuthor));
    }

    @Test
    void addAuthorIsNotValidAuthorNameTest2() {
        Author testAuthor = createAuthor();
        testAuthor.setName("");
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addAuthor(testAuthor));
    }

    @Test
    void addAuthorHappyPathTest() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Author expectedAuthor = authorDAO.findAuthorByName(testAuthor.getName());
        Assertions.assertEquals(expectedAuthor.getAge(), testAuthor.getAge());
    }

    @Test
    void addAuthorHappyPathTest1() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Author expectedAuthor = authorDAO.findAuthorByName(testAuthor.getName());
        Assertions.assertEquals(expectedAuthor.getName(), testAuthor.getName());
    }

    @Test
    void getAllAuthorsHappyPathTest() {
        Author[] authors = new Author[0];
        Assertions.assertEquals(authors.length, authorDAO.getAllAuthors().toArray().length);
    }

    @Test
    void getAllAuthorsHappyPathTest1() {
        Author testAuthor = createAuthor();
        Author testAutror2 = new Author();
        testAutror2.setName("test2");
        testAutror2.setAge(22);
        testAutror2.setFavouriteGenre(Genre.BIOGRAPHY);
        authorDAO.addAuthor(testAuthor);
        authorDAO.addAuthor(testAutror2);
        Author[] authors = new Author[2];
        authors[0] = authorDAO.findAuthorByName(testAuthor.getName());
        authors[1] = authorDAO.findAuthorByName(testAutror2.getName());
        Assertions.assertEquals(authors.length, authorDAO.getAllAuthors().toArray().length);
    }

    @Test
    void getAllAuthorsHappyPathTest2() {
        Author testAuthor = createAuthor();
        Author testAutror2 = new Author();
        testAutror2.setName("test2");
        testAutror2.setAge(22);
        testAutror2.setFavouriteGenre(Genre.BIOGRAPHY);
        authorDAO.addAuthor(testAuthor);
        authorDAO.addAuthor(testAutror2);
        Assertions.assertEquals(testAuthor.getName(), authorDAO.getAllAuthors().get(0).getName());
    }

    @Test
    void getAllAuthorsHappyPathTest3() {
        Author testAuthor = createAuthor();
        Author testAuthor2 = new Author();
        testAuthor2.setName("test2");
        testAuthor2.setAge(22);
        testAuthor2.setFavouriteGenre(Genre.BIOGRAPHY);
        authorDAO.addAuthor(testAuthor);
        authorDAO.addAuthor(testAuthor2);
        Assertions.assertEquals(testAuthor2.getName(), authorDAO.getAllAuthors().get(1).getName());
    }

    @Test
    void getAllBooksHappyPathTest() {
        Book[] books = new Book[0];
        Assertions.assertEquals(books.length, authorDAO.getAllBooks().toArray().length);
    }

    @Test
    void getAllBooksHappyPathTest1() {
        Author testAuthor = createAuthor();
        Book book = createBook(testAuthor);
        Book book2 = createBook(testAuthor);
        book2.setTitle("testBook2");
        book2.setAuthor(testAuthor);
        book2.setGenre(Genre.ROMANCE);
        book2.setNumberOfPages(77);
        authorDAO.addAuthor(testAuthor);
        authorDAO.addBookToAuthor(book, testAuthor.getName());
        authorDAO.addBookToAuthor(book2, testAuthor.getName());
        Book[] books = new Book[2];
        books[0] = authorDAO.findBookByTitle(book.getTitle());
        books[1] = authorDAO.findBookByTitle(book2.getTitle());
        Assertions.assertEquals(books.length, authorDAO.getAllBooks().toArray().length);
    }

    @Test
    void getAllBooksHappyPathTest2() {
        Author testAuthor = createAuthor();
        Book book = createBook(testAuthor);
        Book book2 = createBook(testAuthor);
        book2.setTitle("testBook2");
        book2.setAuthor(testAuthor);
        book2.setGenre(Genre.ROMANCE);
        book2.setNumberOfPages(77);
        authorDAO.addAuthor(testAuthor);
        authorDAO.addBookToAuthor(book, testAuthor.getName());
        authorDAO.addBookToAuthor(book2, testAuthor.getName());
        Assertions.assertEquals(book.getNumberOfPages(), authorDAO.getAllBooks().get(0).getNumberOfPages());
    }

    @Test
    void getAllBooksHappyPathTest3() {
        Author testAuthor = createAuthor();
        Book book = createBook(testAuthor);
        Book book2 = createBook(testAuthor);
        book2.setTitle("testBook2");
        book2.setAuthor(testAuthor);
        book2.setGenre(Genre.ROMANCE);
        book2.setNumberOfPages(77);
        authorDAO.addAuthor(testAuthor);
        authorDAO.addBookToAuthor(book, testAuthor.getName());
        authorDAO.addBookToAuthor(book2, testAuthor.getName());
        Assertions.assertEquals(book2.getTitle(), authorDAO.getAllBooks().get(1).getTitle());
    }

    @Test
    void addBookToAuthorBookNullTest() {
        Author author = createAuthor();
        authorDAO.addAuthor(author);
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addBookToAuthor(null, author.getName()));
    }

    @Test
    void addBookToAuthorBookNullTest1() {
        Author author = createAuthor();
        authorDAO.addAuthor(author);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addBookToAuthor(null, author.getName()));
        String expectedMessage = "Book cannot be null or book is not valid";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void addBookToAuthorBookNotValidTest() {
        Author author = createAuthor();
        authorDAO.addAuthor(author);
        Book book = createBook(author);
        book.setTitle("");
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addBookToAuthor(book, author.getName()));
    }

    @Test
    void addBookToAuthorNameNullTest() {
        Author author = createAuthor();
        authorDAO.addAuthor(author);
        Book book = createBook(author);
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addBookToAuthor(book, null));
    }

    @Test
    void addBookToAuthorNameNullTest1() {
        Author author = createAuthor();
        authorDAO.addAuthor(author);
        Book book = createBook(author);
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addBookToAuthor(book, null));
    }

    @Test
    void addBookToAuthorNameEmptyTest() {
        Author author = createAuthor();
        authorDAO.addAuthor(author);
        author.setName("");
        Book book = createBook(author);
        Assertions.assertThrows(IllegalArgumentException.class, () -> authorDAO.addBookToAuthor(book, author.getName()));
    }

    @Test
    void addBookToAuthorHappyPathTest() {
        Author author = createAuthor();
        Book book = createBook(author);
        authorDAO.addAuthor(author);
        authorDAO.addBookToAuthor(book, author.getName());
        Assertions.assertEquals(book.getTitle(), authorDAO.getBooksOfAuthor(author.getName()).get(0).getTitle());
    }

    @Test
    void addBookToAuthorHappyPathTest1() {
        Author author = createAuthor();
        Book book = createBook(author);
        Book book2 = createBook(author);
        book2.setTitle("testBook2");
        book2.setAuthor(author);
        book2.setGenre(Genre.TRAVEL);
        book2.setNumberOfPages(331);
        authorDAO.addAuthor(author);
        authorDAO.addBookToAuthor(book, author.getName());
        authorDAO.addBookToAuthor(book2, author.getName());
        Assertions.assertEquals(book2.getNumberOfPages(), authorDAO.getBooksOfAuthor(author.getName()).get(0).getNumberOfPages());
    }

    @Test
    void addBookToAuthorHappyPathTest2() {
        Author author = createAuthor();
        Book book = createBook(author);
        Book book2 = createBook(author);
        book2.setTitle("testBook2");
        book2.setAuthor(author);
        book2.setGenre(Genre.TRAVEL);
        book2.setNumberOfPages(331);
        authorDAO.addAuthor(author);
        authorDAO.addBookToAuthor(book, author.getName());
        authorDAO.addBookToAuthor(book2, author.getName());
        Book[] books = new Book[2];
        Assertions.assertEquals(books.length, authorDAO.getBooksOfAuthor(author.getName()).toArray().length);
    }


    @Test
    void getAllAuthorsWithBooksHappyPathTest() {
        Author[] authors = new Author[0];
        Assertions.assertEquals(authors.length, authorDAO.getAllAuthorsWithBooks().toArray().length);
    }

    @Test
    void getAllAuthorsWithBooksHappyPathTest1() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book book = createBook(testAuthor);
        Book book2 = createBook(testAuthor);
        book2.setTitle("testBook2");
        book2.setAuthor(testAuthor);
        book2.setGenre(Genre.TRAVEL);
        book2.setNumberOfPages(331);
        Book book3 = createBook(testAuthor);
        book3.setTitle("testBook3");
        book3.setAuthor(testAuthor);
        book3.setGenre(Genre.TRAVEL);
        book3.setNumberOfPages(777);
        authorDAO.addBookToAuthor(book, testAuthor.getName());
        authorDAO.addBookToAuthor(book2, testAuthor.getName());
        authorDAO.addBookToAuthor(book3, testAuthor.getName());
        List<Author> authors = authorDAO.getAllAuthorsWithBooks();
        List<Book> booksOfTestAuthor = authorDAO.getBooksOfAuthor(testAuthor.getName());
        List<Book>booksOfTestAuthorFromTestedMethod = authors.get(0).getBooks();
        Assertions.assertEquals(booksOfTestAuthor.toArray().length,booksOfTestAuthorFromTestedMethod.toArray().length);


    }

    @Test
    void getAllAuthorsWithBooksHappyPathTest2() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book book = createBook(testAuthor);
        Book book2 = createBook(testAuthor);
        book2.setTitle("testBook2");
        book2.setAuthor(testAuthor);
        book2.setGenre(Genre.TRAVEL);
        book2.setNumberOfPages(331);
        Book book3 = createBook(testAuthor);
        book3.setTitle("testBook3");
        book3.setAuthor(testAuthor);
        book3.setGenre(Genre.TRAVEL);
        book3.setNumberOfPages(777);
        authorDAO.addBookToAuthor(book, testAuthor.getName());
        authorDAO.addBookToAuthor(book2, testAuthor.getName());
        authorDAO.addBookToAuthor(book3, testAuthor.getName());
        List<Author> authors = authorDAO.getAllAuthorsWithBooks();
        List<Book> booksOfTestAuthor = authorDAO.getBooksOfAuthor(testAuthor.getName());
        Book expectedBook=authors.get(0).getBooks().get(0);
        Book actualBook=booksOfTestAuthor.get(2);
        Assertions.assertEquals(expectedBook.getTitle(),actualBook.getTitle());
    }

    @Test
    void getAllAuthorsWithBooksHappyPathTest3() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book book = createBook(testAuthor);
        Book book2 = createBook(testAuthor);
        book2.setTitle("testBook2");
        book2.setAuthor(testAuthor);
        book2.setGenre(Genre.TRAVEL);
        book2.setNumberOfPages(331);
        Book book3 = createBook(testAuthor);
        book3.setTitle("testBook3");
        book3.setAuthor(testAuthor);
        book3.setGenre(Genre.TRAVEL);
        book3.setNumberOfPages(777);
        authorDAO.addBookToAuthor(book, testAuthor.getName());
        authorDAO.addBookToAuthor(book2, testAuthor.getName());
        authorDAO.addBookToAuthor(book3, testAuthor.getName());
        List<Author> authors = authorDAO.getAllAuthorsWithBooks();
        List<Book> booksOfTestAuthor = authorDAO.getBooksOfAuthor(testAuthor.getName());
        Book expectedBook=authors.get(0).getBooks().get(0);
        Book actualBook=booksOfTestAuthor.get(2);
        Assertions.assertEquals(expectedBook.getNumberOfPages(),actualBook.getNumberOfPages());
    }
    @Test
    void getAllAuthorsWithBooksHappyPathTest4() {
        Author testAuthor = createAuthor();
        authorDAO.addAuthor(testAuthor);
        Book book = createBook(testAuthor);
        Book book2 = createBook(testAuthor);
        book2.setTitle("testBook2");
        book2.setAuthor(testAuthor);
        book2.setGenre(Genre.TRAVEL);
        book2.setNumberOfPages(331);
        Book book3 = createBook(testAuthor);
        book3.setTitle("testBook3");
        book3.setAuthor(testAuthor);
        book3.setGenre(Genre.TRAVEL);
        book3.setNumberOfPages(777);
        authorDAO.addBookToAuthor(book, testAuthor.getName());
        authorDAO.addBookToAuthor(book2, testAuthor.getName());
        authorDAO.addBookToAuthor(book3, testAuthor.getName());
        List<Author> authors = authorDAO.getAllAuthorsWithBooks();
        List<Book> booksOfTestAuthor = authorDAO.getBooksOfAuthor(testAuthor.getName());
        Book expectedBook=authors.get(0).getBooks().get(0);
        Book actualBook=booksOfTestAuthor.get(2);
        Assertions.assertEquals(expectedBook,actualBook);
    }
}
