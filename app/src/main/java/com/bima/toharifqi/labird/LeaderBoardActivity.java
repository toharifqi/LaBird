package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.adapter.LeaderBoardAdapter;
import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneBoard;
import com.bima.toharifqi.labird.model.LeaderBoardModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class LeaderBoardActivity extends AppCompatActivity implements IFirebaseLoadDoneBoard , NavigationView.OnNavigationItemSelectedListener  {
    private RecyclerView boardRecyclerView;
    private LeaderBoardAdapter boardAdapter;
    private TextView sumUsers;
    CoordinatorLayout contentView;
    String poin;

    //Drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //constant
    static final float END_SCALE =  0.7f;

    AlertDialog dialog;

    IFirebaseLoadDoneBoard iFirebaseLoadDone;

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            int sumItem = (int) dataSnapshot.getChildrenCount();
            sumUsers.setText("Jumlah Pengguna: " + sumItem);
            List<LeaderBoardModel> boardList = new ArrayList<>();
            for (DataSnapshot boardSnapshot:dataSnapshot.getChildren())
                boardList.add(boardSnapshot.getValue(LeaderBoardModel.class));

            iFirebaseLoadDone.onFirebaseLoadSuccess(boardList);
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
        setContentView(R.layout.activity_leader_board);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        sumUsers = findViewById(R.id.sum_users);

        contentView = findViewById(R.id.contentView);

        //get poin from intent
        Bundle extras = getIntent().getExtras();
        poin = extras.getString("poinText");

        query = FirebaseDatabase.getInstance().getReference("leaderBoard").orderByChild("poin");

        iFirebaseLoadDone = this;

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(LeaderBoardActivity.this).build();
        dialog.setMessage("Mohon tunggu...");

        loadBoard();

        boardRecyclerView = findViewById(R.id.recycler_leaderboard);
        boardRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        //drawer menu stuff
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);



        navigationDrawer();

    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_leaderboard);

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //scale the view
                final float diffScaldedOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaldedOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //translate the view
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff  = contentView.getWidth() * diffScaldedOffset/2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.nav_leaderboard:
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(LeaderBoardActivity.this, ProfileActivity.class);
                intent.putExtra("username", GlobalValueUser.userName);
                intent.putExtra("poinText", poin);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadBoard() {
        dialog.show();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onFirebaseLoadSuccess(List<LeaderBoardModel> boardList) {
        boardAdapter = new LeaderBoardAdapter(LeaderBoardActivity.this, boardList);
        boardRecyclerView.setAdapter(boardAdapter);
        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(LeaderBoardActivity.this, ""+message, Toast.LENGTH_SHORT).show();
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
