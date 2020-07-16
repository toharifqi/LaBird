package com.bima.toharifqi.labird.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SpesiesModel implements Parcelable {
    private String spesiesName, spesiesLatin, spesiesDescription, spesiesImage, spesiesSound, spesiesId;

    public SpesiesModel() {
    }

    public SpesiesModel(String spesiesName, String spesiesLatin, String spesiesDescription, String spesiesImage, String spesiesSound, String spesiesId) {
        this.spesiesName = spesiesName;
        this.spesiesLatin = spesiesLatin;
        this.spesiesDescription = spesiesDescription;
        this.spesiesImage = spesiesImage;
        this.spesiesSound = spesiesSound;
        this.spesiesId = spesiesId;
    }

    protected SpesiesModel(Parcel in) {
        spesiesName = in.readString();
        spesiesLatin = in.readString();
        spesiesDescription = in.readString();
        spesiesImage = in.readString();
        spesiesSound = in.readString();
        spesiesId = in.readString();
    }

    public static final Creator<SpesiesModel> CREATOR = new Creator<SpesiesModel>() {
        @Override
        public SpesiesModel createFromParcel(Parcel in) {
            return new SpesiesModel(in);
        }

        @Override
        public SpesiesModel[] newArray(int size) {
            return new SpesiesModel[size];
        }
    };

    public String getSpesiesName() {
        return spesiesName;
    }

    public void setSpesiesName(String spesiesName) {
        this.spesiesName = spesiesName;
    }

    public String getSpesiesLatin() {
        return spesiesLatin;
    }

    public void setSpesiesLatin(String spesiesLatin) {
        this.spesiesLatin = spesiesLatin;
    }

    public String getSpesiesDescription() {
        return spesiesDescription;
    }

    public void setSpesiesDescription(String spesiesDescription) {
        this.spesiesDescription = spesiesDescription;
    }

    public String getSpesiesImage() {
        return spesiesImage;
    }

    public void setSpesiesImage(String spesiesImage) {
        this.spesiesImage = spesiesImage;
    }

    public String getSpesiesSound() {
        return spesiesSound;
    }

    public void setSpesiesSound(String spesiesSound) {
        this.spesiesSound = spesiesSound;
    }

    public String getSpesiesId() {
        return spesiesId;
    }

    public void setSpesiesId(String spesiesId) {
        this.spesiesId = spesiesId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(spesiesName);
        parcel.writeString(spesiesLatin);
        parcel.writeString(spesiesDescription);
        parcel.writeString(spesiesImage);
        parcel.writeString(spesiesSound);
        parcel.writeString(spesiesId);
    }
}
