package com.example.dentalclinicmanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorLoginView {

    @FXML
    private TextField name; // Username field

    @FXML
    private TextField password; // Password field

    @FXML
    private Button login; // Login button

    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    // Handle the login button click
    @FXML
    public void onLoginClick() {
        String username = name.getText();
        String userPassword = password.getText();

        if (username.isEmpty() || userPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter your username and password.");
            return;
        }

        if (authenticateDoctor(username, userPassword)) {
            try {
                // Redirect to the DoctorPage-view.fxml
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("DoctorPage-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 640, 500);
                mainStage.setTitle("Doctor Dashboard");
                mainStage.setScene(scene);
                mainStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the Doctor Dashboard.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials. Please try again.");
        }
    }

    // Authenticate the doctor using the database
    private boolean authenticateDoctor(String username, String userPassword) {
        String query = "SELECT * FROM Doctors WHERE first_name = ? AND password = ?";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, userPassword);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Return true if a match is found
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while verifying credentials.");
        }

        return false;
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("mainScreen-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
}
