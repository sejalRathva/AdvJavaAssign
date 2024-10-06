import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookApp extends JFrame implements ActionListener {
    private JTextField bookIdField, titleField, authorField, publisherField, priceField, searchField;
    private JButton addButton, saveButton, displayButton, searchButton, updateButton, deleteButton;
    private ArrayList<Book> bookList;
    private Book selectedBook = null;

    public BookApp() {
        super("Book Application");

        // Initialize book list
        bookList = new ArrayList<>();

        // Main panel with vertical BoxLayout for arranging elements vertically
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Book details panel
        JPanel bookDetailsPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // Grid layout with 5 rows and 2 columns
        bookDetailsPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));

        // Book ID
        bookDetailsPanel.add(new JLabel("Book ID:"));
        bookIdField = new JTextField(20);
        bookDetailsPanel.add(bookIdField);

        // Title
        bookDetailsPanel.add(new JLabel("Title:"));
        titleField = new JTextField(20);
        bookDetailsPanel.add(titleField);

        // Author
        bookDetailsPanel.add(new JLabel("Author:"));
        authorField = new JTextField(20);
        bookDetailsPanel.add(authorField);

        // Publisher
        bookDetailsPanel.add(new JLabel("Publisher:"));
        publisherField = new JTextField(20);
        bookDetailsPanel.add(publisherField);

        // Price
        bookDetailsPanel.add(new JLabel("Price:"));
        priceField = new JTextField(20);
        bookDetailsPanel.add(priceField);

        // Adding bookDetailsPanel to mainPanel
        mainPanel.add(bookDetailsPanel);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Book"));
        searchPanel.add(new JLabel("Search by Title/Author/Publisher:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        // Adding searchPanel to mainPanel
        mainPanel.add(searchPanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButton = new JButton("Add Book");
        addButton.addActionListener(this);
        buttonPanel.add(addButton);

        saveButton = new JButton("Save to File");
        saveButton.addActionListener(this);
        buttonPanel.add(saveButton);

        displayButton = new JButton("Display Books");
        displayButton.addActionListener(this);
        buttonPanel.add(displayButton);

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        buttonPanel.add(searchButton);

        updateButton = new JButton("Update Book");
        updateButton.addActionListener(this);
        buttonPanel.add(updateButton);

        deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(this);
        buttonPanel.add(deleteButton);

        // Adding buttonPanel to mainPanel
        mainPanel.add(buttonPanel);

        // Adding the mainPanel to the frame
        add(mainPanel);

        // Window settings
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addBook();
        } else if (e.getSource() == saveButton) {
            saveBooksToFile();
        } else if (e.getSource() == displayButton) {
            displayBooks();
        } else if (e.getSource() == searchButton) {
            searchBook();
        } else if (e.getSource() == updateButton) {
            updateBook();
        } else if (e.getSource() == deleteButton) {
            deleteBook();
        }
    }

    private void addBook() {
        try {
            int bookId = Integer.parseInt(bookIdField.getText());
            String title = titleField.getText();
            String author = authorField.getText();
            String publisher = publisherField.getText();
            double price = Double.parseDouble(priceField.getText());

            // Basic validation
            if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || price <= 0) {
                JOptionPane.showMessageDialog(this, "Please fill all fields with valid data.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // Add book to list
                bookList.add(new Book(bookId, title, author, publisher, price));
                JOptionPane.showMessageDialog(this, "Book added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear input fields
                clearFields();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveBooksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Books.dat"))) {
            oos.writeObject(bookList);
            JOptionPane.showMessageDialog(this, "Books saved to file successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving books to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Books.dat"))) {
            List<Book> booksFromFile = (List<Book>) ois.readObject();

            // Create a JTextArea to display the books
            JTextArea textArea = new JTextArea(10, 30);
            for (Book book : booksFromFile) {
                textArea.append(book.toString() + "\n");
            }
            textArea.setEditable(false);

            // Show books in a scrollable dialog
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(this, scrollPane, "Books List", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error reading books from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBook() {
        String searchText = searchField.getText().toLowerCase();
        selectedBook = null;

        for (Book book : bookList) {
            if (book.getTitle().toLowerCase().contains(searchText) ||
                    book.getAuthor().toLowerCase().contains(searchText) ||
                    book.getPublisher().toLowerCase().contains(searchText) ||
                    String.valueOf(book.getBookId()).contains(searchText)) {

                selectedBook = book;
                displaySelectedBook();
                return;
            }
        }

        if (selectedBook == null) {
            JOptionPane.showMessageDialog(this, "No book found with the given search term.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displaySelectedBook() {
        if (selectedBook != null) {
            bookIdField.setText(String.valueOf(selectedBook.getBookId()));
            titleField.setText(selectedBook.getTitle());
            authorField.setText(selectedBook.getAuthor());
            publisherField.setText(selectedBook.getPublisher());
            priceField.setText(String.valueOf(selectedBook.getPrice()));
        }
    }

    private void updateBook() {
        if (selectedBook != null) {
            selectedBook.setTitle(titleField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setPublisher(publisherField.getText());
            selectedBook.setPrice(Double.parseDouble(priceField.getText()));

            JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please search for a book to update.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        if (selectedBook != null) {
            bookList.remove(selectedBook);
            JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please search for a book to delete.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        bookIdField.setText("");
        titleField.setText("");
        authorField.setText("");
        publisherField.setText("");
        priceField.setText("");
        searchField.setText("");
    }

    // Main method to start the application
    public static void main(String[] args) {
        new BookApp();
    }
}
