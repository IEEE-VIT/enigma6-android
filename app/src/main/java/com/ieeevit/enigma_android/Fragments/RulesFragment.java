package com.ieeevit.enigma_android.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ieeevit.enigma_android.Activities.LaunchingActivity;
import com.ieeevit.enigma_android.Activities.WorkingActivity;
import com.ieeevit.enigma_android.Adapters.RulesAdapter;
import com.ieeevit.enigma_android.Models.Rules;
import com.ieeevit.enigma_android.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class RulesFragment extends Fragment {

    private MaterialButton play;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Rules> rulesList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;

    public RulesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_rules, container, false);

        initializeViews(rootView);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LaunchingActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        prepareRulesData();
        return rootView;
    }

    private void initializeViews(View rootView) {
        play = (rootView).findViewById(R.id.fragment_rules_material_button_play);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new RulesAdapter(rulesList);
        recyclerView = rootView.findViewById(R.id.recyclerView_rules);
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
