package com.mealmate.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "mealmate.wechat")
@Data
public class WeChatProperties {

    private String appid; // App ID of the mini program
    private String secret; // Secret key of the mini program
    private String mchid; // Merchant ID
    private String mchSerialNo; // Serial number of the merchant API certificate
    private String privateKeyFilePath; // File path of the merchant private key
    private String apiV3Key; // Decryption key for the certificate
    private String weChatPayCertFilePath; // File path of the platform certificate
    private String notifyUrl; // Callback URL for successful payment
    private String refundNotifyUrl; // Callback URL for successful refund

}
