package com.example.cryptobot;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Example {
//    static String key = "---myKey---";
//    static String secret = "---mySecret---";
//    String nonce, signature, data, path;
//    static String domain = "https://api.kraken.com";
//
//    void account_balance() {
//        nonce = String.valueOf(System.currentTimeMillis());
//        data = "nonce=" + nonce;
//        path = "/0/private/Balance";
//        calculateSignature();
//        String answer = post(domain + path, data);
//        // on empty accounts, returns {"error":[],"result":{}}
//        // this is a known Kraken bug
//    }
//
//    String post(String address, String output) {
//        String answer = "";
//        HttpsURLConnection c = null;
//        try {
//            URL u = new URL(address);
//            c = (HttpsURLConnection)u.openConnection();
//            c.setRequestMethod("POST");
//            c.setRequestProperty("API-Key", key);
//            c.setRequestProperty("API-Sign", signature);
//            c.setDoOutput(true);
//            DataOutputStream os = new DataOutputStream(c.getOutputStream());
//            os.writeBytes(output);
//            os.flush();
//            os.close();
//            BufferedReader br = null;
//            if(c.getResponseCode() >= 400) {
//                System.exit(1);
//            }
//            br = new BufferedReader(new InputStreamReader((c.getInputStream())));
//            String line;
//            while ((line = br.readLine()) != null)
//                answer += line;
//        } catch (Exception x) {
//            System.exit(1);
//        } finally {
//            c.disconnect();
//        }
//        return answer;
//    }
//
//    void calculateSignature() {
//        signature = "";
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            md.update((nonce + data).getBytes());
//            Mac mac = Mac.getInstance("HmacSHA512");
//            mac.init(new SecretKeySpec(Base64.decodeBase64(secret.getBytes()), "HmacSHA512"));
//            mac.update(path.getBytes());
//            signature = new String(Base64.encodeBase64(mac.doFinal(md.digest())));
//        } catch(Exception e) {}
//        return;
//    }
    public static URI buildSymbolPriceURI(String symbol){
        return UriComponentsBuilder.fromHttpUrl("https://api.binance.com/api/v3/ticker/price")
                .queryParam("symbol", symbol)
                .build().toUri();
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        final List<String> tempCurrencySymbols = new ArrayList<>();
        tempCurrencySymbols.add("BTCUSDT");
        tempCurrencySymbols.add("LTCUSDT");
        RestTemplate rt = new RestTemplate();
//        List<Object> map = new ArrayList<>()
        for(String currency: tempCurrencySymbols){
            URI url = buildSymbolPriceURI(currency);
            ResponseEntity<List<Object>> exchange = rt.exchange(new RequestEntity<>(HttpMethod.GET, url),
                    new ParameterizedTypeReference<>() {
                    });
            List<Object> response = exchange.getBody();
            System.out.println(response);
        }
    }
}
