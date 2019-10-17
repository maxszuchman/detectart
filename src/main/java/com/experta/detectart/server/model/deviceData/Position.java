package com.experta.detectart.server.model.deviceData;

import javax.persistence.Embeddable;

@Embeddable
public class Position {

    private double latitude, longitude, accuracy;

    public Position() {}

    public Position(final double latitude, final double longitude, final double accuracy) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(final double accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "Position [latitude=" + latitude + ", longitude=" + longitude + ", accuracy=" + accuracy + "]";
    }


}
