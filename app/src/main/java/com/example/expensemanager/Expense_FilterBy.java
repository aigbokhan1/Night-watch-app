package com.example.expensemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.expensemanager.Adaptor.ExpenseFilterAdapter;
import com.example.expensemanager.Model.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Expense_FilterBy extends Fragment {

    RecyclerView filterRV;
    RecyclerView filterValuesRV;
    ExpenseFilterAdapter filterAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview =  inflater.inflate(R.layout.fragment_expense_filter_by, container, false);
        filterRV = myview.findViewById(R.id.filterRV);
        filterValuesRV = myview.findViewById(R.id.filterValuesRV);
        filterRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        filterValuesRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<String> category = Arrays.asList("Transportation", "Apparel", "Breakfast", "Lunch", "Dinner", "General");
        if (!Preferences.efilters.containsKey(Filter.INDEX_CATEGORY)) {
            Preferences.efilters.put(Filter.INDEX_CATEGORY, new Filter("Category", category, new ArrayList()));
        }
        List<String> mode = Arrays.asList("Cash", "DebitCard", "CreditCard", "NetBanking");
        if (!Preferences.efilters.containsKey(Filter.INDEX_MODE)) {
            Preferences.efilters.put(Filter.INDEX_MODE, new Filter("Mode", mode, new ArrayList()));
        }
        List<String> currency = Arrays.asList("EUR","USD", "INR", "GBP");
        if (!Preferences.efilters.containsKey(Filter.INDEX_CURRENCY)) {
            Preferences.efilters.put(Filter.INDEX_CURRENCY, new Filter("Currency", currency, new ArrayList()));
        }

        filterAdapter = new ExpenseFilterAdapter(getActivity(), Preferences.efilters, filterValuesRV);
        filterRV.setAdapter(filterAdapter);

        Button btnClear = myview.findViewById(R.id.clearB);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.efilters.get(Filter.INDEX_CATEGORY).setSelected(new ArrayList());
                Preferences.efilters.get(Filter.INDEX_MODE).setSelected(new ArrayList());
                Preferences.efilters.get(Filter.INDEX_CURRENCY).setSelected(new ArrayList());
                FragmentTransaction fr= getFragmentManager().beginTransaction();
                fr.replace(R.id.main_frame, new ExpensehistoryFragment());
                fr.commit();
            }
        });

        Button btnApply = myview.findViewById(R.id.applyB);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr= getFragmentManager().beginTransaction();
                fr.replace(R.id.main_frame, new ExpensehistoryFragment());
                fr.commit();
            }
        });
        return myview;
    }
}
//Comment