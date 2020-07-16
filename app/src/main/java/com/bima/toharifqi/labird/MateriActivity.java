package com.bima.toharifqi.labird;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bima.toharifqi.labird.model.MateriModel;
import com.bumptech.glide.Glide;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

public class MateriActivity extends AppCompatActivity {
    Toolbar toolbar;

    TextView materiTitle, materiAuthor, materiDesc, materiContent;
    ImageView materiImage, imageContent;
    MateriModel materiModelExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        // Action Bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        //Initiate Views
        materiAuthor = findViewById(R.id.materiAuthor);
        materiContent = findViewById(R.id.materiContent);
        materiDesc = findViewById(R.id.materiDesc);
        materiTitle = findViewById(R.id.materiTitle);
        materiImage = findViewById(R.id.materiImage);
        imageContent = findViewById(R.id.imageContent);

        //get extras
        materiModelExtra = getIntent().getParcelableExtra("materiModelExtra");

        //set content from firebase
        materiAuthor.setText(materiModelExtra.getMateriAuthor());
        materiContent.setText(Html.fromHtml(materiModelExtra.getMateriContent()));
        materiDesc.setText(materiModelExtra.getMateriDesc());
        materiTitle.setText(materiModelExtra.getMateriTitle());
        Glide.with(MateriActivity.this).load(materiModelExtra.getMateriImage()).into(materiImage);

        //check if materi has image content
        if (materiModelExtra.getMateriContentImage().equals("null")){
            imageContent.setVisibility(View.GONE);
        }else {
            Glide.with(MateriActivity.this).load(materiModelExtra.getMateriContentImage()).into(imageContent);

            ZoomInImageViewAttacher imageAttacher = new ZoomInImageViewAttacher();
            imageAttacher.attachImageView(imageContent);
            imageAttacher.setZoomable(true);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
