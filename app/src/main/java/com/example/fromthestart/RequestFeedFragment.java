package com.example.fromthestart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFeedFragment extends Fragment implements RequestSelectListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<RequestModel> itemArrayList;
    private String[] itemHeading;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public RequestFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestFeedFragment newInstance(String param1, String param2) {
        RequestFeedFragment fragment = new RequestFeedFragment();
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
        View view = inflater.inflate(R.layout.fragment_request_feed, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Request Feed");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataInitialize();

        recyclerView = view.findViewById(R.id.requestfeed_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        RequestListAdapter requestListAdapter = new RequestListAdapter(getContext(), itemArrayList, this);
        recyclerView.setAdapter(requestListAdapter);
        requestListAdapter.notifyDataSetChanged();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().findViewById(R.id.requestfeed_recyclerview).invalidate();
                getActivity().findViewById(R.id.requestfeed_recyclerview).requestLayout();
            }
        },1500);

    }

    private void dataInitialize() {

        itemArrayList = new ArrayList<>();

        db.collection("requests").whereEqualTo("posterID", user.getUid()).orderBy("createDate", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();
                        RequestModel request = new RequestModel(map.get("requester").toString(), map.get("requesterName").toString(),map.get("poster").toString(), map.get("postID").toString(),
                                Integer.parseInt(map.get("quantity").toString()), map.get("detail").toString(), map.get("pickupDate").toString(), map.get("createDate").toString(),
                                map.get("postName").toString());
                        request.setRequestID(map.get("requestID").toString());
                        request.setPosterID(map.get("posterID").toString());
                        itemArrayList.add(request);
                        Log.d("sucess", itemArrayList.get(0).toString());
                    }
                } else {
                    Log.d("ERROR", task.getException().toString());
                }
            }
        });

    }

    @Override
    public void onRequestClick(RequestModel requestModel, View view) {
        //Toast.makeText(getContext(), itemModel.getHeading(), Toast.LENGTH_LONG).show();
        Bundle result = new Bundle();
        result.putString("requestID", requestModel.getRequestID());
        getParentFragmentManager().setFragmentResult("itemData", result);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        //transaction.replace(R.id.fragmentContainerView, new ItemDetailFragment());
        //transaction.addToBackStack(null);
        Navigation.findNavController(view).navigate(R.id.action_requestFeedFragment_to_requestFeedDetailFragment);
        transaction.commit();

    }

}