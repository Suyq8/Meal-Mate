package com.mealmate.utils;

import java.math.BigDecimal;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class StripeUtil {

    private String apiKey;
    private String signingSecret;

    public StripeUtil(String apiKey, String signingSecret) {
        this.apiKey = apiKey;
        this.signingSecret = signingSecret;
        Stripe.apiKey = this.apiKey;
    }

    public String checkOut(String serialNumber, BigDecimal totalPrice, String origin) {
        SessionCreateParams params = SessionCreateParams.builder()
                .setClientReferenceId(serialNumber)
                .setSuccessUrl(origin + "/success.html")
                .setCancelUrl(origin + "/cancel.html")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmountDecimal(totalPrice.multiply(BigDecimal.valueOf(100)))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("total price")
                                                                .build())
                                                .build())
                                .build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .build();

        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            log.info(e.getMessage());
            throw new RuntimeException("Failed to create stripe session");
        }
        // log.info("session: {}", session);

        return session.getUrl();
    }

    public Event constructEvent(String payload, String sigHeader) {
        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, signingSecret);
        } catch (StripeException e) {
            // Invalid signature
            log.info(e.getMessage());
            throw new RuntimeException("Signature verification failed");
        }

        return event;
    }

    public void refund(String paymentIntentId) {
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntentId)
                    .build();
            Refund.create(params);
        } catch (StripeException e) {
            log.info(e.getMessage());
            throw new RuntimeException("refund failed");
        }
    }
}
