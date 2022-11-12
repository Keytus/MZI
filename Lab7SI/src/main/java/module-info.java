module com.example.lab7si {
    requires javafx.controls;
    requires javafx.fxml;
    requires starkbank.ecdsa;


    opens com.example.lab7si to javafx.fxml;
    exports com.example.lab7si;
}