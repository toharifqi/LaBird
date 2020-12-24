package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.CourseModel;

import java.util.List;

public interface IFirebaseLoadDoneCourse extends IFirebaseLoadDone{
    void onFirebaseLoadSuccessCourse(List<CourseModel> courseList);
}
