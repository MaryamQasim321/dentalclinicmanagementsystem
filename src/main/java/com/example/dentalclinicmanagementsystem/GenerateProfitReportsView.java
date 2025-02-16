package com.example.dentalclinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class GenerateProfitReportsView {
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
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<ProfitReport> profitReportTable;

    @FXML
    private TableColumn<ProfitReport, Date> profitDateColumn;

    @FXML
    private TableColumn<ProfitReport, BigDecimal> totalRevenueColumn;

    @FXML
    private TableColumn<ProfitReport, BigDecimal> totalExpenseColumn;

    @FXML
    private TableColumn<ProfitReport, BigDecimal> profitAmountColumn;

    @FXML
    private Label totalProfitLabel;

    @FXML
    public void initialize() {
        setupTableColumns();
    }

    private void setupTableColumns() {
        profitDateColumn.setCellValueFactory(new PropertyValueFactory<>("profitDate"));
        totalRevenueColumn.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
        totalExpenseColumn.setCellValueFactory(new PropertyValueFactory<>("totalExpense"));
        profitAmountColumn.setCellValueFactory(new PropertyValueFactory<>("profitAmount"));
    }

    @FXML
    private void onCalculateProfitClick() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            totalProfitLabel.setText("Please select both start and end dates.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            totalProfitLabel.setText("Start date cannot be after end date.");
            return;
        }

        ObservableList<ProfitReport> profitReports = FXCollections.observableArrayList();

        String query = "{CALL GetDailyProfitBetweenDates(?, ?)}";

        try (Connection connection = DatabaseHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall(query)) {

            callableStatement.setDate(1, java.sql.Date.valueOf(startDate));
            callableStatement.setDate(2, java.sql.Date.valueOf(endDate));

            boolean hasResultSet = callableStatement.execute();
            BigDecimal totalProfit = BigDecimal.ZERO;

            // Process the ResultSet
            if (hasResultSet) {
                try (ResultSet resultSet = callableStatement.getResultSet()) {
                    while (resultSet.next()) {
                        BigDecimal profitAmount = resultSet.getBigDecimal("profit_amount");
                        profitReports.add(new ProfitReport(
                                resultSet.getDate("profit_date"),
                                resultSet.getBigDecimal("total_revenue"),
                                resultSet.getBigDecimal("total_expense"),
                                profitAmount
                        ));
                        totalProfit = totalProfit.add(profitAmount);
                    }
                }
            }

            // Update the TableView and Total Profit Label
            profitReportTable.setItems(profitReports);
            totalProfitLabel.setText(String.format("Total Profit: $%.2f", totalProfit));

        } catch (SQLException e) {
            e.printStackTrace();
            totalProfitLabel.setText("Error fetching profit report.");
        }
    }
}
