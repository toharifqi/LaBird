package com.bima.toharifqi.labird.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class BirdUploadModel implements Parcelable {
    private String senderId, timeStamp, photoUrl, itemId;

    public BirdUploadModel() {
    }

    public BirdUploadModel(String senderId, String timeStamp, String photoUrl, String itemId) {
        this.senderId = senderId;
        this.timeStamp = timeStamp;
        this.photoUrl = photoUrl;
        this.itemId = itemId;
    }

    protected BirdUploadModel(Parcel in) {
        senderId = in.readString();
        timeStamp = in.readString();
        photoUrl = in.readString();
        itemId = in.readString();
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

    public String getItemId() {
        return itemId;
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
        parcel.writeString(itemId);
    }

    public Map<String, Object> addBird() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("senderId", senderId);
        result.put("timeStamp", timeStamp);
        result.put("photoUrl", photoUrl);
        result.put("itemId", itemId);

        return result;
    }
}
