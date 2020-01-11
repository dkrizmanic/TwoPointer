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

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    GameClickListener listener;
    List<Game> gameList;

    public GameAdapter() {

        gameList = new ArrayList<>();
    }

    public GameAdapter(GameClickListener listener, List<Game> gameList) {
        this.listener = listener;
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item,parent,false);
        return new GameViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {

        Game game = gameList.get(position);
        holder.bind(game);

    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_gameName)
        TextView tv_gameName;
        @BindView(R.id.textView_gameDate)
        TextView tv_gameDate;

        public GameViewHolder(@NonNull View itemView, GameClickListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(Game game) {

            tv_gameName.setText(game.getGameName());
            tv_gameDate.setText(game.getDate());

        }

        @OnClick
        public void onGameClick(){
            listener.onGameClick(gameList.get(getAdapterPosition()));
        }
    }
}
