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
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookAppointmentView {
    private static Stage mainStage;

    @FXML
    private ComboBox<String> treatmentComboBox;
    @FXML
    private ListView<String> selectedTreatmentsListView;
    @FXML
    private TextField patientNameField;
    @FXML
    private TextField doctorNameField;
    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private TextArea notesTextArea;
    @FXML
    private ChoiceBox<String> statusChoiceBox;

    private ObservableList<String> selectedTreatments = FXCollections.observableArrayList();

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    @FXML
    public void initialize() {
        loadTreatments();
        loadStatuses();
        selectedTreatmentsListView.setItems(selectedTreatments);
    }

    private void loadTreatments() {
        String query = "SELECT treatment_name FROM Treatments";
        try (Connection connection = DatabaseHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ObservableList<String> treatments = FXCollections.observableArrayList();
            while (resultSet.next()) {
                treatments.add(resultSet.getString("treatment_name"));
            }
            treatmentComboBox.setItems(treatments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load treatments.");
        }
    }

    private void loadStatuses() {
        ObservableList<String> statuses = FXCollections.observableArrayList("Scheduled", "Completed", "Cancelled");
        statusChoiceBox.setItems(statuses);
    }

    @FXML
    public void onAddTreatment() {
        String selectedTreatment = treatmentComboBox.getValue();
        if (selectedTreatment != null && !selectedTreatments.contains(selectedTreatment)) {
            selectedTreatments.add(selectedTreatment);
        }
    }

    @FXML
    public void onRemoveTreatment() {
        String selectedTreatment = selectedTreatmentsListView.getSelectionModel().getSelectedItem();
        if (selectedTreatment != null) {
            selectedTreatments.remove(selectedTreatment);
        }
    }

    @FXML
    public void onSubmitAppointment() {
        String patientName = patientNameField.getText();
        String doctorName = doctorNameField.getText();
        LocalDate appointmentDate = appointmentDatePicker.getValue();
        String status = statusChoiceBox.getValue();
        String notes = notesTextArea.getText();

        if (patientName.isEmpty() || doctorName.isEmpty() || appointmentDate == null || status == null || selectedTreatments.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Data", "Please fill in all fields and select at least one treatment.");
            return;
        }

        try (Connection connection = DatabaseHelper.getConnection()) {
            // Ensure patient exists
            int patientId = fetchOrCreatePatient(connection, patientName);

            // Ensure doctor exists
            int doctorId = fetchOrCreateDoctor(connection, doctorName);

            // Generate a new unique appointment ID
            int appointmentId = getNextAppointmentId(connection);

            // Insert new appointment
            String insertAppointmentQuery = "INSERT INTO Appointments (appointment_id, patient_id, doctor_id, appointment_date, status, notes) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement appointmentStatement = connection.prepareStatement(insertAppointmentQuery)) {
                appointmentStatement.setInt(1, appointmentId);
                appointmentStatement.setInt(2, patientId);
                appointmentStatement.setInt(3, doctorId);
                appointmentStatement.setTimestamp(4, Timestamp.valueOf(appointmentDate.atStartOfDay()));
                appointmentStatement.setString(5, status);
                appointmentStatement.setString(6, notes);
                appointmentStatement.executeUpdate();
            }

            // Insert treatments into Appointment_Treatments
            insertAppointmentTreatments(connection, appointmentId, selectedTreatments);

            // Insert bill details and pass appointmentDate
            insertBillDetails(connection, appointmentId, patientId, selectedTreatments, appointmentDate);

            // Fetch and display bill details
            showBillDetails(appointmentId);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to book appointment.");
        }
    }


    private void insertBillDetails(Connection connection, int appointmentId, int patientId, ObservableList<String> treatments, LocalDate appointmentDate) throws SQLException {
        String insertBillQuery = "INSERT INTO billing (appointment_id, patient_id, total_bill, extra_cost, status) VALUES (?, ?, ?, ?, ?)";

        double totalBill = calculateTotalBill(connection, treatments);
        double extraCost = 0.0;
        double profitAmount = totalBill - extraCost;

        try (PreparedStatement stmt = connection.prepareStatement(insertBillQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointmentId);
            stmt.setInt(2, patientId);
            stmt.setDouble(3, totalBill);
            stmt.setDouble(4, extraCost);
            stmt.setString(5, "pending");

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int billingId = generatedKeys.getInt(1);
                System.out.println("Billing record inserted successfully. Billing ID: " + billingId);

                // Insert revenue with the correct appointment date
                insertRevenue(connection, appointmentId, patientId, profitAmount, appointmentDate);

                // Update daily profit on the appointment date
                updateDailyProfit(connection, profitAmount, extraCost, appointmentDate);
            }
        }
    }



    private double calculateTotalBill(Connection connection, ObservableList<String> treatments) throws SQLException {
        double total = 0.0;
        String query = "SELECT cost FROM Treatments WHERE treatment_name = ?";

        for (String treatment : treatments) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, treatment);
                ResultSet resultSet = stmt.executeQuery();
                if (resultSet.next()) {
                    total += resultSet.getDouble("cost");
                }
            }
        }
        return total;
    }

    private void showBillDetails(int appointmentId) {
        try (Connection connection = DatabaseHelper.getConnection()) {
            String query = "SELECT * FROM bill_details WHERE appointment_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, appointmentId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String billContent = String.format("""
                            ************ Dental Clinic Appointment Bill ************
                            Billing ID: %d
                            Patient Name: %s
                            Appointment Date: %s
                            Treatments: %s
                            Total Cost: $%.2f
                            Extra Cost: $%.2f
                            *******************************************************
                            """,
                            resultSet.getInt("billing_id"),
                            resultSet.getString("patient_name"),
                            resultSet.getTimestamp("appointment_date"),
                            resultSet.getString("treatments"),
                            resultSet.getDouble("total_bill"),
                            resultSet.getDouble("extra_cost"));

                    showAlert(Alert.AlertType.INFORMATION, "Appointment Bill", billContent);
                } else {
                    showAlert(Alert.AlertType.WARNING, "No Bill Found", "Bill details are not available.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to fetch bill details.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private int fetchOrCreatePatient(Connection connection, String patientName) throws SQLException {
        return fetchOrCreateId(connection, "Patients", "patient_id", "first_name", patientName);
    }

    private int fetchOrCreateDoctor(Connection connection, String doctorName) throws SQLException {
        return fetchOrCreateId(connection, "Doctors", "doctor_id", "first_name", doctorName);
    }

    private int fetchOrCreateId(Connection connection, String table, String idColumn, String nameColumn, String name) throws SQLException {
        String selectQuery = String.format("SELECT %s FROM %s WHERE %s = ?", idColumn, table, nameColumn);
        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
            selectStmt.setString(1, name);
            ResultSet resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(idColumn);
            }
        }

        String insertQuery = String.format("INSERT INTO %s (%s) VALUES (?)", table, nameColumn);
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, name);
            insertStmt.executeUpdate();
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        return -1;
    }

    private Integer getExistingAppointmentId(Connection connection, int patientId, int doctorId, LocalDate appointmentDate) throws SQLException {
        String query = "SELECT appointment_id FROM Appointments WHERE patient_id = ? AND doctor_id = ? AND DATE(appointment_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setDate(3, Date.valueOf(appointmentDate));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("appointment_id"); // Return existing appointment ID
            }
        }
        return null; // No duplicate found
    }
    private void insertAppointmentTreatments(Connection connection, int appointmentId, ObservableList<String> treatments) throws SQLException {
        String fetchTreatmentIdQuery = "SELECT treatment_id FROM Treatments WHERE treatment_name = ?";
        String insertAppointmentTreatmentQuery = "INSERT INTO Appointment_Treatments (appointment_id, treatment_id) VALUES (?, ?)";

        for (String treatmentName : treatments) {
            try (PreparedStatement fetchStmt = connection.prepareStatement(fetchTreatmentIdQuery);
                 PreparedStatement insertStmt = connection.prepareStatement(insertAppointmentTreatmentQuery)) {

                fetchStmt.setString(1, treatmentName);
                ResultSet resultSet = fetchStmt.executeQuery();
                if (resultSet.next()) {
                    int treatmentId = resultSet.getInt("treatment_id");

                    insertStmt.setInt(1, appointmentId);
                    insertStmt.setInt(2, treatmentId);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
    private int getNextAppointmentId(Connection connection) throws SQLException {
        String query = "SELECT MAX(appointment_id) FROM Appointments";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt(1) + 1;
            } else {
                return 1;
            }
        }
    }
    @FXML
    public void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("DoctorPage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }
    private void insertRevenue(Connection connection, int appointmentId, int patientId, double profitAmount, LocalDate appointmentDate) throws SQLException {
        String insertRevenueQuery = "INSERT INTO revenue (patient_id, appointment_id, profit_amount, profit_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertRevenueQuery)) {
            stmt.setInt(1, patientId);
            stmt.setInt(2, appointmentId);
            stmt.setDouble(3, profitAmount);
            stmt.setDate(4, Date.valueOf(appointmentDate)); // Ensure revenue is inserted with appointment date
            stmt.executeUpdate();
            System.out.println("Revenue recorded successfully for Appointment ID: " + appointmentId + " on " + appointmentDate);
        }
    }

    private void updateDailyProfit(Connection connection, double revenue, double expense, LocalDate appointmentDate) throws SQLException {
        String updateProfitQuery = """
        INSERT INTO daily_profit (profit_date, total_revenue, total_expense, profit_amount)
        VALUES (?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE 
        total_revenue = total_revenue + VALUES(total_revenue),
        total_expense = total_expense + VALUES(total_expense),
        profit_amount = total_revenue - total_expense
    """;

        try (PreparedStatement stmt = connection.prepareStatement(updateProfitQuery)) {
            stmt.setDate(1, Date.valueOf(appointmentDate)); // Use appointment date instead of CURDATE()
            stmt.setDouble(2, revenue);
            stmt.setDouble(3, expense);
            stmt.setDouble(4, revenue - expense);
            stmt.executeUpdate();
            System.out.println("Daily profit updated successfully for date: " + appointmentDate);
        }
    }


}
