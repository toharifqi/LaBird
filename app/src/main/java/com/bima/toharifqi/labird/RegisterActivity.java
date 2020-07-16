package com.bima.toharifqi.labird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bima.toharifqi.labird.model.UserModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout nameInput, usernameInput, emailInput, numberInput, passwordInput;
    DatabaseReference userDb, leaderBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.namaLengkap);
        usernameInput = findViewById(R.id.username);
        emailInput = findViewById(R.id.email);
        numberInput = findViewById(R.id.waNumber);
        passwordInput = findViewById(R.id.password);

        userDb = FirebaseDatabase.getInstance().getReference("users");
        leaderBoard = FirebaseDatabase.getInstance().getReference("leaderBoard");


    }

    private Boolean validateName() {
        String val = nameInput.getEditText().getText().toString();

        if (val.isEmpty()) {
            nameInput.setError("Nama lengkap tidak boleh kosong");
            return false;
        } else {
            nameInput.setError(null);
            nameInput.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = usernameInput.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            usernameInput.setError("Username tidak boleh kosong");
            return false;
        } else if (val.length() >= 15) {
            usernameInput.setError("Username terlalu panjang");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            usernameInput.setError("Whitespace tidak diijinkan");
            return false;
        } else {
            usernameInput.setError(null);
            usernameInput.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = emailInput.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            emailInput.setError("Email tidak boleh kosong");
            return false;
        } else if (!val.matches(emailPattern)) {
            emailInput.setError("Email tidak valid");
            return false;
        } else {
            emailInput.setError(null);
            emailInput.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = numberInput.getEditText().getText().toString();

        if (val.isEmpty()) {
            numberInput.setError("Nomor WA tidak boleh kosong");
            return false;
        } else {
            numberInput.setError(null);
            numberInput.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = passwordInput.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            passwordInput.setError("Password tidak boleh kosong");
            return false;
        } else if (val.length()<=8) {
            passwordInput.setError("Password kurang panjang");
            return false;
        } else {
            passwordInput.setError(null);
            passwordInput.setErrorEnabled(false);
            return true;
        }
    }


    public void register(View view){
        if(!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateUsername()){
            return;
        }

        final String name = nameInput.getEditText().getText().toString();
        final String username = usernameInput.getEditText().getText().toString();
        String email = emailInput.getEditText().getText().toString();
        String waNumber = numberInput.getEditText().getText().toString();
        String password = passwordInput.getEditText().getText().toString();

        final UserModel userModel = new UserModel(email, name, password, username, waNumber);

        final String userEnteredUsername = usernameInput.getEditText().getText().toString().trim();
        Query checkUsername = userDb.orderByChild("userName").equalTo(userEnteredUsername);
        checkUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    usernameInput.setError("Maaf, username sudah pernah digunakan");
                }else {
                    userDb.child(username).setValue(userModel);
                    leaderBoard.child(username).child("name").setValue(name);
                    leaderBoard.child(username).child("level").setValue(1);
                    leaderBoard.child(username).child("poin").setValue(0);
                    leaderBoard.child(username).child("username").setValue(username);
                    Toast.makeText(RegisterActivity.this, "Anda berhasil terdaftar, silahkan login", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void toLogin(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
