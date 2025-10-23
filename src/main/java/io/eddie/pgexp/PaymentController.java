package io.eddie.pgexp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final RestTemplate restTemplate;
    private final ObjectMapper om = new ObjectMapper();

    @GetMapping("/success")
    public String success() {
        return "success";
    }


    public static record TossPaymentRequest(
            String paymentKey,
            String orderId,
            Long amount
    ) {}

    @ResponseBody
    @PostMapping("/confirm")
    public String confirm(
            @RequestBody TossPaymentRequest request
    ) {

        // 시크릿키 작업
        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

//        String target = "Basic %s:".formatted(widgetSecretKey);
        String target = widgetSecretKey + ":";

        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedSecretKey = "Basic " + encoder.encodeToString(target.getBytes(StandardCharsets.UTF_8));

        // 헤더 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON); // Content-Type: application/json
        httpHeaders.set("Authorization", encryptedSecretKey);

        Map<String, Object> requestMap = om.convertValue(request, new TypeReference<>() {
        });

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestMap, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.tosspayments.com/v1/payments/confirm",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("성공!");
            return "success";
        } else {
            log.info("실패!");
            return "fail";
        }

    }

}
