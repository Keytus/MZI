package com.example.lab8si;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javafx.embed.swing.SwingFXUtils;


public class Controller {
    private Stage stage;
    @FXML
    private TextArea sourceTextArea;
    @FXML
    private TextArea signedTextArea;
    @FXML
    private Text messageText;
    @FXML
    private ImageView sourceImageView;
    @FXML
    private ImageView encryptedImageView;
    BufferedImage sourceImage;
    String fileName;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    void Load(ActionEvent event) throws IOException {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\User\\Desktop\\Tests\\Jpegs"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            fileName = file.getName();
            sourceImage = ImageIO.read(file);
            sourceImageView.setImage(SwingFXUtils.toFXImage(sourceImage, null));
        }
    }
    @FXML
    void EncryptDecrypt(ActionEvent event) throws Exception {
        String message = sourceTextArea.getText();
        messageText.setText("");
        if (sourceImage != null && !message.equals("")){
            int imageSize = sourceImage.getWidth() * sourceImage.getHeight();
            if (message.length() * 8 + 32 > imageSize){
                messageText.setText("Message too long for this image");
                return;
            }
            embedInteger(sourceImage, message.length(), 0, 0);
            byte[] messageBytes = message.getBytes();
            for(int i=0; i<messageBytes.length; i++){
                embedByte(sourceImage, messageBytes[i], i*8+32, 0);
            }

            BufferedImage encryptedImage = sourceImage;

            encryptedImageView.setImage(SwingFXUtils.toFXImage(encryptedImage, null));
            saveImage(encryptedImage, fileName);

            int len = extractInteger(encryptedImage, 0, 0);
            byte[] encryptedByte = new byte[len];
            for(int i=0; i<len; i++){
                encryptedByte[i] = extractByte(encryptedImage, i*8+32, 0);
            }
            signedTextArea.setText(new String(encryptedByte));
        }
        else {
            if (sourceImage == null){
                messageText.setText("No image");
            }
            if (message.equals("")){
                messageText.setText("No message");
            }
        }
    }
    private void saveImage(BufferedImage sourceImage, String fileName) throws IOException {
        File outfile = new File(fileName);
        ImageIO.write(sourceImage, "jpeg", outfile);
    }
    private BufferedImage loadEncryptedImage(String fileName) throws IOException {
        File file = new File(fileName);
        return ImageIO.read(file);
    }
    private void embedInteger(BufferedImage img, int n, int start, int storageBit) {
        int maxX = img.getWidth();
        int maxY = img.getHeight();
        int startX = start/maxY;
        int startY = start - startX*maxY;
        int count=0;
        for(int i=startX; i<maxX && count<32; i++) {
            for(int j=startY; j<maxY && count<32; j++) {
                int rgb = img.getRGB(i, j), bit = getBitValue(n, count);
                rgb = setBitValue(rgb, storageBit, bit);
                img.setRGB(i, j, rgb);
                count++;
            }
        }
    }
    private void embedByte(BufferedImage img, byte b, int start, int storageBit) {
        int maxX = img.getWidth();
        int maxY = img.getHeight();
        int startX = start/maxY;
        int startY = start - startX*maxY;
        int count=0;
        for(int i=startX; i<maxX && count<8; i++) {
            for(int j=startY; j<maxY && count<8; j++) {
                int rgb = img.getRGB(i, j), bit = getBitValue(b, count);
                rgb = setBitValue(rgb, storageBit, bit);
                img.setRGB(i, j, rgb);
                count++;
            }
        }
    }
    private int extractInteger(BufferedImage img, int start, int storageBit) {
        int maxX = img.getWidth();
        int maxY = img.getHeight();
        int startX = start/maxY;
        int startY = start - startX*maxY;
        int count=0;
        int length = 0;
        for(int i=startX; i<maxX && count<32; i++) {
            for(int j=startY; j<maxY && count<32; j++) {
                int rgb = img.getRGB(i, j), bit = getBitValue(rgb, storageBit);
                length = setBitValue(length, count, bit);
                count++;
            }
        }
        return length;
    }

    private byte extractByte(BufferedImage img, int start, int storageBit) {
        int maxX = img.getWidth();
        int maxY = img.getHeight();
        int startX = start/maxY;
        int startY = start - startX*maxY;
        int count=0;
        byte b = 0;
        for(int i=startX; i<maxX && count<8; i++) {
            for(int j=startY; j<maxY && count<8; j++) {
                int rgb = img.getRGB(i, j), bit = getBitValue(rgb, storageBit);
                b = (byte)setBitValue(b, count, bit);
                count++;
            }
        }
        return b;
    }
    private int getBitValue(int n, int location) {
        int v = n & (int) Math.round(Math.pow(2, location));
        return v==0?0:1;
    }
    private int setBitValue(int n, int location, int bit) {
        int toggle = (int) Math.pow(2, location), bv = getBitValue(n, location);
        if(bv == bit)
            return n;
        if(bv == 0 && bit == 1)
            n |= toggle;
        else if(bv == 1 && bit == 0)
            n ^= toggle;
        return n;
    }
}