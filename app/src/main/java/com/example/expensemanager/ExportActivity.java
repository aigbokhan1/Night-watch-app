package com.example.expensemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportActivity extends Activity {




    List<Data> items = new ArrayList<Data>();
    private FirebaseAuth auth;
    private DatabaseReference ExpenseDatabase;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

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

    private int amount;
    private String category;
    private String mode;
    private String date;
    private String note;
    private String currency;
    private String id;



    private String post;

    private String[] cat = {"Transportation", "Apparel", "Breakfast", "Lunch", "Dinner", "General"};
    private String[] pay_mode = {"Cash", "DebitCard", "CreditCard", "NetBanking"};
    private String[] curr = {"EUR", "USD", "INR", "GBP"};






    private Button btnIncome_Export;
    private Button btnExpense_Export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        export();


    }


    private void export() {

        btnIncome_Export = findViewById(R.id.btn_income_export);
        btnExpense_Export = findViewById(R.id.btn_expense_export);


      //  btnIncome_Export.setOnClickListener(new View.OnClickListener() {
      //      @Override
       //     public void onClick(View v) {


                //generate income data



             //   public static class Post {

               //     public String author;
               //     public String title;

               //     public Post(String author, String title) {
                        // ...
             //       }

            //    }

// Get a reference to our posts
           //     final FirebaseDatabase database = FirebaseDatabase.getInstance();
           //     DatabaseReference ref = database.getReference("ExpenseDatabase");

// Attach a listener to read the data at our posts reference
              //  ref.addValueEventListener(new ValueEventListener() {
              //      @Override
              //      public void onDataChange(DataSnapshot dataSnapshot) {

                //        for (DataSnapshot customsnapshot : dataSnapshot.getChildren()) {
                //            Data data = dataSnapshot.getValue(Data.class);
                //            Amount += data.getAmount();
                //            String Amount = String.valueOf(amount);
               //             System.out.println(Data);

                //        }

               //     @Override
               //     public void onCancelled(DatabaseError databaseError) {
               //         System.out.println("The read failed: " + databaseError.getCode());
                //    }
              //  });






             //   View myview = inflater.inflate(R.layout.activity_export, container, false);

                auth = FirebaseAuth.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                String uid = user.getUid();
                DatabaseReference ref = database.getReference(auth.getUid()).child("IncomeData").child(uid);
                List<Data> items = new ArrayList<Data>();








        btnIncome_Export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ref.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange( DataSnapshot dataSnapshot) {
                         //itemsList = new ArrayList<>();
                         Data data = dataSnapshot.getValue(Data.class);
                         System.out.println(data);
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                         System.out.println("The read failed:" + databaseError.getCode());
                     }

                 });

                    //    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //        String getAmount = snapshot.child("amount").getValue().toString();
                     //       String getCategory = snapshot.child("category").getValue().toString();
                    //        String getContact = snapshot.child("contact").getValue().toString();
                    //        String getNote = snapshot.child("note").getValue().toString();
                     //       String getId = snapshot.child("id").getValue().toString();
                    //        String getDate = snapshot.child("date").getValue().toString();
                     //       String getMode = snapshot.child("mode").getValue().toString();
                    //        String getCurrency = snapshot.child("currency").getValue().toString();

                        //    exportCSV(getAmount, getCategory, getContact, getNote, getId, getDate, getMode, getCurrency);
                      //  }
                  //  }

                 //   @Override
                 //   public void onCancelled(@NonNull DatabaseError databaseError) {

              //      }
             //   });
          //  }
      //  });










        // ref.addValueEventListener(new ValueEventListener() {
               //     @Override
               //     public void onDataChange(DataSnapshot dataSnapshot) {
               // for (DataSnapshot customsnapshot1 : dataSnapshot.getChildren()) {
              //      Data data = customsnapshot1.getValue(Data.class);
               //     assert data != null;
                 //   int amount = data.getAmount();
                //    String category = data.getCategory();
               //     String mode = data.getMode();
               //     String date = data.getDate();
               //     String note = data.getNote();
                //    String id = data.getId();
                //    String currency = data.getCurrency();
               //     String contact = data.getContact();
               //     items.add(new Data(amount, note, category, id, mode, date, currency, contact));
     //   exportCSV(getAmount, getCategory, getContact, getNote, getId, getDate, getMode, getCurrency);

            //    TotalExpenseEUR = myview.findViewById(R.id.expenseEUR_txt_result);
             //   TotalExpenseUSD = myview.findViewById(R.id.expenseUSD_txt_result);
             //   TotalExpenseINR = myview.findViewById(R.id.expenseINR_txt_result);
             //   TotalExpenseGBP = myview.findViewById(R.id.expenseGBP_txt_result);
             //   recyclerView = myview.findViewById(R.id.recycler_id_expense);

              //  LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
             //   layoutManager.setReverseLayout(true);
             //   layoutManager.setStackFromEnd(true);
             //   recyclerView.setHasFixedSize(true);
             //   recyclerView.setLayoutManager(layoutManager);



             //   @Override
             //   public void onStart() {
              //      super.onStart();
              //      FirebaseRecyclerAdapter<Data, IncomeRecursive.MyViewHolder> adapter=new FirebaseRecyclerAdapter<Data, IncomeRecursive.MyViewHolder>
                      //      (
                       //             Data.class,
                       //             R.layout.income_recycler_data,
                       //             IncomeRecursive.MyViewHolder.class,
                       //             ExpenseDatabase
                       //     ) {
                   //     @Override
                  //      protected void populateViewHolder(IncomeRecursive.MyViewHolder myViewHolder, final Data data, final int i) {
                   //         myViewHolder.setNote("Note: " + data.getNote());
                   //         myViewHolder.setMode("Mode: " + data.getMode());
                   //         myViewHolder.setCategory("Category: " + data.getCategory());
                   //         myViewHolder.setDate("Date: " + data.getDate());
                   //         myViewHolder.setAmount("Amount: " +data.getAmount());
                   //         myViewHolder.setCurrency("Currency: " +data.getCurrency());


            //    btnIncome_Export.setOnClickListener(new View.OnClickListener() {
             //       @Override
              //      public void onClick(View v) {
               //         ref.addValueEventListener(new ValueEventListener() {
               //             @Override
               //             public void onDataChange( DataSnapshot dataSnapshot) {
               //                 //itemsList = new ArrayList<>();
               //                 Data data = dataSnapshot.getValue(Data.class);
               //                 System.out.println(data);
              //              }

              //              @Override
              //              public void onCancelled(DatabaseError databaseError) {
                   //             System.out.println("The read failed:" + databaseError.getCode());
                   //         }

                   //     });





             //   StringBuilder data = new StringBuilder();
              //  data.append("Time, Distance");
             //   for (int i = 0; i < 5; i++) {
              //      data.append("\n" + String.valueOf(i) + "," + String.valueOf(i * i));
              //  }




              //  try {
               //     FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
               //     out.write(Data.toString().getBytes());
               //     out.close();

               //     Context context = getApplicationContext();
               //     File filelocation = new File(getFilesDir(), "data.csv");
               //     Uri path = FileProvider.getUriForFile(context, "com.example.expensemanager.fileprovider", filelocation);
                //    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                //    fileIntent.setType("text/csv");
                //    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                //    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                 //   startActivity(Intent.createChooser(fileIntent, "Send mail"));


              //  } catch (Exception e) {
              //      e.printStackTrace();
              //  }


                btnExpense_Export.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //generate export data






                        StringBuilder data = new StringBuilder();
                        data.append("Time, Distance");
                        for (int i = 0; i < 5; i++) {
                            data.append("\n" + String.valueOf(i) + "," + String.valueOf(i * i));

                        }




                        try {
                            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
                            out.write(data.toString().getBytes());
                            out.close();

                            Context context = getApplicationContext();
                            File filelocation = new File(getFilesDir(), "data.csv");
                            Uri path = FileProvider.getUriForFile(context, "com.example.expensemanager.fileprovider", filelocation);
                            Intent fileIntent = new Intent(Intent.ACTION_SEND);
                            fileIntent.setType("text/csv");
                            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                            startActivity(Intent.createChooser(fileIntent, "Send mail"));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                });

            }
        });

    }
}