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

public class ExpenditureView {
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
    private TableView<Expense> expensesTable;

    @FXML
    private TableColumn<Expense, Integer> expenseIdColumn;

    @FXML
    private TableColumn<Expense, String> expenseNameColumn;

    @FXML
    private TableColumn<Expense, Double> expenseAmountColumn;

    @FXML
    private TableColumn<Expense, String> expenseDateColumn;

    @FXML
    private TextField expenseNameField;

    @FXML
    private TextField expenseAmountField;

    @FXML
    private DatePicker expenseDatePicker;

    @FXML
    private Button addExpenseButton;

    private ObservableList<Expense> expenseList = FXCollections.observableArrayList();

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<Revenue> revenueTable;

    @FXML
    private TableColumn<Revenue, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Revenue, String> patientNameColumn;

    @FXML
    private TableColumn<Revenue, String> serviceDetailsColumn;

    @FXML
    private TableColumn<Revenue, Double> revenueGeneratedColumn;

    @FXML
    private TableColumn<Revenue, String> appointmentDateColumn;

    @FXML
    private Button generateRevenueButton;

    private ObservableList<Revenue> revenueList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up expense table columns
        expenseIdColumn.setCellValueFactory(cellData -> cellData.getValue().expenseIdProperty().asObject());
        expenseNameColumn.setCellValueFactory(cellData -> cellData.getValue().expenseNameProperty());
        expenseAmountColumn.setCellValueFactory(cellData -> cellData.getValue().expenseAmountProperty().asObject());
        expenseDateColumn.setCellValueFactory(cellData -> cellData.getValue().expenseDateProperty());

        // Load existing expenses
        loadExpenses();

        // Add expense functionality
        addExpenseButton.setOnAction(event -> addExpense());

        // Set up revenue table columns
        appointmentIdColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentIdProperty().asObject());
        patientNameColumn.setCellValueFactory(cellData -> cellData.getValue().patientNameProperty());
        serviceDetailsColumn.setCellValueFactory(cellData -> cellData.getValue().serviceNameProperty());
        revenueGeneratedColumn.setCellValueFactory(cellData -> cellData.getValue().revenueGeneratedProperty().asObject());
        appointmentDateColumn.setCellValueFactory(cellData -> cellData.getValue().appointmentDateProperty());

        generateRevenueButton.setOnAction(event -> generateRevenueReport());
    }

    private void loadExpenses() {
        expenseList.clear();
        String query = "SELECT * FROM expenses";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                expenseList.add(new Expense(
                        resultSet.getInt("expense_id"),
                        resultSet.getString("expense_name"),
                        resultSet.getDouble("expense_amount"),
                        resultSet.getDate("expense_date").toString()
                ));
            }

            expensesTable.setItems(expenseList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load expenses.");
        }
    }

    private void addExpense() {
        String expenseName = expenseNameField.getText();
        String expenseAmountText = expenseAmountField.getText();
        String expenseDate = expenseDatePicker.getValue() != null ? expenseDatePicker.getValue().toString() : null;

        if (expenseName.isEmpty() || expenseAmountText.isEmpty() || expenseDate == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
            return;
        }

        try {
            double expenseAmount = Double.parseDouble(expenseAmountText);

            String insertExpenseQuery = "INSERT INTO expenses (expense_name, expense_amount, expense_date) VALUES (?, ?, ?)";

            try (Connection connection = DatabaseHelper.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertExpenseQuery)) {

                preparedStatement.setString(1, expenseName);
                preparedStatement.setDouble(2, expenseAmount);
                preparedStatement.setDate(3, java.sql.Date.valueOf(expenseDate));
                preparedStatement.executeUpdate();
            }

            // Update the daily profit table to subtract this expense
            updateDailyProfitAfterExpense(expenseDate, expenseAmount);

            // Clear fields
            expenseNameField.clear();
            expenseAmountField.clear();
            expenseDatePicker.setValue(null);

            // Reload table
            loadExpenses();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Expense added successfully!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid number for the expense amount.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to add expense.");
        }
    }

    private void generateRevenueReport() {
        String startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : null;
        String endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : null;

        if (startDate == null || endDate == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select both start and end dates.");
            return;
        }

        String query = "{CALL GenerateRevenueReport(?, ?)}";

        revenueList.clear();

        try (Connection connection = DatabaseHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall(query)) {

            callableStatement.setDate(1, java.sql.Date.valueOf(startDate));
            callableStatement.setDate(2, java.sql.Date.valueOf(endDate));

            try (ResultSet resultSet = callableStatement.executeQuery()) {
                while (resultSet.next()) {
                    revenueList.add(new Revenue(
                            resultSet.getInt("appointment_id"),
                            resultSet.getString("patient_name"),
                            resultSet.getString("service_details"),
                            resultSet.getDouble("revenue_generated"),
                            resultSet.getDate("appointment_date").toString()
                    ));
                }

                revenueTable.setItems(revenueList);

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to fetch revenue data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to execute revenue report.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void updateDailyProfitAfterExpense(String expenseDate, double expenseAmount) {
        String updateProfitQuery = """
        INSERT INTO daily_profit (profit_date, total_revenue, total_expense, profit_amount)
        VALUES (?, 0, ?, -?)
        ON DUPLICATE KEY UPDATE 
        total_expense = total_expense + VALUES(total_expense),
        profit_amount = total_revenue - total_expense
    """;

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateProfitQuery)) {

            stmt.setDate(1, java.sql.Date.valueOf(expenseDate));
            stmt.setDouble(2, expenseAmount);
            stmt.setDouble(3, expenseAmount);
            stmt.executeUpdate();

            System.out.println("Daily profit updated after expense on " + expenseDate);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to update daily profit after expense.");
        }
    }

}
