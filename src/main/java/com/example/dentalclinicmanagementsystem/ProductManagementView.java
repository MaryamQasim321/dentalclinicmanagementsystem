package com.example.dentalclinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProductManagementView {

    @FXML
    private TextField productNameField;
    private static Stage mainStage;
    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    @FXML
    private TextField productPriceField;

    @FXML
    private TextField productQuantityField;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Double> pricePerUnitColumn;

    @FXML
    private TableColumn<Product, Integer> quantityAvailableColumn;

    @FXML
    private TableColumn<Product, String> lastUpdatedColumn;

    @FXML
    private Label lowStockLabel;

    private ObservableList<Product> productList;

    @FXML
    public void initialize() {
        // Initialize the TableView columns
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        pricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));
        quantityAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
        lastUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        productList = FXCollections.observableArrayList();
        productTableView.setItems(productList);

        loadProducts(); // Load product data
        checkLowStock(); // Update low stock alerts
    }

    @FXML
    private void onAddUpdateProductClick() {
        String productName = productNameField.getText();
        String productPriceText = productPriceField.getText();
        String productQuantityText = productQuantityField.getText();

        if (productName.isEmpty() || productPriceText.isEmpty() || productQuantityText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill out all fields.");
            return;
        }

        try {
            double productPrice = Double.parseDouble(productPriceText);
            int productQuantity = Integer.parseInt(productQuantityText);

            if (productPrice <= 0 || productQuantity < 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Price must be positive and quantity cannot be negative.");
                return;
            }

            saveOrUpdateProduct(productName, productPrice, productQuantity);
            clearForm();
            loadProducts();
            checkLowStock();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Price and quantity must be numeric values.");
        }
    }


    private void saveOrUpdateProduct(String productName, Double productPrice, Integer productQuantity) {
        String query;
        boolean isUpdate = false;

        try (Connection connection = DatabaseHelper.getConnection()) {
            // Check if product already exists
            query = "SELECT product_id FROM products WHERE product_name = ?";
            PreparedStatement checkStmt = connection.prepareStatement(query);
            checkStmt.setString(1, productName);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                // Update existing product
                StringBuilder updateQuery = new StringBuilder("UPDATE products SET ");
                boolean updateRequired = false;

                if (productPrice != null) {
                    updateQuery.append("price_per_unit = ?, ");
                    updateRequired = true;
                }

                if (productQuantity != null) {
                    updateQuery.append("quantity_available = ?, ");
                    updateRequired = true;
                }

                if (!updateRequired) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Operation", "Please provide either price or quantity to update.");
                    return;
                }

                updateQuery.append("updated_at = CURRENT_TIMESTAMP WHERE product_name = ?");
                query = updateQuery.toString();
                isUpdate = true;

                PreparedStatement stmt = connection.prepareStatement(query);
                int paramIndex = 1;

                if (productPrice != null) {
                    stmt.setDouble(paramIndex++, productPrice);
                }

                if (productQuantity != null) {
                    stmt.setInt(paramIndex++, productQuantity);
                }

                stmt.setString(paramIndex, productName);
                stmt.executeUpdate();
            } else {
                // Add new product
                query = "INSERT INTO products (product_name, price_per_unit, quantity_available) VALUES (?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, productName);
                stmt.setDouble(2, productPrice);
                stmt.setInt(3, productQuantity);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while saving the product.");
        }
    }


    private void loadProducts() {
        productList.clear();
        String query = "SELECT product_name, price_per_unit, quantity_available, updated_at FROM products";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                String productName = resultSet.getString("product_name");
                double pricePerUnit = resultSet.getDouble("price_per_unit");
                int quantityAvailable = resultSet.getInt("quantity_available");
                String lastUpdated = resultSet.getTimestamp("updated_at").toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                productList.add(new Product(productName, pricePerUnit, quantityAvailable, lastUpdated));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while loading products.");
        }
    }

    private void checkLowStock() {
        StringBuilder lowStockProducts = new StringBuilder("Low stock alert for: ");
        boolean hasLowStock = false;

        for (Product product : productList) {
            if (product.getQuantityAvailable() <= 10) {
                hasLowStock = true;
                lowStockProducts.append(product.getProductName()).append(", ");
            }
        }

        if (hasLowStock) {
            lowStockLabel.setText(lowStockProducts.substring(0, lowStockProducts.length() - 2));
        } else {
            lowStockLabel.setText("No low stock alerts.");
        }
    }

    private void clearForm() {
        productNameField.clear();
        productPriceField.clear();
        productQuantityField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("DoctorPage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }

}
