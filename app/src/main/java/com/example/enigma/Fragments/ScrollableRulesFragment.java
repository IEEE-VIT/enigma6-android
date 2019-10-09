package com.example.enigma.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enigma.Adapters.RulesAdapter;
import com.example.enigma.Models.Rules;
import com.example.enigma.R;

import java.util.ArrayList;

public class ScrollableRulesFragment extends Fragment {

    private ArrayList<Rules> rulesList = new ArrayList<>();

    public ScrollableRulesFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_scrollable_rules, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_rules);
        RulesAdapter adapter = new RulesAdapter(rulesList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        prepareRulesData();
        return rootView;

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
