package com.example.krizmanic.twopointer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.textView_helloUser)
    TextView tv_helloUser;
    @BindView(R.id.recycler_city)
    RecyclerView recyclerView_city;
    @BindView(R.id.progressBar_main)
    ProgressBar progressBar;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference referenceCity, referenceUser;

    CityAdapter adapter;
    List<City> cityList;
    City city;

    CityClickListener listener = new CityClickListener() {
        @Override
        public void onCityClick(City city) {
            openCity(city);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);

        cityList = new ArrayList<>();
        adapter = new CityAdapter(listener, cityList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_city.setLayoutManager(linearLayoutManager);
        recyclerView_city.setItemAnimator(new DefaultItemAnimator());
        recyclerView_city.setAdapter(adapter);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        referenceCity = FirebaseDatabase.getInstance().getReference().child("cities");
        referenceCity.keepSynced(true);
        referenceUser = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        referenceUser.keepSynced(true);

        setToolbar();

        fetchData();
    }

    private void setToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Query query = referenceUser.child("username");
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

        Query query = referenceCity;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        city = new City();
                        city.setCityName(ds.getKey());
                        cityList.add(city);
                    }
                    adapter = new CityAdapter(listener, cityList);
                    recyclerView_city.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        progressBar.setVisibility(View.INVISIBLE);
    }


    private void openCity(City city) {

        Intent intent = new Intent(MainActivity.this, CityGamesActivity.class);
        Bundle extra = new Bundle();

        extra.putString(Constants.CITY_NAME, city.getCityName());
        intent.putExtras(extra);

        startActivity(intent);
    }

    @OnClick(R.id.imageView_logout)
    public void logoutUser() {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        finish();
    }

    @OnClick(R.id.button_addNewGame)
    public void addNewGame() {
        startActivity(new Intent(MainActivity.this, NewGameActivity.class));
    }
}
