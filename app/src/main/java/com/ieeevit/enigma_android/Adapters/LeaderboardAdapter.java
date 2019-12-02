package com.ieeevit.enigma_android.Adapters;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ieeevit.enigma_android.Models.Leaderboard;
import com.ieeevit.enigma_android.R;

import java.util.ArrayList;


public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>{

    private ArrayList<Leaderboard> leaderboardList;
    private Context context;

    public LeaderboardAdapter(ArrayList<Leaderboard> leaderboardList, Context context) {
        this.leaderboardList = leaderboardList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_leaderboard, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Leaderboard leaderboard = leaderboardList.get(position);

        SharedPreferences sharedPreferences = context.getSharedPreferences("Current", Context.MODE_PRIVATE);
        if(leaderboard.getName().equals(sharedPreferences.getString("CurrentPlayer", null)))
        {
            holder.rank.setTextColor(Color.parseColor("#a422ff"));
            holder.score.setTextColor(Color.parseColor("#a422ff"));
            holder.name.setTextColor(Color.parseColor("#a422ff"));
            holder.ques.setTextColor(Color.parseColor("#a422ff"));
            holder.rank.setText(leaderboard.getRank());
            holder.score.setText(leaderboard.getScore());
            holder.ques.setText(leaderboard.getQues());
            holder.name.setText(leaderboard.getName());
        }
        else if(leaderboard.getRank().equals("RANK"))
        {
            holder.rank.setTextColor(Color.parseColor("#a422ff"));
            holder.score.setTextColor(Color.parseColor("#a422ff"));
            holder.name.setTextColor(Color.parseColor("#a422ff"));
            holder.ques.setTextColor(Color.parseColor("#a422ff"));
            holder.rank.setText(leaderboard.getRank());
            holder.score.setText(leaderboard.getScore());
            holder.ques.setText(leaderboard.getQues());
            holder.name.setText(leaderboard.getName());
        }
        else
        {
            holder.rank.setTextColor(Color.parseColor("#ffffff"));
            holder.score.setTextColor(Color.parseColor("#ffffff"));
            holder.name.setTextColor(Color.parseColor("#ffffff"));
            holder.ques.setTextColor(Color.parseColor("#ffffff"));
            holder.rank.setText(leaderboard.getRank());
            holder.score.setText(leaderboard.getScore());
            holder.ques.setText(leaderboard.getQues());
            holder.name.setText(leaderboard.getName());
        }
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView rank;
        private TextView name;
        private TextView ques;
        private TextView score;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = (TextView) itemView.findViewById(R.id.rank);
            ques = (TextView) itemView.findViewById(R.id.ques);
            name = (TextView) itemView.findViewById(R.id.name);
            score = (TextView) itemView.findViewById(R.id.score);
        }
    }
}
