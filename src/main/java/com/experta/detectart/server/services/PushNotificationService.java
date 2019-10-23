package com.experta.detectart.server.services;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.experta.detectart.server.model.Device;
import com.experta.detectart.server.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class PushNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    private static final String FIREBASE_URL_STRING = "https://fcm.googleapis.com/fcm/send";
    private static final String FIREBASE_AUTHORIZATION_FOR_EXPERTA = "key=AAAA6lH4ZNw:APA91bH49ZO7JfixyF7YhveBoduPqOEF8yZS8r-J33EztXU2QvVpSKCfdgx1_cCATVPRvNoGEpcJJVLekq_DyZaYGU8WuL6OdQrBn1PrXxnD0wJGiRLvocxJ8DyVfydkVkqfYi_A3ZnM";

    private RestTemplate restTemplate = new RestTemplate();
    private URI FIREBASE_URI;
    private ObjectMapper mapper;

    public PushNotificationService() {
        mapper = new ObjectMapper();

        try {
            FIREBASE_URI = new URI(FIREBASE_URL_STRING);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void pushNoificationToToken(final User user, final Device device) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, FIREBASE_AUTHORIZATION_FOR_EXPERTA);

        ArrayNode registration_ids = mapper.createArrayNode();
        registration_ids.add(user.getApplicationToken());

        ObjectNode notification = mapper.createObjectNode();
        notification.put("click_action", ".MainActivity");
        notification.put("body", "Monóxido de carbóno por encima del límite seguro en dispositivo ID: "
                                    + device.getMacAddress());
        notification.put("title", "Alarma!");
        notification.put("icon", "experta_logo");
        notification.put("sound", "alarma");

        ObjectNode body = mapper.createObjectNode();
        body.set("notification", notification);
        body.set("registration_ids", registration_ids);

        log.info("Sending an ALARM push notification to {} {}, {}, with token {}"
                , user.getFirstName()
                , user.getLastName()
                , user.getEmail()
                , user.getApplicationToken());
        log.info(body.toPrettyString());

        RequestEntity<ObjectNode> requestEntity = RequestEntity.post(FIREBASE_URI)
                                                               .headers(headers)
                                                               .body(body);

        ResponseEntity<ObjectNode> response = restTemplate.exchange(requestEntity, ObjectNode.class);
    }

}
