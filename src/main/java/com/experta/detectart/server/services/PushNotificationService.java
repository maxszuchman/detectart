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
import com.experta.detectart.server.model.deviceData.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class PushNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    private static final String FIREBASE_URL_STRING = "https://fcm.googleapis.com/fcm/send";
    private static final String FIREBASE_AUTHORIZATION_FOR_EXPERTA = "key=AAAA8u0Mou4:APA91bHCAeuMRdoCGz0RwdbwrilbODwm7jg_tePILZdFGNOTsKZWirY8iO5SLwcXoXU1cmk24eBiAARXv--47_-2uTdwEzfaoQngYmR_jK3mM1lo9n0OAd2H05Q7x4ia1Ie_dn6zaoUq";

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

    private void pushNotification(final User user, final HttpHeaders headers, final ObjectNode notification, final ArrayNode registration_ids) {

        ObjectNode body = mapper.createObjectNode();
        body.set("notification", notification);
        body.set("registration_ids", registration_ids);

        log.info("Sending a PUSH notification to {} {}, {}, with token {} and body {}"
                , user.getFullName()
                , user.getId()
                , user.getApplicationToken()
                , notification.toPrettyString());
        log.info(body.toPrettyString());

        RequestEntity<ObjectNode> requestEntity = RequestEntity.post(FIREBASE_URI)
                                                               .headers(headers)
                                                               .body(body);

        ResponseEntity<ObjectNode> response = restTemplate.exchange(requestEntity, ObjectNode.class);
    }

    public void pushNotificationForEachSensorToToken(final User user, final Device device) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, FIREBASE_AUTHORIZATION_FOR_EXPERTA);

        ArrayNode registration_ids = mapper.createArrayNode();
        registration_ids.add(user.getApplicationToken());

        ObjectNode notification = mapper.createObjectNode();
        notification.put("icon", "experta_logo");
        notification.put("sound", "alarma");

        if (device.getSensor1Status() == Status.ALARM) {
            notification.put("click_action", ".MainActivity");
            notification.put("body", "Monóxido de carbóno por encima del límite seguro en dispositivo: "
                                        + device.getAlias());
            notification.put("title", "Alarma por CO!");

            pushNotification(user, headers, notification, registration_ids);
        }

        if (device.getSensor2Status() == Status.ALARM) {
            notification.put("click_action", ".MainActivity");
            notification.put("body", "Gas natural por encima del límite seguro en dispositivo ID: "
                                        + device.getAlias());
            notification.put("title", "Alarma por Gas Natural!");

            pushNotification(user, headers, notification, registration_ids);
        }

        if (device.getSensor3Status() == Status.ALARM) {
            notification.put("click_action", ".MainActivity");
            notification.put("body", "Humo encima del límite seguro en dispositivo ID: "
                                        + device.getAlias());
            notification.put("title", "Alarma por HUMO!");

            pushNotification(user, headers, notification, registration_ids);
        }
    }

    public void pushNotificationDeviceBackToNormal(final User user, final Device device) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, FIREBASE_AUTHORIZATION_FOR_EXPERTA);

        ArrayNode registration_ids = mapper.createArrayNode();
        registration_ids.add(user.getApplicationToken());

        ObjectNode notification = mapper.createObjectNode();
        notification.put("icon", "experta_logo");

        notification.put("click_action", ".MainActivity");
        notification.put("body", "Estado NORMAL otra vez, en dispositivo: "
                                    + device.getAlias());
        notification.put("title", "Vuelta a estado NORMAL");

        pushNotification(user, headers, notification, registration_ids);
    }

}
