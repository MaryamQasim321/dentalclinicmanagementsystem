module com.example.dentalclinicmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.dentalclinicmanagementsystem to javafx.fxml;
    exports com.example.dentalclinicmanagementsystem;
}