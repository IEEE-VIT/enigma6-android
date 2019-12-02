package com.ieeevit.enigma_android.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ieeevit.enigma_android.Activities.WorkingActivity;
import com.ieeevit.enigma_android.Adapters.LeaderboardAdapter;
import com.ieeevit.enigma_android.BuildConfig;
import com.ieeevit.enigma_android.Interfaces.FetchLeaderBoard;
import com.ieeevit.enigma_android.Models.CurPlayer;
import com.ieeevit.enigma_android.Models.FetchingLeaderboard;
import com.ieeevit.enigma_android.Models.Leaderboard;
import com.ieeevit.enigma_android.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ScrollableLeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ImageView hamburger;
    private ImageView tint;
    private FirebaseAuth auth;
    private Leaderboard l;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Leaderboard> leaderboardList = new ArrayList<>();
    private CurPlayer curPlayer;
    private Integer count;
    private SharedPreferences sharedPreferences;
    private Call<FetchingLeaderboard> call;
    private ShimmerFrameLayout mShimmerViewContainer;
    private Snackbar snackbar;

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

    @Override
    public void onResume() {
        super.onResume();
        WorkingActivity.getOpenBottomSheets().setChecked(2);
    }

    private void initializeViews(View rootView) {
        adapter = new LeaderboardAdapter(leaderboardList, getContext());
        recyclerView = rootView.findViewById(R.id.recycler_leaderboard);
        hamburger = rootView.findViewById(R.id.leaderboard_hamburger);
        layoutManager = new LinearLayoutManager(getContext());
        auth = FirebaseAuth.getInstance();
        count = 0;
        sharedPreferences = getContext().getSharedPreferences("Current", MODE_PRIVATE);
        mShimmerViewContainer = rootView.findViewById(R.id.shimmer_view_container);
        tint = rootView.findViewById(R.id.leaderboard_tint);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call!=null)
            call.cancel();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void prepareLeaderboard() {
            tint.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
            Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            final FetchLeaderBoard leaderBoard = retrofit.create(FetchLeaderBoard.class);
            call = leaderBoard.fetchLeaderBoard(auth.getCurrentUser().getUid());
            call.enqueue(new Callback<FetchingLeaderboard>() {
                @Override
                public void onResponse(Call<FetchingLeaderboard> call, Response<FetchingLeaderboard> response) {
                    count = 0;
                    FetchingLeaderboard body = response.body();
                    CurPlayer[] leaderboard = body.getPayload().getLeaderBoard();
                    l = new Leaderboard("RANK", "NAME", "SOLVED", "SCORE");
                    leaderboardList.add(l);
                    curPlayer = body.getPayload().getCurPlayer();
                    for (CurPlayer player : leaderboard) {
                        String name = player.getName();
                        int level = player.getLevel();
                        int points = (int) (player.getPoints());
                        int rank = player.getRank();
                        if (curPlayer.getName().equals(name))
                            count = 1;
                        l = new Leaderboard(String.valueOf(rank), name, String.valueOf(level - 1), String.valueOf(points));
                        leaderboardList.add(l);
                    }
                    if (count == 0) {
                        CurPlayer player = body.getPayload().getCurPlayer();
                        String name = player.getName();
                        int level = player.getLevel();
                        int points = (int) player.getPoints();
                        int rank = player.getRank();

                        l = new Leaderboard(String.valueOf(rank), name, String.valueOf(level - 1), String.valueOf(points));
                        leaderboardList.add(l);
                    }
                    adapter.notifyDataSetChanged();
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    tint.setVisibility(View.INVISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("CurrentPlayer", curPlayer.getName());
                    editor.apply();
                }

                @Override
                public void onFailure(Call<FetchingLeaderboard> call, Throwable t) {
                    if (call.isCanceled()) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        tint.setVisibility(View.INVISIBLE);
                    } else {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        tint.setVisibility(View.INVISIBLE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }
            });
    }
}
