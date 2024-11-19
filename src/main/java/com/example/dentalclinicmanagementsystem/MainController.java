package com.example.dentalclinicmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML

    private static Stage mainStage;

    public void setMainStage(Stage stage){
        mainStage = stage;
    }

    @FXML
    protected void onClickLoginButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("Login-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
    @FXML
    protected void onClickAboutUsButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("aboutUs-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
    @FXML
    protected void onClickContactUsButton() throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("contactUs.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }
    @FXML
    public void onClickHomeButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(MainApplication.class.getResource("mainScreen-view.fxml"));
        Scene scene= new Scene(fxmlLoader.load(), 640, 500);
        mainStage.setTitle("Dental Clinic Management System");
        mainStage.setScene(scene);
        mainStage.show();

    }

}