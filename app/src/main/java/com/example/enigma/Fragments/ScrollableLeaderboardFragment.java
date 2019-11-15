package com.example.enigma.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.enigma.Activities.WorkingActivity;
import com.example.enigma.Adapters.LeaderboardAdapter;
import com.example.enigma.BuildConfig;
import com.example.enigma.Interfaces.FetchLeaderBoard;
import com.example.enigma.Models.CurPlayer;
import com.example.enigma.Models.FetchingLeaderboard;
import com.example.enigma.Models.Leaderboard;
import com.example.enigma.R;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScrollableLeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ImageView hamburger;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Leaderboard l;
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
        progressBar.setVisibility(View.VISIBLE);
        prepareLeaderboard();
        return rootView;
    }

    private void initializeViews(View rootView) {
        adapter = new LeaderboardAdapter(leaderboardList);
        recyclerView = rootView.findViewById(R.id.recycler_leaderboard);
        hamburger = rootView.findViewById(R.id.leaderboard_hamburger);
        layoutManager = new LinearLayoutManager(getContext());
        auth = FirebaseAuth.getInstance();
        progressBar = rootView.findViewById(R.id.scrollable_leaderboard_progress_bar);
    }

        private void prepareLeaderboard() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        final FetchLeaderBoard leaderBoard = retrofit.create(FetchLeaderBoard.class);

        Call<FetchingLeaderboard> call = leaderBoard.fetchLeaderBoard(auth.getCurrentUser().getUid());

        call.enqueue(new Callback<FetchingLeaderboard>() {
            @Override
            public void onResponse(Call<FetchingLeaderboard> call, Response<FetchingLeaderboard> response) {
                FetchingLeaderboard body = response.body();
                CurPlayer[] leaderboard = body.getPayload().getLeaderBoard();
                l = new Leaderboard("RANK", "NAME", "QUES", "SCORE");
                leaderboardList.add(l);



                for (CurPlayer player : leaderboard) {
                    String name = player.getName();
                    int level = player.getLevel();
                    int points = player.getPoints();
                    int rank = player.getRank();

                    l = new Leaderboard(String.valueOf(rank), name, String.valueOf(level - 1), String.valueOf(points));
                    leaderboardList.add(l);

                }

                CurPlayer player = body.getPayload().getCurPlayer();
                String name = player.getName();
                int level = player.getLevel();
                int points = player.getPoints();
                int rank = player.getRank();

                l = new Leaderboard(String.valueOf(rank), name, String.valueOf(level - 1), String.valueOf(points));
                leaderboardList.add(l);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<FetchingLeaderboard> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
