import java.util.*;
interface LibraryOperations {
    void addBook(Book book);
    boolean borrowBook(int bookID);
    boolean returnBook(int bookID);
    void viewBooks();
}
abstract class Book {
    protected int bookID;
    protected String title;
    protected String author;
    public Book(int bookID, String title, String author) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
    }
    public int getBookID() {
        return bookID;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public abstract String getDetails();
}
class LibraryBook extends Book {
    public LibraryBook(int bookID, String title, String author) {
        super(bookID, title, author);
    }
    @Override
    public String getDetails() {
        return "ID: " + bookID + ", Title: " + title + ", Author: " + author;
    }
}
class Library implements LibraryOperations {
    private final List<Book> books = new ArrayList<>();
    private final Set<Integer> borrowedBooks = new HashSet<>();
    @Override
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added successfully.");
    }
    public boolean borrowBook(int bookID) {
        for (Book book : books) {
            if (book.getBookID() == bookID && !borrowedBooks.contains(bookID)) {
                borrowedBooks.add(bookID);
                System.out.println("Book borrowed successfully.");
                return true;
            }
        }
        System.out.println("Book not available for borrowing.");
        return false;
    }
    @Override
    public boolean returnBook(int bookID) {
        if (borrowedBooks.remove(bookID)) {
            System.out.println("Book returned successfully.");
            return true;
        }
        System.out.println("Book return failed. Either invalid ID or not borrowed.");
        return false;
    }
    @Override
    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        for (Book book : books) {
            System.out.println(book.getDetails() + (borrowedBooks.contains(book.getBookID()) ? " (Borrowed)" : " (Available)"));
        }
    }
}
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. View Books");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Book ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();
                    library.addBook(new LibraryBook(id, title, author));
                }
                case 2 -> {
                    System.out.print("Enter Book ID to borrow: ");
                    int id = scanner.nextInt();
                    library.borrowBook(id);
                }
                case 3 -> {
                    System.out.print("Enter Book ID to return: ");
                    int id = scanner.nextInt();
                    library.returnBook(id);
                }
                case 4 -> library.viewBooks();
                case 5 -> {
                    System.out.println("Exiting system...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}