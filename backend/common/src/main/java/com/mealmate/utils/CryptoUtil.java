package com.mealmate.utils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Slf4j
public class CryptoUtil {
    private String secretKey; 

    public String decryptAES(String encryptedData) throws Exception {
        log.info("decryptAES: {}", secretKey);
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKeySpec key = new SecretKeySpec(decodedKey, "AES");;

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(original);
    }
}
