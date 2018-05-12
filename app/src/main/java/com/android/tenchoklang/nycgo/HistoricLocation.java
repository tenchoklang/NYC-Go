package com.android.tenchoklang.nycgo;

/**
 * Created by tench on 5/7/2018.
 */

public class HistoricLocation {
    private double lat;
    private double lon;
    private String imageUrl;

    public HistoricLocation(double lat, double lon, String description) {
        this.lat = lat;
        this.lon = lon;
        this.imageUrl = description;
    }

    public double getLat() {  return lat;  }
    public void setLat(double lat) { this.lat = lat;  }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon;  }

    public String getDescription() { return imageUrl;  }
    public void setDescription(String description) { this.imageUrl = description;  }

    @Override
    public String toString() {
        return "HistoricLocation{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", description='" + imageUrl + '\'' +
                '}';
    }
}
