package com.mekpap.mekPap.history;

public class HistoryObject {
    private String rideId;
    private String requestTime;

    public HistoryObject(String rideId, String requestTime) {
        this.rideId = rideId;
        this.requestTime = requestTime;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }


}
