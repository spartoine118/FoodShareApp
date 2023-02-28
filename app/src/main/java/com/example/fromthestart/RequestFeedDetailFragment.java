package com.example.fromthestart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestFeedDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFeedDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String requestID;

    public RequestFeedDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestFeedDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestFeedDetailFragment newInstance(String param1, String param2) {
        RequestFeedDetailFragment fragment = new RequestFeedDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_request_feed_detail, container, false);
        getParentFragmentManager().setFragmentResultListener("itemData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                TextView itemName = (TextView) view.findViewById(R.id.requestfeed_itemname);
                TextView requestQuantity = (TextView) view.findViewById(R.id.requestfeed_quantity);
                TextView requestPoster = (TextView) view.findViewById(R.id.requestfeed_requester);
                TextView requestDate = (TextView) view.findViewById(R.id.requestfeed_date);
                TextView requestPickupDate = (TextView) view.findViewById(R.id.requestfeed_itempickup);
                TextView requestLocation = (TextView) view.findViewById(R.id.requestfeed_location);
                TextView requestDetails = (TextView) view.findViewById(R.id.request_detail);

                DocumentReference docRef = db.collection("requests").document(result.getString("requestID"));
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                itemName.setText(document.getData().get("postName").toString());
                                requestQuantity.setText(document.getData().get("quantity").toString());
                                requestPoster.setText(document.getData().get("requesterName").toString());
                                requestDate.setText(document.getData().get("createDate").toString());
                                requestPickupDate.setText(document.getData().get("pickupDate").toString());
                                requestDetails.setText(document.getData().get("detail").toString());
                                requestID = document.getData().get("requestID").toString();

                            } else {
                                Log.d("error", "No such document");
                            }
                        } else {
                            Log.d("error", "get failed with ", task.getException());
                        }
                    }
                });

                Button approveButton = (Button) view.findViewById(R.id.requestfeed_button);
                approveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("approveStatus", true);
                        db.collection("requests").document(result.getString("requestID"))
                                .set(data, SetOptions.merge());
                    }
                });

            }
        });

        return view;
    }
}