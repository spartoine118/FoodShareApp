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
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchPageFragment extends Fragment implements ItemSelectListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<FoodPost> itemArrayList;
    private String[] itemHeading;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SearchPageFragment() {
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
    public static SearchPageFragment newInstance(String param1, String param2) {
        SearchPageFragment fragment = new SearchPageFragment();
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
        View view = inflater.inflate(R.layout.fragment_searchpage, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataInitialize();
        EditText searchQuery = (EditText) view.findViewById(R.id.searchpage_searchbar);
        ImageButton button = (ImageButton) view.findViewById(R.id.searchpage_serachButton);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        ItemListAdapter itemListAdapter = new ItemListAdapter(getContext(), itemArrayList, this);
        recyclerView.setAdapter(itemListAdapter);
        itemListAdapter.notifyDataSetChanged();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().findViewById(R.id.recyclerview).invalidate();
                getActivity().findViewById(R.id.recyclerview).requestLayout();
            }
        },1500);

    }

    private void dataInitialize() {

        itemArrayList = new ArrayList<>();

        db.collection("foodposts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();
                        FoodPost foodPost = new FoodPost(map.get("postTitle").toString(), map.get("location").toString(), map.get("foodttype").toString(),
                                Integer.parseInt(map.get("quantity").toString()), map.get("details").toString(),
                                map.get("createdate").toString(), map.get("availabletill").toString(), map.get("posterID").toString(), map.get("posterUsername").toString());
                        foodPost.setPostID(map.get("postID").toString());
                        itemArrayList.add(foodPost);
                    }
                } else {
                    Log.d("ERROR", task.getException().toString());
                }
            }
        });

    }

    @Override
    public void onItemClick(FoodPost foodPost, View view) {
        //Toast.makeText(getContext(), itemModel.getHeading(), Toast.LENGTH_LONG).show();
        Bundle result = new Bundle();
        result.putString("postID", foodPost.getPostID());
        getParentFragmentManager().setFragmentResult("itemData", result);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        //transaction.replace(R.id.fragmentContainerView, new ItemDetailFragment());
        //transaction.addToBackStack(null);
        Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_itemDetailFragment);
        transaction.commit();

    }
}