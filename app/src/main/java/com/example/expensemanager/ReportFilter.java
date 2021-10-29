package com.example.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.expensemanager.Model.Data;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.function.IntBinaryOperator;
import java.util.stream.IntStream;

public class ReportFilter extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference IncomeDatabase;
    private DatabaseReference ExpenseDatabase;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private DashboardFragment dashBoardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;

    private Spinner spinner_category;
    private Spinner spinner_mode;
    private String[] cat = {"Transportation", "Apparel", "Breakfast", "Lunch", "Dinner", "General","Salary", "PocketMoney"};
    private final float[] ctgAmts = new float[cat.length];
    private String category;

    private String temp_cat;
    private String status_switch_income;
    private String status_switch_expense;
    private FirebaseUser user;
    private String uid;
    private String expense_bundle = "";
    private String income_bundle = "";

    public int[] sum;
    private int[] expense_amount = new int[cat.length];
    private int[] income_amount = new int[cat.length];

    DatePickerDialog datepicker;
    EditText min_date;
    EditText max_date;
    Button btn;
    TextView tvw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_filter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pie Graph");
        frameLayout=findViewById(R.id.main_frame);

        DrawerLayout drawerLayout= findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        //drawerLayout.addDrawerListener(toggle);
        //toggle.syncState();
        //NavigationView navigationView = findViewById(R.id.naView);
        //navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationbar);



        Button search = findViewById(R.id.search_button);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Intent report = new Intent(ReportFilter.this, HomeActivity.class);
                        startActivity(report);
                        bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                        return true;

                    case R.id.income:

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        IncomeFragment fragment = new IncomeFragment();
                        fragmentTransaction.add(R.id.main_frame, fragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.expense:
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                        ExpenseFragment fragment1 = new ExpenseFragment();
                        fragmentTransaction1.add(R.id.main_frame, fragment1);
                        fragmentTransaction1.commit();
                        return true;


                    default:
                        return false;
                }
            }
        });



        final Switch edtcategory = findViewById(R.id.category);
        final Switch edtmode = findViewById(R.id.enter_payment_graph);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                //category = edtcategory.getSelectedItem().toString().trim();
                //mode = edtmode.getSelectedItem().toString().trim();

                if (edtcategory.isChecked()) {
                    status_switch_expense = "Expense";
                }
                else{
                    status_switch_expense = "";
                }

                if (edtmode.isChecked()) {
                    status_switch_income = "Income";
                }
                else{
                    status_switch_income = "";
                }




                if (status_switch_expense.equals("Expense") & status_switch_income.equals("")) {
                    System.out.println("Entering into Expense if");
                    auth = FirebaseAuth.getInstance();
                    user = auth.getCurrentUser();
                    uid = user.getUid();

                    Query query_expense_date = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

                    query_expense_date.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("inside loop");
                            int total_expense = 0;
                            String cat_type;
                            //expense_amount = new int[cat.length];
                            for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                                Data data = customsnapshot.getValue(Data.class);
                                temp_cat = data.getCategory();
                                for (int i = 0; i < cat.length; i++) {
                                    if (cat[i].equals(temp_cat)) {
                                        expense_amount[i] += data.getAmount();
                                        if (expense_amount[i] > 1000000000000.0) {
                                            expense_amount[i] = (int) 1000000000000.0;
                                        }
                                    }
                                }
                                total_expense += data.getAmount();
                                String total_expense_strng = String.valueOf(total_expense);
                                //System.out.println(Arrays.toString(expense_amount));

                            }

                            sum = applyOn2Arrays((x, y) -> x - y, expense_amount, income_amount);
                            Intent report = new Intent(ReportFilter.this, Report.class);
                            Bundle bundle = new Bundle();
                            report.putExtra("tb", bundle);
                            report.putExtra("categories", cat);
                            report.putExtra("income_amount", income_amount);
                            report.putExtra("expense_amount", expense_amount);
                            report.putExtra("Total", sum);
                            String catString = "";
                            Arrays.fill(income_amount, 0);
                            Arrays.fill(expense_amount, 0);
                            startActivity(report);
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    System.out.println("Expense");
                    System.out.println(Arrays.toString(expense_amount));
                }

                if (status_switch_income.equals("Income") & status_switch_expense.equals("")) {
                    System.out.println("Entering into Income if");
                    auth = FirebaseAuth.getInstance();
                    user = auth.getCurrentUser();
                    uid = user.getUid();

                    Query query_income_date = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

                    query_income_date.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("inside loop");
                            int total_expense = 0;
                            String cat_type;
                            //expense_amount = new int[cat.length];
                            for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                                Data data = customsnapshot.getValue(Data.class);
                                temp_cat = data.getCategory();
                                for (int i = 0; i < cat.length; i++) {
                                    if (cat[i].equals(temp_cat)) {
                                        income_amount[i] += data.getAmount();
                                        if (income_amount[i] > 1000000000000.0) {
                                            income_amount[i] = (int) 1000000000000.0;
                                        }
                                    }
                                }
                                total_expense += data.getAmount();
                                String total_expense_strng = String.valueOf(total_expense);
                                //System.out.println(Arrays.toString(expense_amount));

                            }

                            sum = applyOn2Arrays((x, y) -> x - y, expense_amount, income_amount);
                            Intent report = new Intent(ReportFilter.this, Report.class);
                            Bundle bundle = new Bundle();
                            report.putExtra("tb", bundle);
                            report.putExtra("categories", cat);
                            report.putExtra("income_amount", income_amount);
                            report.putExtra("expense_amount", expense_amount);
                            report.putExtra("Total", sum);
                            String catString = "";
                            Arrays.fill(income_amount, 0);
                            Arrays.fill(expense_amount, 0);
                            startActivity(report);
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    System.out.println("Income");
                    System.out.println(Arrays.toString(income_amount));
                }

                if (status_switch_income.equals("Income") & status_switch_expense.equals("Expense")) {
                    System.out.println("Entering into Income/Expense if");
                    auth = FirebaseAuth.getInstance();
                    user = auth.getCurrentUser();
                    uid = user.getUid();

                    Query query_expense_date = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

                    query_expense_date.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("inside loop");
                            int total_expense = 0;
                            String cat_type;
                            //expense_amount = new int[cat.length];
                            for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                                Data data = customsnapshot.getValue(Data.class);
                                temp_cat = data.getCategory();
                                for (int i = 0; i < cat.length; i++) {
                                    if (cat[i].equals(temp_cat)) {
                                        expense_amount[i] += data.getAmount();
                                        if (expense_amount[i] > 1000000000000.0) {
                                            expense_amount[i] = (int) 1000000000000.0;
                                        }
                                    }
                                }
                                total_expense += data.getAmount();
                                String total_expense_strng = String.valueOf(total_expense);
                                //System.out.println(Arrays.toString(expense_amount));

                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Query query_income_date = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

                    query_income_date.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("inside loop");
                            int total_expense = 0;
                            String cat_type;
                            //expense_amount = new int[cat.length];
                            for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                                Data data = customsnapshot.getValue(Data.class);
                                temp_cat = data.getCategory();
                                for (int i = 0; i < cat.length; i++) {
                                    if (cat[i].equals(temp_cat)) {
                                        income_amount[i] += data.getAmount();
                                        if (income_amount[i] > 1000000000000.0) {
                                            income_amount[i] = (int) 1000000000000.0;
                                        }
                                    }
                                }
                                total_expense += data.getAmount();
                                String total_expense_strng = String.valueOf(total_expense);
                                //System.out.println(Arrays.toString(expense_amount));

                            }

                            sum = applyOn2Arrays((x, y) -> x - y, expense_amount, income_amount);
                            Intent report = new Intent(ReportFilter.this, Report.class);
                            Bundle bundle = new Bundle();
                            report.putExtra("tb", bundle);
                            report.putExtra("categories", cat);
                            report.putExtra("income_amount", income_amount);
                            report.putExtra("expense_amount", expense_amount);
                            report.putExtra("Total", sum);
                            String catString = "";
                            Arrays.fill(income_amount, 0);
                            Arrays.fill(expense_amount, 0);
                            startActivity(report);
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    System.out.println("Income");
                    System.out.println(Arrays.toString(income_amount));
                }



            }

        });
    }



    private int[] applyOn2Arrays(IntBinaryOperator operator, int[] a, int b[]) {
        return IntStream.range(0, a.length)
                .map(index -> Math.abs(operator.applyAsInt(a[index], b[index])))
                .toArray();


    }

//    public void displaySelectedListener(int itemId) {
//        Fragment fragment = null;
//
//        switch (itemId) {
//            case R.id.dashboard:
//                fragment=new DashboardFragment();
//                bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
//                break;
//            case R.id.income:
//                fragment=new IncomehistoryFragment();
//                bottomNavigationView.setItemBackgroundResource(R.color.income_color);
//                break;
//            case R.id.expense:
//                fragment=new ExpensehistoryFragment();
//                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
//                break;
//
//            //case R.id.income_update:
//            //  fragment = new IncomeUpdate();
//            //bottomNavigationView.setItemBackgroundResource(R.color.income_color);
//            //break;
//
//            //case R.id.expense_update:
//            //  fragment = new ExpenseUpdate();
//            //bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
//            //break;
//
//            case R.id.Expense_Recursive:
//                fragment = new ExpenseRecursive();
//                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
//                break;
//
//            case R.id.Income_Recursive:
//                fragment = new IncomeRecursive();
//                bottomNavigationView.setItemBackgroundResource(R.color.income_color);
//                break;
//
//
//            case R.id.support:
//                fragment = new SupportFragment();
//                bottomNavigationView.setItemBackgroundResource(R.color.income_color);
//                break;
//
//
//            case R.id.about:
//                fragment = new AboutFragment();
//                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
//                break;
//            case R.id.help:
//                fragment = new HelpFragment();
//                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
//                break;
//
//
//
//        }
//
//        if (fragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.main_frame, fragment);
//            ft.commit();
//
//        }
//
//    }
//
//    @Override
//    public void onBackPressed(){
//        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
//        if (drawerLayout.isDrawerOpen(GravityCompat.END)){
//            drawerLayout.closeDrawer(GravityCompat.END);
//        }else{
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item){
//        displaySelectedListener(item.getItemId());
//        return true;
//    }



}
