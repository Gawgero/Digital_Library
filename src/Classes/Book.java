package Classes;
//εδω εχουμε τα κοινα στοιχεια που εχουν τα λογοτεχνικα βιβλια με τα επιστημονικα
public abstract class Book {
    String title;
    String author;
    String ISBN;
    int year;
    BookType type;
    public abstract String toString();
// εδω διαβαζουμε τους τιτλους τους συγγραφεις το isbn τον τυπο του βιβλιου κκαι στα επιστημονικα και λογοτεχνικα βιβλια αλλα στα επιστημονικα διαβαζουμε και το ειδος
    public static Book createBook(String data) {
        String[] dataStr = data.split(",");
        Book book;
        if (dataStr[0].equals("0")) {
            book= new LiteratureBook(dataStr[1],dataStr[2],dataStr[3],Integer.parseInt(dataStr[4]), (LiteratureType) BookType.getType(dataStr[5]));
        } else {
            book= new ScientificBook(dataStr[1],dataStr[2],dataStr[3],Integer.parseInt(dataStr[4]), (ScientificType) BookType.getType(dataStr[5]), dataStr[6]);
        }
        return book;
    }

    public String  getTitle () {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getISBN() {
        return ISBN;
    }
    public int getYear() {
        return year;
    }
    public BookType getType() {
        return type;
    }
}
