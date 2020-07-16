package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.bima.toharifqi.labird.model.CourseModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

import java.io.IOException;

import dmax.dialog.SpotsDialog;

public class CourseActivity2 extends AppCompatActivity {
    private TextView courseName, courseDescription;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button browseaBtn, uploadBtn;
    ImageView photoImage;

    Uri imageUri;
    int Image_Request_Code = 7;

    StorageReference photoStorageReference;
    DatabaseReference photoDatabaseReference;
    AlertDialog dialog;
    CourseModel courseModelExtra;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course2);


        // Action Bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        //get data from previous activity
        courseModelExtra = getIntent().getParcelableExtra("courseModelExtra");

        //tipycal views
        courseName = findViewById(R.id.courseName2);
        courseDescription = findViewById(R.id.courseDescription2);

        //set tipycal data
        courseName.setText("Langkah Belajar: "+courseModelExtra.getCourseTitle());
        courseDescription.setText(R.string.langkah_belajar2);

        //views for uploading photos
        browseaBtn = findViewById(R.id.browseBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        photoImage = findViewById(R.id.imagePhoto);
        radioGroup = findViewById(R.id.radioGroupfoto);

        ZoomInImageViewAttacher imageAttacher = new ZoomInImageViewAttacher();
        imageAttacher.attachImageView(photoImage);
        imageAttacher.setZoomable(true);

        //take photos
        photoStorageReference = FirebaseStorage.getInstance().getReference("fotoSketsa");
        photoDatabaseReference = FirebaseDatabase.getInstance().getReference("userAnswer").child(GlobalValueUser.userName).child("fotoSketsa");
        dialog = new SpotsDialog.Builder().setContext(CourseActivity2.this).build();

        browseaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                photoImage.setImageBitmap(bitmap);
                photoImage.setVisibility(View.VISIBLE);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void uploadImage() {
        if (imageUri != null) {

            dialog.setMessage("Sedang mengupload...");
            dialog.show();
            StorageReference storageReference2 = photoStorageReference.child(System.currentTimeMillis() + "." + GetFileExtension(imageUri));
            storageReference2.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            radioButton = findViewById(selectedId);
                            String TempImageName = courseModelExtra.getCourseTitle()+"_"+radioButton.getText();
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Foto berhasil diupload!", Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests")
                            uploadInfo imageUploadInfo = new uploadInfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());
                            String ImageUploadId = photoDatabaseReference.push().getKey();
                            photoDatabaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    });
        }
        else {

            Toast.makeText(CourseActivity2.this, "Tolong pilih foto terlebih dahulu!", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void toEvaluation(View view){
        Intent intent = new Intent(CourseActivity2.this, EvaluationActivity.class);
        intent.putExtra("courseModelExtra", courseModelExtra);
        startActivity(intent);
    }
}
