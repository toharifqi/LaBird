package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.bima.toharifqi.labird.adapter.ExpertListAdapter;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneExpert;
import com.bima.toharifqi.labird.model.ExpertModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AddBirdActivity extends AppCompatActivity implements IFirebaseLoadDoneExpert {
    private RecyclerView expertRecyclerView;
    private ExpertListAdapter expertListAdapter;

    AlertDialog dialog;

    IFirebaseLoadDoneExpert iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<ExpertModel> expertModelList = new ArrayList<>();
            for (DataSnapshot expertSnapshot: dataSnapshot.getChildren())
                expertModelList.add(expertSnapshot.getValue(ExpertModel.class));
            iFirebaseLoadDone.onFirebaseLoadSuccess(expertModelList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
        }
    };

    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bird);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        query = FirebaseDatabase.getInstance().getReference("experts");

        iFirebaseLoadDone = this;

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(AddBirdActivity.this).build();
        dialog.setMessage("Mohon tunggu...");

        loadExpert();

        expertRecyclerView = findViewById(R.id.recyclerview_expert);
        expertRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void loadExpert() {
        dialog.show();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFirebaseLoadSuccess(List<ExpertModel> expertModels) {
        expertListAdapter = new ExpertListAdapter(AddBirdActivity.this, expertModels);
        expertRecyclerView.setAdapter(expertListAdapter);
        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(AddBirdActivity.this, ""+message, Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void onStop() {
        query.removeEventListener(valueEventListener);
        super.onStop();
    }
}