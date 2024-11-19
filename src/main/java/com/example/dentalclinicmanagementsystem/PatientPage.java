package com.example.dentalclinicmanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientPage {

    private static Stage mainStage;

    public static void setMainStage(Stage stage){
        mainStage = stage;
    }

    @FXML
    public void onClickCalculateBillButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("generateProfit-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }
    @FXML
    public void onClickViewAboutDoctorButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("generateProfit-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }
    @FXML
    public void onClickBookAppointmentButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("generateProfit-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }
    @FXML
    public void onClickViewDiscountsButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("generateProfit-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();
    }
    @FXML
    public void onClickViewTreatmentsButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("generateProfit-view.fxml"));
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
}
