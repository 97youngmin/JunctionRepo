package com.example.junctionxasia;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListItem> listItems;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.textViewTitle.setText(listItem.getName());
        holder.textViewCompany.setText(listItem.getCompany());
        holder.textViewQuantity.setText(listItem.getQuantity().toString());
        holder.textViewPrice.setText(listItem.getPrice().toString());
        holder.textViewExpiry.setText(listItem.getExpiry());
        Picasso.get()
                .load(listItem.getImage())
                .into(holder.imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewCompany;
        public TextView textViewQuantity;
        public TextView textViewPrice;
        public TextView textViewExpiry;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewCompany = (TextView) itemView.findViewById(R.id.textViewCompany);
            textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuantity);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            textViewExpiry = (TextView) itemView.findViewById(R.id.textViewExpiry);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}