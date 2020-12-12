package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.CourseModel;

import java.util.List;

public interface IFirebaseLoadDoneCourse {
    void onFirebaseLoadSuccessCourse(List<CourseModel> courseList);
    void onFirebaseLoadFailedCourse(String message);
}
