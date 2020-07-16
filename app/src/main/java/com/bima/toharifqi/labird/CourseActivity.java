package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.adapter.VidAdapter;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDonePic;
import com.bima.toharifqi.labird.model.CourseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity implements IFirebaseLoadDonePic {
    private TextView courseName, courseDescription;
    private ImageView imageContent;
    private RecyclerView vidRecyclerView;
    private VidAdapter vidAdapter;
    String courseId;


    IFirebaseLoadDonePic iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<String> vidList = new ArrayList<>();
            for (DataSnapshot vidSnapshot:dataSnapshot.getChildren())
                vidList.add(vidSnapshot.getValue().toString());

            iFirebaseLoadDone.onFirebaseLoadSuccess(vidList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    Toolbar toolbar;
    Query query;

    CourseModel courseModelExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        // Action Bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        //get data from adapter
        courseModelExtra = getIntent().getParcelableExtra("courseModelExtra");
        courseId = courseModelExtra.getCourseId();

        //tipycal views
        courseName = findViewById(R.id.courseName);
        courseDescription = findViewById(R.id.courseDescription);
        imageContent = findViewById(R.id.imageContent);

        //set tipycal data
        courseName.setText("Langkah Belajar: "+courseModelExtra.getCourseTitle());
        courseDescription.setText(R.string.langkah_belajar);
        imageContent.setImageResource(R.drawable.morfologi_burung);
        ZoomInImageViewAttacher imageAttacher = new ZoomInImageViewAttacher();
        imageAttacher.attachImageView(imageContent);
        imageAttacher.setZoomable(true);


        query = FirebaseDatabase.getInstance().getReference("course").child(courseId).child("courseVid");

        iFirebaseLoadDone = this;
        loadVid();

        vidRecyclerView = findViewById(R.id.vidRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CourseActivity.this, LinearLayoutManager.HORIZONTAL, false);
        vidRecyclerView.setLayoutManager(layoutManager);
        vidRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public void toCourse2 (View v){
        Intent intent = new Intent(CourseActivity.this, CourseActivity2.class);
        intent.putExtra("courseModelExtra", courseModelExtra);
        startActivity(intent);
    }

    private void loadVid() {
        query.addValueEventListener(valueEventListener);
    }

    public void toAllSpecies(View view){
        Intent intent = new Intent(CourseActivity.this, AllSpesiesActivity.class);
        intent.putExtra("courseName", courseModelExtra.getCourseTitle());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onFirebaseLoadSuccess(List<String> picList) {
        vidAdapter = new VidAdapter(picList, CourseActivity.this, CourseActivity.this.getLifecycle());
        vidRecyclerView.setAdapter(vidAdapter);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(CourseActivity.this, ""+message, Toast.LENGTH_SHORT).show();
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
