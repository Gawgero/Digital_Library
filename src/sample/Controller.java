package sample;

import Classes.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    @FXML Button addBooks;
    @FXML Button addBackFromAddBooks;
    @FXML Label addInformationForAddBooks;
    @FXML Button showButton;
    @FXML Button searchButton;
    @FXML Button addButton;
    @FXML Button exitButton;
    @FXML AnchorPane searchPanel;
    @FXML Button backShowButton;
    @FXML Button backSearchButton;
    @FXML ComboBox<String> choiceScientificORLiterature;
    @FXML VBox contentVBox;
    @FXML VBox menuVBox;
    @FXML TextField addTittle;
    @FXML AnchorPane showBooksPane;
    @FXML TextField searchField;
    @FXML Button searchBoxButton;
    @FXML TextField addAuthor;
    @FXML VBox fieldSpace;
    @FXML TextField addISBN;
    @FXML TextField addYear;
    @FXML ComboBox<String> addType;
    @FXML TextArea addFreeText;

    @FXML Label errorLabel;

    @FXML AnchorPane addPane;

    @FXML AnchorPane sureDialog;
    @FXML Button yesDialogButton;
    @FXML Button noDialogButton;
    @FXML VBox dialogVBox;

    @FXML Label okLabel;
    @FXML Label noResultsLabel;


    ArrayList<Book> books = new ArrayList<>();

    @FXML private void initialize()
    {
        showButton.setOnAction(event -> {
            showBooksPane.setVisible(true);
            menuVBox.setVisible(false);
        });
        backShowButton.setOnAction(event -> {
            showBooksPane.setVisible(false);
            menuVBox.setVisible(true);
        });

        searchButton.setOnAction(event -> {
           menuVBox.setVisible(false);
           searchPanel.setVisible(true);
        });
        backSearchButton.setOnAction(event -> {
            searchPanel.setVisible(false);
            menuVBox.setVisible(true);
        });

        addButton.setOnAction(event -> {
            addPane.setVisible(true);
            menuVBox.setVisible(false);
        });
        addBackFromAddBooks.setOnAction(event -> {
            addPane.setVisible(false);
            menuVBox.setVisible(true);
        });

        exitButton.setOnAction(event -> {
            Platform.exit();
        });

        try {
            readDB();
        } catch (FileNotFoundException e) {
            try {
                createDB();
                readDB();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        searchBoxButton.setOnAction(event -> {
            fieldSpace.getChildren().clear();
            Book[] showBooks = search(searchField.getText());
            for (int i = 0; i < showBooks.length; i++) {
                addBook(showBooks[i], fieldSpace, false);
            }
            if (showBooks.length == 0) {
                Label noResultsLabel = new Label("No Results Found");
                fieldSpace.getChildren().add(noResultsLabel);
            }
        });

        choiceScientificORLiterature.getItems().add("Literature");
        choiceScientificORLiterature.getItems().add("Scientific");
        choiceScientificORLiterature.setOnAction(event -> {
            if (choiceScientificORLiterature.getValue().equals("Literature")) {
                addType.getItems().clear();
                for (int i = 0; i < LiteratureType.values().length; i++) {
                    addType.getItems().add(LiteratureType.values()[i].toString());
                }
                addType.setValue(addType.getItems().get(0));
                addFreeText.setVisible(false);
            } else if (choiceScientificORLiterature.getValue().equals("Scientific")) {
                addType.getItems().clear();
                for (int i = 0; i < ScientificType.values().length; i++) {
                    addType.getItems().add(ScientificType.values()[i].toString());
                }
                addType.setValue(addType.getItems().get(0));
                addFreeText.setVisible(true);
            }
        });

        addBooks.setOnAction(event -> {
            errorLabel.setVisible(false);
            String sciLitType = choiceScientificORLiterature.getValue() != null ? choiceScientificORLiterature.getValue() : "";
            String title = addTittle.getText();
            String author = addAuthor.getText();
            String isbn = addISBN.getText();
            String year = addYear.getText();
            String bookType = addType.getValue() != null ? addType.getValue() : "";
            String sciField = addFreeText.getText();

            if (!sciLitType.isEmpty()
                    && !title.isEmpty()
                    && !author.isEmpty()
                    && !isbn.isEmpty()
                    && !year.isEmpty()
                    && !bookType.isEmpty()
                    && ((sciLitType.equals("Scientific") && !sciField.isEmpty()) || sciLitType.equals("Literature"))) {
                if (!title.matches(".*\\d.*")) {
                    if (!author.matches(".*\\d.*")) {
                        if (isNumber(isbn) && isbn.length() == 13 && (isbn.startsWith("978") || isbn.startsWith("979"))) {
                            if (isNumber(year) && year.length() == 4) {
                                title = normalizeString(title);
                                author = normalizeString(author);

                                Book newBook;
                                if (sciLitType.equals("Literature")) {
                                    newBook = new LiteratureBook(title, author, isbn, Integer.parseInt(year),(LiteratureType) BookType.getType(bookType));
                                } else {
                                    newBook = new ScientificBook(title, author, isbn, Integer.parseInt(year),(ScientificType) BookType.getType(bookType), sciField);
                                }

                                FileWriter fr;
                                try {
                                    fr = new FileWriter(new File("db.txt"), true);
                                    fr.write(newBook.toString() + System.getProperty("line.separator"));
                                    fr.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                books.add(newBook);
                                addBook(newBook, contentVBox, true);

                                choiceScientificORLiterature.setValue("Choose Book Type");
                                addTittle.clear();
                                addAuthor.clear();
                                addISBN.clear();
                                addYear.clear();
                                addType.setValue("Select type first");
                                addFreeText.clear();

                                addPane.setVisible(false);
                                menuVBox.setVisible(true);
                            } else {
                                errorLabel.setVisible(true);
                                errorLabel.setText("Invalid year input");
                            }
                        } else {
                            errorLabel.setVisible(true);
                            errorLabel.setText("Invalid ISBN input");
                        }
                    } else {
                        errorLabel.setVisible(true);
                        errorLabel.setText("Invalid author name");
                    }
                } else {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Invalid book title");
                }
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText("Field empty or not Selected");
            }
        });
    }
    public static boolean isNumber(String strNum) {
        try {
            double number = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    private String normalizeString(String input) {
        char[] alphabet = input.toUpperCase().toCharArray();
        for (int i = 0; i < alphabet.length; i++) {
            switch (alphabet[i]) {
                case 'Ά':
                    alphabet[i] = 'Α';
                    break;
                case 'Ί':
                    alphabet[i] = 'Ι';
                    break;
                case 'Έ':
                    alphabet[i] = 'Ε';
                    break;
                case 'Ύ':
                    alphabet[i] = 'Υ';
                    break;
                case 'Ή':
                    alphabet[i] = 'Η';
                    break;
                case 'Ό':
                    alphabet[i] = 'Ο';
                    break;
                case 'Ώ':
                    alphabet[i] = 'Ω';
                    break;
            }
        }
        return String.valueOf(alphabet);
    }
    private Book[] search(String input) {
        if (input.isEmpty()) {
            return new Book[0];
        }
        input = normalizeString(input);

        ArrayList<Book> result = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().contains(input) || books.get(i).getAuthor().contains(input)) {
                result.add(books.get(i));
            }
        }

        return result.toArray(new Book[0]);
    }
    private void addBook(Book book, VBox container, boolean addDeleteButton) {
        HBox groupBox = new HBox();
        groupBox.setAlignment(Pos.CENTER_LEFT);
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(7));
        HBox hBox = new HBox();
        Label title = new Label(book.getTitle());
        Label author = new Label(book.getAuthor());
        author.setPadding(new Insets(0, 20, 0, 0));
        Label ISBN = new Label(book.getISBN());
        Label year = new Label(String.valueOf(book.getYear()));
        Label type = new Label(book.getType().toString());


        hBox.getChildren().add(author);
        hBox.getChildren().add(year);
        vBox.getChildren().add(title);
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(ISBN);
        vBox.getChildren().add(type);
        boolean isScientific;
        try {
            ScientificType temp = (ScientificType) book.getType();
            isScientific = true;
        } catch(Exception e) {
            isScientific = false;
        }
        if (isScientific) {
            Label sciField = new Label(((ScientificBook) book).getScientificField());
            vBox.getChildren().add(sciField);
        }

        groupBox.getChildren().add(vBox);

        if (addDeleteButton) {
            Button delete = new Button("", new ImageView("https://img.icons8.com/flat_round/20/000000/delete-sign.png"));
            delete.setStyle("-fx-background-color: transparent; -fx-cursor: hand");
            delete.setOnAction(event -> {
                sureDialog.setVisible(true);
                dialogVBox.getChildren().clear();
                addBook(book, dialogVBox, false);
                yesDialogButton.setOnAction(event1 -> {
                    sureDialog.setVisible(false);

                    books.remove(book);
                    try {
                        deleteBook(book);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    container.getChildren().remove(groupBox);

                    okLabel.setVisible(true);

                    new Timer().schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    okLabel.setVisible(false);
                                }
                            },
                            2000
                    );
                });
                noDialogButton.setOnAction(event1 -> {
                    sureDialog.setVisible(false);
                });
            });
            groupBox.getChildren().add(delete);
        }

        container.getChildren().add(groupBox);
    }
    private void deleteBook(Book book) throws IOException {
        File inputFile = new File("db.txt");
        File tempFile = new File("tempDB.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = book.toString();
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToRemove)) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
    private void readDB() throws FileNotFoundException {
        File myObj = new File("db.txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            Book book = Book.createBook(data);
            books.add(book);
            addBook(book, contentVBox, true);
        }
        myReader.close();
    }
    private void createDB() throws IOException {
        Book book1 = new LiteratureBook("Ο ΞΕΝΟΣ", "CAMUS ALBERT", "9789600321999", 2005, LiteratureType.MYTHSTORY);
        Book book2 = new LiteratureBook("ΟΤΑΝ ΣΚΟΤΩΝΟΥΝ ΤΑ ΚΟΤΣΥΦΙΑ", "Lee Harper", "9789606204067", 2005, LiteratureType.MYTHSTORY);
        Book book3 = new ScientificBook("Η ΕΠΙΣΤΗΜΗ ΤΟΥ INTERSTELLAR", "Thorne Kip S.", "9786185289157", 2017, ScientificType.BOOK, "ΦΥΣΙΚΗ");
        Book book4 = new ScientificBook("ΚΛΑΣΣΙΚΗ ΜΗΧΑΝΙΚΗ", "Kibble Tom W. B.  Berkshire Frank H.", "9789605243784", 2012, ScientificType.BOOK, "ΦΥΣΙΚΗ");
        Book book5 = new ScientificBook("ΚΒΑΝΤΙΚΑ ΠΑΡΑΔΟΞΑ", "Al-Khalili Jim", "9789607990983", 2005, ScientificType.BOOK, "ΦΥΣΙΚΗ");
        Book book6 = new LiteratureBook("ΟΣΟΙ ΑΓΑΠΙΟΥΝΤΑΙ", "Hislop Victoria", "9786180131420", 2019, LiteratureType.NOVEL);
        Book book7 = new LiteratureBook("ΓΥΝΑΙΚΕΣ ΧΩΡΙΣ ΕΛΕΟΣ", "Läckberg Camilla", "9786180319286", 2019, LiteratureType.NARRATIVE);
        Book book8= new LiteratureBook("ΤΩΡΑ ΝΑ ΔΟΥΜΕ ΕΣΕΙΣ ΤΙ ΘΑ ΚΑΝΑΤΕ", "Γώγου Κατερίνα", "9789600359886", 2015, LiteratureType.POETRY);
        Book book9 = new ScientificBook("Η ΣΥΜΜΑΧΙΑ ΤΩΝ ΜΥΣΤΙΚΩΝ ΕΠΙΣΤΗΜΟΝΩΝ ΚΑΙ Ο ΚΛΕΦΤΗΣ ΙΔΕΩΝ", "Davis Eleanor", "9789601637914", 2011, ScientificType.MAGAZINE, "ΦΑΝΤΑΣΙΑΣ");
        Book book10 = new ScientificBook("ΑΝΘΡΩΠΙΝΟ ΣΩΜΑ", "Claybourne Anna", "9789606055119", 2018, ScientificType.BOOK, "ΙΑΤΡΙΚΗ");

        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);
        books.add(book5);
        books.add(book6);
        books.add(book7);
        books.add(book8);
        books.add(book9);
        books.add(book10);
        File myObj = new File("db.txt");
        myObj.createNewFile();

        FileWriter myWriter = new FileWriter("db.txt");
        for (int i = 0; i < books.size(); i++) {
            myWriter.write(books.get(i).toString()+"\n");
        }
        myWriter.close();
        System.out.println("Successfully wrote to the file.");
    }

}
