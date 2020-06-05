package Classes;

public class LiteratureBook extends Book {

    public LiteratureBook(String title,
                          String author,
                          String ISBN,
                          int year,
                          LiteratureType type) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.year = year;
        this.type = type;
    }

    @Override
    public String toString() {
        return "0," + title
                + ',' + author
                + ',' + ISBN
                + ',' + year
                + ',' + type;
    }
}
