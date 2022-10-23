module com.example.lab6si {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.bouncycastle.provider;
    requires org.bouncycastle.pkix;


    opens com.example.lab6si to javafx.fxml;
    exports com.example.lab6si;
}