package com.experta.detectart.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.experta.detectart.server.model.User;

@Component
public class PushNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    public static final String FIREBASE_URL = "https://fcm.googleapis.com/fcm/send";

    public void pushNoificationToToken(final User user) {
        log.info("Sending an ALARM push notification to {} {}, {}, with token {}"
                , user.getFirstName()
                , user.getLastName()
                , user.getEmail()
                , user.getApplicationToken());
    }

}
