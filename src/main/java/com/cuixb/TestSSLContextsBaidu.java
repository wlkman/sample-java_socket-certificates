package com.cuixb;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;

public class TestSSLContextsBaidu {

    public static void main(String args[]) throws Exception{
        //Creates default factory based on the standard JSSE trust material (cacerts file in the security properties directory).
//        SSLContext c1 = SSLContexts.createDefault();

        //Creates default SSL context based on system properties.
//        SSLContext c2 = SSLContexts.createSystemDefault(); // liberty

        //Creates custom SSL context.
//        SSLContextBuilder builder = SSLContexts.custom();

        //loading local certificate
        //File file=new File("C:/Users/XinBinCui/Desktop/baidu.keystore");
        File file=new File("src/main/java/com/cuixb/baidu.keystore");
        FileInputStream fis=new FileInputStream(file);
        String str="123456";
        char[] pass=str.toCharArray();
        KeyStore ks=KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(fis, pass);

        //add local certificate to sslcontext
        SSLContextBuilder mybuilder = SSLContexts.custom().loadKeyMaterial(ks, pass);
        mybuilder.loadTrustMaterial(ks,new TrustSelfSignedStrategy());

        //create SSLConnectionSocketFactory
        HostnameVerifier hostnameVerifier =SSLConnectionSocketFactory.getDefaultHostnameVerifier();
        SSLContext mySSLContext = mybuilder.build();
//        String[] supportedProtocols ={"TLSv1", "TLSv1.1", "TLSv1.2"};
//        String[] supportedCipherSuites ={"SSL_RSA_WITH_AES_128_GCM_SHA256", "SSL_RSA_WITH_AES_128_CBC_SHA256", "SSL_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", "SSL_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", "SSL_ECDHE_RSA_WITH_AES_128_CBC_SHA", "SSL_ECDHE_RSA_WITH_AES_128_CBC_SHA256", "SSL_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "SSL_ECDHE_ECDSA_WITH_AES_128_CBC_SHA", "SSL_RSA_WITH_AES_128_CBC_SHA"};

        //SSLConnectionSocketFactory sslConnectionSocketFactory =new SSLConnectionSocketFactory(mySSLContext,supportedProtocols , supportedCipherSuites, hostnameVerifier);
        SSLConnectionSocketFactory sslConnectionSocketFactory =new SSLConnectionSocketFactory(mySSLContext,new String[] { "TLSv1" }, null, hostnameVerifier);

        //create BasicCredentialsProvider
//        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
//        credsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("",""));

        //create HttpClients
        //.setDefaultCredentialsProvider(credsProvider):Basic authentication
        //CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).setSSLSocketFactory(sslConnectionSocketFactory).build();
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();

        HttpGet get = new HttpGet("https://www.baidu.com/");
        CloseableHttpResponse response = null;
        String result ="";
        try {
            response = httpClient.execute(get);
            if(response != null && response.getStatusLine().getStatusCode() == 200)
            {
                HttpEntity entity = response.getEntity();
                result = entityToString(entity);
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
                if(response != null)
                {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static String entityToString(HttpEntity entity) throws IOException {
        String result = null;
        if(entity != null)
        {
            long lenth = entity.getContentLength();
            if(lenth != -1 && lenth < 2048)
            {
                result = EntityUtils.toString(entity,"UTF-8");
            }else {
                InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = buffer.toString();
            }
        }
        return result;
    }


}
