package com.example.expensemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;





public class IncomehistoryFragment extends Fragment {


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
    private EditText date_edit;
    private Spinner mode_edit;
    private Spinner currency_edit;
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

    FloatingActionButton fabMain, fab1,fab2;
    Animation fabOpen, fabClose,rotateForward, rotateBackward;
    boolean isOpen= false;


    private String post;

    private String[] cat = {"Salary", "PocketMoney", "General"};
    private String[] pay_mode = {"Cash", "NetBanking"};
    private String[] curr = {"EUR", "USD", "INR", "GBP"};


    public IncomehistoryFragment() {
        // Required empty public constructor
    }

    public void animateFab()
    {
        if(isOpen)
        {
            fabMain.startAnimation(rotateForward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen=false;
        }
        else
        {
            fabMain.startAnimation(rotateBackward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen=true;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myview = inflater.inflate(R.layout.fragment_incomehistory, container, false);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        IncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        TotalIncomeEUR = myview.findViewById(R.id.incomeEUR_txt_result);
        TotalIncomeUSD = myview.findViewById(R.id.incomeUSD_txt_result);
        TotalIncomeINR = myview.findViewById(R.id.incomeINR_txt_result);
        TotalIncomeGBP = myview.findViewById(R.id.incomeGBP_txt_result);
        recyclerView = myview.findViewById(R.id.recycler_id_income);
        filterFromDate = myview.findViewById(R.id.filter_from_date);
        filterToDate = myview.findViewById(R.id.filter_to_date);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        fabMain = myview.findViewById(R.id.fabMain);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myview) {
                animateFab();
            }
        });

        fab1 = myview.findViewById(R.id.fabOne);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myview) {
                animateFab();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.main_frame, new Income_FilterBy());
                fr.commit();
            }
        });

        fab2 = myview.findViewById(R.id.fabTwo);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myview) {
                animateFab();


                //data to export




                Toast.makeText(getActivity(), "Coming Soon in Next Prototype", Toast.LENGTH_SHORT).show();
            }
        });


        fabOpen = AnimationUtils.loadAnimation(getContext(),R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_backward);



        final ImageButton applyDate = myview.findViewById(R.id.apply_date);
        final ImageButton clearFilter = myview.findViewById(R.id.clear_filter);

        filterFromDate.setInputType(InputType.TYPE_NULL);
        filterFromDate.setOnClickListener(new View.OnClickListener() {
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
                                filterFromDate.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });
        filterToDate.setInputType(InputType.TYPE_NULL);
        filterToDate.setOnClickListener(new View.OnClickListener() {
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
                                filterToDate.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });

        IncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot customsnapshot1 : dataSnapshot.getChildren()) {
                    Data data = customsnapshot1.getValue(Data.class);
                    assert data != null;
                    int amount = data.getAmount();
                    String category = data.getCategory();
                    String mode = data.getMode();
                    String date = data.getDate();
                    String note = data.getNote();
                    String currency = data.getCurrency();
                    String id = data.getId();
                    items.add(new Data(amount, note, category, id, mode, date,currency));
                    CategoryModeCurrencyFilter();
                    applyDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View myview) {
                            String From_Date = filterFromDate.getText().toString().trim();
                            String To_Date = filterToDate.getText().toString().trim();
                            if (!From_Date.isEmpty() && !To_Date.isEmpty()) {

                                ArrayList<Data> FilteredList_Date = new ArrayList<>();
                                for (Data FilterByDate : items) {
                                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                    Date FromDate1 = new Date();
                                    try {
                                        FromDate1 = df.parse(From_Date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date ToDate1 = new Date();
                                    try {
                                        ToDate1 = df.parse(To_Date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    Date Itemdate = new Date();
                                    try {
                                        Itemdate = formatter.parse(FilterByDate.getDate());
                                    } catch (ParseException e) {

                                    }

                                    if (FromDate1.compareTo(Itemdate) * Itemdate.compareTo(ToDate1) >= 0) {
                                        FilteredList_Date.add(FilterByDate);
                                    }
                                }

                                items = FilteredList_Date;
                            }

                            mAdapter = new iDataAdapter(getActivity(), items);
                            recyclerView.setAdapter(mAdapter);

                        }
                    });

                    clearFilter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            filterFromDate.setText("");
                            filterToDate.setText("");
                            if(!Preferences.ifilters.isEmpty()) {
                                Preferences.ifilters.get(Filter.INDEX_CATEGORY).setSelected(new ArrayList());
                                Preferences.ifilters.get(Filter.INDEX_MODE).setSelected(new ArrayList());
                                Preferences.ifilters.get(Filter.INDEX_CURRENCY).setSelected(new ArrayList());
                            }
                            FragmentTransaction fr= getFragmentManager().beginTransaction();
                            fr.replace(R.id.main_frame, new IncomehistoryFragment());
                            fr.commit();

                        }
                    });

                    String From_Date1 = filterFromDate.getText().toString().trim();
                    String To_Date1 = filterToDate.getText().toString().trim();
                    if(From_Date1.isEmpty() && To_Date1.isEmpty()) {
                        TotalIncome();
                    }
                    mAdapter = new iDataAdapter(getActivity(), items);
                    recyclerView.setAdapter(mAdapter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myview;
    }

    public void CategoryModeCurrencyFilter(){
        if (!Preferences.ifilters.isEmpty()) {
            ArrayList<Data> filteredItems = new ArrayList<Data>();
            List<String> categories = Preferences.ifilters.get(Filter.INDEX_CATEGORY).getSelected();
            List<String> modes = Preferences.ifilters.get(Filter.INDEX_MODE).getSelected();
            List<String> currencies = Preferences.ifilters.get(Filter.INDEX_CURRENCY).getSelected();
            for (Data item : items) {
                boolean categoryMatched = true;
                if (categories.size() > 0 && !categories.contains(item.getCategory())) {
                    categoryMatched = false;
                }
                boolean modeMatched = true;
                if (modes.size() > 0 && !modes.contains(item.getMode())) {
                    modeMatched = false;
                }
                boolean currencyMatched = true;
                if (currencies.size() > 0 && !currencies.contains(item.getCurrency())) {
                    currencyMatched = false;
                }
                if (categoryMatched && modeMatched && currencyMatched) {
                    filteredItems.add(item);
                }
            }
            items = filteredItems;
        }
    }
    public void TotalIncome() {
        int total_income_EUR = 0;
        int total_income_USD = 0;
        int total_income_INR = 0;
        int total_income_GBP = 0;
        ArrayList<Data> totalincomeEUR = new ArrayList<>();
        ArrayList<Data> totalincomeUSD = new ArrayList<>();
        ArrayList<Data> totalincomeINR = new ArrayList<>();
        ArrayList<Data> totalincomeGBP = new ArrayList<>();
        for (Data SortCurrency : items) {
            String CheckCurrency = SortCurrency.getCurrency();
            if (CheckCurrency != null && CheckCurrency.equals("EUR")) {
                totalincomeEUR.add(SortCurrency);
            }
            if (CheckCurrency != null && CheckCurrency.equals("USD")) {
                totalincomeUSD.add(SortCurrency);
            }
            if (CheckCurrency != null && CheckCurrency.equals("INR")) {
                totalincomeINR.add(SortCurrency);
            }
            if (CheckCurrency != null && CheckCurrency.equals("GBP")) {
                totalincomeGBP.add(SortCurrency);
            }
        }

        for (Data amount1 : totalincomeEUR) {
            total_income_EUR += amount1.getAmount();
            String total_income_strng = String.valueOf(total_income_EUR);
            Currency currsymbol = Currency.getInstance("EUR");
            String symbol = currsymbol.getSymbol();
            TotalIncomeEUR.setText(symbol + " " + total_income_strng + ".00");
        }
        for (Data amount1 : totalincomeUSD) {
            total_income_USD += amount1.getAmount();
            String total_income_strng = String.valueOf(total_income_USD);
            Currency currsymbol = Currency.getInstance("USD");
            String symbol = currsymbol.getSymbol();
            TotalIncomeUSD.setText(symbol + " " + total_income_strng + ".00");
        }
        for (Data amount1 : totalincomeINR) {
            total_income_INR += amount1.getAmount();
            String total_income_strng = String.valueOf(total_income_INR);
            Currency currsymbol = Currency.getInstance("INR");
            String symbol = currsymbol.getSymbol();
            TotalIncomeINR.setText(symbol + " " + total_income_strng + ".00");
        }
        for (Data amount1 : totalincomeGBP) {
            total_income_GBP += amount1.getAmount();
            String total_income_strng = String.valueOf(total_income_GBP);
            Currency currsymbol = Currency.getInstance("GBP");
            String symbol = currsymbol.getSymbol();
            TotalIncomeGBP.setText(symbol + " " + total_income_strng + ".00");
        }
    }

    public class iDataAdapter extends RecyclerView.Adapter<iDataAdapter.MyViewHolder> {

        private Context context;
        private List<Data> items;

        public iDataAdapter(Context context, List<Data> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.income_recycler_data, viewGroup, false);
            MyViewHolder mvh = new MyViewHolder(view);
            return mvh;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
            myViewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = items.get(position).getAmount();
                    category = items.get(position).getCategory();
                    mode = items.get(position).getMode();
                    date = items.get(position).getDate();
                    note = items.get(position).getNote();
                    id = items.get(position).getId();
                    currency = items.get(position).getCurrency();
                    Dataitemupdate();
                }
            });

            myViewHolder.amount2.setText("Amount: " + items.get(position).getAmount());
            myViewHolder.category2.setText("Category: " + items.get(position).getCategory());
            myViewHolder.mode2.setText("Mode: " + items.get(position).getMode());
            myViewHolder.date2.setText("Date: " + items.get(position).getDate());
            myViewHolder.note2.setText("Note: " + items.get(position).getNote());
            myViewHolder.currency2.setText("Currency: " + items.get(position).getCurrency());


        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            View container;
            TextView amount2;
            TextView category2;
            TextView mode2;
            TextView date2;
            TextView note2;
            TextView currency2;

            public MyViewHolder(View view) {
                super(view);
                container = view;
                amount2 = view.findViewById(R.id.ammount_txt_income);
                category2 = view.findViewById(R.id.category_txt_income);
                mode2 = view.findViewById(R.id.mode_txt_income);
                date2 = view.findViewById(R.id.date_txt_income);
                note2 = view.findViewById(R.id.note_txt_income);
                currency2 = view.findViewById(R.id.currency_txt_income);
            }
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

//        button_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String amount_value = String.valueOf(amount);
//                amount_value = amount_edit.getText().toString().trim();
//                int myamount=Integer.parseInt(amount_value);
//                category = category_edit.getSelectedItem().toString().trim();
//                mode = mode_edit.getSelectedItem().toString().trim();
//                date = date_edit.getText().toString().trim();
//                note = note_edit.getText().toString().trim();
//                Data data = new Data(myamount,note,category,post,mode,date);
//                IncomeDatabase.child(post).setValue(data);
//                Toast.makeText(getActivity(),"Data Updated Successfully",Toast.LENGTH_SHORT).show();
//                dialog1.dismiss();
//
//            }
//        });
//
//        button_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog1.dismiss();
//
//            }
//        });
//
//        dialog1.show();

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String amount = amount_edit.getText().toString().trim();
                int myamount = Integer.parseInt(amount);
                String note = note_edit.getText().toString().trim();
                String mdate = date_edit.getText().toString().trim();
                String category = category_edit.getSelectedItem().toString().trim();
                String mode = mode_edit.getSelectedItem().toString().trim();
                String currency = currency_edit.getSelectedItem().toString().trim();
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

















