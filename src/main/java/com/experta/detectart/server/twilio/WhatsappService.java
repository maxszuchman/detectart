package com.experta.detectart.server.twilio;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.experta.detectart.server.model.Contact;

//Install the Java helper library from twilio.com/docs/java/install

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class WhatsappService {

    private static final Logger log = LoggerFactory.getLogger(WhatsappService.class);

    private static final String TWILIO_PHONE_NUMBER = System.getenv("FROM_TELEPHONE_NUMBER");

    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

    private List<String> sids;

    public WhatsappService() {
        sids = new ArrayList<String>();
    }

    public void sendWhatsappMessageToContacts(final EmergencyMessage emergencyMessage) {

       Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

       for (Contact contact : emergencyMessage.getContacts()) {
           sendWhatsappMessage(emergencyMessage.getMessage(), contact.getPhone());
       }
    }

    public void sendWhatsappMessage(final String message, final String phoneNumber) {
        log.info("Enviando WHATSAPP desde {} a {}, mensaje: {}", TWILIO_PHONE_NUMBER, phoneNumber, message);

        Message whatsappMessage = Message.creator(new PhoneNumber("whatsapp:" + phoneNumber)
                                                  , new PhoneNumber("whatsapp:+" + TWILIO_PHONE_NUMBER)
                                                  , message)
                                         .create();

        sids.add(whatsappMessage.getSid());
    }
}
