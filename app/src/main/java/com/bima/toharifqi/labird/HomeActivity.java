package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    String nameFromDB, usernameFromDB, phoneNoFromDB, emailFromDB;
    ImageView menuIcon, dashboardBg;
    CoordinatorLayout contentView;
    TextView poinText;

    //Drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //constant
    static final float END_SCALE =  0.7f;

    DatabaseReference dataPoin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("LaBird");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);

        collapsingToolbarLayout.setTitleEnabled(false);

        //calendar stuffs
        TextView txtDate = findViewById(R.id.dateText);
        TextView txtGreeting = findViewById(R.id.greetingText);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time =>" + c);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
        tz.getDisplayName(false, TimeZone.SHORT, Locale.ENGLISH);
        df.setTimeZone(tz);
        String formattedDate = df.format(c);


        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("k");
        int greeting = Integer.parseInt(String.valueOf(dateFormat.format(c)));

        if (greeting > 0 && greeting <= 11) {
            txtGreeting.setText("Selamat Pagi ");
        } else if (greeting > 11 && greeting <= 15) {
            txtGreeting.setText("Selamat Siang ");
        } else if (greeting > 15 && greeting <= 19) {
            txtGreeting.setText("Selamat Sore ");
        } else if (greeting > 19 && greeting <= 24) {
            txtGreeting.setText("Selamat Malam ");
        } else {
            txtGreeting.setText("gagal_memuat_data");
        }

        txtDate.setText(formattedDate);


        //set user identity
        nameFromDB = GlobalValueUser.nama;
        usernameFromDB = GlobalValueUser.userName;
        phoneNoFromDB = GlobalValueUser.waNumber;
        emailFromDB = GlobalValueUser.email;


        TextView userRealName = findViewById(R.id.userName);
        TextView userEmail = findViewById(R.id.userEmail);
        final ProgressBar levelProgressBar = findViewById(R.id.progress_bar);
        final TextView progressText = findViewById(R.id.percentText);
        poinText = findViewById(R.id.pointText);
        final TextView levelText = findViewById(R.id.levelText);
        final TextView targetPoinText = findViewById(R.id.target_poin);

        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.contentView);
        dashboardBg = findViewById(R.id.dashboardBg);
        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/labird.appspot.com/o/dashboard_bg.png?alt=media&token=51c31aa0-3bd1-40f3-9ca6-fbcad4d823f3").into(dashboardBg);

        userRealName.setText(nameFromDB);
        userEmail.setText(emailFromDB);

        //get point data from firebase
        dataPoin = FirebaseDatabase.getInstance().getReference("leaderBoard");
        dataPoin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String poinValue = String.valueOf(dataSnapshot.child(GlobalValueUser.userName).child("poin").getValue());
                String levelString = String.valueOf(dataSnapshot.child(GlobalValueUser.userName).child("level").getValue());
                int poinValuePlus = Integer.valueOf(poinValue)*(-1);
                int levelInteger = Integer.valueOf(levelString);

                //make poin level here
                int targetPoin = 20+(10*(levelInteger*levelInteger));
                float progressValue = ((float) poinValuePlus/targetPoin)*100;

                levelProgressBar.setProgress((int) progressValue);
                progressText.setText(String.format("%.0f",progressValue)+"%");
                poinText.setText(poinValuePlus+" pts");
                levelText.setText("level "+levelString);
                targetPoinText.setText("Target: "+targetPoin+" poin");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //drawer menu stuff
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);



        navigationDrawer();

    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

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
                break;
            case R.id.nav_leaderboard:
                Intent intentL = new Intent(HomeActivity.this, LeaderBoardActivity.class);
                String poinL = poinText.getText().toString();
                intentL.putExtra("poinText", poinL);
                startActivity(intentL);
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                String poin = poinText.getText().toString();
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

    public void toAllSpecies(View view){
        startActivity(new Intent(HomeActivity.this, AllSpesiesActivity.class));
    }

    public void toAllMateri(View view){
        startActivity(new Intent(HomeActivity.this, AllMateriActivity.class));
    }

    public void toAllQuiz(View view){
        startActivity(new Intent(HomeActivity.this, AllQuizActivity.class));
    }

    public void toAllCourses(View view){
        startActivity(new Intent(HomeActivity.this, AllCourseActivity.class));
    }

    public void toDailyEvaluation(View view){
        startActivity(new Intent(HomeActivity.this, DailyEvaluationActivity.class));
    }

}
