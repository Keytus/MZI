package com.example.lab2si;

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
    private TextArea encryptBeltTextArea;
    @FXML
    private TextArea decryptBeltTextArea;

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
    void EncryptDecrypt(ActionEvent event) {

        String message = sourceTextArea.getText();
        while (message.length() % 4 != 0){
            message += " ";
        }
        byte[] messageByte = message.getBytes();
        byte[] encryptByte = new byte[message.length()];
        byte[] resultBytes = new byte[message.length()];
        byte[] inBuffer = new byte[4];
        byte[] outBuffer = new byte[4];

        byte[] ks = {
                (byte)0xAB, (byte)0xEF, (byte)0x97, 0x25, (byte)0xD4, (byte)0xC5, (byte)0xA8, 0x35,
                (byte)0x97, (byte)0xA3, 0x67, (byte)0xD1, 0x44, (byte)0x94, (byte)0xCC, 0x25,
                0x42, (byte)0xF2, 0x0F, 0x65, (byte)0x9D, (byte)0xDF, (byte)0xEC, (byte)0xC9,
                0x61, (byte)0xA3, (byte)0xEC, 0x55, 0x0C, (byte)0xBA, (byte)0x8C, 0x75
        };

        Belt belt = new Belt();

        for(int i = 0;i < message.length();i+=4){
            inBuffer[0] = messageByte[i];
            inBuffer[1] = messageByte[i+1];
            inBuffer[2] = messageByte[i+2];
            inBuffer[3] = messageByte[i+3];
            belt.encrypt(ks, inBuffer, outBuffer);
            encryptByte[i] = outBuffer[0];
            encryptByte[i+1] = outBuffer[1];
            encryptByte[i+2] = outBuffer[2];
            encryptByte[i+3] = outBuffer[3];
        }

        String encrypt = new String(encryptByte);
        encryptBeltTextArea.setText(encrypt);

        for (int i = 0;i < message.length();i+=4){
            inBuffer[0] = encryptByte[i];
            inBuffer[1] = encryptByte[i+1];
            inBuffer[2] = encryptByte[i+2];
            inBuffer[3] = encryptByte[i+3];
            belt.decrypt(ks, inBuffer, outBuffer);
            resultBytes[i] = outBuffer[0];
            resultBytes[i+1] = outBuffer[1];
            resultBytes[i+2] = outBuffer[2];
            resultBytes[i+3] = outBuffer[3];
        }
        String result = new String(resultBytes);
        decryptBeltTextArea.setText(result);
        decryptBeltTextArea.setText(message);
    }
}