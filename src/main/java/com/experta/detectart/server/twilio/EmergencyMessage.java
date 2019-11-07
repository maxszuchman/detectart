package com.experta.detectart.server.twilio;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import com.experta.detectart.server.model.Contact;
import com.experta.detectart.server.model.Device;
import com.experta.detectart.server.model.User;
import com.experta.detectart.server.model.deviceData.Sensor;

public class EmergencyMessage {

    public static final String FROM_TELEPHONE_NUMBER = "14155238886";

    private static final String GOOGLE_MAPS_API = "https://www.google.com/maps/search/?api=1query=";

    private List<Sensor> sensors;
    private Collection<Contact> contacts;
    private Device device;
    private User user;
    private Instant sentTime;

    public EmergencyMessage(final List<Sensor> sensors, final Collection<Contact> contacts, final User user
                            , final Device device) {
        super();
        this.sensors = sensors;
        this.contacts = contacts;
        sentTime = Instant.now();
    }

    public String getMessage() {
        String message = "Alerta de ";;

        for (Sensor sensor : sensors) {
            message += sensor.getType() + ", ";
        }

        message = message.substring(0, message.length() - 2);
        message += " en el dispositivo " + device.getAlias();
        message += " del usuario " + user.getFullName();
        message += " ubicado en " + GOOGLE_MAPS_API;
        message += device.getLatitude() + "," + device.getLongitude();

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
