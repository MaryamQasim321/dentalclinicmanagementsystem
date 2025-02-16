package com.example.dentalclinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ViewTreatmentsView {

    private static Stage mainStage;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    @FXML
    private TableColumn<Treatment, Integer> idColumn;
    @FXML
    private TableColumn<Treatment, String> nameColumn;
    @FXML
    private TableColumn<Treatment, String> descriptionColumn;
    @FXML
    private TableColumn<Treatment, Double> costColumn;
    @FXML
    private TableColumn<Treatment, Void> actionColumn; // Column for action buttons
    @FXML
    private TableView<Treatment> treatmentsTable;
    @FXML
    private VBox dynamicPane;
    @FXML
    private Button enterTreatmentButton;

    private boolean isFormVisible = false;

    @FXML
    public void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("DoctorPage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        addActionButtons();
        loadTreatments();
        enterTreatmentButton.setOnAction(event -> toggleEnterTreatmentForm());
    }

    private void toggleEnterTreatmentForm() {
        if (isFormVisible) {
            dynamicPane.getChildren().clear();
            isFormVisible = false;
        } else {
            Label nameLabel = new Label("Enter Treatment Name:");
            nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #30527a; -fx-font-weight: bold;");

            TextField nameField = new TextField();
            nameField.setPromptText("Name");
            nameField.setStyle("-fx-border-color: #30527a; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5;");

            Label descriptionLabel = new Label("Enter Description:");
            descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #30527a; -fx-font-weight: bold;");

            TextField descriptionField = new TextField();
            descriptionField.setPromptText("Description");
            descriptionField.setStyle("-fx-border-color: #30527a; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5;");

            Label costLabel = new Label("Enter Cost:");
            costLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #30527a; -fx-font-weight: bold;");

            TextField costField = new TextField();
            costField.setPromptText("Cost");
            costField.setStyle("-fx-border-color: #30527a; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5;");

            Button submitButton = new Button("Submit");
            submitButton.setStyle("-fx-background-color: #30527a; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5; -fx-background-radius: 5;");

            submitButton.setOnAction(e -> handleSubmit(nameField, descriptionField, costField));

            Button cancelButton = new Button("Cancel");
            cancelButton.setStyle("-fx-background-color: #30527a; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5; -fx-background-radius: 5;");
            cancelButton.setOnAction(e -> {
                dynamicPane.getChildren().clear();
                isFormVisible = false;
            });

            dynamicPane.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionField, costLabel, costField, submitButton, cancelButton);

            isFormVisible = true;
        }
    }

    private void handleSubmit(TextField nameField, TextField descriptionField, TextField costField) {
        String name = nameField.getText();
        String description = descriptionField.getText();
        String costText = costField.getText();

        if (name.isEmpty() || description.isEmpty() || costText.isEmpty()) {
            showAlert("Validation Error", "All fields are required!");
            return;
        }

        try {
            double cost = Double.parseDouble(costText);
            int treatmentId = getNextTreatmentId();

            try (Connection connection = DatabaseHelper.getConnection()) {
                String query = "INSERT INTO treatments (treatment_id, treatment_name, description, cost) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, treatmentId);
                statement.setString(2, name);
                statement.setString(3, description);
                statement.setDouble(4, cost);
                statement.executeUpdate();

                loadTreatments();
                dynamicPane.getChildren().clear();
                isFormVisible = false;
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Failed to add treatment.");
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Cost must be a valid number.");
        }
    }

    private int getNextTreatmentId() {
        String query = "SELECT MAX(treatment_id) AS max_id FROM treatments";
        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void loadTreatments() {
        ObservableList<Treatment> treatments = FXCollections.observableArrayList();
        String query = "SELECT * FROM treatments";

        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("treatment_id");
                String name = resultSet.getString("treatment_name");
                String description = resultSet.getString("description");
                double cost = resultSet.getDouble("cost");

                treatments.add(new Treatment(id, name, description, cost));
            }

            treatmentsTable.setItems(treatments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Treatment treatment = getTableView().getItems().get(getIndex());
                    editTreatment(treatment);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton);
                    setGraphic(buttons);
                }
            }
        });
    }


    private void editTreatment(Treatment treatment) {
        if (treatment == null) return;

        // Create dialog for editing name
        TextInputDialog nameDialog = new TextInputDialog(treatment.getName());
        nameDialog.setTitle("Edit Treatment");
        nameDialog.setHeaderText("Edit Treatment Name");
        nameDialog.setContentText("Enter new name:");
        Optional<String> nameResult = nameDialog.showAndWait();

        // Create dialog for editing price
        TextInputDialog priceDialog = new TextInputDialog(String.valueOf(treatment.getCost()));
        priceDialog.setTitle("Edit Treatment");
        priceDialog.setHeaderText("Edit Treatment Price");
        priceDialog.setContentText("Enter new price:");
        Optional<String> priceResult = priceDialog.showAndWait();

        // If both dialogs have valid inputs, proceed to update the database
        if (nameResult.isPresent() && priceResult.isPresent()) {
            String newName = nameResult.get();
            String newPriceText = priceResult.get();

            try {
                double newPrice = Double.parseDouble(newPriceText);

                // Update the treatment in the database
                String updateQuery = "UPDATE treatments SET treatment_name = ?, cost = ? WHERE treatment_id = ?";
                try (Connection connection = DatabaseHelper.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

                    preparedStatement.setString(1, newName);
                    preparedStatement.setDouble(2, newPrice);
                    preparedStatement.setInt(3, treatment.getId());
                    preparedStatement.executeUpdate();

                    // Update the treatment in the table
                    treatment.setName(newName);
                    treatment.setCost(newPrice);
                    treatmentsTable.refresh();

                    showAlert("Success", "Treatment updated successfully.");
                }
            } catch (NumberFormatException e) {
                showAlert("Validation Error", "Price must be a valid number.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Failed to update treatment.");
            }
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
