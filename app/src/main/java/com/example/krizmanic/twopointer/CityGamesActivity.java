package com.example.krizmanic.twopointer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

public class CityGamesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_CityGames)
    Toolbar toolbar;
    @BindView(R.id.textView_helloUserCityGames)
    TextView tv_helloUser;
    @BindView(R.id.recycler_cityGames)
    RecyclerView recyclerView_cityGames;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference gameReference, userReference;

    GameAdapter adapter;
    List<Game> gameList;
    Game game;

    Intent intent;
    Bundle extra;

    GameClickListener listener = new GameClickListener() {
        @Override
        public void onGameClick(Game game) {
            openGame(game);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_games);
        ButterKnife.bind(this);

        gameList = new ArrayList<>();
        adapter = new GameAdapter(listener, gameList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_cityGames.setLayoutManager(linearLayoutManager);
        recyclerView_cityGames.setItemAnimator(new DefaultItemAnimator());
        recyclerView_cityGames.setAdapter(adapter);

        intent = getIntent();
        extra = intent.getExtras();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        gameReference = FirebaseDatabase.getInstance().getReference().child("cities").child(extra.getString(Constants.CITY_NAME));
        gameReference.keepSynced(true);
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        userReference.keepSynced(true);

        setToolbar();
        fetchData();
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Query query = userReference.child("username");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tv_helloUser.setText("Hello "+ dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchData(){
        Query query = gameReference;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        game = new Game();
                        game.setDate(ds.getValue(Game.class).getDate());
                        game.setGameName(ds.getValue(Game.class).getGameName());
                        game.setGameID(ds.getValue(Game.class).getGameID());
                        gameList.add(game);
                    }
                    adapter = new GameAdapter(listener,gameList);
                    recyclerView_cityGames.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void openGame(Game game) {

        Intent intent = new Intent(CityGamesActivity.this, GameDetailsActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(Constants.GAME_ID, game.getGameID());
        bundle.putString(Constants.CITY_NAME, extra.getString(Constants.CITY_NAME));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.imageView_logoutCityGames)
    public void logoutUser(){
        auth.signOut();
        startActivity(new Intent(CityGamesActivity.this, RegisterActivity.class));
        finish();
    }
}
