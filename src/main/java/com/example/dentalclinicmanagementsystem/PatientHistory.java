package com.example.dentalclinicmanagementsystem;

import javafx.beans.property.*;

import java.time.LocalDate;

public class PatientHistory {
    private final IntegerProperty historyId;
    private final IntegerProperty patientId;
    private final ObjectProperty<LocalDate> visitDate;
    private final StringProperty medicalHistory;
    private final StringProperty dentalHistory;
    private final StringProperty medications;
    private final StringProperty allergies;
    private final StringProperty previousTreatments;
    private final StringProperty patientName;

    // Constructor
    public PatientHistory(int historyId, int patientId, LocalDate visitDate, String medicalHistory,
                          String dentalHistory, String medications, String allergies,
                          String previousTreatments, String patientName) {
        this.historyId = new SimpleIntegerProperty(historyId);
        this.patientId = new SimpleIntegerProperty(patientId);
        this.visitDate = new SimpleObjectProperty<>(visitDate);
        this.medicalHistory = new SimpleStringProperty(medicalHistory);
        this.dentalHistory = new SimpleStringProperty(dentalHistory);
        this.medications = new SimpleStringProperty(medications);
        this.allergies = new SimpleStringProperty(allergies);
        this.previousTreatments = new SimpleStringProperty(previousTreatments);
        this.patientName = new SimpleStringProperty(patientName);
    }

    // Getters
    public int getHistoryId() {
        return historyId.get();
    }

    public IntegerProperty historyIdProperty() {
        return historyId;
    }

    public int getPatientId() {
        return patientId.get();
    }

    public IntegerProperty patientIdProperty() {
        return patientId;
    }

    public LocalDate getVisitDate() {
        return visitDate.get();
    }

    public ObjectProperty<LocalDate> visitDateProperty() {
        return visitDate;
    }

    public String getMedicalHistory() {
        return medicalHistory.get();
    }

    public StringProperty medicalHistoryProperty() {
        return medicalHistory;
    }

    public String getDentalHistory() {
        return dentalHistory.get();
    }

    public StringProperty dentalHistoryProperty() {
        return dentalHistory;
    }

    public String getMedications() {
        return medications.get();
    }

    public StringProperty medicationsProperty() {
        return medications;
    }

    public String getAllergies() {
        return allergies.get();
    }

    public StringProperty allergiesProperty() {
        return allergies;
    }

    public String getPreviousTreatments() {
        return previousTreatments.get();
    }

    public StringProperty previousTreatmentsProperty() {
        return previousTreatments;
    }

    public String getPatientName() {
        return patientName.get();
    }

    public StringProperty patientNameProperty() {
        return patientName;
    }

    // Setters (optional, if you need them for updating data)
    public void setHistoryId(int historyId) {
        this.historyId.set(historyId);
    }

    public void setPatientId(int patientId) {
        this.patientId.set(patientId);
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate.set(visitDate);
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory.set(medicalHistory);
    }

    public void setDentalHistory(String dentalHistory) {
        this.dentalHistory.set(dentalHistory);
    }

    public void setMedications(String medications) {
        this.medications.set(medications);
    }

    public void setAllergies(String allergies) {
        this.allergies.set(allergies);
    }

    public void setPreviousTreatments(String previousTreatments) {
        this.previousTreatments.set(previousTreatments);
    }

    public void setPatientName(String patientName) {
        this.patientName.set(patientName);
    }
}
