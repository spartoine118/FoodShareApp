package com.example.fromthestart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<RequestModel> itemArrayList;
    private RequestSelectListener listener;

    public RequestListAdapter(Context context, ArrayList<RequestModel> itemArrayList, RequestSelectListener listener) {
        this.context = context;
        this.itemArrayList = itemArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.food_list_item, parent, false);
        return new RequestListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestListAdapter.MyViewHolder holder, int position) {

        RequestModel request = itemArrayList.get(position);
        holder.heading.setText(request.getPostName());
        holder.itemDate.setText(request.getCreateDate());
        holder.description.setText(request.getDetail());
        holder.poster.setText(request.getRequesterName());
        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRequestClick(itemArrayList.get(position), v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout itemContainer;
        TextView heading;
        TextView description;
        TextView poster;
        TextView itemDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.tvHeading);
            description = itemView.findViewById(R.id.tvDesc);
            poster = itemView.findViewById(R.id.itemPoster);
            itemDate = itemView.findViewById(R.id.itemDate);
            itemContainer = itemView.findViewById(R.id.item_container);


        }
    }

}
