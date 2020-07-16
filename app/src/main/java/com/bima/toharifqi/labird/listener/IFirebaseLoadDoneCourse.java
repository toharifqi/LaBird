package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.CourseModel;

import java.util.List;

public interface IFirebaseLoadDoneCourse {
    void onFirebaseLoadSuccess(List<CourseModel> courseList);
    void onFirebaseLoadFailed(String message);
}
