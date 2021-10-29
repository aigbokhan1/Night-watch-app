package com.example.expensemanager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class Report extends AppCompatActivity {
    private int[] income_amount;
    private int[] expense_amount;
    private int[] sum;
    private String[] categories;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public static final int[] COLORS = {
            rgb("#153"), rgb("#FFD700"), rgb("#07f50f"), rgb("#05f0ec"),
            rgb("#7a05f0"), rgb("#d305e6"), rgb("#e30264"), rgb("#380204"), rgb("#c98feb"),
            rgb("#8ed3e8"), rgb("#8ce6cb"), rgb("#9ce68a"), rgb("#edc980"), rgb("#473d28"),
            rgb("#483D8B")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Bundle b = getIntent().getBundleExtra("tb");
        income_amount = getIntent().getIntArrayExtra("income_amount");
        expense_amount = getIntent().getIntArrayExtra("expense_amount");
        sum = getIntent().getIntArrayExtra("Total");
        categories = getIntent().getStringArrayExtra("categories");
        System.out.println("Catgories#########################");
        System.out.println(Arrays.toString(categories));
        System.out.println(Arrays.toString(income_amount));
        System.out.println(Arrays.toString(expense_amount));
        System.out.println(Arrays.toString(sum));


        PieGraph pieGraphFragment = new PieGraph();
        pieGraphFragment.total_sum = this.sum;
        pieGraphFragment.income_amount = this.income_amount;
        pieGraphFragment.expense_amount = this.expense_amount;
        pieGraphFragment.category = this.categories;

        setContentView(R.layout.fragment_pie_graph);
        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        //PieChart pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(false);
        ArrayList<PieEntry> yValues = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        for(int x =0; x<sum.length; x++){
            if (!categories[x].equalsIgnoreCase("") && sum[x] != 0){
                yValues.add(new PieEntry(sum[x], categories[x]));
                xValues.add(categories[x]);


            }

        }
        System.out.println("PIECHARTTTTTTT");
        System.out.println(Arrays.toString(new ArrayList[]{xValues}));


        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.YELLOW);
        PieDataSet dataSet = new PieDataSet(yValues, "");
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(COLORS);
        PieData data = new PieData(dataSet);
//        dataSet.setValueLinePart1OffsetPercentage(90.f);
//        dataSet.setValueLinePart1Length(.10f);
//        dataSet.setValueLinePart2Length(.50f);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(false);
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


    }
}
