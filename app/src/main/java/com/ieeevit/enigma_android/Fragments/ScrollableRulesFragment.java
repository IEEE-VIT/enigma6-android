package com.ieeevit.enigma_android.Fragments;

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
import com.ieeevit.enigma_android.Activities.WorkingActivity;
import com.ieeevit.enigma_android.Adapters.RulesAdapter;
import com.ieeevit.enigma_android.Models.Rules;
import com.ieeevit.enigma_android.R;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

public class ScrollableRulesFragment extends Fragment {

    private ImageView hamburger;
    private ArrayList<Rules> rulesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RulesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AppBarLayout toolbar;

    public ScrollableRulesFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        WorkingActivity.getOpenBottomSheets().setChecked(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_scrollable_rules, container, false);
        initializeViews(rootView);
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingActivity.getOpenDrawerFragments().OpenDrawer();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        prepareRulesData();
        return rootView;
    }

    private void initializeViews(View rootView) {
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new RulesAdapter(rulesList);
        hamburger = rootView.findViewById(R.id.rules_hamburger);
        recyclerView = rootView.findViewById(R.id.recycler_rules);
        toolbar = rootView.findViewById(R.id.toolbar);
    }

    private void prepareRulesData() {
        Rules rule = new Rules("Enigma 6 is an online cryptic event where players solve a series of challenging riddles and puzzles to win exciting cash prizes!");
        rulesList.add(rule);
        rule = new Rules("The points earned on each question are totally relative to the competition - the sooner you solve a question, the higher your score shall be!");
        rulesList.add(rule);
        rule = new Rules("Upon using a hint, a one-time penalty of 15% shall be applied on the points earned from the corresponding question.");
        rulesList.add(rule);
        rule = new Rules("Enigma 6 shall end on 8th December, 4:20 PM");
        rulesList.add(rule);
        rule = new Rules("Participant with most points will be declared winner");
        rulesList.add(rule);
        rule = new Rules("Any form of malpractice shall be dealt with extreme seriousness. We are constantly trying to enhance the experience and security of the system. Your cooperation is highly appreciated.");
        rulesList.add(rule);
    }
}
