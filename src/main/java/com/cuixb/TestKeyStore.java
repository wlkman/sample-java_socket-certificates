package com.cuixb;

import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;

public class TestKeyStore {

    public static void main(String... args) throws Exception {


        File file=new File("src/main/java/com/cuixb/cuixb.keystore");
        FileInputStream fis=new FileInputStream(file);
        String str="123456";
        char[] pass=str.toCharArray();
        KeyStore ks=KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(fis, pass);

        System.out.println("/**  显示所有证书的别名**/");
        Enumeration<String> e=ks.aliases();
        while(e.hasMoreElements())System.out.println(e.nextElement());


//        System.out.println("/***显示别名为aaa的证书**/");
//        Certificate c=ks.getCertificate("aaa");
//        System.out.println(c.toString());
    }



}

