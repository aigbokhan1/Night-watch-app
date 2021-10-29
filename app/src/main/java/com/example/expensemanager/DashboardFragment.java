package com.example.expensemanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.expensemanager.Model.Data;

import java.util.Currency;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    private TextView total_income;
    private TextView total_expense;

    private FirebaseAuth auth;
    private DatabaseReference Incomedatabase;
    private DatabaseReference Expensedatabase;

    private RecyclerView incomerecycler;
    private RecyclerView expenserecycler;
    private RadioButton currencyEUR;
    private RadioButton currencyUSD;
    private RadioButton currencyINR;
    private RadioButton currencyGBP;


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currview=inflater.inflate(R.layout.fragment_dashboard, container, false);

        auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        String id=user.getUid();

        Incomedatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(id);
        Expensedatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(id);
        Incomedatabase.keepSynced(true);
        Expensedatabase.keepSynced(true);

        total_income = currview.findViewById(R.id.income_set_result);
        total_expense = currview.findViewById(R.id.expense_set_result);

        currencyEUR = (RadioButton) currview.findViewById(R.id.currEUR);
        currencyUSD = (RadioButton) currview.findViewById(R.id.currUSD);
        currencyINR = (RadioButton) currview.findViewById(R.id.currINR);
    //    currencyGBP = (RadioButton) currview.findViewById(R.id.currGBP);

       // if(currencyEUR.isChecked()) {

            Query IncomeDBEUR = Incomedatabase.orderByChild("currency").equalTo("EUR");

            IncomeDBEUR.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int incomesum = 0;
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Data data = snap.getValue(Data.class);
                        incomesum += data.getAmount();
                        String income_result = String.valueOf(incomesum);
                        Currency currsymbol = Currency.getInstance("EUR");
                        String symbol = currsymbol.getSymbol();
                        total_income.setText(symbol + " " + income_result + ".00");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Query ExpenseDBEUR = Expensedatabase.orderByChild("currency").equalTo("EUR");
            ExpenseDBEUR.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int expensesum = 0;
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Data data = snap.getValue(Data.class);
                        expensesum += data.getAmount();
                        String expense_result = String.valueOf(expensesum);
                        Currency currsymbol = Currency.getInstance("EUR");
                        String symbol = currsymbol.getSymbol();
                        total_expense.setText(symbol + " " +expense_result + ".00");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
      //  }
        incomerecycler=currview.findViewById(R.id.dashboard_recycler_income);
        expenserecycler=currview.findViewById(R.id.dashboard_recycler_expense);

        LinearLayoutManager incomelayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        incomelayoutManager.setReverseLayout(true);
        incomelayoutManager.setStackFromEnd(true);

        incomerecycler.setHasFixedSize(true);
        incomerecycler.setLayoutManager(incomelayoutManager);

        LinearLayoutManager expenselayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,true);
        expenselayoutManager.setReverseLayout(true);
        expenselayoutManager.setStackFromEnd(true);

        expenserecycler.setHasFixedSize(true);
        expenserecycler.setLayoutManager(expenselayoutManager);

        return currview;
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Data,IncomeViewHolder>incomeAdapter=new FirebaseRecyclerAdapter<Data, IncomeViewHolder>(
                Data.class,
                R.layout.income_dashboard,
                DashboardFragment.IncomeViewHolder.class,
                Incomedatabase
        ) {
            @Override
            protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data data, int i) {
                incomeViewHolder.setamount_income(data.getAmount());
                incomeViewHolder.setcategory_income(data.getCategory());
                incomeViewHolder.setmode_income(data.getMode());
                incomeViewHolder.setdate_income(data.getDate());
                incomeViewHolder.setnote_income(data.getNote());

            }
        };
        incomerecycler.setAdapter(incomeAdapter);

        FirebaseRecyclerAdapter<Data,ExpenseViewHolder>expenseAdapter = new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(
                Data.class,
                R.layout.expense_dashboard,
                DashboardFragment.ExpenseViewHolder.class,
                Expensedatabase
        ) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder expenseViewHolder, Data data, int i) {

                expenseViewHolder.setamount_income(data.getAmount());
                expenseViewHolder.setcategory_income(data.getCategory());
                expenseViewHolder.setmode_income(data.getMode());
                expenseViewHolder.setdate_income(data.getDate());
                expenseViewHolder.setnote_income(data.getNote());

            }
        };
        expenserecycler.setAdapter(expenseAdapter);

    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder{
        View Income_view;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            Income_view=itemView;
        }

        public void setamount_income(int amount){
            TextView income_amount=Income_view.findViewById(R.id.amount_income_ds);
            String amount_str=String.valueOf(amount);
            income_amount.setText(amount_str);

        }

        public void setcategory_income(String category){

            TextView income_category=Income_view.findViewById(R.id.category_income_ds);
            income_category.setText(category);
        }

        public void setmode_income(String mode){
            TextView income_mode=Income_view.findViewById(R.id.mode_income_ds);
            income_mode.setText(mode);
        }

        public void setnote_income(String note){
            TextView income_note=Income_view.findViewById(R.id.note_income_ds);
            income_note.setText(note);
        }

        public void setdate_income(String date){
            TextView Date=Income_view.findViewById(R.id.date_income_ds);
            Date.setText(date);
        }
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{
        View Expense_view;


        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            Expense_view = itemView;
        }

        public void setamount_income(int amount){
            TextView expense_amount=Expense_view.findViewById(R.id.amount_expense_ds);
            String amount_str=String.valueOf(amount);
            expense_amount.setText(amount_str);

        }

        public void setcategory_income(String category){
            TextView expense_category=Expense_view.findViewById(R.id.category_expense_ds);
            expense_category.setText(category);
        }

        public void setmode_income(String mode){
            TextView expense_mode=Expense_view.findViewById(R.id.mode_expense_ds);
            expense_mode.setText(mode);
        }

        public void setnote_income(String note){
            TextView expense_note=Expense_view.findViewById(R.id.note_expense_ds);
            expense_note.setText(note);
        }

        public void setdate_income(String date){
            TextView expense_Date=Expense_view.findViewById(R.id.date_expense_ds);
            expense_Date.setText(date);
        }
    }
}
