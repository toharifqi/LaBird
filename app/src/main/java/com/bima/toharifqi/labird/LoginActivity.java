package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {
    AlertDialog dialog;

    TextView logoName, sloganName;
    ImageView logoImage;
    TextInputLayout username, password;
    Button masukButton, buttonTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //to display loading dialog
        dialog = new SpotsDialog.Builder().setContext(LoginActivity.this).build();
        dialog.setMessage("Mohon tunggu...");

        logoImage = findViewById(R.id.logoImage);
        logoName = findViewById(R.id.logo_name);
        sloganName = findViewById(R.id.slogan_name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        masukButton = findViewById(R.id.masukButton);
        buttonTwo = findViewById(R.id.buttonTwo);


    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Username tidak boleh kosong");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Password tidak boleh kosong");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }




    public void login(View view){
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser() {
        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("userName").equalTo(userEnteredUsername);
        dialog.show();
        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userEnteredPassword)) {
                        username.setError(null);
                        username.setErrorEnabled(false);
                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("nama").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("userName").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("waNumber").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(), BoardingActivity.class);
                        GlobalValueUser.nama = nameFromDB;
                        GlobalValueUser.email = emailFromDB;
                        GlobalValueUser.userName = usernameFromDB;
                        GlobalValueUser.waNumber = phoneNoFromDB;
                        dialog.dismiss();
                        startActivity(intent);
                        finish();
                    } else {
                        dialog.dismiss();
                        password.setError("Password salah");
                        password.requestFocus();
                    }
                } else {
                    dialog.dismiss();
                    username.setError("Username tidak ditemukan");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void toDaftar(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View, String>(logoImage, "logo_image");
        pairs[1] = new Pair<View, String>(logoName, "logo_name");
        pairs[2] = new Pair<View, String>(sloganName, "slogan_trans");
        pairs[3] = new Pair<View, String>(username, "username_trans");
        pairs[4] = new Pair<View, String>(password, "password_trans");
        pairs[5] = new Pair<View, String>(masukButton, "button_trans");
        pairs[6] = new Pair<View, String>(buttonTwo, "button2_trans");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
            startActivity(intent, options.toBundle());
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}
