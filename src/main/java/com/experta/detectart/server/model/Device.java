package com.experta.detectart.server.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "devices")
public class Device extends AuditModel {

    @Id
    @NotEmpty
    private String macAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    private String alias;

    private String model;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Double accuracy;

    public Device() {}

    public Device(@NotEmpty final String macAddress, final User user, final String alias, final String model, @NotEmpty final Double latitude,
            @NotEmpty final Double longitude, @NotEmpty final Double accuracy) {
        super();
        this.macAddress = macAddress;
        this.user = user;
        this.alias = alias;
        this.model = model;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(final String macAddress) {
        this.macAddress = macAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String model) {
        this.model = model;
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

    public Device copyInto(final Device other) {

        other.setMacAddress(this.macAddress);
        other.setAlias(this.alias);
        other.setModel(this.model);
        other.setLatitude(this.latitude);
        other.setLongitude(this.longitude);
        other.setAccuracy(this.accuracy);

        return other;
    }

}
