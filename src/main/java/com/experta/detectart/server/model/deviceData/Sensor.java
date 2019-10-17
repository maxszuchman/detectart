package com.experta.detectart.server.model.deviceData;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Embeddable
public class Sensor {

    private String type;

    @Column(name="status", nullable = false, length = 8)
    @Enumerated(value = EnumType.STRING)
    private Status sensorStatus;

    @NotNull
    private Integer level;

    public Sensor() {}

    public Sensor(final String type, final Status status, final Integer level) {
        super();
        this.type = type;
        this.sensorStatus = status;
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Status getStatus() {
        return sensorStatus;
    }

    public void setStatus(final Status status) {
        this.sensorStatus = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(final Integer level) {
        this.level = level;
    }

}
