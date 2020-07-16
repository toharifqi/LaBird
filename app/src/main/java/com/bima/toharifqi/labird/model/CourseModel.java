package com.bima.toharifqi.labird.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseModel implements Parcelable {
    private String courseTitle, courseImage, courseId;

    public CourseModel() {
    }

    public CourseModel(String courseTitle, String courseImage, String courseId) {
        this.courseTitle = courseTitle;
        this.courseImage = courseImage;
        this.courseId = courseId;
    }

    protected CourseModel(Parcel in) {
        courseTitle = in.readString();
        courseImage = in.readString();
        courseId = in.readString();
    }

    public static final Creator<CourseModel> CREATOR = new Creator<CourseModel>() {
        @Override
        public CourseModel createFromParcel(Parcel in) {
            return new CourseModel(in);
        }

        @Override
        public CourseModel[] newArray(int size) {
            return new CourseModel[size];
        }
    };

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(courseTitle);
        parcel.writeString(courseImage);
        parcel.writeString(courseId);
    }
}
