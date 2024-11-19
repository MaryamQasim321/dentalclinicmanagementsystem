package com.example.dentalclinicmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    private static Stage mainStage;

    public static void setMainStage(Stage stage){
        mainStage = stage;
    }

    @FXML
    public void onClickPatientLoginButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("PatientPage-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
    @FXML
    public void onClickDoctorLoginButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("doctorLogin-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
    @FXML
    public void onClickadminLoginButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("adminlogin-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
//    @FXML
//    public void onClickDoctorLoginButton() throws IOException {
//        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("DoctorPage-view.fxml"));
//        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
//        mainStage.setTitle("Dental Clinic Management System");
//        mainStage.setScene(scene);
//        mainStage.show();
//
//    }
    @FXML
    public void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("mainScreen-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
}
