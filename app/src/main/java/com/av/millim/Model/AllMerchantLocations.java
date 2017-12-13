package com.av.millim.Model;

import java.io.Serializable;

/**
 * Created by Maiada on 11/26/2017.
 */

public class AllMerchantLocations implements Serializable {
    public  String  Longitude;
    public  String  Latitude;
    public  String  storeName;


    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
