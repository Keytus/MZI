module com.example.lab5si {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab5si to javafx.fxml;
    exports com.example.lab5si;
}