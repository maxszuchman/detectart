package com.experta.detectart.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "whatsapp_messages")
public class WhatsappMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String user_id;

    @NotEmpty
    private String user_name;

    @NotEmpty
    private String application_id;

    @NotEmpty
    private String device_id;

    @NotEmpty
    private String device_alias;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Double accuracy;

    @NotEmpty
    private String contact_name;

    @NotEmpty
    private String contact_phone;

    // El sid es el id del mensaje que nos devuelve Twilio
    @NotNull
    private String sid;

    @NotNull
    private String messageString;

    public WhatsappMessage() {}

    public WhatsappMessage(final Long id, @NotEmpty final String user_id, @NotEmpty final String user_name,
            @NotEmpty final String application_id, @NotEmpty final String device_id, @NotEmpty final String device_alias,
            @NotEmpty final Double latitude, @NotEmpty final Double longitude, @NotEmpty final Double accuracy,
            @NotEmpty final String contact_name, @NotEmpty final String contact_phone, @NotNull final String sid,
            @NotNull final String messageString) {
        super();
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.application_id = application_id;
        this.device_id = device_id;
        this.device_alias = device_alias;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.contact_name = contact_name;
        this.contact_phone = contact_phone;
        this.sid = sid;
        this.messageString = messageString;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(final String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(final String user_name) {
        this.user_name = user_name;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(final String application_id) {
        this.application_id = application_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(final String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_alias() {
        return device_alias;
    }

    public void setDevice_alias(final String device_alias) {
        this.device_alias = device_alias;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(final Double accuracy) {
        this.accuracy = accuracy;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(final String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(final String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(final String sid) {
        this.sid = sid;
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(final String messageString) {
        this.messageString = messageString;
    }

}
