package com.example.dentalclinicmanagementsystem;

import javafx.beans.property.*;

public class Revenue {
    private final IntegerProperty appointmentId;
    private final StringProperty patientName;
    private final StringProperty serviceName;
    private final DoubleProperty revenueGenerated;
    private final StringProperty appointmentDate;

    public Revenue(int appointmentId, String patientName, String serviceName, double revenueGenerated, String appointmentDate) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.patientName = new SimpleStringProperty(patientName);
        this.serviceName = new SimpleStringProperty(serviceName);
        this.revenueGenerated = new SimpleDoubleProperty(revenueGenerated);
        this.appointmentDate = new SimpleStringProperty(appointmentDate);
    }

    // Getters and properties for binding
    public IntegerProperty appointmentIdProperty() { return appointmentId; }
    public StringProperty patientNameProperty() { return patientName; }
    public StringProperty serviceNameProperty() { return serviceName; }
    public DoubleProperty revenueGeneratedProperty() { return revenueGenerated; }
    public StringProperty appointmentDateProperty() { return appointmentDate; }
}
