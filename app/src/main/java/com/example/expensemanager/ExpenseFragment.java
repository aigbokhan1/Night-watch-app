package com.example.expensemanager;

import android.Manifest;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;
    private DatabaseReference mRecursiveExpense;
    private Switch switch_cat;
    private Spinner spinner_category;
    private Spinner spinner_mode;
    private Spinner spinner_currency;
    private Spinner expense_contact;
    private String[] cat = {"Transportation", "Apparel", "Breakfast", "Lunch", "Dinner", "General","Lent to Friend"};
    private String[] pay_mode = {"Cash", "DebitCard", "CreditCard", "NetBanking"};
    private String[] curr = {"EUR", "USD", "INR", "GBP"};
    private ArrayList<String> arrayList;
    DatePickerDialog datepicker;
    EditText eText;
    Button btn;
    TextView tvw;


    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expense2, container, false);
        spinner_category = (Spinner) view.findViewById(R.id.spinner1);
        spinner_mode = (Spinner) view.findViewById(R.id.spinner2);
        spinner_currency = (Spinner) view.findViewById(R.id.spinner3);
        switch_cat = (Switch) view.findViewById(R.id.expense_switch);
        expense_contact = (Spinner) view.findViewById(R.id.expense_contact);

        Button btnSave=view.findViewById(R.id.btnSave);
        Button btnCansel=view.findViewById(R.id.btnCancel);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mExpenseDatabase=FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);
        mExpenseDatabase.keepSynced(true);

        mRecursiveExpense=FirebaseDatabase.getInstance().getReference().child("ExpenseRecursive").child(uid);
        mRecursiveExpense.keepSynced(true);



        ArrayAdapter<CharSequence> mSortAdapter_expense = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, cat);
        mSortAdapter_expense.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(mSortAdapter_expense);

        ArrayAdapter<CharSequence> mSortAdapter_mode_expense = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, pay_mode);
        mSortAdapter_mode_expense.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mode.setAdapter(mSortAdapter_mode_expense);

        ArrayAdapter<CharSequence> mSortAdapter_currency_expense = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, curr);
        mSortAdapter_mode_expense.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_currency.setAdapter(mSortAdapter_currency_expense);

        //tvw=(TextView)view.findViewById(R.id.textView1);
        eText=(EditText)view.findViewById(R.id.etdate_expense);
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

        final EditText edtAmmount=view.findViewById(R.id.ammount_edt);
        final Spinner edtcategory = view.findViewById(R.id.spinner1);
        final Spinner edtmode = view.findViewById(R.id.spinner2);
        final Spinner edtcurrency = view.findViewById(R.id.spinner3);
        final EditText edtDate = view.findViewById(R.id.etdate_expense);
        final Switch simpleswitch = view.findViewById(R.id.expense_switch);
        final EditText edtnote = view.findViewById(R.id.note_edt);

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
        expense_contact.setAdapter(mcontact_expense);
        final Spinner edtcontact = view.findViewById(R.id.expense_contact);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String amount = edtAmmount.getText().toString().trim();
                String note= edtnote.getText().toString().trim();
                String mdate = edtDate.getText().toString().trim();
                String mcontact = edtcontact.getSelectedItem().toString().trim();
                //Date mDate= dateFormat.parse(mdate);
                String category = edtcategory.getSelectedItem().toString().trim();
                String mode = edtmode.getSelectedItem().toString().trim();
                String currency = edtcurrency.getSelectedItem().toString().trim();

                note = note+ " : "+mcontact;

                if (TextUtils.isEmpty(amount)){
                    edtAmmount.setError("Required Field..");
                    return;
                }
                int ourammontint=Integer.parseInt(amount);

                String id=mExpenseDatabase.push().getKey();
                Data data=new Data(ourammontint,note,category,id,mode,mdate,currency);
                mExpenseDatabase.child(id).setValue(data);

                if (switch_cat.isChecked()) {
                    System.out.println("into switch");
                    String exp_id=mRecursiveExpense.push().getKey();
                    Data exp_data=new Data(ourammontint,note,category,id,mode,mdate,currency);
                    mRecursiveExpense.child(id).setValue(data);

                }
                else{
                    //System.out.println("outside switch");
                }

                Toast.makeText(getActivity(),"Data ADDED",Toast.LENGTH_SHORT).show();
                edtAmmount.setText("");
                edtDate.setText("");
                edtnote.setText("");
                //dialog.dismiss();
                FragmentTransaction fr= getFragmentManager().beginTransaction();
                fr.replace(R.id.main_frame, new ExpensehistoryFragment());
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
        //return inflater.inflate(R.layout.fragment_expense2, container, false);
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
