module com.example.lab4si {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.bouncycastle.provider;


    opens com.example.lab4si to javafx.fxml;
    exports com.example.lab4si;
}