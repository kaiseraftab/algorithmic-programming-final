package nl.nhlstenden.ap.model;

import java.util.Comparator;

public class DataRecord implements Comparable<DataRecord> {

    private int id;
    private String title;
    private int year;
    private double rating;
    private String genre;

    public DataRecord(int id, String title, int year, double rating, String genre) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public static final Comparator<DataRecord> BY_ID = Comparator.comparingInt(DataRecord::getId);

    public static final Comparator<DataRecord> BY_TITLE = Comparator.comparing(DataRecord::getTitle);

    public static final Comparator<DataRecord> BY_YEAR = Comparator.comparingInt(DataRecord::getYear);

    public static final Comparator<DataRecord> BY_RATING = Comparator.comparingDouble(DataRecord::getRating);

    public static final Comparator<DataRecord> BY_GENRE = Comparator.comparing(DataRecord::getGenre);

    public static Comparator<DataRecord> getComparatorByField(String fieldName) {
        return switch (fieldName.toLowerCase()) {
            case "id" -> BY_ID;
            case "title" -> BY_TITLE;
            case "year" -> BY_YEAR;
            case "rating" -> BY_RATING;
            case "genre" -> BY_GENRE;
            default -> throw new IllegalArgumentException("Unknown field: " + fieldName);
        };
    }

    public static String[] getSortableFields() {
        return new String[]{"id", "title", "year", "rating", "genre"};
    }

    public String getFieldValue(String fieldName) {
        return switch (fieldName.toLowerCase()) {
            case "id" -> String.valueOf(id);
            case "title" -> title;
            case "year" -> String.valueOf(year);
            case "rating" -> String.valueOf(rating);
            case "genre" -> genre;
            default -> "N/A";
        };
    }

    @Override
    public int compareTo(DataRecord other) {
        return this.title.compareTo(other.title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DataRecord that = (DataRecord) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("DataRecord{id=%d, title='%s', year=%d, rating=%.1f, genre='%s'}",
                id, title, year, rating, genre);
    }
}
