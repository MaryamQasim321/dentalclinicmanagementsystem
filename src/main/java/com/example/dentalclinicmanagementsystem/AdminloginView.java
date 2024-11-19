package com.example.dentalclinicmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AdminloginView {
    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }


    @FXML
    private TextField usernameField; // Connected to the username input field

    @FXML
    private PasswordField password; // Connected to the password input field

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DentalClinicDB";
    private static final String DB_USER = "root"; // Update with your MySQL username
    private static final String DB_PASSWORD = "root"; // Update with your MySQL password

//    @FXML
//    public void handleLogin(ActionEvent event) {
//        String enteredUsername = usernameField.getText();
//        String enteredPassword = password.getText();
//
//        if (authenticateAdmin(enteredUsername, enteredPassword)) {
//            // Show success alert
//            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, Admin!");
//        } else {
//            // Show error alert
//            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password!");
//        }
//    }

    private boolean authenticateAdmin(String username, String password) {
        String query = "SELECT * FROM Admin WHERE name = ? AND password = ?";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("successful"); // Ensure this line executes
                return true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false if an exception occurs
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        System.out.println("Login button clicked"); // Debugging

        String enteredUsername = usernameField.getText();
        String enteredPassword = password.getText();

        if (authenticateAdmin(enteredUsername, enteredPassword)) {
            System.out.println("Correct credentials entered"); // Debugging
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, Admin!");
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("adminPage-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 640, 500);
                mainStage.setTitle("Dental Clinic Management System");
                mainStage.setScene(scene);
                mainStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Unable to load admin page.");
            }
        } else {
            System.out.println("Incorrect credentials entered"); // Debugging
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password!");
        }
    }

    @FXML
    public void onClickBackButton() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainScreen-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 500);
            mainStage.setTitle("Dental Clinic Management System");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Log the error to understand the issue
        }
    }
}
