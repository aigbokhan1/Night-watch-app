package com.example.expensemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanager.Model.Data;
import com.example.expensemanager.Model.Filter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseRecursive#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseRecursive extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth auth;
    private DatabaseReference ExpenseDatabase;
    private RecyclerView recyclerView;

    private TextView TotalExpenseEUR;
    private TextView TotalExpenseUSD;
    private TextView TotalExpenseINR;
    private TextView TotalExpenseGBP;

    private EditText amount_edit;
    private Spinner category_edit;
    private EditText date_edit;
    private Spinner mode_edit;
    private Spinner currency_edit;
    private EditText note_edit;
    private Button button_update;
    private Button button_delete;
    private Button button_add;
    private EditText filterFromDate;
    private EditText filterToDate;
    List<Data> items = new ArrayList<Data>();
    private int amount;
    private String category;
    private String mode;
    private String date;
    private String note;
    private String currency;
    private String id;
    private RecyclerView.Adapter mAdapter;

    private String post;

    private String[] cat = {"Transportation", "Apparel", "Breakfast", "Lunch", "Dinner", "General"};
    private String[] pay_mode = {"Cash", "DebitCard", "CreditCard", "NetBanking"};
    private String[] curr = {"EUR", "USD", "INR", "GBP"};

    public ExpenseRecursive() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseRecursive.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseRecursive newInstance(String param1, String param2) {
        ExpenseRecursive fragment = new ExpenseRecursive();
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
        // Inflate the layout for this fragment

        View myview = inflater.inflate(R.layout.fragment_expense_recursive, container, false);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        ExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseRecursive").child(uid);
        TotalExpenseEUR = myview.findViewById(R.id.expenseEUR_txt_result);
        TotalExpenseUSD = myview.findViewById(R.id.expenseUSD_txt_result);
        TotalExpenseINR = myview.findViewById(R.id.expenseINR_txt_result);
        TotalExpenseGBP = myview.findViewById(R.id.expenseGBP_txt_result);
        recyclerView = myview.findViewById(R.id.recycler_id_expense);
        filterFromDate = myview.findViewById(R.id.filter_from_date);
        filterToDate = myview.findViewById(R.id.filter_to_date);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        Query ExpenseDBEUR = ExpenseDatabase.orderByChild("currency").equalTo("EUR");
        Query ExpenseDBUSD = ExpenseDatabase.orderByChild("currency").equalTo("USD");
        Query ExpenseDBINR = ExpenseDatabase.orderByChild("currency").equalTo("INR");
        Query ExpenseDBGBP = ExpenseDatabase.orderByChild("currency").equalTo("GBP");

        ExpenseDBEUR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total_expense = 0;
                Currency currsymbol = Currency.getInstance("EUR");
                String symbol = currsymbol.getSymbol();
                //TotalExpenseEUR.setText(symbol + " 0");
                for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                    Data data = customsnapshot.getValue(Data.class);
                    total_expense += data.getAmount();
                    String total_expense_strng = String.valueOf(total_expense);
                    TotalExpenseEUR.setText(symbol + " " + total_expense_strng + ".00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ExpenseDBUSD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total_expense = 0;
                Currency currsymbol = Currency.getInstance("USD");
                String symbol = currsymbol.getSymbol();
                //TotalExpenseUSD.setText(symbol + " 0");
                for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                    Data data = customsnapshot.getValue(Data.class);
                    total_expense += data.getAmount();
                    String total_expense_strng = String.valueOf(total_expense);
                    TotalExpenseUSD.setText(symbol + " " + total_expense_strng + ".00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ExpenseDBINR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total_expense = 0;
                Currency currsymbol = Currency.getInstance("INR");
                String symbol = currsymbol.getSymbol();
                //TotalExpenseINR.setText(symbol + " 0");
                for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                    Data data = customsnapshot.getValue(Data.class);
                    total_expense += data.getAmount();
                    String total_expense_strng = String.valueOf(total_expense);
                    TotalExpenseINR.setText(symbol + " " + total_expense_strng + ".00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ExpenseDBGBP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total_expense = 0;
                Currency currsymbol = Currency.getInstance("GBP");
                String symbol = currsymbol.getSymbol();
                //TotalExpenseGBP.setText(symbol + " 0");
                for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                    Data data = customsnapshot.getValue(Data.class);
                    total_expense += data.getAmount();
                    String total_expense_strng = String.valueOf(total_expense);
                    TotalExpenseGBP.setText(symbol + " " + total_expense_strng + ".00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data, IncomeRecursive.MyViewHolder> adapter=new FirebaseRecyclerAdapter<Data, IncomeRecursive.MyViewHolder>
                (
                        Data.class,
                        R.layout.income_recycler_data,
                        IncomeRecursive.MyViewHolder.class,
                        ExpenseDatabase
                ) {
            @Override
            protected void populateViewHolder(IncomeRecursive.MyViewHolder myViewHolder, final Data data, final int i) {
                myViewHolder.setNote("Note: " + data.getNote());
                myViewHolder.setMode("Mode: " + data.getMode());
                myViewHolder.setCategory("Category: " + data.getCategory());
                myViewHolder.setDate("Date: " + data.getDate());
                myViewHolder.setAmount("Amount: " +data.getAmount());
                myViewHolder.setCurrency("Currency: " +data.getCurrency());

                myViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post = getRef(i).getKey();
                        amount = data.getAmount();
                        category = data.getCategory();
                        mode = data.getMode();
                        date = data.getDate();
                        note = data.getNote();
                        currency = data.getCurrency();

                        Dataitemupdate();
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }

        private void setNote(String note){
            TextView Note = view.findViewById(R.id.note_txt_expense);
            Note.setText(note);
        }
        private  void setCategory(String category){
            TextView Category = view.findViewById(R.id.category_txt_expense);
            Category.setText(category);
        }
        private void setMode(String mode){
            TextView Mode = view.findViewById(R.id.mode_txt_expense);
            Mode.setText(mode);
        }
        private void setDate(String date){
            TextView Date=view.findViewById(R.id.date_txt_expense);
            Date.setText(date);
        }
        void setAmount(String amount){
            TextView Mode = view.findViewById(R.id.ammount_txt_income);
            Mode.setText(amount);
        }
        void setCurrency(String currency){
            TextView Currency = view.findViewById(R.id.currency_txt_income);
            Currency.setText(currency);
        }

    }




    private void Dataitemupdate(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View currview=inflater.inflate(R.layout.expense_data_update,null);
        dialog.setView(currview);

        amount_edit=currview.findViewById(R.id.edt_amount_expense);
        category_edit=(Spinner)currview.findViewById(R.id.edt_category_expense);
        mode_edit = (Spinner)currview.findViewById(R.id.edt_mode_expense);
        currency_edit = (Spinner)currview.findViewById(R.id.edt_currency_expense);
        date_edit=(EditText)currview.findViewById(R.id.edt_date_expense);
        note_edit=(EditText)currview.findViewById(R.id.edt_note_expense);
        //button_update=currview.findViewById(R.id.btn_update_expense);
        button_delete=currview.findViewById(R.id.btn_delete_expense);
        button_add=currview.findViewById(R.id.btn_update_expense);

        amount_edit.setText(String.valueOf(amount));
        amount_edit.setSelection(String.valueOf(amount).length());
        note_edit.setText(note);
        note_edit.setSelection(note_edit.length());
        date_edit.setText(date);
        date_edit.setSelection(date_edit.length());
        date_edit.setInputType(InputType.TYPE_NULL);
        date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog datepicker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month+1;
                                date_edit.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();

            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add(category);
        for (int i= 0;i<cat.length;i++){
            if (!cat[i].equals(category)){
                categories.add(cat[i]);
            }
            else{

            }

        }

        List<String> modes = new ArrayList<String>();
        modes.add(mode);
        for (int i= 0;i<pay_mode.length;i++){
            if (!pay_mode[i].equals(mode)){
                modes.add(pay_mode[i]);
            }
            else{

            }

        }

        List<String> currencies = new ArrayList<String>();
        currencies.add(currency);
        for (int i= 0;i<curr.length;i++){
            if (!curr[i].equals(currency)){
                currencies.add(curr[i]);
            }
            else{

            }

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_edit.setAdapter(dataAdapter);

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,modes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode_edit.setAdapter(modeAdapter);

        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,currencies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currency_edit.setAdapter(currencyAdapter);

        //category_edit.setTextAlignment(category);

        final AlertDialog dialog1=dialog.create();

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String amount = amount_edit.getText().toString().trim();
                int myamount=Integer.parseInt(amount);
                String note= note_edit.getText().toString().trim();
                String mdate = date_edit.getText().toString().trim();
                String category = category_edit.getSelectedItem().toString().trim();
                String mode = mode_edit.getSelectedItem().toString().trim();
                String currency = currency_edit.getSelectedItem().toString().trim();
                String id=ExpenseDatabase.push().getKey();
                Data data = new Data(myamount,note,category,post,mode,mdate,currency);
                ExpenseDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data ADDED",Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialog1.show();

    }

}







