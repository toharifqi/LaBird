package com.bima.toharifqi.labird.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MateriModel implements Parcelable {
    String materiTitle, materiContent, materiImage, materiAuthor, materiDesc, materiContentImage = "null";

    public MateriModel() {
    }

    public MateriModel(String materiTitle, String materiContent, String materiImage, String materiAuthor, String materiDesc, String materiContentImage) {
        this.materiTitle = materiTitle;
        this.materiContent = materiContent;
        this.materiImage = materiImage;
        this.materiAuthor = materiAuthor;
        this.materiDesc = materiDesc;
        this.materiContentImage = materiContentImage;
    }


    protected MateriModel(Parcel in) {
        materiTitle = in.readString();
        materiContent = in.readString();
        materiImage = in.readString();
        materiAuthor = in.readString();
        materiDesc = in.readString();
        materiContentImage = in.readString();
    }

    public static final Creator<MateriModel> CREATOR = new Creator<MateriModel>() {
        @Override
        public MateriModel createFromParcel(Parcel in) {
            return new MateriModel(in);
        }

        @Override
        public MateriModel[] newArray(int size) {
            return new MateriModel[size];
        }
    };

    public String getMateriTitle() {
        return materiTitle;
    }

    public void setMateriTitle(String materiTitle) {
        this.materiTitle = materiTitle;
    }

    public String getMateriContent() {
        return materiContent;
    }

    public void setMateriContent(String materiContent) {
        this.materiContent = materiContent;
    }

    public String getMateriImage() {
        return materiImage;
    }

    public void setMateriImage(String materiImage) {
        this.materiImage = materiImage;
    }

    public String getMateriDesc() {
        return materiDesc;
    }

    public void setMateriDesc(String materiDesc) {
        this.materiDesc = materiDesc;
    }

    public String getMateriAuthor() {
        return materiAuthor;
    }

    public void setMateriAuthor(String materiAuthor) {
        this.materiAuthor = materiAuthor;
    }

    public String getMateriContentImage() {
        return materiContentImage;
    }

    public void setMateriContentImage(String materiContentImage) {
        this.materiContentImage = materiContentImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(materiTitle);
        parcel.writeString(materiContent);
        parcel.writeString(materiImage);
        parcel.writeString(materiAuthor);
        parcel.writeString(materiDesc);
        parcel.writeString(materiContentImage);
    }
}
