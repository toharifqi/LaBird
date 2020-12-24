package com.bima.toharifqi.labird.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class BirdUploadModel implements Parcelable {
    private String senderId, timeStamp, photoUrl;

    public BirdUploadModel() {
    }

    public BirdUploadModel(String senderId, String timeStamp, String photoUrl) {
        this.senderId = senderId;
        this.timeStamp = timeStamp;
        this.photoUrl = photoUrl;
    }

    protected BirdUploadModel(Parcel in) {
        senderId = in.readString();
        timeStamp = in.readString();
        photoUrl = in.readString();
    }

    public static final Creator<BirdUploadModel> CREATOR = new Creator<BirdUploadModel>() {
        @Override
        public BirdUploadModel createFromParcel(Parcel in) {
            return new BirdUploadModel(in);
        }

        @Override
        public BirdUploadModel[] newArray(int size) {
            return new BirdUploadModel[size];
        }
    };

    public String getSenderId() {
        return senderId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(senderId);
        parcel.writeString(timeStamp);
        parcel.writeString(photoUrl);
    }

    public Map<String, Object> addBird() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("senderId", senderId);
        result.put("timeStamp", timeStamp);
        result.put("photoUrl", photoUrl);

        return result;
    }
}
