package com.example.dentalclinicmanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminPageView {
    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    // FXML fields
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField roleField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField hireDateField;

    @FXML
    private TextField passwordField;

    static int id = 2; // Starting ID (increment if you have existing records)

    // Method to insert doctor data into the database
    private void insertDoctorData(String firstName, String lastName, String role, String phoneNumber, String email, String hireDate, String password) {
        String query = "INSERT INTO doctors (doctor_id, first_name, last_name, role, phone, email, hire_date, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            id++; // Increment the doctor_id
            preparedStatement.setInt(1, id); // Set doctor_id
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, role);
            preparedStatement.setString(5, phoneNumber); // Matches "phone" in the table
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, hireDate);
            preparedStatement.setString(8, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Doctor data inserted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failure", "Failed to insert doctor data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while inserting data.");
        }
    }

    // Method to handle "Enter Data" button click
    @FXML
    public void onEnterData() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String role = roleField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String hireDate = hireDateField.getText(); // Format: YYYY-MM-DD
        String password = passwordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || role.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || hireDate.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "All fields are required!");
            return;
        }

        // Insert data into the database
        insertDoctorData(firstName, lastName, role, phoneNumber, email, hireDate, password);

        // Clear the fields after inserting
        firstNameField.clear();
        lastNameField.clear();
        roleField.clear();
        phoneNumberField.clear();
        emailField.clear();
        hireDateField.clear();
        passwordField.clear();
    }

    // Method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to handle "Back" button click
    @FXML
    public void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainScreen-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }
}
