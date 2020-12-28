package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bima.toharifqi.labird.adapter.SpeciesGridAdapter;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneSpesies;
import com.bima.toharifqi.labird.model.SpesiesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AllSpeciesActivity extends AppCompatActivity implements IFirebaseLoadDoneSpesies {
    private RecyclerView spesiesRecyclerView;
    private SpeciesGridAdapter speciesGridAdapter;

    AlertDialog dialog;

    IFirebaseLoadDoneSpesies iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<SpesiesModel> spesiesList = new ArrayList<>();
            for (DataSnapshot spesiesSnapshot:dataSnapshot.getChildren())
                spesiesList.add(spesiesSnapshot.getValue(SpesiesModel.class));
            iFirebaseLoadDone.onFirebaseLoadSuccessSpesies(spesiesList);
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
        setContentView(R.layout.activity_all_species);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);

        String courseName;
        Intent intent = getIntent();
        if (intent.getStringExtra("courseName")==null){
            courseName = "kosong";
        }else {
            courseName = intent.getStringExtra("courseName");
        }

        switch (courseName){
            case "Sumber Kendedes":
                query = FirebaseDatabase.getInstance().getReference("spesies").orderByChild("akendedes").equalTo(true);
                break;
            case "Sumber Waras":
                query = FirebaseDatabase.getInstance().getReference("spesies").orderByChild("awaras").equalTo(true);
                break;
            case "Sumber Taman":
                query = FirebaseDatabase.getInstance().getReference("spesies").orderByChild("ataman").equalTo(true);
                break;
            case "Sumber Sira":
                query = FirebaseDatabase.getInstance().getReference("spesies").orderByChild("asira").equalTo(true);
                break;
            case "Sumber Jenon":
                query = FirebaseDatabase.getInstance().getReference("spesies").orderByChild("ajenon").equalTo(true);
                break;
            case "Sumber Wendit":
                query = FirebaseDatabase.getInstance().getReference("spesies").orderByChild("awendit").equalTo(true);
                break;
            case "kosong":
                query = FirebaseDatabase.getInstance().getReference("spesies");
                break;
        }
        iFirebaseLoadDone = this;

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(AllSpeciesActivity.this).build();
        dialog.setMessage("Mohon tunggu...");

        loadSpesies();

        spesiesRecyclerView = findViewById(R.id.recyclerview_id);
        spesiesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    }

    private void loadSpesies() {
        dialog.show();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onFirebaseLoadSuccessSpesies(List<SpesiesModel> spesiesList) {
        speciesGridAdapter = new SpeciesGridAdapter(AllSpeciesActivity.this, spesiesList);
        spesiesRecyclerView.setAdapter(speciesGridAdapter);
        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(AllSpeciesActivity.this, ""+message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        query.removeEventListener(valueEventListener);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        query.removeEventListener(valueEventListener);
        super.onStop();
    }
}
