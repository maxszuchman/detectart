package com.experta.detectart.server.twilio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.experta.detectart.server.model.Contact;
import com.experta.detectart.server.model.WhatsappMessage;
import com.experta.detectart.server.repository.WhatsappRepository;

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

    @Autowired
    WhatsappRepository whatsappRepository;

    public WhatsappService() {}

    public void sendWhatsappMessageToContacts(final EmergencyMessage emergencyMessage) {

       Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

       for (Contact contact : emergencyMessage.getContacts()) {
           sendWhatsappMessage(emergencyMessage, contact);
       }
    }

    public void sendWhatsappMessage(final EmergencyMessage emergencyMessage, final Contact contact) {
        log.info("Enviando WHATSAPP desde {} a {}, mensaje: {}", TWILIO_PHONE_NUMBER, contact.getPhone(), emergencyMessage.getMessage());

        Message whatsappMessage = Message.creator(new PhoneNumber("whatsapp:" + contact.getPhone())
                                                  , new PhoneNumber("whatsapp:+" + TWILIO_PHONE_NUMBER)
                                                  , emergencyMessage.getMessage())
                                         .create();

        WhatsappMessage messageLog = new WhatsappMessage(null
                                                        , emergencyMessage.getUser()
                                                        , emergencyMessage.getDevice()
                                                        , contact
                                                        , whatsappMessage.getSid()
                                                        , emergencyMessage.getMessage());

        whatsappRepository.save(messageLog);
    }
}
