package com.example.lab3si;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class Controller {
    private Stage stage;
    @FXML
    private TextArea sourceTextArea;
    @FXML
    private TextArea encryptRSATextArea;
    @FXML
    private TextArea decryptRSATextArea;

    public Stage getStage() {
        return stage;
    }

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
            while((character=reader.read())!=-1)
            {
                result.append((char) character);
            }
            sourceTextArea.setText(result.toString());
        }
    }
    @FXML
    void EncryptDecrypt(ActionEvent event) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        String message =  sourceTextArea.getText();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        Key pub = kp.getPublic();
        Key pvt = kp.getPrivate();

        cipher.init(Cipher.ENCRYPT_MODE, pub);
        byte[] encryptedText = cipher.doFinal(message.getBytes());
        encryptRSATextArea.setText(new String(encryptedText, StandardCharsets.UTF_8));

        cipher.init(Cipher.DECRYPT_MODE, pvt);
        byte[] decryptedText = cipher.doFinal(encryptedText);
        decryptRSATextArea.setText(new String(decryptedText, StandardCharsets.UTF_8));
    }
}