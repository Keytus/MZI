module com.example.lab2si {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.bouncycastle.provider;


    opens com.example.lab2si to javafx.fxml;
    exports com.example.lab2si;
}