package com.example.expensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
//import android.widget.Toolbar;
//import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private DashboardFragment dashBoardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;
    private IncomehistoryFragment incomehistoryFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Money Manager");
        setSupportActionBar(toolbar);
        frameLayout=findViewById(R.id.main_frame);
        DrawerLayout drawerLayout= findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.naView);
        navigationView.setNavigationItemSelectedListener(this);


        bottomNavigationView = findViewById(R.id.bottomNavigationbar);
        //setSupportActionBar(androidx.appcompat.widget.Toolbar);
        //mToolbar.setTitle("Money Tracker");
        //setSupportActionBar(toolbar);

        dashBoardFragment=new DashboardFragment();
        incomeFragment=new IncomeFragment();
        expenseFragment=new ExpenseFragment();
        incomehistoryFragment=new IncomehistoryFragment();



        setFragment(dashBoardFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        setFragment(dashBoardFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                        return true;

                    case R.id.income:
                        setFragment(incomeFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                        return true;

                    case R.id.expense:
                        setFragment(expenseFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }


    public void displaySelectedListener(int itemId) {
        Fragment fragment = null;

        switch (itemId) {
            case R.id.dashboard:
                fragment=new DashboardFragment();
                bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                break;
            case R.id.income:
                fragment=new IncomehistoryFragment();
                bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                break;
            case R.id.expense:
                fragment=new ExpensehistoryFragment();
                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                break;

            //case R.id.income_update:
            //  fragment = new IncomeUpdate();
            //bottomNavigationView.setItemBackgroundResource(R.color.income_color);
            //break;

            //case R.id.expense_update:
            //  fragment = new ExpenseUpdate();
            //bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
            //break;

            case R.id.Expense_Recursive:
                fragment = new ExpenseRecursive();
                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                break;

            case R.id.Income_Recursive:
                fragment = new IncomeRecursive();
                bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                break;

            //   case R.id.Default_Currency:
            //   fragment = new DefaultCurrency();
            //   bottomNavigationView.setItemBackgroundResource(R.color.income_color);
            //   break;

            case R.id.support:
                fragment = new SupportFragment();
                bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                break;


            case R.id.about:
                fragment = new AboutFragment();
                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                break;
            case R.id.help:
                fragment = new HelpFragment();
                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                break;

            case R.id.chart:
                startActivity(new Intent(getApplicationContext(), ReportFilter.class));
                break;

          //  case R.id.export:
             //   fragment = new ExportFragment();
             //   bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
             //   break;

            case R.id.export:
                startActivity(new Intent(getApplicationContext(), ExportActivity.class));
                break;



        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, fragment);
            ft.commit();

        }

    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        displaySelectedListener(item.getItemId());
        return true;
    }

}
