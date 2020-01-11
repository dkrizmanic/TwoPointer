package com.example.krizmanic.twopointer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krizmanictwopointer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class NewGameActivity extends AppCompatActivity {

    @BindView(R.id.editText_gameName)
    EditText et_gameName;
    @BindView(R.id.editText_cityName)
    EditText et_cityName;
    @BindView(R.id.editText_address)
    EditText et_address;
    @BindView(R.id.editText_date)
    EditText et_date;
    @BindView(R.id.editText_time)
    EditText et_time;
    @BindView(R.id.toolbar_newGame)
    Toolbar toolbar;
    @BindView(R.id.textView_helloUserNewGame)
    TextView tv_helloUser;



    DatabaseReference reference, userReference;
    FirebaseAuth auth;
    FirebaseUser user;

    public static final String data = "abcdefghijklmnopqrstuvwxyz0123456789";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("cities");
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        setToolbar();

    }


    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Query query = userReference.child("username");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tv_helloUser.setText("Hello " + dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @OnClick(R.id.btn_Add)
    public void addNewGame(){

        String gameID = randomIDGenerator();
        String gameName = et_gameName.getText().toString();
        String cityName = et_cityName.getText().toString();
        String address = et_address.getText().toString();
        String date = et_date.getText().toString();
        String time = et_time.getText().toString();

        DatabaseReference newEntry = reference.child(cityName).child(gameID);

        if(gameName.isEmpty()||cityName.isEmpty()||address.isEmpty()||date.isEmpty()||time.isEmpty()){
            Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show();
        }else {
            newEntry.child("gameID").setValue(gameID);
            newEntry.child("cityName").setValue(cityName);
            newEntry.child("address").setValue(address);
            newEntry.child("date").setValue(date);
            newEntry.child("time").setValue(time);
            newEntry.child("gameName").setValue(gameName);
            newEntry.child("numOfPlayers").setValue("0");

            startActivity(new Intent(NewGameActivity.this, MainActivity.class));
            finish();
        }


    }

    @OnClick(R.id.imageView_logoutNewGame)
    public void logoutUser(){
        auth.signOut();
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    public String randomIDGenerator() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            sb.append(data.charAt(random.nextInt(data.length())));
        }

        return sb.toString();

    }
}
