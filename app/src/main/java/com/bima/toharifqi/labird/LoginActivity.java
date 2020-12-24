package com.bima.toharifqi.labird;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bima.toharifqi.labird.helper.GlobalValueUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {
    Button registerBtn;
    AlertDialog dialog;
    TextInputLayout email;
    FirebaseAuth fAuth;
    ImageView logoImage;
    TextView logoName;
    Button loginBtn;
    TextInputLayout password;
    TextView sloganName;

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
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.masukButton);
        registerBtn = findViewById(R.id.buttonTwo);

        FirebaseAuth instance = FirebaseAuth.getInstance();
        this.fAuth = instance;
        if (instance.getCurrentUser() != null) {
            setGlobalValueUser(this.fAuth.getCurrentUser().getUid());
        }

    }

    private void setGlobalValueUser(final String uid) {
        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("uId").equalTo(uid);
        this.dialog.show();
        checkUser.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                GlobalValueUser.nama = (String) dataSnapshot.child(uid).child("nama").getValue(String.class);
                GlobalValueUser.email = (String) dataSnapshot.child(uid).child("email").getValue(String.class);
                GlobalValueUser.userName = (String) dataSnapshot.child(uid).child("userName").getValue(String.class);
                GlobalValueUser.waNumber = (String) dataSnapshot.child(uid).child("waNumber").getValue(String.class);
                GlobalValueUser.uId = uid;
                LoginActivity.this.dialog.dismiss();

                if (dataSnapshot.child(uid).child("firstTime").exists()){
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }else {
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, BoardingActivity.class));
                }
                LoginActivity.this.finish();
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private Boolean validateEmail() {
        if (this.email.getEditText().getText().toString().isEmpty()) {
            this.email.setError("Username tidak boleh kosong!");
            return false;
        }
        this.email.setError(null);
        this.email.setErrorEnabled(false);
        return true;
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Password tidak boleh kosong!");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void login(View view){
        if (!validateEmail() | !validatePassword()) {
            return;
        } else {
            String userEnteredEmail = this.email.getEditText().getText().toString().trim();
            String userEnteredPassword = this.password.getEditText().getText().toString().trim();
            dialog.show();
            fAuth.signInWithEmailAndPassword(userEnteredEmail, userEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        LoginActivity.this.onSignInSuccess(task.getResult().getUser());
                    } else {
                        Toast.makeText(LoginActivity.this, "tidak dapat login, pastikan email dan password anda benar!", Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onSignInSuccess(FirebaseUser user) {
        setGlobalValueUser(user.getUid());
        finish();
    }

    public void toDaftar(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        Pair[] pairs = {
                new Pair(this.logoImage, "logo_image"),
                new Pair(this.logoName, "logo_name"),
                new Pair(this.sloganName, "slogan_trans"),
                new Pair(this.email, "email_trans"),
                new Pair(this.password, "password_trans"),
                new Pair(this.loginBtn, "button_trans"),
                new Pair(this.registerBtn, "button2_trans")};
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, pairs).toBundle());
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}
