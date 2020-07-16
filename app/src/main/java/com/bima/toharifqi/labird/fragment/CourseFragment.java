package com.bima.toharifqi.labird.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.R;
import com.bima.toharifqi.labird.adapter.CourseAdapter;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneCourse;
import com.bima.toharifqi.labird.model.CourseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment implements IFirebaseLoadDoneCourse {

    ViewPager viewPagerCourse;
    CourseAdapter courseAdapter;

    IFirebaseLoadDoneCourse iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List <CourseModel> courseList = new ArrayList<>();
            for (DataSnapshot courseSnapshot:dataSnapshot.getChildren())
                courseList.add(courseSnapshot.getValue(CourseModel.class));
            iFirebaseLoadDone.onFirebaseLoadSuccess(courseList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
        }
    };

    Query query;
    View view;

    public CourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course, container, false);

        query = FirebaseDatabase.getInstance().getReference("course");

        iFirebaseLoadDone = this;

        loadCourse();

        viewPagerCourse = (ViewPager) view.findViewById(R.id.courseViewPager);
        viewPagerCourse.setPadding(20,0,20,0);
        viewPagerCourse.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    private void loadCourse() {
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public void onFirebaseLoadSuccess(List<CourseModel> courseList) {
        courseAdapter = new CourseAdapter(courseList, getContext());
        viewPagerCourse.setAdapter(courseAdapter);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        query.removeEventListener(valueEventListener);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        query.removeEventListener(valueEventListener);
        super.onStop();
    }
}
