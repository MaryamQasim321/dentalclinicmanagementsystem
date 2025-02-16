package com.example.dentalclinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.*;

public class ViewAppointmentsView {

    private static Stage mainStage;

    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, Integer> patientIdColumn;
    @FXML
    private TableColumn<Appointment, Integer> doctorIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDateColumn;
    @FXML
    private TableColumn<Appointment, String> statusColumn;
    @FXML
    private TableColumn<Appointment, String> notesColumn;
    @FXML
    private TableColumn<Appointment, String> patientNameColumn;
    @FXML
    private TableColumn<Appointment, String> patientContactColumn;
    @FXML
    private TableColumn<Appointment, Double> totalCostColumn;
    @FXML
    private TableColumn<Appointment, String> treatmentsColumn;

    @FXML
    private ComboBox<String> statusComboBox;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    @FXML
    public void initialize() {
        // Initialize the status combo box with options
        statusComboBox.setItems(FXCollections.observableArrayList("Scheduled", "Completed", "Cancelled"));
        setupTableColumns();
        loadAppointments();

        // Enable table editing
        appointmentsTable.setEditable(true);
    }

    private void setupTableColumns() {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patientContactColumn.setCellValueFactory(new PropertyValueFactory<>("patientContact"));
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        treatmentsColumn.setCellValueFactory(new PropertyValueFactory<>("treatments"));

        enableInlineEditing();
    }

    private void enableInlineEditing() {
        // Allow editing on the TableView
        appointmentsTable.setEditable(true);

        // Make patient ID column editable
        patientIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        patientIdColumn.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setPatientId(event.getNewValue());
            updateDatabase("patient_id", String.valueOf(event.getNewValue()), appointment.getAppointmentId());
        });

        // Make doctor ID column editable
        doctorIdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        doctorIdColumn.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setDoctorId(event.getNewValue());
            updateDatabase("doctor_id", String.valueOf(event.getNewValue()), appointment.getAppointmentId());
        });

        // Make appointment date column editable
        appointmentDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        appointmentDateColumn.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setAppointmentDate(event.getNewValue());
            updateDatabase("appointment_date", event.getNewValue(), appointment.getAppointmentId());
        });

        // Make status column editable with ComboBox
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Scheduled", "Completed", "Cancelled");
        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statusOptions));
        statusColumn.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setStatus(event.getNewValue());
            updateDatabase("status", event.getNewValue(), appointment.getAppointmentId());
        });

        // Make notes column editable
        notesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        notesColumn.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setNotes(event.getNewValue());
            updateDatabase("notes", event.getNewValue(), appointment.getAppointmentId());
        });

        // Make patient name column editable
        patientNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        patientNameColumn.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setPatientName(event.getNewValue());
            updateDatabase("patient_name", event.getNewValue(), appointment.getAppointmentId());
        });

        // Make patient contact column editable
        patientContactColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        patientContactColumn.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setPatientContact(event.getNewValue());
            updateDatabase("contact_number", event.getNewValue(), appointment.getAppointmentId());
        });

        // Make total cost column editable
        totalCostColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        totalCostColumn.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setTotalCost(event.getNewValue());
            updateDatabase("total_treatment_cost", String.valueOf(event.getNewValue()), appointment.getAppointmentId());
        });

        // Make treatments column editable
        treatmentsColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        treatmentsColumn.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setTreatments(event.getNewValue());
            updateDatabase("treatments", event.getNewValue(), appointment.getAppointmentId());
        });
    }


    private void loadAppointments() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String query = "SELECT * FROM AppointmentDetailsView";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                appointments.add(new Appointment(
                        resultSet.getInt("appointment_id"),
                        resultSet.getInt("patient_id"),
                        resultSet.getInt("doctor_id"),
                        resultSet.getString("appointment_date"),
                        resultSet.getString("status"),
                        resultSet.getString("notes"),
                        resultSet.getString("treatments"),
                        resultSet.getString("patient_name"),
                        resultSet.getString("contact_number"), // Ensure contact_number exists in the view
                        resultSet.getDouble("total_treatment_cost")
                ));
            }
            appointmentsTable.setItems(appointments);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load appointments.");
        }
    }

    private void updateDatabase(String columnName, String newValue, int appointmentId) {
        String query = "UPDATE Appointments SET " + columnName + " = ? WHERE appointment_id = ?";

        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, newValue);
            preparedStatement.setInt(2, appointmentId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to update appointment.");
        }
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

    @FXML
    private void onFilterAppointmentsClick() {
        String selectedStatus = statusComboBox.getValue();

        if (selectedStatus == null || selectedStatus.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Filter Error", "Please select a status to filter appointments.");
            return;
        }

        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
        String query = "{CALL FilterAppointmentsByStatus(?)}";

        try (Connection connection = DatabaseHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall(query)) {

            callableStatement.setString(1, selectedStatus);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                filteredAppointments.add(new Appointment(
                        resultSet.getInt("appointment_id"),
                        resultSet.getInt("patient_id"),
                        resultSet.getInt("doctor_id"),
                        resultSet.getString("appointment_date"),
                        resultSet.getString("status"),
                        resultSet.getString("notes"),
                        resultSet.getString("treatments"),
                        resultSet.getString("patient_name"),
                        resultSet.getString("contact_number"),
                        resultSet.getDouble("total_treatment_cost")
                ));
            }
            appointmentsTable.setItems(filteredAppointments);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to filter appointments.");
        }
    }
}
