package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bima.toharifqi.labird.helper.GlobalValue;
import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.bima.toharifqi.labird.model.BirdUploadModel;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddBirdActivity2 extends AppCompatActivity {
    private Button openFolderBtn, openCameraBtn, uploadBtn;
    private DatabaseReference mDatabaseReference;
    private StorageReference storageReference;
    private FirebaseAuth fAuth;
    private ImageView birdPic;
    private Uri birdPicUri;
    public ProgressDialog dialog;
    private String dateTime;
    private String currentPhotoPath;
    private String expertName, expertUid;
    private TextView desclaimerTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bird2);

        // Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SimpleDateFormat sdf = new SimpleDateFormat("E dd M yyyy - HH:mm:ss");
        dateTime = sdf.format(new Date()) + " WIB";

        expertName = getIntent().getExtras().getString("expertExtraName");
        expertUid = getIntent().getExtras().getString("expertExtraUid");

        openFolderBtn = findViewById(R.id.btn_openfolder);
        openCameraBtn = findViewById(R.id.btn_opencamera);
        uploadBtn = findViewById(R.id.btn_uploadpic);
        birdPic = findViewById(R.id.upload_image);
        desclaimerTv = findViewById(R.id.desclaimer_upload);
        desclaimerTv.setText("Foto yang anda upload akan dikirim ke " + expertName + " untuk dianalisis, Tekan tombol upload foto di bawah jika sudah yakin!");
        uploadBtn.setText("Upload foto ke " + expertName);
        openFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        openCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "bird-" + GlobalValueUser.uId + dateTime;
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

                    currentPhotoPath = imageFile.getAbsolutePath();

                    birdPicUri = FileProvider.getUriForFile(AddBirdActivity2.this, "com.bima.toharifqi.labird.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, birdPicUri);
                    startActivityForResult(intent, GlobalValue.CAMERA_REQUEST_CODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPhoto();
            }
        });
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GlobalValue.PHOTO_REQUEST_CODE);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathname,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathname);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathname, options);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== GlobalValue.PHOTO_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            birdPicUri=data.getData();
            birdPic.setImageURI(birdPicUri);
        }
        if (requestCode == GlobalValue.CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            //Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

            birdPic.setImageBitmap(decodeSampledBitmapFromResource(currentPhotoPath, 100, 100));

        }
    }

    public void showDialogNoInput(){
        dialog.dismiss();

        new MaterialStyledDialog.Builder(this).setTitle("Oops...")
                .setDescription("Pastikan anda telah mengambil foto dari folder atau dari kamera ponsel anda.")
                .setPositiveText("OK")
                .setHeaderDrawable(R.drawable.unavailable)
                .setCancelable(true)
                .setScrollable(true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                }).show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    private void submitPhoto() {
        dialog = ProgressDialog.show(AddBirdActivity2.this, "",
                "Menambahkan foto burung baru. Mohon tunggu ...", true);

        boolean isNullPhotoUrl = birdPicUri == null;
        if (isNullPhotoUrl){
            showDialogNoInput();
        }else {
            final String timeStampString = dateTime.replace(" ", "");
            final String senderId = GlobalValueUser.uId;

            storageReference = FirebaseStorage.getInstance().getReference().child("uploadedBird/" + senderId).child(dateTime + "/");
            StorageTask storageTask = storageReference.putFile(birdPicUri);
            Task<Uri> uriTask = storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();

                        BirdUploadModel birdUploadModel = new BirdUploadModel(senderId, dateTime, downloadUri.toString());
                        Map<String, Object> postValues = birdUploadModel.addBird();
                        Map<String, Object> childUpdates = new HashMap<>();

                        childUpdates.put("/birdUploads/" + expertUid + "/" + senderId+timeStampString, postValues);
                        mDatabaseReference.updateChildren(childUpdates);

                        Toast.makeText(AddBirdActivity2.this, "Photo telah ter upload!", Toast.LENGTH_SHORT).show();

                    }
                    dialog.dismiss();
                    showPoUpDialog();
                }
            });
        }


    }

    private void showPoUpDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Foto berhasil terupload!");
        alertDialogBuilder
                .setMessage("Klik Ok untuk kembali ke beranda..")
                .setIcon(R.drawable.icon)
                .setCancelable(false)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(new Intent(AddBirdActivity2.this, HomeActivity.class));
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}