module com.example.lab3si {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.bouncycastle.provider;


    opens com.example.lab3si to javafx.fxml;
    exports com.example.lab3si;
}