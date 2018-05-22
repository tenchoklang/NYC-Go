package com.android.tenchoklang.nycgo;

/**
 * Created by tench on 5/21/2018.
 */

public class Hologram {
    double lat;
    double lon;
    String photoUrl;

    public Hologram(double lat, double lon, String photoUrl) {
        this.lat = lat;
        this.lon = lon;
        this.photoUrl = photoUrl;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
