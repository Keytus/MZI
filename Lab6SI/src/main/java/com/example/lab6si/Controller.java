package com.example.lab6si;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.security.spec.ECGenParameterSpec;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

public class Controller {
    private Stage stage;
    @FXML
    private TextArea sourceTextArea;
    @FXML
    private TextArea signedTextArea;
    @FXML
    private TextArea checkTextArea;
    String pfxFileContent = "test.pfx";
    KeyPair keyPair;
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
        X509Certificate certificate = GenerateCertificate();

        byte[] dataToSign = sourceTextArea.getText().getBytes();

        byte[] sign = Sign(dataToSign, certificate);

        signedTextArea.setText(new String(sign));

        checkTextArea.setText(String.valueOf(Check(sign)));
    }
    X509Certificate GenerateCertificate() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CertificateException, OperatorCreationException, KeyStoreException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance( "ECGOST3410", "BC" );
        keyPairGenerator.initialize( new ECGenParameterSpec( "GostR3410-2001-CryptoPro-A" ) );
        keyPair = keyPairGenerator.generateKeyPair();

        org.bouncycastle.asn1.x500.X500Name subject = new org.bouncycastle.asn1.x500.X500Name( "CN=Me" );
        org.bouncycastle.asn1.x500.X500Name issuer = subject; // self-signed
        BigInteger serial = BigInteger.ONE; // serial number for self-signed does not matter a lot
        Date notBefore = new Date();
        Date notAfter = new Date( notBefore.getTime() + TimeUnit.DAYS.toMillis( 365 ) );

        org.bouncycastle.cert.X509v3CertificateBuilder certificateBuilder = new org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder(
                issuer, serial,
                notBefore, notAfter,
                subject, keyPair.getPublic()
        );
        org.bouncycastle.cert.X509CertificateHolder certificateHolder = certificateBuilder.build(
                new org.bouncycastle.operator.jcajce.JcaContentSignerBuilder( "GOST3411withECGOST3410" )
                        .build( keyPair.getPrivate() )
        );
        org.bouncycastle.cert.jcajce.JcaX509CertificateConverter certificateConverter = new org.bouncycastle.cert.jcajce.JcaX509CertificateConverter();
        X509Certificate certificate = certificateConverter.getCertificate( certificateHolder );

        KeyStore keyStore = KeyStore.getInstance( "PKCS12" );
        keyStore.load( null, null ); // initialize new keystore
        keyStore.setEntry(
                "alias",
                new KeyStore.PrivateKeyEntry(
                        keyPair.getPrivate(),
                        new Certificate[] { certificate }
                ),
                new KeyStore.PasswordProtection( "entryPassword".toCharArray() )
        );
        keyStore.store( new FileOutputStream(pfxFileContent), "keystorePassword".toCharArray() );

        return certificate;
    }
    byte[] Sign(byte[] dataToSign, X509Certificate certificate) throws CMSException, IOException, CertificateEncodingException, OperatorCreationException {

        X509Certificate[] certificates = new X509Certificate[1];
        certificates[0] = certificate;

        CMSTypedData msg = new CMSProcessableByteArray(dataToSign);
        Store certStore = new JcaCertStore(Arrays.asList(certificates));
        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        ContentSigner signer = new org.bouncycastle.operator.jcajce.JcaContentSignerBuilder("GOST3411withECGOST3410").setProvider("BC").build(keyPair.getPrivate());
        gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(signer, certificates[0]));
        gen.addCertificates(certStore);
        CMSSignedData sigData = gen.generate(msg, true);

        return sigData.getEncoded();
    }
    boolean Check(byte[] sign) throws CMSException, CertificateException, OperatorCreationException {
        CMSSignedData signedData = new CMSSignedData(sign);

        SignerInformation signer;

        Store<X509CertificateHolder> certStoreInSing = signedData.getCertificates();
        signer = signedData.getSignerInfos().getSigners().iterator().next();

        Collection certCollection = certStoreInSing.getMatches(signer.getSID());
        Iterator certIt = certCollection.iterator();

        X509CertificateHolder certHolder = (X509CertificateHolder) certIt.next();
        X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certHolder);

        return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(cert));
    }
}