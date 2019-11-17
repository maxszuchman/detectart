package com.experta.detectart.server.services;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.experta.detectart.server.model.Device;
import com.experta.detectart.server.model.PushNotification;
import com.experta.detectart.server.model.User;
import com.experta.detectart.server.model.deviceData.Status;
import com.experta.detectart.server.repository.PushNotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class PushNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    private static final String FIREBASE_URL_STRING = System.getenv("FIREBASE_URL");
    private static final String FIREBASE_AUTHORIZATION_FOR_EXPERTA = System.getenv("FIREBASE_AUTHORIZATION_FOR_EXPERTA");

    @Autowired
    private PushNotificationRepository pushNotificationRepository;

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

    private void pushNotification(final User user, final Device device, final HttpHeaders headers, final ObjectNode data, final ArrayNode registration_ids) {

        ObjectNode body = mapper.createObjectNode();
        body.set("data", data);
        body.set("registration_ids", registration_ids);

        log.info("Sending a PUSH notification to {} {}, {}, with token {} and body {}"
                , user.getFullName()
                , user.getId()
                , user.getApplicationToken()
                , data.toPrettyString());
        log.info(body.toPrettyString());

        RequestEntity<ObjectNode> requestEntity = RequestEntity.post(FIREBASE_URI)
                                                               .headers(headers)
                                                               .body(body);

        ResponseEntity<ObjectNode> response = restTemplate.exchange(requestEntity, ObjectNode.class);

        PushNotification pushNotification = new PushNotification(null
                                                                , user.getId()
                                                                , user.getFullName()
                                                                , user.getApplicationToken()
                                                                , device.getMacAddress()
                                                                , device.getAlias()
                                                                , device.getLatitude()
                                                                , device.getLongitude()
                                                                , device.getAccuracy()
                                                                , data.get("icon").asText()
                                                                , data.get("sound").asText()
                                                                , data.get("click_action").asText()
                                                                , data.get("body").asText()
                                                                , data.get("title").asText());

        pushNotificationRepository.save(pushNotification);
    }

    public void pushNotificationForEachSensorToToken(final User user, final Device device) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, FIREBASE_AUTHORIZATION_FOR_EXPERTA);

        ArrayNode registration_ids = mapper.createArrayNode();
        registration_ids.add(user.getApplicationToken());

        ObjectNode data = mapper.createObjectNode();
        data.put("icon", "icon.png");
        data.put("sound", "alarma.mp3");

        if (device.getSensor1Status() == Status.ALARM) {
            data.put("click_action", ".MainActivity");
            data.put("body", "Monóxido de carbóno por encima del límite seguro en dispositivo: "
                                        + device.getAlias());
            data.put("title", "Alarma por CO!");

            pushNotification(user, device, headers, data, registration_ids);
        }

        if (device.getSensor2Status() == Status.ALARM) {
            data.put("click_action", ".MainActivity");
            data.put("body", "Gas natural por encima del límite seguro en dispositivo ID: "
                                        + device.getAlias());
            data.put("title", "Alarma por Gas Natural!");

            pushNotification(user, device, headers, data, registration_ids);
        }

        if (device.getSensor3Status() == Status.ALARM) {
            data.put("click_action", ".MainActivity");
            data.put("body", "Humo encima del límite seguro en dispositivo ID: "
                                        + device.getAlias());
            data.put("title", "Alarma por HUMO!");

            pushNotification(user, device, headers, data, registration_ids);
        }
    }

    public void pushNotificationDeviceBackToNormal(final User user, final Device device) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, FIREBASE_AUTHORIZATION_FOR_EXPERTA);

        ArrayNode registration_ids = mapper.createArrayNode();
        registration_ids.add(user.getApplicationToken());

        ObjectNode data = mapper.createObjectNode();
        data.put("icon", "icon.png");
        data.put("sound", "ding.mp3");

        data.put("click_action", ".MainActivity");
        data.put("body", "Estado NORMAL otra vez, en dispositivo: "
                                    + device.getAlias());
        data.put("title", "Vuelta a estado NORMAL");

        pushNotification(user, device, headers, data, registration_ids);
    }

}
