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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editText_emailLogin)
    EditText et_login;
    @BindView(R.id.editText_passwordLogin)
    EditText et_password;
    @BindView(R.id.progressBar_login)
    ProgressBar progressBar;
    @BindView(R.id.btn_login)
    Button btn_login;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.btn_login)
    public void loginUser() {

        String email = et_login.getText().toString();
        String password = et_password.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.INVISIBLE);

        if (email.isEmpty() || password.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            btn_login.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                progressBar.setVisibility(View.INVISIBLE);
                                btn_login.setVisibility(View.VISIBLE);
                                finish();
                            }
                        }
                    });
        }

    }
}
