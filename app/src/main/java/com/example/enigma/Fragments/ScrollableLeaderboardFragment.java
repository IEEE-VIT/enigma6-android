package com.example.enigma.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigma.Activities.WorkingActivity;
import com.example.enigma.Adapters.LeaderboardAdapter;
import com.example.enigma.Models.Leaderboard;
import com.example.enigma.R;

import java.util.ArrayList;

public class ScrollableLeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ImageView hamburger;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Leaderboard> leaderboardList = new ArrayList<>();

    public ScrollableLeaderboardFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scrollable_leaderboard, container, false);
        initializeViews(rootView);

        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingActivity.getOpenDrawerFragments().OpenDrawer();
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        prepareLeaderboard();
        return rootView;
    }

    private void prepareLeaderboard() {
        Leaderboard leaderboard = new Leaderboard("01", "Vibhor Chinda", "10", "100");
        leaderboardList.add(leaderboard);
        leaderboard = new Leaderboard("01", "Vibhor Chinda", "10", "100");
        leaderboardList.add(leaderboard);
        leaderboard = new Leaderboard("01", "Vibhor Chinda", "10", "100");
        leaderboardList.add(leaderboard);
        leaderboard = new Leaderboard("01", "Vibhor Chinda", "10", "100");
        leaderboardList.add(leaderboard);
        leaderboard = new Leaderboard("01", "Vibhor Chinda", "10", "100");
        leaderboardList.add(leaderboard);
        leaderboard = new Leaderboard("01", "Vibhor Chinda", "10", "100");
        leaderboardList.add(leaderboard);
        leaderboard = new Leaderboard("01", "Vibhor Chinda", "10", "100");
        leaderboardList.add(leaderboard);
        leaderboard = new Leaderboard("01", "Vibhor Chinda", "10", "100");
        leaderboardList.add(leaderboard);
        leaderboard = new Leaderboard("01", "Vibhor Chinda", "10", "100");
        leaderboardList.add(leaderboard);
        leaderboard = new Leaderboard("01", "Vibhor Chinda", "10", "100");
        leaderboardList.add(leaderboard);
    }

    private void initializeViews(View rootView) {
        adapter = new LeaderboardAdapter(leaderboardList);
        recyclerView = rootView.findViewById(R.id.recycler_leaderboard);
        hamburger = rootView.findViewById(R.id.leaderboard_hamburger);
        layoutManager = new LinearLayoutManager(getContext());
    }
}
