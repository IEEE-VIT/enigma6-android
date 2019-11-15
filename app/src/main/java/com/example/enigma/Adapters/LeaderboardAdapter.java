package com.example.enigma.Adapters;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.enigma.Models.Leaderboard;
import com.example.enigma.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>{

    private ArrayList<Leaderboard> leaderboardList;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private int count = 0;

    public LeaderboardAdapter(ArrayList<Leaderboard> leaderboardList) {
        this.leaderboardList = leaderboardList;
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
        holder.rank.setText(leaderboard.getRank());
        holder.score.setText(leaderboard.getScore());
        holder.ques.setText(leaderboard.getQues());
        holder.name.setText(leaderboard.getName());
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
