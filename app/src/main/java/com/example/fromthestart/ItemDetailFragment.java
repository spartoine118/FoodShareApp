package com.example.fromthestart;

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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class ItemDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    Button sendRequestButton;
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Map<String, Object> data = new HashMap<>();
    public String posterID;
    private String postID;

    public ItemDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemDetailFragment newInstance(String param1, String param2) {
        ItemDetailFragment fragment = new ItemDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        getParentFragmentManager().setFragmentResultListener("itemData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                TextView name = view.findViewById(R.id.itemdetail_name);
                TextView postDate = view.findViewById(R.id.itemdetail_date);
                TextView availableDate = view.findViewById(R.id.itemdetail_availabletill);
                TextView description = view.findViewById(R.id.itemdetail_description);
                TextView usernamme = view.findViewById(R.id.itemdetail_username);
                Button deleteButton = view.findViewById(R.id.itemdetail_delete);



                DocumentReference docRef = db.collection("foodposts").document(result.getString("postID"));
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                data = document.getData();
                                name.setText(data.get("postTitle").toString());
                                usernamme.setText(data.get("posterUsername").toString());
                                description.setText(data.get("details").toString());
                                postDate.setText(data.get("createdate").toString());
                                availableDate.setText(data.get("availabletill").toString());
                                if(user.getUid().equalsIgnoreCase(data.get("posterID").toString())){
                                    deleteButton.setVisibility(View.VISIBLE);
                                }
                                else{
                                    deleteButton.setVisibility(View.INVISIBLE);
                                }
                                postID = data.get("postID").toString();

                            } else {
                                Log.d("error", "No such document");
                            }
                        } else {
                            Log.d("error", "get failed with ", task.getException());
                        }
                    }
                });


                sendRequestButton = (Button) view.findViewById(R.id.itemdetaiL_button);
                sendRequestButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getParentFragmentManager().setFragmentResult("requestData", result);
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        //transaction.replace(R.id.fragmentContainerView, new ItemDetailFragment());
                        //transaction.addToBackStack(null);
                        Navigation.findNavController(view).navigate(R.id.action_itemDetailFragment_to_requestFragment);
                        transaction.commit();
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.collection("foodposts").document(postID)
                                .delete();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        //transaction.replace(R.id.fragmentContainerView, new ItemDetailFragment());
                        //transaction.addToBackStack(null);
                        Navigation.findNavController(view).navigate(R.id.action_itemDetailFragment_to_homeFragment);
                        transaction.commit();
                    }
                });

            }
        });



        return view;
    }
}