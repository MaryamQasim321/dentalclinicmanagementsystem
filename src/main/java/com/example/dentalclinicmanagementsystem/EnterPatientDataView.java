package com.example.dentalclinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class EnterPatientDataView {
    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;  // Address field (fixed)

    @FXML
    private TextField emergencyContactField;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private DatePicker visitingDatePicker;

    @FXML
    private TextArea dentalHistoryArea;

    @FXML
    private TextField medicationsField;

    @FXML
    private TextField allergiesField;

    @FXML
    private TextField previousTreatmentsField;

    @FXML
    private ListView<String> medicalHistoryListView;

    @FXML
    private ListView<String> selectedMedicalHistoriesListView;

    private ObservableList<String> selectedMedicalHistories = FXCollections.observableArrayList();

    private int lastGeneratedPatientId = -1;

    @FXML
    public void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DoctorPage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }

    @FXML
    public void onAddMedicalHistory() {
        String selectedHistory = medicalHistoryListView.getSelectionModel().getSelectedItem();
        if (selectedHistory != null && !selectedMedicalHistories.contains(selectedHistory)) {
            selectedMedicalHistories.add(selectedHistory);
        }
        selectedMedicalHistoriesListView.setItems(selectedMedicalHistories);
    }
    @FXML
    public void onRemoveMedicalHistory() {
        String selectedHistory = selectedMedicalHistoriesListView.getSelectionModel().getSelectedItem();
        if (selectedHistory != null) {
            selectedMedicalHistories.remove(selectedHistory);
        }
        selectedMedicalHistoriesListView.setItems(selectedMedicalHistories);
    }

    @FXML
    public void onClickEnterPatientDataButton() {
        // Get values from UI fields
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        String dob = dobPicker.getValue() != null ? dobPicker.getValue().toString() : null;

        // If any required field is empty, set it to null instead of showing an alert
        if (firstName.isEmpty()) firstName = null;
        if (lastName.isEmpty()) lastName = null;
        if (phoneNumber.isEmpty()) phoneNumber = null;
        if (email.isEmpty()) email = null;
        if (address.isEmpty()) address = null;

        // If DOB is null, it will remain null; we will use it directly.

        try (Connection connection = DatabaseHelper.getConnection()) {
            String query = "INSERT INTO patients (first_name, last_name, date_of_birth, phone, email, address) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, dob);
                ps.setString(4, phoneNumber);
                ps.setString(5, email);
                ps.setString(6, address);
                ps.executeUpdate();

                // Retrieve the generated patient ID
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    lastGeneratedPatientId = rs.getInt(1);
                }

                // Now call the insertPatientHistory function to save the patient history
                insertPatientHistory();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Patient data and history saved!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save patient data.");
            e.printStackTrace();
        }
    }

    private void insertPatientHistory() {
        if (lastGeneratedPatientId == -1) {
            showAlert(Alert.AlertType.ERROR, "Error", "No valid patient ID. Please try again.");
            return;
        }

        // Get values from the patient history fields and set them to null if empty
        String visitDate = visitingDatePicker.getValue() != null ? visitingDatePicker.getValue().toString() : null;
        String medicalHistory = selectedMedicalHistories != null ? String.join(", ", selectedMedicalHistories) : null;
        String dentalHistory = dentalHistoryArea.getText().trim();
        String medications = medicationsField.getText().trim();
        String allergies = allergiesField.getText().trim();
        String previousTreatments = previousTreatmentsField.getText().trim();

        // Set any empty fields to null
        if (dentalHistory.isEmpty()) dentalHistory = null;
        if (medications.isEmpty()) medications = null;
        if (allergies.isEmpty()) allergies = null;
        if (previousTreatments.isEmpty()) previousTreatments = null;

        try (Connection connection = DatabaseHelper.getConnection()) {
            String query = "INSERT INTO patient_history (patient_id, visit_date, medical_history, dental_history, medications, allergies, previous_treatments) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, lastGeneratedPatientId);
                ps.setString(2, visitDate);
                ps.setString(3, medicalHistory);
                ps.setString(4, dentalHistory);
                ps.setString(5, medications);
                ps.setString(6, allergies);
                ps.setString(7, previousTreatments);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save patient history.");
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        selectedMedicalHistoriesListView.setItems(selectedMedicalHistories);
        loadMedicalHistories();
    }

    private void loadMedicalHistories() {
        try (Connection connection = DatabaseHelper.getConnection()) {
            String query = "SELECT medical_history FROM medical_history";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                ObservableList<String> medicalHistories = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    medicalHistories.add(resultSet.getString("medical_history"));
                }
                medicalHistoryListView.setItems(medicalHistories);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load medical histories.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
