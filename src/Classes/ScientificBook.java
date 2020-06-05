package Classes;

public class ScientificBook extends Book {
    String scientificField;
    public ScientificBook(String title,
                          String author,
                          String ISBN,
                          int year,
                          ScientificType type,
                          String scientificField) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.year = year;
        this.type = type;
        this.scientificField = scientificField;
    }

    @Override
    public String toString() {
        return "1," + title
                + ',' + author
                + ',' + ISBN
                + ',' + year
                + ',' + type
                + ',' + scientificField;
    }
    public String getScientificField() {
        return scientificField;
    }
}
