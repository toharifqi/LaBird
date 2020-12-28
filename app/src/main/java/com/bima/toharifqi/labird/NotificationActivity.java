package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.bima.toharifqi.labird.adapter.NotifListAdapter;
import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneNotif;
import com.bima.toharifqi.labird.model.NotifModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class NotificationActivity extends AppCompatActivity implements IFirebaseLoadDoneNotif {
    private RecyclerView notifRecyclerView;
    private NotifListAdapter notifListAdapter;

    AlertDialog dialog;
    IFirebaseLoadDoneNotif iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<NotifModel> notifModelList = new ArrayList<>();
            for (DataSnapshot notifSnapshot : dataSnapshot.getChildren())
                notifModelList.add(notifSnapshot.getValue(NotifModel.class));
            iFirebaseLoadDone.onFirebaseLoadSuccess(notifModelList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
        }
    };

    DatabaseReference notifDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        notifDb = FirebaseDatabase.getInstance().getReference("notification").child(GlobalValueUser.uId);

        iFirebaseLoadDone = this;

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(NotificationActivity.this).build();
        dialog.setMessage("Mohon tunggu...");

        loadNotif();

        notifRecyclerView = findViewById(R.id.recycler_notif);
        notifRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL, false));

    }

    private void loadNotif() {
        dialog.show();
        notifDb.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onFirebaseLoadSuccess(List<NotifModel> notifList) {
        notifListAdapter = new NotifListAdapter(NotificationActivity.this, notifList);
        notifRecyclerView.setAdapter(notifListAdapter);
        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(NotificationActivity.this, ""+message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        notifDb.removeEventListener(valueEventListener);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        notifDb.removeEventListener(valueEventListener);
        super.onStop();
    }
}