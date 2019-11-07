package com.experta.detectart.twilio;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import com.experta.detectart.server.model.Contact;
import com.experta.detectart.server.model.deviceData.Sensor;

public class EmergencyMessage {

    public static final String ALERT_ON_SENSOR = "ALERT ON SENSOR";
    public static final String FROM_TELEPHONE_NUMBER = "14155238886";

    private List<Sensor> sensors;
    private Collection<Contact> contacts;
    private Instant sentTime;

    public EmergencyMessage(final List<Sensor> sensors, final Collection<Contact> contacts) {
        super();
        this.sensors = sensors;
        this.contacts = contacts;
        sentTime = Instant.now();
    }

    public String getMessage() {
        String message = ALERT_ON_SENSOR;

        if (sensors.size() > 1) {
            message += "S";
        }

        for (Sensor sensor : sensors) {
            message += sensor.getType() + ", ";
        }

        message = message.substring(0, message.length() - 2);
        return message;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public Collection<Contact> getContacts() {
        return contacts;
    }

    public Instant getSentTime() {
        return sentTime;
    }
}
