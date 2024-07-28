package com.mealmate.controller.user;

import java.util.Map;

import com.mealmate.service.OrderService;
import com.mealmate.utils.StripeUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeObject;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentRetrieveParams;

@RestController("StripeWebhookController")
@RequestMapping("/webhook")
@Tag(name = "Stripe Webhook Controller")
@Slf4j
public class StripeWebhookController {

    @Autowired
    private StripeUtil stripeUtil;
    @Autowired
    private OrderService orderService;
    private static final Map<String, Integer> paymentMethodTypeMap = Map.of("alipay", 1, "wechat_pay", 2, "card", 3, "paypal", 4, "cashapp", 5);

    @PostMapping
    public void handle(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader, HttpServletResponse response) {

        Event event;
        try {
            event = stripeUtil.constructEvent(payload, sigHeader);
        } catch (Exception e) {
            // Invalid signature
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if ("checkout.session.completed".equals(event.getType())) {
            log.info("event: {}", event);
            Optional<StripeObject> sessionOptional = event.getDataObjectDeserializer().getObject();
            Session session = (Session) sessionOptional.get();

            String payMethodType;
            String paymentIntentId;
            try {
                PaymentIntent paymentIntent = PaymentIntent.retrieve(
                    session.getPaymentIntent(),
                        PaymentIntentRetrieveParams.builder()
                                .addExpand("payment_method")
                                .build(),
                        null
                );

                PaymentMethod paymentMethod = paymentIntent.getPaymentMethodObject();
                payMethodType = paymentMethod.getType();
                paymentIntentId = paymentIntent.getId();
            } catch (StripeException e) {
                log.error("Failed to retrieve payment method: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // log.info("payment method: {}", payMethodType);
            orderService.completeCheckoutSession(session.getClientReferenceId(), paymentMethodTypeMap.get(payMethodType), paymentIntentId);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
