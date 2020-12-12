package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.adapter.CourseSnapAdapter;
import com.bima.toharifqi.labird.adapter.MateriSnapAdapter;
import com.bima.toharifqi.labird.adapter.QuizSnapAdapter;
import com.bima.toharifqi.labird.adapter.SpesiesSnapAdapter;
import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneCourse;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneMateri;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneQuiz;
import com.bima.toharifqi.labird.listener.IFirebaseLoadDoneSpesies;
import com.bima.toharifqi.labird.model.CourseModel;
import com.bima.toharifqi.labird.model.MateriModel;
import com.bima.toharifqi.labird.model.QuizModel;
import com.bima.toharifqi.labird.model.SpesiesModel;
import com.bumptech.glide.Glide;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IFirebaseLoadDoneMateri, IFirebaseLoadDoneSpesies, IFirebaseLoadDoneCourse, IFirebaseLoadDoneQuiz {

    private Toolbar toolbar;
    private String nameFromDB, usernameFromDB, phoneNoFromDB, emailFromDB;
    private ImageView menuIcon, dashboardBg;
    private CoordinatorLayout contentView;
    TextView poinText;
    FloatingActionButton fab;
    RecyclerView recyclerViewMateri, recyclerViewSpesies, recyclerViewCourse, recyclerViewQuiz;
    SnapHelper snapHelper;
    private LinearLayout shimmerMateri, shimmerSpesies, shimmerCourse, shimmerQuiz;

    //Drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //constant
    static final float END_SCALE =  0.7f;

    //loading data from firebase
    DatabaseReference dataPoin;
    Query queryMateri, querySpesies, queryCourse, queryQuiz;
    IFirebaseLoadDoneMateri iFirebaseLoadDoneMateri;
    IFirebaseLoadDoneSpesies iFirebaseLoadDoneSpesies;
    IFirebaseLoadDoneCourse iFirebaseLoadDoneCourse;
    IFirebaseLoadDoneQuiz iFirebaseLoadDoneQuiz;

    //adapters
    private MateriSnapAdapter materiAdapter;
    private SpesiesSnapAdapter spesiesAdapter;
    private CourseSnapAdapter courseAdapter;
    private QuizSnapAdapter quizAdapter;

    //value listeners
    ValueEventListener valueEventListenerMateri = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<MateriModel> materiList = new ArrayList<>();
            for (DataSnapshot materiSnapshot:dataSnapshot.getChildren())
                materiList.add(materiSnapshot.getValue(MateriModel.class));
            iFirebaseLoadDoneMateri.onFirebaseLoadSuccessMateri(materiList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDoneMateri.onFirebaseLoadFailedMateri(databaseError.getMessage());
        }
    };

    ValueEventListener valueEventListenerSpesies = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<SpesiesModel> spesiesList = new ArrayList<>();
            for (DataSnapshot spesiesSnapshot:dataSnapshot.getChildren()){
                spesiesList.add(spesiesSnapshot.getValue(SpesiesModel.class));
            }
            iFirebaseLoadDoneSpesies.onFirebaseLoadSuccessSpesies(spesiesList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDoneSpesies.onFirebaseLoadFailedSpesies(databaseError.getMessage());
        }
    };

    ValueEventListener valueEventListenerCourse = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List <CourseModel> courseList = new ArrayList<>();
            for (DataSnapshot courseSnapshot:dataSnapshot.getChildren())
                courseList.add(courseSnapshot.getValue(CourseModel.class));
            iFirebaseLoadDoneCourse.onFirebaseLoadSuccessCourse(courseList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDoneCourse.onFirebaseLoadFailedCourse(databaseError.getMessage());
        }
    };

    ValueEventListener valueEventListenerQuiz = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<QuizModel> quizList = new ArrayList<>();
            for (DataSnapshot quizSnapshot:dataSnapshot.getChildren())
                quizList.add(quizSnapshot.getValue(QuizModel.class));
            iFirebaseLoadDoneQuiz.onFirebaseLoadSuccessQuiz(quizList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDoneQuiz.onFirebaseLoadFailedQuiz(databaseError.getMessage());
        }
    };


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
        fab = findViewById(R.id.fab);
        recyclerViewMateri = findViewById(R.id.recyclerview_materi);
        recyclerViewSpesies = findViewById(R.id.recyclerview_spesies);
        recyclerViewCourse = findViewById(R.id.recyclerview_course);
        recyclerViewQuiz = findViewById(R.id.recyclerview_quiz);
        shimmerMateri = findViewById(R.id.shimmer_materi);
        shimmerSpesies = findViewById(R.id.shimmer_spesies);
        shimmerCourse = findViewById(R.id.shimmer_course);
        shimmerQuiz = findViewById(R.id.shimmer_quiz);

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
                String poinValue = String.valueOf(dataSnapshot.child(GlobalValueUser.uId).child("poin").getValue());
                String levelString = String.valueOf(dataSnapshot.child(GlobalValueUser.uId).child("level").getValue());
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

        //loading data from firebase
        queryMateri = FirebaseDatabase.getInstance().getReference("materi").orderByKey();
        querySpesies = FirebaseDatabase.getInstance().getReference("spesies").orderByKey();
        queryCourse = FirebaseDatabase.getInstance().getReference("course");
        queryQuiz = FirebaseDatabase.getInstance().getReference("quiz").orderByChild("order");
        iFirebaseLoadDoneMateri = this;
        iFirebaseLoadDoneSpesies = this;
        iFirebaseLoadDoneCourse = this;
        iFirebaseLoadDoneQuiz = this;
        loadMateri();
        loadSpesies();
        loadCourse();
        loadQuiz();

        snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(recyclerViewMateri);
        snapHelper.attachToRecyclerView(recyclerViewSpesies);
        snapHelper.attachToRecyclerView(recyclerViewCourse);
        snapHelper.attachToRecyclerView(recyclerViewQuiz);
        recyclerViewMateri.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSpesies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCourse.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewQuiz.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewMateri.setHasFixedSize(true);
        recyclerViewSpesies.setHasFixedSize(true);
        recyclerViewCourse.setHasFixedSize(true);
        recyclerViewQuiz.setHasFixedSize(true);

    }

    private void loadQuiz() {
        queryQuiz.addValueEventListener(valueEventListenerQuiz);
    }

    private void loadCourse() {
        queryCourse.addValueEventListener(valueEventListenerCourse);
    }

    private void loadSpesies() {
        querySpesies.addValueEventListener(valueEventListenerSpesies);
    }

    private void loadMateri() {
        queryMateri.addValueEventListener(valueEventListenerMateri);
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
                intent.putExtra("poinText", poin);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
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

    public void toAddBird(View view){
        startActivity(new Intent(HomeActivity.this, AddBirdActivity.class));
    }

    @Override
    public void onDestroy() {
        queryMateri.removeEventListener(valueEventListenerMateri);
        querySpesies.removeEventListener(valueEventListenerSpesies);
        queryCourse.removeEventListener(valueEventListenerCourse);
        queryQuiz.removeEventListener(valueEventListenerQuiz);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        queryMateri.removeEventListener(valueEventListenerMateri);
        querySpesies.removeEventListener(valueEventListenerSpesies);
        queryCourse.removeEventListener(valueEventListenerCourse);
        queryQuiz.removeEventListener(valueEventListenerQuiz);
        super.onStop();
    }

    @Override
    public void onFirebaseLoadSuccessMateri(ArrayList<MateriModel> materiList) {
        materiAdapter = new MateriSnapAdapter(materiList, HomeActivity.this);
        recyclerViewMateri.setAdapter(materiAdapter);
        shimmerMateri.setVisibility(View.GONE);
    }

    @Override
    public void onFirebaseLoadFailedMateri(String message) {
        Toast.makeText(HomeActivity.this, ""+message, Toast.LENGTH_SHORT).show();
        shimmerMateri.setVisibility(View.GONE);
    }

    @Override
    public void onFirebaseLoadSuccessSpesies(List<SpesiesModel> spesiesList) {
        spesiesAdapter = new SpesiesSnapAdapter(spesiesList, HomeActivity.this);
        recyclerViewSpesies.setAdapter(spesiesAdapter);
        shimmerSpesies.setVisibility(View.GONE);
    }

    @Override
    public void onFirebaseLoadFailedSpesies(String message) {
        Toast.makeText(HomeActivity.this, ""+message, Toast.LENGTH_SHORT).show();
        shimmerSpesies.setVisibility(View.GONE);
    }

    @Override
    public void onFirebaseLoadSuccessCourse(List<CourseModel> courseList) {
        courseAdapter = new CourseSnapAdapter(courseList, HomeActivity.this);
        recyclerViewCourse.setAdapter(courseAdapter);
        shimmerCourse.setVisibility(View.GONE);
    }

    @Override
    public void onFirebaseLoadFailedCourse(String message) {
        Toast.makeText(HomeActivity.this, ""+message, Toast.LENGTH_SHORT).show();
        shimmerCourse.setVisibility(View.GONE);
    }

    @Override
    public void onFirebaseLoadSuccessQuiz(List<QuizModel> quizList) {
        quizAdapter = new QuizSnapAdapter(quizList, HomeActivity.this);
        recyclerViewQuiz.setAdapter(quizAdapter);
        shimmerQuiz.setVisibility(View.GONE);
    }

    @Override
    public void onFirebaseLoadFailedQuiz(String message) {
        Toast.makeText(HomeActivity.this, ""+message, Toast.LENGTH_SHORT).show();
        shimmerQuiz.setVisibility(View.GONE);
    }
}
