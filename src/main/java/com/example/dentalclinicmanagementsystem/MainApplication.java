package com.example.dentalclinicmanagementsystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainScreen-view.fxml"));


        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Dental clinic management system");
        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();

        MainController mainController= fxmlLoader.getController();
        mainController.setMainStage(stage);

        AvailableSlotsView.setMainStage(stage);

        BookAppointmentView.setMainStage(stage);

        CalculateBillView.setMainStage(stage);

        DoctorPage.setMainStage(stage);

        EnterPatientDataView.setMainStage(stage);

        GenerateAppointmentReportView.setMainStage(stage);

        GenerateProfitReportsView.setMainStage(stage);

        PatientAppointmentsPageView.setMainStage(stage);

        PatientHistoryView.setMainStage(stage);

        ViewTreatmentsView.setMainStage(stage);

        Login.setMainStage(stage);

        ContactUs.setMainStage(stage);

        AboutUs.setMainStage(stage);

        AdminloginView.setMainStage(stage);

        AdminPageView.setMainStage(stage);

        DoctorPage.setMainStage(stage);

        DoctorLoginView.setMainStage(stage);

        ViewAppointmentsView.setMainStage(stage);

        ProductManagementView.setMainStage(stage);

        GenerateProfitReportsView.setMainStage(stage);

        ViewPricingHistory.setMainStage(stage);

        ExpenditureView.setMainStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}