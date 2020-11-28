package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView realNameTv, userNameTv, poinTv, phoneTv, emailTv;
    CircleImageView profileImage;
    String poin;
    CoordinatorLayout contentView;

    //Drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //constant
    static final float END_SCALE =  0.7f;
    int TAKE_IMAGE_CODE = 10001;
    public static final String TAG = "ProfileActivity";

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        contentView = findViewById(R.id.contentView);

        //set the views
        realNameTv = findViewById(R.id.realname_textview);

        userNameTv = findViewById(R.id.username_textview);
        poinTv = findViewById(R.id.poin_textview);
        phoneTv = findViewById(R.id.phone_textview);
        emailTv = findViewById(R.id.email_textview);
        profileImage = findViewById(R.id.profil_imageview);

        //get poin value from homeActivity
        Bundle extras = getIntent().getExtras();
        poin = extras.getString("poinText");
        poinTv.setText(poin);

        userRef = FirebaseDatabase.getInstance().getReference("users").child(GlobalValueUser.uId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                realNameTv.setText(String.valueOf(dataSnapshot.child("nama").getValue()));
                userNameTv.setText(String.valueOf(dataSnapshot.child("userName").getValue()));
                phoneTv.setText(String.valueOf(dataSnapshot.child("waNumber").getValue()));
                emailTv.setText(String.valueOf(dataSnapshot.child("email").getValue()));
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
        navigationView.setCheckedItem(R.id.nav_profile);

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
                Intent intent = new Intent(ProfileActivity.this, LeaderBoardActivity.class);
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

    @Override
    public boolean onSupportNavigateUp() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

}
