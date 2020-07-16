package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.bima.toharifqi.labird.adapter.MateriGridAdapter;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneMateri;
import com.bima.toharifqi.labird.model.MateriModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AllMateriActivity extends AppCompatActivity implements IFirebaseLoadDoneMateri {
    private RecyclerView materiRecyclerView;
    private MateriGridAdapter materiGridAdapter;

    AlertDialog dialog;

    IFirebaseLoadDoneMateri iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<MateriModel> materiList = new ArrayList<>();
            for (DataSnapshot materiSnapshot:dataSnapshot.getChildren())
                materiList.add(materiSnapshot.getValue(MateriModel.class));
            iFirebaseLoadDone.onFirebaseLoadMateriSuccess(materiList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadMateriFailed(databaseError.getMessage());
        }
    };

    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_materi);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);

        query = FirebaseDatabase.getInstance().getReference("materi");
        iFirebaseLoadDone = this;

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(AllMateriActivity.this).build();
        dialog.setMessage("Mohon tunggu...");

        loadMateri();

        materiRecyclerView = findViewById(R.id.recyclerview_materi);
        materiRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    private void loadMateri() {
        dialog.show();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFirebaseLoadMateriSuccess(List<MateriModel> materiList) {
        materiGridAdapter = new MateriGridAdapter(AllMateriActivity.this, materiList);
        materiRecyclerView.setAdapter(materiGridAdapter);
        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadMateriFailed(String message) {
        Toast.makeText(AllMateriActivity.this, ""+message, Toast.LENGTH_SHORT).show();
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
