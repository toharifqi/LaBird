package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.bima.toharifqi.labird.adapter.CourseGridAdapter;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneCourse;
import com.bima.toharifqi.labird.model.CourseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AllCourseActivity extends AppCompatActivity implements IFirebaseLoadDoneCourse {

    private RecyclerView courseRecyclerView;
    private CourseGridAdapter courseGridAdapter;

    AlertDialog dialog;

    IFirebaseLoadDoneCourse iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<CourseModel> courseList = new ArrayList<>();
            for (DataSnapshot courseSnapshot:dataSnapshot.getChildren())
                courseList.add(courseSnapshot.getValue(CourseModel.class));
            iFirebaseLoadDone.onFirebaseLoadSuccessCourse(courseList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadFailedCourse(databaseError.getMessage());
        }
    };

    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_course);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);

        query = FirebaseDatabase.getInstance().getReference("course");

        iFirebaseLoadDone = this;

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(AllCourseActivity.this).build();
        dialog.setMessage("Mohon tunggu...");

        loadCourse();

        courseRecyclerView = findViewById(R.id.recyclerview_course);
        courseRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

    }

    private void loadCourse() {
        dialog.show();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFirebaseLoadSuccessCourse(List<CourseModel> courseList) {
        courseGridAdapter = new CourseGridAdapter(AllCourseActivity.this, courseList);
        courseRecyclerView.setAdapter(courseGridAdapter);
        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailedCourse(String message) {
        Toast.makeText(AllCourseActivity.this, ""+message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
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
