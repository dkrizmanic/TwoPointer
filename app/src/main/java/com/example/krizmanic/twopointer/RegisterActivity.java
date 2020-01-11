package com.example.krizmanic.twopointer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.krizmanictwopointer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.editText_usernameRegistration)
    EditText et_username;
    @BindView(R.id.editText_emailRegistration)
    EditText et_email;
    @BindView(R.id.editText_passwordRegistration)
    EditText et_password;
    @BindView(R.id.progressBar_registration)
    ProgressBar progressBar;
    @BindView(R.id.btn_registration)
    Button btn_registration;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("users");

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }

    @OnClick(R.id.btn_registration)
    public void registerUser() {

        final String username = et_username.getText().toString();
        final String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            btn_registration.setVisibility(View.INVISIBLE);
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, task.getException().toString(),
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                btn_registration.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration succesfull", Toast.LENGTH_SHORT).show();
                                user = auth.getCurrentUser();
                                String userID = user.getUid();
                                reference.child(userID).child("username").setValue(username);
                                reference.child(userID).child("userID").setValue(userID);
                                reference.child(userID).child("email").setValue(email);
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                progressBar.setVisibility(View.INVISIBLE);
                                btn_registration.setVisibility(View.VISIBLE);
                                finish();
                            }
                        }
                    });


        }
    }

    @OnClick(R.id.textview_goToLogin)
    public void goToLogin(){

        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

    }
}
