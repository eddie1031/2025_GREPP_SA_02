package io.eddie.pgexp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
public class PaymentController {

    public static record TossPaymentRequest(
            String paymentKey,
            String orderId,
            Long amount
    ) {}

    @PostMapping("/confirm")
    public String confirm(
            @RequestBody TossPaymentRequest request
    ) {

        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

//        String target = "Basic %s:".formatted(widgetSecretKey);
        String target = "Basic " + widgetSecretKey + ":";

        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedSecretKey = encoder.encodeToString(target.getBytes(StandardCharsets.UTF_8));




        return "";

    }

}
