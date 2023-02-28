package com.example.fromthestart;

import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.app.DatePickerDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Date availableDate;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePostFragment newInstance(String param1, String param2) {
        CreatePostFragment fragment = new CreatePostFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        Button createpostbutton = (Button) view.findViewById(R.id.createpost_button);
        TextView setDatetext = (TextView) view.findViewById(R.id.createpost_setdate);
        EditText address = (EditText) view.findViewById(R.id.editTextTextPostalAddress);
        EditText foodType = (EditText) view.findViewById(R.id.editTextTextPersonName2);
        EditText quantity = (EditText) view.findViewById(R.id.editTextNumber);
        EditText details = (EditText) view.findViewById(R.id.editTextTextMultiLine);
        EditText title = (EditText) view.findViewById(R.id.createpost_titleedittext);

        createpostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                FoodPost foodPost = new FoodPost(title.getText().toString(), address.getText().toString(), foodType.getText().toString(), Integer.parseInt(quantity.getText().toString()), details.getText().toString(),
                        availableDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(),
                        LocalDate.now().toString(), user.getUid());
                db.collection("foodposts").add(foodPost).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("postID", documentReference.getId());
                        data.put("posterUsername", user.getDisplayName());
                        db.collection("foodposts").document(documentReference.getId()).set(data, SetOptions.merge());
                    }
                });
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                Navigation.findNavController(view).navigate(R.id.action_createPostFragment_to_homeFragment);
                transaction.commit();
            }
        });

        setDatetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                setDatetext.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                c.set(year,monthOfYear, dayOfMonth);
                                availableDate = c.getTime();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        return view;
    }
}