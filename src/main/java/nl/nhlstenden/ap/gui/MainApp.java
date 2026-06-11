package nl.nhlstenden.ap.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import nl.nhlstenden.ap.algorithms.*;
import nl.nhlstenden.ap.datastructures.*;
import nl.nhlstenden.ap.model.DataRecord;
import nl.nhlstenden.ap.util.AlgorithmTimer;
import nl.nhlstenden.ap.util.DatasetLoader;

import java.io.File;
import java.util.Comparator;

public class MainApp extends Application {

    private CustomLinkedList<DataRecord> linkedList = new CustomLinkedList<>();
    private CustomHashMap<Integer, DataRecord> hashMap = new CustomHashMap<>();
    private CustomBinarySearchTree<DataRecord> bst = new CustomBinarySearchTree<>();

    private CustomList<DataRecord> workingList = new CustomArrayList<>();

    private Label statusLabel;
    private Label datasetInfoLabel;
    private TextArea resultArea;
    private ComboBox<String> dataStructureCombo;
    private ComboBox<String> sortAlgorithmCombo;
    private ComboBox<String> searchAlgorithmCombo;
    private ComboBox<String> sortFieldCombo;
    private ComboBox<String> searchFieldCombo;
    private TextField searchValueField;
    private TextField subsetField;
    private TableView<DataRecord> tableView;

    private String loadedFilePath = "None";
    private int totalRecords = 0;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        root.setTop(createTopPanel());

        root.setLeft(createControlPanel());

        root.setCenter(createResultPanel());

        statusLabel = new Label("Ready. Import a dataset to begin.");
        statusLabel.setStyle("-fx-font-size: 13px; -fx-padding: 8px;");
        root.setBottom(statusLabel);

        Scene scene = new Scene(root, 1100, 750);
        primaryStage.setTitle("Algorithmic Programming - Data Structures & Algorithms");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createTopPanel() {
        VBox topPanel = new VBox(8);
        topPanel.setPadding(new Insets(0, 0, 10, 0));

        Label titleLabel = new Label("Algorithmic Programming - Final Assignment");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox importRow = new HBox(10);
        importRow.setAlignment(Pos.CENTER_LEFT);

        Button importButton = new Button("Import CSV Dataset");
        importButton.setStyle("-fx-font-size: 13px;");
        importButton.setOnAction(e -> importDataset());

        subsetField = new TextField();
        subsetField.setPromptText("Max records (empty = all)");
        subsetField.setPrefWidth(180);

        datasetInfoLabel = new Label("No dataset loaded");
        datasetInfoLabel.setStyle("-fx-font-size: 13px;");

        importRow.getChildren().addAll(importButton, subsetField, datasetInfoLabel);
        topPanel.getChildren().addAll(titleLabel, importRow);

        return topPanel;
    }

    private VBox createControlPanel() {
        VBox controlPanel = new VBox(12);
        controlPanel.setPadding(new Insets(0, 15, 0, 0));
        controlPanel.setPrefWidth(280);

        Label dsLabel = new Label("Data Structure");
        dsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        dataStructureCombo = new ComboBox<>();
        dataStructureCombo.getItems().addAll("LinkedList", "HashMap", "BinarySearchTree");
        dataStructureCombo.setValue("LinkedList");
        dataStructureCombo.setMaxWidth(Double.MAX_VALUE);

        Button convertButton = new Button("Convert to Data Structure");
        convertButton.setMaxWidth(Double.MAX_VALUE);
        convertButton.setOnAction(e -> convertToDataStructure());

        Separator sep1 = new Separator();

        Label sortLabel = new Label("Sorting");
        sortLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        sortAlgorithmCombo = new ComboBox<>();
        sortAlgorithmCombo.getItems().addAll("Insertion Sort", "Quick Sort");
        sortAlgorithmCombo.setValue("Insertion Sort");
        sortAlgorithmCombo.setMaxWidth(Double.MAX_VALUE);

        sortFieldCombo = new ComboBox<>();
        sortFieldCombo.getItems().addAll(DataRecord.getSortableFields());
        sortFieldCombo.getItems().add("natural order");
        sortFieldCombo.setValue("title");
        sortFieldCombo.setMaxWidth(Double.MAX_VALUE);

        Button sortButton = new Button("Sort");
        sortButton.setMaxWidth(Double.MAX_VALUE);
        sortButton.setStyle("-fx-font-size: 13px;");
        sortButton.setOnAction(e -> executeSort());

        Separator sep2 = new Separator();

        Label searchLabel = new Label("Searching");
        searchLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        searchAlgorithmCombo = new ComboBox<>();
        searchAlgorithmCombo.getItems().addAll("Sequential Search", "Binary Search");
        searchAlgorithmCombo.setValue("Sequential Search");
        searchAlgorithmCombo.setMaxWidth(Double.MAX_VALUE);

        searchFieldCombo = new ComboBox<>();
        searchFieldCombo.getItems().addAll(DataRecord.getSortableFields());
        searchFieldCombo.getItems().add("natural order");
        searchFieldCombo.setValue("title");
        searchFieldCombo.setMaxWidth(Double.MAX_VALUE);

        searchValueField = new TextField();
        searchValueField.setPromptText("Search value...");

        Button searchButton = new Button("Search");
        searchButton.setMaxWidth(Double.MAX_VALUE);
        searchButton.setStyle("-fx-font-size: 13px;");
        searchButton.setOnAction(e -> executeSearch());

        controlPanel.getChildren().addAll(
                dsLabel, dataStructureCombo, convertButton,
                sep1,
                sortLabel, new Label("Algorithm:"), sortAlgorithmCombo,
                new Label("Sort by field:"), sortFieldCombo, sortButton,
                sep2,
                searchLabel, new Label("Algorithm:"), searchAlgorithmCombo,
                new Label("Search by field:"), searchFieldCombo, searchValueField, searchButton
        );

        return controlPanel;
    }

    private VBox createResultPanel() {
        VBox resultPanel = new VBox(10);

        tableView = new TableView<>();
        tableView.setPrefHeight(400);

        TableColumn<DataRecord, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<DataRecord, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(250);

        TableColumn<DataRecord, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(80);

        TableColumn<DataRecord, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingCol.setPrefWidth(80);

        TableColumn<DataRecord, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setPrefWidth(120);

        tableView.getColumns().addAll(idCol, titleCol, yearCol, ratingCol, genreCol);

        Label resultLabel = new Label("Output Log");
        resultLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(180);
        resultArea.setStyle("-fx-font-family: monospace;");

        resultPanel.getChildren().addAll(tableView, resultLabel, resultArea);
        return resultPanel;
    }

    private void importDataset() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV Dataset");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }

        try {
            linkedList.clear();
            hashMap.clear();
            bst.clear();
            workingList = new CustomArrayList<>();

            int maxRecords = Integer.MAX_VALUE;
            String subsetText = subsetField.getText().trim();
            if (!subsetText.isEmpty()) {
                maxRecords = Integer.parseInt(subsetText);
            }

            DatasetLoader.load(file.getAbsolutePath(), linkedList, maxRecords);
            totalRecords = linkedList.size();
            loadedFilePath = file.getName();

            datasetInfoLabel.setText("Loaded: " + loadedFilePath + " | Records: " + totalRecords);
            statusLabel.setText("Dataset imported successfully. Convert to a data structure to continue.");

            refreshTable(linkedList);
            logResult("Imported " + totalRecords + " records from " + loadedFilePath);

        } catch (Exception e) {
            statusLabel.setText("Error importing dataset: " + e.getMessage());
            logResult("ERROR: " + e.getMessage());
        }
    }

    private void convertToDataStructure() {
        if (linkedList.isEmpty()) {
            statusLabel.setText("Import a dataset first!");
            return;
        }

        String selected = dataStructureCombo.getValue();
        AlgorithmTimer timer = new AlgorithmTimer();

        timer.start();

        switch (selected) {
            case "LinkedList":
                // The sort/search algorithms are generic over CustomList, and
                // CustomLinkedList implements it, so they run directly on the
                // linked list itself rather than on a copied buffer.
                workingList = linkedList;
                break;

            case "HashMap":
                // A HashMap is not index-addressable, so its values are
                // flattened into an array-backed list for the algorithms.
                hashMap.clear();
                for (DataRecord record : linkedList) {
                    hashMap.put(record.getId(), record);
                }
                workingList = new CustomArrayList<>();
                for (DataRecord record : hashMap.values()) {
                    workingList.add(record);
                }
                break;

            case "BinarySearchTree":
                // A BST is not index-addressable, so its in-order traversal is
                // flattened into an array-backed list for the algorithms.
                bst.clear();
                for (DataRecord record : linkedList) {
                    bst.insert(record);
                }
                workingList = new CustomArrayList<>();
                for (DataRecord record : bst.inOrder()) {
                    workingList.add(record);
                }
                break;
        }

        timer.stop();

        refreshTable(workingList);
        String msg = "Converted " + totalRecords + " records to " + selected + " in " + timer.getElapsedDetailed();
        statusLabel.setText(msg);
        logResult(msg);
    }

    private void executeSort() {
        if (workingList.isEmpty()) {
            statusLabel.setText("Convert data to a structure first!");
            return;
        }

        String algoName = sortAlgorithmCombo.getValue();
        String fieldName = sortFieldCombo.getValue();
        boolean natural = fieldName.equalsIgnoreCase("natural order");

        Sorter<DataRecord> sorter;
        if (algoName.equals("Insertion Sort")) {
            sorter = new InsertionSort<>();
        } else {
            sorter = new QuickSort<>();
        }

        AlgorithmTimer timer = new AlgorithmTimer();
        timer.start();
        if (natural) {
            sorter.sort(workingList);
        } else {
            sorter.sort(workingList, DataRecord.getComparatorByField(fieldName));
        }
        timer.stop();

        refreshTable(workingList);

        String msg = sorter.getName() + " by " + fieldName
                + " | Data Structure: " + dataStructureCombo.getValue()
                + " | Records: " + workingList.size()
                + " | Speed: " + timer.getElapsedDetailed();
        statusLabel.setText(msg);
        logResult(msg);
    }

    private void executeSearch() {
        if (workingList.isEmpty()) {
            statusLabel.setText("Convert data to a structure first!");
            return;
        }

        String searchValue = searchValueField.getText().trim();
        if (searchValue.isEmpty()) {
            statusLabel.setText("Enter a search value!");
            return;
        }

        String algoName = searchAlgorithmCombo.getValue();
        String fieldName = searchFieldCombo.getValue();
        boolean natural = fieldName.equalsIgnoreCase("natural order");

        DataRecord target;
        Comparator<DataRecord> comparator;
        if (natural) {
            target = new DataRecord(0, searchValue, 0, 0.0, "");
            comparator = null;
        } else {
            target = createSearchTarget(fieldName, searchValue);
            if (target == null) {
                statusLabel.setText("Invalid search value for field: " + fieldName);
                return;
            }
            comparator = DataRecord.getComparatorByField(fieldName);
        }

        if (algoName.equals("Binary Search")) {
            if (natural) {
                new QuickSort<DataRecord>().sort(workingList);
            } else {
                new QuickSort<DataRecord>().sort(workingList, comparator);
            }
            refreshTable(workingList);
        }

        Searcher<DataRecord> searcher;
        if (algoName.equals("Sequential Search")) {
            searcher = new SequentialSearch<>();
        } else {
            searcher = new BinarySearch<>();
        }

        AlgorithmTimer timer = new AlgorithmTimer();
        timer.start();
        int resultIndex = natural
                ? searcher.search(workingList, target)
                : searcher.search(workingList, target, comparator);
        timer.stop();

        String msg;
        if (resultIndex != -1) {
            DataRecord found = workingList.get(resultIndex);
            msg = searcher.getName() + " by " + fieldName + " = '" + searchValue + "'"
                    + " | Data Structure: " + dataStructureCombo.getValue()
                    + " | FOUND at index " + resultIndex + ": " + found
                    + " | Speed: " + timer.getElapsedDetailed();

            tableView.getSelectionModel().select(resultIndex);
            tableView.scrollTo(resultIndex);
        } else {
            msg = searcher.getName() + " by " + fieldName + " = '" + searchValue + "'"
                    + " | Data Structure: " + dataStructureCombo.getValue()
                    + " | NOT FOUND"
                    + " | Speed: " + timer.getElapsedDetailed();
        }

        statusLabel.setText(msg);
        logResult(msg);
    }

    private DataRecord createSearchTarget(String fieldName, String value) {
        try {
            return switch (fieldName.toLowerCase()) {
                case "id" -> new DataRecord(Integer.parseInt(value), "", 0, 0.0, "");
                case "title" -> new DataRecord(0, value, 0, 0.0, "");
                case "year" -> new DataRecord(0, "", Integer.parseInt(value), 0.0, "");
                case "rating" -> new DataRecord(0, "", 0, Double.parseDouble(value), "");
                case "genre" -> new DataRecord(0, "", 0, 0.0, value);
                default -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void refreshTable(CustomList<DataRecord> list) {
        tableView.getItems().clear();
        for (DataRecord record : list) {
            tableView.getItems().add(record);
        }
    }

    private void logResult(String message) {
        resultArea.appendText(message + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
