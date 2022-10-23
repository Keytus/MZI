package com.example.lab1si;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.crypto.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


public class Controller {
    private Stage stage;
    @FXML
    private TextArea sourceTextArea;
    @FXML
    private TextArea encryptDoubleDESTextArea;
    @FXML
    private TextArea decryptDoubleDESTextArea;
    @FXML
    private TextArea encryptTripleDESTextArea;
    @FXML
    private TextArea decryptTripleDESTextArea;
    @FXML
    private TextArea encryptGOSTTextArea;
    @FXML
    private TextArea decryptGOSTTextArea;

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
    void EncryptDecrypt(ActionEvent event) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchProviderException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        SecretKey firstDESKey = keyGenerator.generateKey();
        SecretKey secondDESKey = keyGenerator.generateKey();
        keyGenerator = KeyGenerator.getInstance("GOST28147");
        SecretKey keyGOST = keyGenerator.generateKey();

        DoubleDESEncrypt(sourceTextArea.getText(),firstDESKey,secondDESKey);
        TripleDESEncrypt(sourceTextArea.getText(),firstDESKey,secondDESKey);
        GOSTEncrypt(sourceTextArea.getText(),keyGOST);
    }
    private void DoubleDESEncrypt(String message, SecretKey firstKey, SecretKey secondKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, firstKey);
        byte[] resultMessage = cipher.doFinal(message.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secondKey);
        resultMessage = cipher.doFinal(resultMessage);
        encryptDoubleDESTextArea.setText(new String(resultMessage, StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, secondKey);
        resultMessage = cipher.doFinal(resultMessage);
        cipher.init(Cipher.DECRYPT_MODE, firstKey);
        resultMessage = cipher.doFinal(resultMessage);
        decryptDoubleDESTextArea.setText(new String(resultMessage, StandardCharsets.UTF_8));
    }
    private void TripleDESEncrypt(String message, SecretKey firstKey, SecretKey secondKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, firstKey);
        byte[] resultMessage = cipher.doFinal(message.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secondKey);
        resultMessage = cipher.doFinal(resultMessage);
        cipher.init(Cipher.ENCRYPT_MODE, firstKey);
        resultMessage = cipher.doFinal(resultMessage);
        encryptTripleDESTextArea.setText(new String(resultMessage, StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, firstKey);
        resultMessage = cipher.doFinal(resultMessage);
        cipher.init(Cipher.DECRYPT_MODE, secondKey);
        resultMessage = cipher.doFinal(resultMessage);
        cipher.init(Cipher.DECRYPT_MODE, firstKey);
        resultMessage = cipher.doFinal(resultMessage);
        decryptTripleDESTextArea.setText(new String(resultMessage, StandardCharsets.UTF_8));
    }
    private void GOSTEncrypt(String message, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("GOST28147/ECB/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] resultMessage = cipher.doFinal(message.getBytes());
        encryptGOSTTextArea.setText(new String(resultMessage, StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, key);
        resultMessage = cipher.doFinal(resultMessage);
        decryptGOSTTextArea.setText(new String(resultMessage, StandardCharsets.UTF_8));
    }
}
