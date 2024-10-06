import java.io.Serializable;

public class Book implements Serializable {
    private int bookId;
    private String title;
    private String author;
    private String publisher;
    private double price;

    public Book(int bookId, String title, String author, String publisher, double price) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public double getPrice() {
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BookID: " + bookId + ", Title: " + title + ", Author: " + author + ", Publisher: " + publisher
                + ", Price: " + price;
    }
}
