package com.bima.toharifqi.labird.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SpesiesModel implements Parcelable {
    private String spesiesName, spesiesLatin, spesiesImage, spesiesSound, spesiesId, uploadedBy, sDescText, sSoundText, sDistributionText, sHabitText, sFoundedText;

    public SpesiesModel() {
    }

    public SpesiesModel(String spesiesName, String spesiesLatin, String spesiesImage, String spesiesSound
            , String spesiesId, String uploadedBy, String sDescText, String sSoundText, String sDistributionText
            , String sHabitText, String sFoundedText) {
        this.spesiesName = spesiesName;
        this.spesiesLatin = spesiesLatin;
        this.spesiesImage = spesiesImage;
        this.spesiesSound = spesiesSound;
        this.spesiesId = spesiesId;
        this.uploadedBy = uploadedBy;
        this.sDescText = sDescText;
        this.sSoundText = sSoundText;
        this.sDistributionText = sDistributionText;
        this.sHabitText = sHabitText;
        this.sFoundedText = sFoundedText;
    }

    protected SpesiesModel(Parcel in) {
        spesiesName = in.readString();
        spesiesLatin = in.readString();
        spesiesImage = in.readString();
        spesiesSound = in.readString();
        spesiesId = in.readString();
        uploadedBy = in.readString();
        sDescText = in.readString();
        sSoundText = in.readString();
        sDistributionText = in.readString();
        sHabitText = in.readString();
        sFoundedText = in.readString();
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

    public String getSpesiesLatin() {
        return spesiesLatin;
    }

    public String getSpesiesImage() {
        return spesiesImage;
    }

    public String getSpesiesSound() {
        return spesiesSound;
    }

    public String getSpesiesId() {
        return spesiesId;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public String getsDescText() {
        return sDescText;
    }

    public String getsSoundText() {
        return sSoundText;
    }

    public String getsDistributionText() {
        return sDistributionText;
    }

    public String getsHabitText() {
        return sHabitText;
    }

    public String getsFoundedText() {
        return sFoundedText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(spesiesName);
        parcel.writeString(spesiesLatin);
        parcel.writeString(spesiesImage);
        parcel.writeString(spesiesSound);
        parcel.writeString(spesiesId);
        parcel.writeString(uploadedBy);
        parcel.writeString(sDescText);
        parcel.writeString(sSoundText);
        parcel.writeString(sDistributionText);
        parcel.writeString(sHabitText);
        parcel.writeString(sFoundedText);
    }
}
