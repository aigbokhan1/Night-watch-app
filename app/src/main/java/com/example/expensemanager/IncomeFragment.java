package com.example.expensemanager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanager.Model.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mRecursiveIncome;
    private Switch switch_cat;
    private ArrayList<String> arrayList;
    private Spinner income_contact;
    private Spinner spinner_category;
    private Spinner spinner_mode;
    private Spinner spinner_currency;
    private String[] cat = {"Salary", "PocketMoney", "General", "Received from Friend"};
    private String[] pay_mode = {"Cash", "NetBanking"};
    private String[] curr = {"EUR",  "USD",  "INR",  "GBP"};

    DatePickerDialog datepicker;
    EditText eText;
    Button btn;
    TextView tvw;


    public IncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income2, container, false);
        spinner_category = (Spinner) view.findViewById(R.id.spinner1_income);
        spinner_mode = (Spinner) view.findViewById(R.id.spinner2_income);
        spinner_currency = (Spinner) view.findViewById(R.id.spinner3_income);
        switch_cat = (Switch) view.findViewById(R.id.income_switch);
        income_contact = (Spinner) view.findViewById(R.id.income_contact);
        Button btnSave=view.findViewById(R.id.btnSave_income);
        Button btnCansel=view.findViewById(R.id.btnCancel_income);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mIncomeDatabase.keepSynced(true);

        mRecursiveIncome=FirebaseDatabase.getInstance().getReference().child("IncomeRecursive").child(uid);
        mRecursiveIncome.keepSynced(true);

        ArrayAdapter<CharSequence> mSortAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, cat);
        mSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(mSortAdapter);

        ArrayAdapter<CharSequence> mSortAdapter_mode = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, pay_mode);
        mSortAdapter_mode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mode.setAdapter(mSortAdapter_mode);

        ArrayAdapter<CharSequence> mSortAdapter_currency = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, curr);
        mSortAdapter_mode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_currency.setAdapter(mSortAdapter_currency);

        eText=(EditText)view.findViewById(R.id.etdate_income);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                datepicker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month+1;
                                eText.setText(dayOfMonth+"/"+month+"/"+year);
                            }
                        }, year, month, day);
                datepicker.show();


            }
        });

        switch_cat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                }
                else{
                }

            }
        });

        final EditText edtAmmount=view.findViewById(R.id.ammount_edt_income);
        final Spinner edtcategory = view.findViewById(R.id.spinner1_income);
        final Spinner edtmode = view.findViewById(R.id.spinner2_income);
        final Spinner edtcurrency = view.findViewById(R.id.spinner3_income);
        final EditText edtDate = view.findViewById(R.id.etdate_income);
        final EditText edtnote = view.findViewById(R.id.note_edt_income);

        arrayList = new ArrayList<>();
        if ( Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                !=PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    1);
        }
        else
        {
            getcontact();
        }
        arrayList.add(0,"Default");
        ArrayAdapter<String> mcontact_expense = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
        mcontact_expense.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        income_contact.setAdapter(mcontact_expense);
        final Spinner edtcontact = view.findViewById(R.id.income_contact);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String amount = edtAmmount.getText().toString().trim();
                String note= edtnote.getText().toString().trim();
                String mdate = edtDate.getText().toString().trim();
                String category = edtcategory.getSelectedItem().toString().trim();
                String currency = edtcurrency.getSelectedItem().toString().trim();
                String mode = edtmode.getSelectedItem().toString().trim();
                String mcontact = edtcontact.getSelectedItem().toString().trim();

                note = note + " : "+mcontact;

                if (TextUtils.isEmpty(amount)){
                    edtAmmount.setError("Required Field..");
                    return;
                }

                int incomeamount=Integer.parseInt(amount);
                String id=mIncomeDatabase.push().getKey();
                Data data=new Data(incomeamount,note,category,id,mode,mdate,currency);
                mIncomeDatabase.child(id).setValue(data);

                if (switch_cat.isChecked()) {
                    System.out.println("into switch");
                    String exp_id=mRecursiveIncome.push().getKey();
                    Data exp_data=new Data(incomeamount,note,category,id,mode,mdate,currency);
                    mRecursiveIncome.child(id).setValue(data);

                }
                else{
                    //System.out.println("outside switch");
                }

                Toast.makeText(getActivity(),"Data ADDED",Toast.LENGTH_SHORT).show();
                edtAmmount.setText("");
                edtDate.setText("");
                edtnote.setText("");
                FragmentTransaction fr= getFragmentManager().beginTransaction();
                fr.replace(R.id.main_frame, new IncomehistoryFragment());
                fr.commit();
            }
        });
        btnCansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAmmount.setText("");
                edtDate.setText("");
                edtnote.setText("");
                //dialog.dismiss();
            }
        });

        return view;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_income2, container, false);
    }

    private void getcontact() {

        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //System.out.println(name);
            arrayList.add(name);
            //arrayList.add(name+"\n"+mobile);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getcontact();
            }
        }
    }

}
