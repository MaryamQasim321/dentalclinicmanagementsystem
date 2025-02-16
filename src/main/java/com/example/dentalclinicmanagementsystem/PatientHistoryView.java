package com.example.dentalclinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class PatientHistoryView {
    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    @FXML
    public void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("DoctorPage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }

    @FXML
    private TableView<PatientHistory> patientHistoryTable;
    @FXML
    private TextField patientNameSearchField; // Search by first name

    @FXML
    private TableColumn<PatientHistory, Integer> historyIdColumn;
    @FXML
    private TableColumn<PatientHistory, Integer> patientIdColumn;
    @FXML
    private TableColumn<PatientHistory, LocalDate> visitDateColumn;
    @FXML
    private TableColumn<PatientHistory, String> medicalHistoryColumn;
    @FXML
    private TableColumn<PatientHistory, String> dentalHistoryColumn;
    @FXML
    private TableColumn<PatientHistory, String> medicationsColumn;
    @FXML
    private TableColumn<PatientHistory, String> allergiesColumn;
    @FXML
    private TableColumn<PatientHistory, String> previousTreatmentsColumn;
    @FXML
    private TableColumn<PatientHistory, String> patientNameColumn;

    @FXML
    private TableColumn<PatientHistory, Void> editColumn;

    @FXML
    public void initialize() {
        historyIdColumn.setCellValueFactory(new PropertyValueFactory<>("historyId"));
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        visitDateColumn.setCellValueFactory(new PropertyValueFactory<>("visitDate"));
        medicalHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));
        dentalHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("dentalHistory"));
        medicationsColumn.setCellValueFactory(new PropertyValueFactory<>("medications"));
        allergiesColumn.setCellValueFactory(new PropertyValueFactory<>("allergies"));
        previousTreatmentsColumn.setCellValueFactory(new PropertyValueFactory<>("previousTreatments"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        makeColumnsEditable();
//        addEditButtonToTable();
        loadPatientHistory();
    }

    private void makeColumnsEditable() {
        patientHistoryTable.setEditable(true);


        visitDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        visitDateColumn.setEditable(true);
        visitDateColumn.setOnEditCommit(event -> {
            PatientHistory history = event.getRowValue();
            history.setVisitDate(event.getNewValue());
            updateDatabase(history, "visit_date", event.getNewValue().toString());
        });

        dentalHistoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dentalHistoryColumn.setEditable(true);
        dentalHistoryColumn.setOnEditCommit(event -> {
            PatientHistory history = event.getRowValue();
            history.setDentalHistory(event.getNewValue());
            updateDatabase(history, "dental_history", event.getNewValue());
        });

        medicalHistoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        medicalHistoryColumn.setEditable(true);
        medicalHistoryColumn.setOnEditCommit(event -> {
            PatientHistory history = event.getRowValue();
            history.setMedicalHistory(event.getNewValue());
            updateDatabase(history, "medical_history", event.getNewValue());
        });

        medicationsColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        medicationsColumn.setEditable(true);
        medicationsColumn.setOnEditCommit(event -> {
            PatientHistory history = event.getRowValue();
            history.setMedications(event.getNewValue());
            updateDatabase(history, "medications", event.getNewValue());
        });

        allergiesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        allergiesColumn.setEditable(true);
        allergiesColumn.setOnEditCommit(event -> {
            PatientHistory history = event.getRowValue();
            history.setAllergies(event.getNewValue());
            updateDatabase(history, "allergies", event.getNewValue());
        });

        previousTreatmentsColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        previousTreatmentsColumn.setEditable(true);
        previousTreatmentsColumn.setOnEditCommit(event -> {
            PatientHistory history = event.getRowValue();
            history.setPreviousTreatments(event.getNewValue());
            updateDatabase(history, "previous_treatments", event.getNewValue());
        });

        patientHistoryTable.getSelectionModel().setCellSelectionEnabled(true);
        patientHistoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void addEditButtonToTable() {
        editColumn = new TableColumn<>("Edit");
        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    PatientHistory history = getTableView().getItems().get(getIndex());
                    enableRowEditing(history);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
        patientHistoryTable.getColumns().add(editColumn);
    }

    private void enableRowEditing(PatientHistory history) {
        patientHistoryTable.setEditable(true);
        patientHistoryTable.refresh();
    }

    private void updateDatabase(PatientHistory history, String columnName, String newValue) {
        String updateQuery = "UPDATE patient_history SET " + columnName + " = ? WHERE history_id = ?";
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, newValue);
            preparedStatement.setInt(2, history.getHistoryId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Update Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update the record in the database.");
            alert.showAndWait();
        }
    }

    private void loadPatientHistory() {
        ObservableList<PatientHistory> patientHistories = FXCollections.observableArrayList();
        String callProcedure = "{CALL GetAllPatientHistory()}";

        try (Connection connection = DatabaseHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall(callProcedure);
             ResultSet resultSet = callableStatement.executeQuery()) {

            while (resultSet.next()) {
                int historyId = resultSet.getInt("history_id");
                int patientId = resultSet.getInt("patient_id");
                String patientName = resultSet.getString("patient_name");

                // Fix NULL handling for visit_date
                Date sqlDate = resultSet.getDate("visit_date");
                LocalDate visitDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                String medicalHistory = resultSet.getString("medical_history");
                String dentalHistory = resultSet.getString("dental_history");
                String medications = resultSet.getString("medications");
                String allergies = resultSet.getString("allergies");
                String previousTreatments = resultSet.getString("previous_treatments");

                PatientHistory history = new PatientHistory(historyId, patientId, visitDate, medicalHistory, dentalHistory,
                        medications, allergies, previousTreatments, patientName);
                patientHistories.add(history);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        patientHistoryTable.setItems(patientHistories);
    }

    @FXML
    private void onSearchButtonClick() {
        String patientNameText = patientNameSearchField.getText();

        if (patientNameText != null && !patientNameText.trim().isEmpty()) {
            loadPatientHistoryByFirstName(patientNameText.trim());
        } else {
            loadPatientHistory();
        }
    }

    private void loadPatientHistoryByFirstName(String firstName) {
        ObservableList<PatientHistory> patientHistories = FXCollections.observableArrayList();
        String callProcedure = "{CALL SearchPatientByFirstName(?)}";

        try (Connection connection = DatabaseHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall(callProcedure)) {

            callableStatement.setString(1, firstName);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                int historyId = resultSet.getInt("history_id");
                int patientId = resultSet.getInt("patient_id");
                String patientName = resultSet.getString("patient_name");
                LocalDate visitDate = resultSet.getDate("visit_date").toLocalDate();
                String medicalHistory = resultSet.getString("medical_history");
                String dentalHistory = resultSet.getString("dental_history");
                String medications = resultSet.getString("medications");
                String allergies = resultSet.getString("allergies");
                String previousTreatments = resultSet.getString("previous_treatments");

                PatientHistory history = new PatientHistory(historyId, patientId, visitDate, medicalHistory, dentalHistory,
                        medications, allergies, previousTreatments, patientName);
                patientHistories.add(history);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        patientHistoryTable.setItems(patientHistories);
    }
}
