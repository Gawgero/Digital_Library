package Classes;

public interface BookType {
    static BookType getType(String type) {
        BookType result = null;
        switch (type) {
            case "MYTHSTORY":
                result = LiteratureType.MYTHSTORY;
                break;
            case "NARRATIVE":
                result = LiteratureType.NARRATIVE;
                break;
            case "NOVEL":
                result = LiteratureType.NOVEL;
                break;
            case "POETRY":
                result = LiteratureType.POETRY;
                break;
            case "BOOK":
                result = ScientificType.BOOK;
                break;
            case "MAGAZINE":
                result = ScientificType.MAGAZINE;
                break;
            case "CONFERENCE_MINUTES":
                result = ScientificType.CONFERENCE_MINUTES;
                break;
        }
        return result;
    }
}
