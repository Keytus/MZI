package com.example.lab7si;

import com.starkbank.ellipticcurve.Ecdsa;
import com.starkbank.ellipticcurve.PrivateKey;
import com.starkbank.ellipticcurve.PublicKey;
import com.starkbank.ellipticcurve.Signature;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Controller {
    private Stage stage;
    @FXML
    private TextArea sourceTextArea;
    @FXML
    private TextArea signedTextArea;
    @FXML
    private TextArea checkTextArea;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    void Load(ActionEvent event) throws IOException {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\User\\Desktop\\Tests\\Txts"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            FileReader reader = new FileReader(file.getAbsolutePath());
            StringBuilder result = new StringBuilder();
            int character;
            while((character=reader.read())!=-1) {
                result.append((char) character);
            }
            sourceTextArea.setText(result.toString());
        }
    }
    @FXML
    void EncryptDecrypt(ActionEvent event) throws Exception {
        // Generate Keys
        PrivateKey privateKey = new PrivateKey();
        PublicKey publicKey = privateKey.publicKey();

        String message = sourceTextArea.getText();
        // Generate Signature
        Signature signature = Ecdsa.sign(message, privateKey);

        signedTextArea.setText(signature.toBase64());

        // Verify if signature is valid
        boolean verified = Ecdsa.verify(message, signature, publicKey) ;

        // Return the signature verification status
        checkTextArea.setText(String.valueOf(verified));
    }
}