package com.example.expensemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * Use the {@link IncomeRecursive#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeRecursive extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Data> items = new ArrayList<Data>();
    private FirebaseAuth auth;
    private DatabaseReference IncomeDatabase;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;


    private TextView TotalIncomeEUR;
    private TextView TotalIncomeUSD;
    private TextView TotalIncomeINR;
    private TextView TotalIncomeGBP;

    private EditText amount_edit;
    private Spinner category_edit;
    private Spinner currency_edit;
    private EditText date_edit;
    private Spinner mode_edit;
    private EditText note_edit;
    private Button button_update;
    private Button button_delete;
    private Button button_add;

    private int amount;
    private String category;
    private String mode;
    private String date;
    private String note;
    private String currency;
    private String id;
    private EditText filterFromDate;
    private EditText filterToDate;


    private String post;

    private String[] cat = {"Salary", "PocketMoney", "General"};
    private String[] pay_mode = {"Cash", "NetBanking"};
    private String[] curr = {"EUR", "USD", "INR", "GBP"};

    public IncomeRecursive() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeRecursive.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeRecursive newInstance(String param1, String param2) {
        IncomeRecursive fragment = new IncomeRecursive();
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
        final View myview = inflater.inflate(R.layout.fragment_income_recursive, container, false);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        IncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeRecursive").child(uid);
        TotalIncomeEUR = myview.findViewById(R.id.incomeEUR_txt_result);
        TotalIncomeUSD = myview.findViewById(R.id.incomeUSD_txt_result);
        TotalIncomeINR = myview.findViewById(R.id.incomeINR_txt_result);
        TotalIncomeGBP = myview.findViewById(R.id.incomeGBP_txt_result);
        recyclerView = myview.findViewById(R.id.recycler_id_income);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        Query IncomeDBEUR = IncomeDatabase.orderByChild("currency").equalTo("EUR");
        Query IncomeDBUSD = IncomeDatabase.orderByChild("currency").equalTo("USD");
        Query IncomeDBINR = IncomeDatabase.orderByChild("currency").equalTo("INR");
        Query IncomeDBGBP = IncomeDatabase.orderByChild("currency").equalTo("GBP");

        IncomeDBEUR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total_income = 0;
                Currency currsymbol = Currency.getInstance("EUR");
                String symbol = currsymbol.getSymbol();
                //TotalIncomeEUR.setText(symbol + " 0");
                for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                    Data data = customsnapshot.getValue(Data.class);
                    total_income += data.getAmount();
                    String total_income_strng = String.valueOf(total_income);
                    TotalIncomeEUR.setText(symbol + " " + total_income_strng + ".00");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        IncomeDBUSD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total_income = 0;
                Currency currsymbol = Currency.getInstance("USD");
                String symbol = currsymbol.getSymbol();
                //TotalIncomeUSD.setText(symbol + " 0");
                for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                    Data data = customsnapshot.getValue(Data.class);
                    total_income += data.getAmount();
                    String total_income_strng = String.valueOf(total_income);
                    TotalIncomeUSD.setText(symbol + " " + total_income_strng + ".00");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        IncomeDBINR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total_income = 0;
                Currency currsymbol = Currency.getInstance("INR");
                String symbol = currsymbol.getSymbol();
                //TotalIncomeINR.setText(symbol + " 0");
                for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                    Data data = customsnapshot.getValue(Data.class);
                    total_income += data.getAmount();
                    String total_income_strng = String.valueOf(total_income);
                    TotalIncomeINR.setText(symbol + " " + total_income_strng + ".00");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        IncomeDBGBP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total_income = 0;
                Currency currsymbol = Currency.getInstance("GBP");
                String symbol = currsymbol.getSymbol();
                //TotalIncomeGBP.setText(symbol + " 0");
                for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                    Data data = customsnapshot.getValue(Data.class);
                    total_income += data.getAmount();
                    String total_income_strng = String.valueOf(total_income);
                    TotalIncomeGBP.setText(symbol + " " + total_income_strng + ".00");
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
        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.income_recycler_data,
                        MyViewHolder.class,
                        IncomeDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, final Data data, final int i) {
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

        void setNote(String note){
            TextView Note = view.findViewById(R.id.note_txt_income);
            Note.setText(note);
        }
        void setCategory(String category){
            TextView Category = view.findViewById(R.id.category_txt_income);
            Category.setText(category);
        }
        void setMode(String mode){
            TextView Mode = view.findViewById(R.id.mode_txt_income);
            Mode.setText(mode);
        }
        void setDate(String date){
            TextView Date=view.findViewById(R.id.date_txt_income);
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



    public void Dataitemupdate() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View currview = inflater.inflate(R.layout.data_update, null);
        dialog.setView(currview);

        amount_edit = currview.findViewById(R.id.edt_amount_income);
        category_edit = (Spinner) currview.findViewById(R.id.edt_category_income);
        mode_edit = (Spinner) currview.findViewById(R.id.edt_mode_income);
        currency_edit = (Spinner) currview.findViewById(R.id.edt_currency_income);
        date_edit = (EditText) currview.findViewById(R.id.edt_date_income);
        note_edit = (EditText) currview.findViewById(R.id.edt_note_income);
        //button_update=currview.findViewById(R.id.btn_update_income);
        button_delete = currview.findViewById(R.id.btn_delete_income);
        button_add = currview.findViewById(R.id.btn_update_income);

        amount_edit.setText(String.valueOf(amount));
        amount_edit.setSelection(String.valueOf(amount).length());
        note_edit.setText(note);
        note_edit.setSelection(String.valueOf(note).length());
        date_edit.setText(date);
        //date_edit.setSelection(date_edit.length());
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
                                month = month + 1;
                                date_edit.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();

            }
        });


        List<String> categories = new ArrayList<String>();
        categories.add(category);
        for (int i = 0; i < cat.length; i++) {
            if (!cat[i].equals(category)) {
                categories.add(cat[i]);
            } else {

            }

        }

        List<String> modes = new ArrayList<String>();
        modes.add(mode);
        for (int i = 0; i < pay_mode.length; i++) {
            if (!pay_mode[i].equals(mode)) {
                modes.add(pay_mode[i]);
            } else {

            }


        }

        List<String> currencies = new ArrayList<String>();
        currencies.add(currency);
        for (int i = 0; i < curr.length; i++) {
            if (!curr[i].equals(currency)) {
                currencies.add(curr[i]);
            } else {

            }

        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_edit.setAdapter(dataAdapter);

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, modes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode_edit.setAdapter(modeAdapter);

        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, currencies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currency_edit.setAdapter(currencyAdapter);

        //category_edit.setTextAlignment(category);

        final AlertDialog dialog1 = dialog.create();

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String amount = amount_edit.getText().toString().trim();
                int myamount = Integer.parseInt(amount);
                String note = note_edit.getText().toString().trim();
                String mdate = date_edit.getText().toString().trim();
                String category = category_edit.getSelectedItem().toString().trim();
                String currency = currency_edit.getSelectedItem().toString().trim();
                String mode = mode_edit.getSelectedItem().toString().trim();
                String id = IncomeDatabase.push().getKey();
                Data data = new Data(myamount, note, category, post, mode, mdate,currency);
                IncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(), "Data ADDED", Toast.LENGTH_SHORT).show();
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

