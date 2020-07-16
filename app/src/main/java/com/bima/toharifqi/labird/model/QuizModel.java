package com.bima.toharifqi.labird.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizModel implements Parcelable {
    private String quizName, quizId, quizDate;
    private int quizPoin;

    public QuizModel() {
    }

    public QuizModel(String quizName, String quizId, String quizDate, int quizPoin) {
        this.quizName = quizName;
        this.quizId = quizId;
        this.quizDate = quizDate;
        this.quizPoin = quizPoin;
    }

    protected QuizModel(Parcel in) {
        quizName = in.readString();
        quizId = in.readString();
        quizDate = in.readString();
        quizPoin = in.readInt();
    }

    public static final Creator<QuizModel> CREATOR = new Creator<QuizModel>() {
        @Override
        public QuizModel createFromParcel(Parcel in) {
            return new QuizModel(in);
        }

        @Override
        public QuizModel[] newArray(int size) {
            return new QuizModel[size];
        }
    };

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(String quizDate) {
        this.quizDate = quizDate;
    }

    public int getQuizPoin() {
        return quizPoin;
    }

    public void setQuizPoin(int quizPoin) {
        this.quizPoin = quizPoin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(quizName);
        parcel.writeString(quizId);
        parcel.writeString(quizDate);
        parcel.writeInt(quizPoin);
    }
}
