package com.example.expensemanager;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.util.ArrayList;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PieGraph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieGraph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public int[] total_sum;
    public int[] expense_amount;
    public int[] income_amount;
    public String[] category;

    public static final int[] COLORS = {
            rgb("#153"), rgb("#FFD700"), rgb("#07f50f"), rgb("#05f0ec"),
            rgb("#7a05f0"), rgb("#d305e6"), rgb("#e30264"), rgb("#380204"), rgb("#c98feb"),
            rgb("#8ed3e8"), rgb("#8ce6cb"), rgb("#9ce68a"), rgb("#edc980"), rgb("#473d28"),
            rgb("#483D8B")};

    public PieGraph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PieGraph.
     */
    // TODO: Rename and change types and number of parameters
    public static PieGraph newInstance(String param1, String param2) {
        PieGraph fragment = new PieGraph();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_graph, container, false);
        PieChart pieChart = view.findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(false);
        ArrayList<PieEntry> yValues = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        for(int x =0; x<income_amount.length; x++){
            if (!category[x].equalsIgnoreCase("") && income_amount[x] != 0){
                yValues.add(new PieEntry(income_amount[x], category[x]));
                xValues.add(category[x]);


            }

        }

        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.YELLOW);
        PieDataSet dataSet = new PieDataSet(yValues, "");
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(COLORS);
        PieData data = new PieData(dataSet);
        //dataSet.setValueLinePart1OffsetPercentage(90.f);
        //dataSet.setValueLinePart1Length(.10f);
        //dataSet.setValueLinePart2Length(.50f);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setDrawEntryLabels(false);
        Legend l = pieChart.getLegend();
        l.setEnabled(true);
        l.setWordWrapEnabled(true);
        l.setTextSize(14);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.CIRCLE);
        pieChart.invalidate();
        // Inflate the layout for this fragment
        return view;
    }
}
