package BookApp;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class BookApp extends JFrame implements ActionListener {
    private JTextField titleField, authorField, publisherField, priceField;
    private JButton addButton, saveButton, displayButton;
    private ArrayList<Book> bookList;

    public BookApp() {
        super("Book Application");

        // book list
        bookList = new ArrayList<>();

        // I'll create form layout
        setLayout(new GridLayout(6, 2));

        // fields
        add(new JLabel("Title:"));
        titleField = new JTextField(20);
        add(titleField);

        add(new JLabel("Author:"));
        authorField = new JTextField(20);
        add(authorField);

        add(new JLabel("Publisher:"));
        publisherField = new JTextField(20);
        add(publisherField);

        add(new JLabel("Price:"));
        priceField = new JTextField(20);
        add(priceField);

        // Buttons
        addButton = new JButton("Add Book");
        addButton.addActionListener(this);
        add(addButton);

        saveButton = new JButton("Save to File");
        saveButton.addActionListener(this);
        add(saveButton);

        displayButton = new JButton("Display Books");
        displayButton.addActionListener(this);
        add(displayButton);

        // width and height
        setSize(400, 300);
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
        }
    }

    // Method to add a book to the list
    private void addBook() {
        try {
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
                bookList.add(new Book(title, author, publisher, price));
                JOptionPane.showMessageDialog(this, "Book added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear input fields after succesful adding book
                titleField.setText("");
                authorField.setText("");
                publisherField.setText("");
                priceField.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to save books to file
    private void saveBooksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Books.dat"))) {
            oos.writeObject(bookList);
            JOptionPane.showMessageDialog(this, "Books saved to file successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving books to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to display books from file
    private void displayBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Books.dat"))) {
            ArrayList<Book> booksFromFile = (ArrayList<Book>) ois.readObject();

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

    public static void main(String[] args) {
        new BookApp();
    }
}
