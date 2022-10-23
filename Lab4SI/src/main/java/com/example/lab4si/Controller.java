package com.example.lab4si;

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
import java.security.*;

public class Controller {
    private Stage stage;
    @FXML
    private TextArea sourceTextArea;
    @FXML
    private TextArea encryptElGamalTextArea;
    @FXML
    private TextArea decryptElGamalTextArea;

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
            while((character=reader.read())!=-1) {
                result.append((char) character);
            }
            sourceTextArea.setText(result.toString());
        }
    }
    @FXML
    void EncryptDecrypt(ActionEvent event) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String message = sourceTextArea.getText();
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ElGamal", "BC");
        SecureRandom random = new SecureRandom();
        keyGen.initialize(2048, random);
        Cipher cipher = Cipher.getInstance("ElGamal/None/NoPadding", "BC");
        KeyPair keys = keyGen.generateKeyPair();
        Key pubKey = keys.getPublic();
        Key privKey = keys.getPrivate();
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        encryptElGamalTextArea.setText(new String(cipherText));
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] resultText = cipher.doFinal(cipherText);
        decryptElGamalTextArea.setText(new String(resultText));
    }
}