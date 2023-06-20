package com.ui.review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ViewHolder> {

    private final List<ReviewData> mValues;

    public MyReviewAdapter(List<ReviewData> items) {
        this.mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_review, parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.reviewName.setText(mValues.get(position).name);
        holder.reviewDescription.setText(mValues.get(position).description);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView reviewName;
        public final TextView reviewDescription;

        //public final RecyclerView.Adapter adapter;
        public ReviewData mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewName = itemView.findViewById(R.id.reviewName);
            reviewDescription = itemView.findViewById(R.id.reviewDescription);

        }

    }
}
