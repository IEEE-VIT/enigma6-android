package com.example.enigma.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.enigma.Models.Rules;
import com.example.enigma.R;
import java.util.ArrayList;


public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.ViewHolder> {

    private ArrayList <Rules> rulesList;

    public RulesAdapter(ArrayList<Rules> rules) {
        this.rulesList = rules;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView rule;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rule = (TextView)itemView.findViewById(R.id.rule);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_rules, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Rules rule = rulesList.get(position);
            holder.rule.setText(rule.getRule());
    }

    @Override
    public int getItemCount() {
        return rulesList.size();
    }
}
