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

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<FoodPost> itemArrayList;
    private ItemSelectListener listener;

    public ItemListAdapter(Context context, ArrayList<FoodPost> itemArrayList, ItemSelectListener listener) {
        this.context = context;
        this.itemArrayList = itemArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.food_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        FoodPost item = itemArrayList.get(position);
        holder.heading.setText(item.getPostTitle());
        holder.desc.setText(item.getDetails());
        holder.date.setText(item.getCreatedate());
        holder.user.setText(item.getPosterUsername());
        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(itemArrayList.get(position), v);
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
        TextView desc;
        TextView date;
        TextView user;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.tvHeading);
            desc = itemView.findViewById(R.id.tvDesc);
            itemContainer = itemView.findViewById(R.id.item_container);
            date = itemView.findViewById(R.id.itemDate);
            user = itemView.findViewById(R.id.itemPoster);


        }
    }

}
