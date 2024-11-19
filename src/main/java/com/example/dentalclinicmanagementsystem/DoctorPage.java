package com.example.dentalclinicmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DoctorPage {
    private static Stage mainStage;

    public static void setMainStage(Stage stage){
        mainStage = stage;
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
    @FXML
    public void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("mainScreen-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
//
//        @FXML
//    public void onClickPatientHistoryButton() throws IOException {
//            System.out.println("patient history");
//
//    }
//        @FXML
//    public void onClickEnterPatientDataButton() throws IOException {
//            System.out.println("patient data");
//
//    }
//        @FXML
//    public void onClickViewAppointmentsButton() throws IOException {
//            System.out.println("view appointments");
//
//    }
//        @FXML
//    public void onClickViewPricingButton() throws IOException {
//            System.out.println("view pricing");
//
//    }
//        @FXML
//    public void onClickViewTreatmentsButton() throws IOException {
//            System.out.println("view treatments");
//
//    }
//        @FXML
//    public void onClickGenerateReportsButton() throws IOException {
//            System.out.println("generate reports");
//    }
//
//    @FXML
//    public void onClickEnterSalesAndPurchasesButton() throws IOException {
//        System.out.println("calculate sales and purchases");
//    }
}
