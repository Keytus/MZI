module com.example.lab8si {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens com.example.lab8si to javafx.fxml;
    exports com.example.lab8si;
}