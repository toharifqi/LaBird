package com.bima.toharifqi.labird.model;

public class NotifModel {
    String spesiesId, message, notifId, expertName, timeStamp;

    public NotifModel() {
    }

    public NotifModel(String spesiesId, String message, String notifId, String expertName, String timeStamp) {
        this.spesiesId = spesiesId;
        this.message = message;
        this.notifId = notifId;
        this.expertName = expertName;
        this.timeStamp = timeStamp;
    }

    public String getSpesiesId() {
        return spesiesId;
    }

    public String getMessage() {
        return message;
    }

    public String getNotifId() {
        return notifId;
    }

    public String getExpertName() {
        return expertName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
