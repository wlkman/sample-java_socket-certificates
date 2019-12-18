package com.cuixb;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.apache.commons.codec.binary.Base64;

public class ExtractPublicKey {

    public static void main(String[] args) {

        try {
            // Load the keystore
            File file = new File("src/main/java/com/cuixb/cuixb.keystore");
            FileInputStream is = new FileInputStream(file);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            String alias = "cuixb";
            String password = "123456";
            char[] passwd = password.toCharArray();
            keystore.load(is, passwd);
            KeyPair kp = getKeyPair(keystore, alias, passwd);
            Base64 base64 = new Base64();
            PublicKey pubKey = kp.getPublic();
            PrivateKey PrivateKey = kp.getPrivate();

            String publicKeyString = base64.encodeBase64String(pubKey.getEncoded());
            String PrivateKeyString = base64.encodeBase64String(PrivateKey.getEncoded());

            System.out.println("*****************Public Key***********************");
            System.out.println(publicKeyString);
            System.out.println("*****************Private Key**********************");
            System.out.println(PrivateKeyString);

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static KeyPair getKeyPair(KeyStore keystore, String alias, char[] password) throws Exception {
        // Get private key
        Key key = keystore.getKey(alias, password);
        if (key instanceof PrivateKey) {
            // Get certificate of public key
            Certificate cert = keystore.getCertificate(alias);

            // Get public key
            PublicKey publicKey = cert.getPublicKey();

            // Return a key pair
            return new KeyPair(publicKey, (PrivateKey)key);
        }
        return null;
    }

}
