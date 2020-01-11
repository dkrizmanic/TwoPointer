package com.example.krizmanic.twopointer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.krizmanictwopointer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_gameDetails)
    Toolbar toolbar;
    @BindView(R.id.textView_helloUserGameDetails)
    TextView tv_helloUser;
    @BindView(R.id.textView_gameNameDetails)
    TextView tv_gameName;
    @BindView(R.id.textView_cityNameGameDetails)
    TextView tv_cityName;
    @BindView(R.id.textView_addressGameDetails)
    TextView tv_address;
    @BindView(R.id.textView_dateGameDetails)
    TextView tv_date;
    @BindView(R.id.textView_timeGameDetails)
    TextView tv_time;
    @BindView(R.id.textView_numberOfPlayers)
    TextView tv_numberOfPlayers;
    @BindView(R.id.btn_Entry)
    Button btn_entry;
    @BindView(R.id.btn_removeEntry)
    Button btn_removeEntry;

    Intent intent;
    Bundle extra;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference gameReference, userReference;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        ButterKnife.bind(this);

        intent = getIntent();
        extra = intent.getExtras();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String cityName = extra.getString(Constants.CITY_NAME);
        String gameID = extra.getString(Constants.GAME_ID);
        gameReference = FirebaseDatabase.getInstance().getReference().child("cities").child(cityName).child(gameID);
        gameReference.keepSynced(true);
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        userReference.keepSynced(true);

        setToolbar();
        fetchData();

    }

    private void setToolbar() {
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

    private void fetchData() {
        Query query = gameReference;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    game = new Game();
                    game.setDate(dataSnapshot.getValue(Game.class).getDate());
                    game.setGameName(dataSnapshot.getValue(Game.class).getGameName());
                    game.setAddress(dataSnapshot.getValue(Game.class).getAddress());
                    game.setTime(dataSnapshot.getValue(Game.class).getTime());
                    game.setNumOfPlayers(dataSnapshot.getValue(Game.class).getNumOfPlayers());
                    setData(game);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setData(Game game) {

        tv_address.setText("Address: " + game.getAddress());
        tv_cityName.setText("City: " + extra.getString(Constants.CITY_NAME));
        tv_gameName.setText(game.getGameName());
        tv_date.setText("Date: " + game.getDate());
        tv_time.setText("Time: " + game.getTime());
        tv_numberOfPlayers.setText("Number of players: " + game.getNumOfPlayers());

        int nOPlayers = Integer.parseInt(game.getNumOfPlayers());
        if (nOPlayers >= 10) {
            btn_entry.setClickable(false);
        }
    }

    @OnClick(R.id.btn_Entry)
    public void entryGame() {
        int numOfPlayers = Integer.parseInt(game.getNumOfPlayers());
        int newNumOfPlayers = numOfPlayers + 1;
        btn_entry.setVisibility(View.INVISIBLE);
        btn_removeEntry.setVisibility(View.VISIBLE);
        DatabaseReference updateReference = gameReference.child("numOfPlayers");
        if (newNumOfPlayers <= 10) {
            updateReference.setValue(String.valueOf(newNumOfPlayers));
            fetchData();
        } else {
            btn_entry.setClickable(false);
        }
    }

    @OnClick(R.id.btn_removeEntry)
    public void removeEntry() {
        int numOfPlayers = Integer.parseInt(game.getNumOfPlayers());
        int newNumOfPlayers = numOfPlayers - 1;
        btn_entry.setVisibility(View.VISIBLE);
        btn_removeEntry.setVisibility(View.INVISIBLE);
        DatabaseReference updateReference = gameReference.child("numOfPlayers");
        if(newNumOfPlayers <=0){
            newNumOfPlayers = 0;

        }
        updateReference.setValue(String.valueOf(newNumOfPlayers));
        fetchData();
    }

    @OnClick(R.id.imageView_logoutGameDetails)
    public void logoutUser(){
        auth.signOut();
        startActivity(new Intent(GameDetailsActivity.this, RegisterActivity.class));
        finish();
    }
}
