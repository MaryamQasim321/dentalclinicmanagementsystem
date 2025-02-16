package com.example.dentalclinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewPricingHistory {
    private static Stage mainStage;

    @FXML
    private TableView<PriceHistory> treatmentsTable;

    @FXML
    private TableColumn<PriceHistory, Integer> treatmentHistoryIdColumn;

    @FXML
    private TableColumn<PriceHistory, Integer> treatmentProductIdColumn;

    @FXML
    private TableColumn<PriceHistory, String> treatmentNameColumn;

    @FXML
    private TableColumn<PriceHistory, Double> treatmentOldPriceColumn;

    @FXML
    private TableColumn<PriceHistory, Double> treatmentNewPriceColumn;

    @FXML
    private TableColumn<PriceHistory, String> treatmentChangeDateColumn;

    @FXML
    private TableView<PriceHistory> productsTable;

    @FXML
    private TableColumn<PriceHistory, Integer> productHistoryIdColumn;

    @FXML
    private TableColumn<PriceHistory, Integer> productProductIdColumn;

    @FXML
    private TableColumn<PriceHistory, String> productNameColumn;

    @FXML
    private TableColumn<PriceHistory, Double> productOldPriceColumn;

    @FXML
    private TableColumn<PriceHistory, Double> productNewPriceColumn;

    @FXML
    private TableColumn<PriceHistory, String> productChangeDateColumn;

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
    public void initialize() {
        setupTables();
        loadPricingHistory("treatment", treatmentsTable); // Load treatment pricing history
        loadPricingHistory("product", productsTable); // Load product pricing history
    }

    private void setupTables() {
        // Setup Treatments Table Columns
        treatmentHistoryIdColumn.setCellValueFactory(cellData -> cellData.getValue().historyIdProperty().asObject());
        treatmentProductIdColumn.setCellValueFactory(cellData -> cellData.getValue().productIdProperty().asObject());
        treatmentNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        treatmentOldPriceColumn.setCellValueFactory(cellData -> cellData.getValue().oldPriceProperty().asObject());
        treatmentNewPriceColumn.setCellValueFactory(cellData -> cellData.getValue().newPriceProperty().asObject());
        treatmentChangeDateColumn.setCellValueFactory(cellData -> cellData.getValue().changeDateProperty());

        // Setup Products Table Columns
        productHistoryIdColumn.setCellValueFactory(cellData -> cellData.getValue().historyIdProperty().asObject());
        productProductIdColumn.setCellValueFactory(cellData -> cellData.getValue().productIdProperty().asObject());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        productOldPriceColumn.setCellValueFactory(cellData -> cellData.getValue().oldPriceProperty().asObject());
        productNewPriceColumn.setCellValueFactory(cellData -> cellData.getValue().newPriceProperty().asObject());
        productChangeDateColumn.setCellValueFactory(cellData -> cellData.getValue().changeDateProperty());
    }

    private void loadPricingHistory(String type, TableView<PriceHistory> tableView) {
        ObservableList<PriceHistory> pricingHistory = FXCollections.observableArrayList();

        // Call the GetPricingHistory procedure
        try (Connection connection = DatabaseHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall("{CALL GetPricingHistory(?)}")) {

            callableStatement.setString(1, type); // Pass "treatment" or "product" as input

            try (ResultSet resultSet = callableStatement.executeQuery()) {
                while (resultSet.next()) {
                    pricingHistory.add(new PriceHistory(
                            resultSet.getInt("history_id"),          // history_id
                            resultSet.getInt("item_id"),             // treatment_id or product_id
                            resultSet.getString("item_name"),        // treatment_name or product_name
                            resultSet.getDouble("old_price"),        // old_price
                            resultSet.getDouble("new_price"),        // new_price
                            resultSet.getString("change_date")       // change_date
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load " + type + " pricing history.");
        }

        tableView.setItems(pricingHistory); // Set the data in the appropriate table
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
