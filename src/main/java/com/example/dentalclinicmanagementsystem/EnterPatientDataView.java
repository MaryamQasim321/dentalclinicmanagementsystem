package com.example.dentalclinicmanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EnterPatientDataView {
    private static Stage mainStage;

    public static void setMainStage(Stage stage){
        mainStage = stage;
    }
    @FXML
    public void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("mainScreen-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }

    @FXML
    public void onClickPatientHistoryButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("patientHistory-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
    @FXML
    public void onClickEnterPatientDataButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("enterPatientData-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
    @FXML
    public void onClickViewAppointmentsButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("viewAppointments-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
    @FXML
    public void onClickViewPricingButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("viewPricing-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
    @FXML
    public void onClickViewTreatmentsButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("viewTreatments-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
    @FXML
    public void onClickGenerateReportsButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("generateProfit-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }
    @FXML
    public void onClickEnterSalesAndPurchasesButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("mainScreen-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }
}
