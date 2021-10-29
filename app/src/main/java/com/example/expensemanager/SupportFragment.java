package com.example.expensemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SupportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String message_content;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SupportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SupportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SupportFragment newInstance(String param1, String param2) {
        SupportFragment fragment = new SupportFragment();
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

        View view = inflater.inflate(R.layout.fragment_support, container, false);
        Button btnSave=view.findViewById(R.id.button_submit);
        Button btnCancel=view.findViewById(R.id.buttonSrc);

        final EditText name=view.findViewById(R.id.editText1);
        final EditText email=view.findViewById(R.id.editText2);
        final EditText content=view.findViewById(R.id.editText3);
        final EditText phone=view.findViewById(R.id.editText4);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name_field = name.getText().toString().trim();
                String email_field = email.getText().toString().trim();
                String content_field = content.getText().toString().trim();
                String phone_field = phone.getText().toString().trim();

                message_content = content_field+"\n"+"Phone Number: "+phone_field+"\n"+"\n"+"Best Regards"+"\n"+"Cutomer";
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"agarwalshubham953@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback From User");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , message_content);
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    Log.i("Finished sending email...", "");

                }catch (android.content.ActivityNotFoundException ex) {
                    //Toast.makeText(, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                email.setText("");
                content.setText("");
                phone.setText("");
            }
        });





        return view;
    }
}
