package com.example.enigma.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.enigma.Activities.WorkingActivity;
import com.example.enigma.Adapters.RulesAdapter;
import com.example.enigma.Models.Rules;
import com.example.enigma.R;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_scrollable_rules, container, false);
        initializeViews(rootView);


        toolbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if(verticalOffset == 0)
                {
                    TextView rulesExpanded = rootView.findViewById(R.id.rules_expanded);
                    TextView rulesCollapsed = rootView.findViewById(R.id.rules_collapsed);
                    rulesExpanded.setVisibility(View.VISIBLE);
                    rulesCollapsed.setVisibility(View.INVISIBLE);
                }
                else if(Math.abs(verticalOffset) >= toolbar.getTotalScrollRange())
                {
                    TextView rulesExpanded = rootView.findViewById(R.id.rules_expanded);
                    TextView rulesCollapsed = rootView.findViewById(R.id.rules_collapsed);
                    rulesExpanded.setVisibility(View.INVISIBLE);
                    rulesCollapsed.setVisibility(View.VISIBLE);
                }
            }
        });

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
        Rules rule = new Rules("For every correct answer without using hints,you will get x points ");
        rulesList.add(rule);
        rule = new Rules("For every correct answer without using hints,you will get x points");
        rulesList.add(rule);
        rule = new Rules("For every correct answer without using hints,you will get x points");
        rulesList.add(rule);
        rule = new Rules("For every correct answer without using hints,you will get x points");
        rulesList.add(rule);
        rule = new Rules("For every correct answer without using hints,you will get x points");
        rulesList.add(rule);
        rule = new Rules("For every correct answer without using hints,you will get x points");
        rulesList.add(rule);
        rule = new Rules("For every correct answer without using hints,you will get x points");
        rulesList.add(rule);
        rule = new Rules("For every correct answer without using hints,you will get x points");
        rulesList.add(rule);
        rule = new Rules("For every correct answer without using hints,you will get x points");
        rulesList.add(rule);
        rule = new Rules("For every correct answer without using hints,you will get x points");
        rulesList.add(rule);
        rule = new Rules("For every correct answer without using hints,you will get x points");
        rulesList.add(rule);
    }
}
