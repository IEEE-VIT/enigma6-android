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
import com.example.enigma.Adapters.RulesAdapter;
import com.example.enigma.Models.Rules;
import com.example.enigma.R;
import java.util.ArrayList;

public class ScrollableRulesFragment extends Fragment {

    private ImageView hamburger;
    private ArrayList<Rules> rulesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RulesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public ScrollableRulesFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scrollable_rules, container, false);
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
