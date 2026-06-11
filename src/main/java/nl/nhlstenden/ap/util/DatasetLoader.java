package nl.nhlstenden.ap.util;

import nl.nhlstenden.ap.datastructures.CustomList;
import nl.nhlstenden.ap.model.DataRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DatasetLoader {

    public static void loadAll(String filePath, CustomList<DataRecord> list) throws IOException {
        load(filePath, list, Integer.MAX_VALUE);
    }

    public static void load(String filePath, CustomList<DataRecord> list, int maxRecords) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("Empty CSV file");
            }

            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < maxRecords) {
                DataRecord record = parseLine(line);
                if (record != null) {
                    list.add(record);
                    count++;
                }
            }
        }
    }

    private static DataRecord parseLine(String line) {
        try {
            String[] fields = splitCsvLine(line);
            if (fields.length < 5) {
                return null;
            }

            int id = Integer.parseInt(fields[0].trim());
            String title = fields[1].trim();
            int year = Integer.parseInt(fields[2].trim());
            double rating = Double.parseDouble(fields[3].trim());
            String genre = fields[4].trim();

            return new DataRecord(id, title, year, rating, genre);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String[] splitCsvLine(String line) {
        java.util.List<String> fields = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());

        return fields.toArray(new String[0]);
    }
}
