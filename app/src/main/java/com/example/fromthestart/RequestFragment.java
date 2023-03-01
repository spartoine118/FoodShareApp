package com.example.fromthestart;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Date availableDateInput;
    private LocalDate deadLine;
    private String postID;
    private TextView username;
    private TextView itemQuantity;
    private String posterID;
    private TextView itemname;
    private String titleName;

    public RequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestFragment newInstance(String param1, String param2) {
        RequestFragment fragment = new RequestFragment();
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
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Make a request");
        getParentFragmentManager().setFragmentResultListener("requestData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                itemname = view.findViewById(R.id.request_itemname);
                username = view.findViewById(R.id.request_username);
                TextView createDate = view.findViewById(R.id.request_itemdate);
                TextView availableDate = view.findViewById(R.id.request_itemavailability);
                itemQuantity = view.findViewById(R.id.request_quantity);
                TextView itemLocation = view.findViewById(R.id.request_location);
                TextView postDate = view.findViewById(R.id.request_createdate);
                TextView descritpion = view.findViewById(R.id.request_description);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


                DocumentReference docRef = db.collection("foodposts").document(result.getString("postID"));
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                itemname.setText(document.getData().get("postTitle").toString());
                                username.setText(document.getData().get("posterUsername").toString());
                                descritpion.setText(document.getData().get("details").toString());
                                postDate.setText(document.getData().get("createdate").toString());
                                availableDate.setText(document.getData().get("availabletill").toString());
                                createDate.setText(document.getData().get("createdate").toString());
                                itemQuantity.setText(document.getData().get("quantity").toString());
                                itemLocation.setText(document.getData().get("location").toString());
                                postID = document.getData().get("postID").toString();
                                posterID = document.getData().get("posterID").toString();
                                titleName = document.getData().get("postTitle").toString();
                                deadLine = LocalDate.parse(document.getData().get("availabletill").toString(), formatter);
                            } else {
                                Log.d("error", "No such document");
                            }
                        } else {
                            Log.d("error", "get failed with ", task.getException());
                        }
                    }
                });

            }
        });

        TextView setDate = (TextView) view.findViewById(R.id.request_inputdate);
        setDate.setOnClickListener(new View.OnClickListener() {
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
                                setDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                c.set(year,monthOfYear, dayOfMonth);
                                availableDateInput = c.getTime();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        EditText inputQuantity = (EditText) view.findViewById(R.id.request_inputquantityuser);
        EditText inputRequest = (EditText) view.findViewById(R.id.request_inputcommentuser);

        Button sendRequestButton = (Button) view.findViewById(R.id.request_button);
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(inputQuantity.getText().toString().isEmpty() || setDate.getText().toString().equalsIgnoreCase("Set date...")){
                    Toast.makeText(view.getContext(), "Please choose a quantity or set the date", Toast.LENGTH_SHORT).show();
                }
                else if (availableDateInput.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(deadLine)){
                    Toast.makeText(view.getContext(), "Please choose a valid date", Toast.LENGTH_SHORT).show();
                }
                else{

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    RequestModel request = new RequestModel(user.getUid(), user.getDisplayName(),username.getText().toString(), postID, Integer.parseInt(inputQuantity.getText().toString()),
                            inputRequest.getText().toString(), availableDateInput.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString(), LocalDate.now().toString(),
                            titleName);

                    db.collection("requests").add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("requestID", documentReference.getId());
                            data.put("posterID", posterID);
                            data.put("postName", titleName);
                            db.collection("requests").document(documentReference.getId()).set(data, SetOptions.merge());
                        }
                    });


                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    Navigation.findNavController(view).navigate(R.id.action_requestFragment_to_homeFragment);
                    transaction.commit();

                }
            }
        });



        return view;
    }
}