package com.experta.detectart.server.model.deviceData;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.experta.detectart.server.model.AuditModelOnlyCreated;

@Entity
public class DeviceData extends AuditModelOnlyCreated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String macAddress;

    @Column(name="status", nullable = false, length = 8)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @NotNull
    private Position position;

    @AttributeOverrides({
        @AttributeOverride(name = "type", column = @Column(name = "sensor1_type")),
        @AttributeOverride(name = "sensorStatus", column = @Column(name = "sensor1_status")),
        @AttributeOverride(name = "level", column = @Column(name = "sensor1_level"))
    })
    @Embedded
    private Sensor sensor1;

    @AttributeOverrides({
        @AttributeOverride(name = "type", column = @Column(name = "sensor2_type")),
        @AttributeOverride(name = "sensorStatus", column = @Column(name = "sensor2_status")),
        @AttributeOverride(name = "level", column = @Column(name = "sensor2_level"))
    })
    @Embedded
    private Sensor sensor2;

    @AttributeOverrides({
        @AttributeOverride(name = "type", column = @Column(name = "sensor3_type")),
        @AttributeOverride(name = "sensorStatus", column = @Column(name = "sensor3_status")),
        @AttributeOverride(name = "level", column = @Column(name = "sensor3_level"))
    })
    @Embedded
    private Sensor sensor3;

    public DeviceData() {}

    public DeviceData(final Long id, @NotNull final String macAddress, final Status status, @NotNull final Position position, final Sensor sensor1,
            final Sensor sensor2, final Sensor sensor3) {
        super();
        this.id = id;
        this.macAddress = macAddress;
        this.status = status;
        this.position = position;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(final String macAddress) {
        this.macAddress = macAddress;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public Sensor getSensor1() {
        return sensor1;
    }

    public void setSensor1(final Sensor sensor1) {
        this.sensor1 = sensor1;
    }

    public Sensor getSensor2() {
        return sensor2;
    }

    public void setSensor2(final Sensor sensor2) {
        this.sensor2 = sensor2;
    }

    public Sensor getSensor3() {
        return sensor3;
    }

    public void setSensor3(final Sensor sensor3) {
        this.sensor3 = sensor3;
    }

}
