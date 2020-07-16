package com.bima.toharifqi.labird;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

public class ZoomPicActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_pic);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");

        // Action Bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        ImageView imageView = findViewById(R.id.profil_imageview);

        Glide.with(this).load(imageUrl).into(imageView);

        ZoomInImageViewAttacher imageAttacher = new ZoomInImageViewAttacher();
        imageAttacher.attachImageView(imageView);
        imageAttacher.setZoomable(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
