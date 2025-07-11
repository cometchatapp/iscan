package com.arteriatech.ss.msec.iscan.v4.quodeck;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JToken {

    private static final String SECRET_KEY = "p20o20e13"; //@TODO Add Signature here
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private JSONObject payload = new JSONObject();
    private String signature;
    private String encodedHeader;

    private JToken() {
        try {
            encodedHeader = encode(new JSONObject(JWT_HEADER));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JToken(JSONObject payload)throws Throwable {
        this(payload.getString("accesstoken"), payload.getLong("iat"));
    }

    public JToken(String sub, long aud) {
        this();
        try {
            payload.put("accesstoken", sub);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                payload.put("iat", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            }
            signature = hmacSha256(encodedHeader + "." + encode(payload), SECRET_KEY);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * For verification
     *
     * @param token
     * @throws NoSuchAlgorithmException
     */
    public JToken(String token) throws NoSuchAlgorithmException {
        this();
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid Token format");
        }
        if (encodedHeader.equals(parts[0])) {
            encodedHeader = parts[0];
        } else {
            throw new NoSuchAlgorithmException("JWT Header is Incorrect: " + parts[0]);
        }

        try {
            payload = new JSONObject(decode(parts[1]));
            if (!payload.has("iat")) {
                throw new JSONException("Payload doesn't contain expiry " + payload);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        signature = parts[2];
    }

    @Override
    public String toString() {
        return encodedHeader + "." + encode(payload) + "." + signature;
    }

    public boolean isValid() {
        boolean isVaild = false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                isVaild= payload.getLong("iat") > (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) //token not expired
                        && signature.equals(hmacSha256(encodedHeader + "." + encode(payload), SECRET_KEY)); //signature matched
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isVaild;
    }

    public String getSubject() {
        try {
            return payload.getString("sub");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<String> getAudience() {
        List<String> list = null;
        try {
            JSONArray arr = payload.getJSONArray("aud");
            list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                list.add(arr.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String encode(JSONObject obj) {
        return encode(obj.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String encode(byte[] bytes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        }
        return "";
    }

    private static String decode(String encodedString) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new String(Base64.getUrlDecoder().decode(encodedString));
        }
        return "";
    }

    /**
     * Sign with HMAC SHA256 (HS256)
     *
     * @param data
     * @return
     * @throws Exception
     */
    private String hmacSha256(String data, String secret) {
        try {

            //MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);//digest.digest(secret.getBytes(StandardCharsets.UTF_8));

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);

            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encode(signedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            Logger.getLogger(JToken.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }
}
