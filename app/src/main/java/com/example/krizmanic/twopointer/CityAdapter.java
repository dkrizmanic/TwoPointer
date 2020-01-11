package com.example.krizmanic.twopointer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.krizmanictwopointer.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    CityClickListener listener;
    List<City> cityList;

    public CityAdapter() {
        cityList = new ArrayList<>();
    }

    public CityAdapter(CityClickListener listener, List<City> cityList) {
        this.listener = listener;
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new CityViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }


    public class CityViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_cityName)
        TextView tv_cityName;

        public CityViewHolder(@NonNull View itemView, CityClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(City city) {
            tv_cityName.setText(city.getCityName());
        }

        @OnClick
        public void onCityClick() {
            listener.onCityClick(cityList.get(getAdapterPosition()));
        }
    }


}
