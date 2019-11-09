package com.experta.detectart.server.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "push_notifications")
public class PushNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    @JsonIgnore
    private Device device;

    @NotEmpty
    private String icon;

    @NotEmpty
    private String sound;

    @NotEmpty
    private String click_action;

    @NotEmpty
    private String body;

    @NotEmpty
    private String title;

    public PushNotification() {}

    public PushNotification(final Long id, final User user, final Device device, @NotEmpty final String icon, @NotEmpty final String sound,
            @NotEmpty final String click_action, @NotEmpty final String body, @NotEmpty final String title) {
        super();
        this.id = id;
        this.user = user;
        this.device = device;
        this.icon = icon;
        this.sound = sound;
        this.click_action = click_action;
        this.body = body;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(final Device device) {
        this.device = device;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(final String sound) {
        this.sound = sound;
    }

    public String getClick_action() {
        return click_action;
    }

    public void setClick_action(final String click_action) {
        this.click_action = click_action;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
}
