module com.example.lab1si {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.bouncycastle.provider;


    opens com.example.lab1si to javafx.fxml;
    exports com.example.lab1si;
}