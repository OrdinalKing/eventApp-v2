package com.ui.myEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentMyeventBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.ui.placeholder.EventData;
import com.ui.placeholder.PlaceholderContent;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMyEventRecyclerViewAdapter extends RecyclerView.Adapter<MyMyEventRecyclerViewAdapter.ViewHolder> {

    private final List<EventData> mValues;
    private final Context context;
    private OnClickListener onClickListener;

    public MyMyEventRecyclerViewAdapter(Context context, List<EventData> items) {

        this.mValues = items;
        this.context = context;
    }

    public interface OnClickListener {
        void onClick(EventData eventData);
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_myevent, parent, false));
    }

    @Override
    @NonNull
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mItems = mValues;
        holder.mIdView.setText(mValues.get(position).name);
        holder.mContentView.setText(mValues.get(position).details);
        Picasso.get().load(mValues.get(position).imageUrl).into(holder.eventImageSrc);
        holder.locationView.setText(mValues.get(position).location);
        Date newdate = mValues.get(position).getDate().toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US);
        String formattedDate = sdf.format(newdate);
        holder.dateView.setText(formattedDate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.mItems.get(position));
                }
            }
        });
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mIdView;
        public final TextView mContentView;

        public final ImageView eventImageSrc;
        public final Button deleteButton;
        public final Button shareButton1;
        public final TextView locationView;
        public final TextView dateView;

        public List<EventData> mItems;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIdView = itemView.findViewById(R.id.eventHeadline);
            mContentView = itemView.findViewById(R.id.eventDetails);
            eventImageSrc = itemView.findViewById(R.id.eventImageSrc);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            shareButton1 = itemView.findViewById(R.id.shareButton1);
            locationView = itemView.findViewById(R.id.location);
            dateView = itemView.findViewById(R.id.date);
            deleteButton.setOnClickListener(this);
            shareButton1.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v == deleteButton) {
                int position = getAdapterPosition();
                Log.d("tag-recyclerview", "--------------------Here I am ---------------------");
                EventData mItem = mItems.get(position);
                mItems.remove(position);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                ArrayList<String> eventlist = new ArrayList<>();
                for(EventData eventdata : mItems)
                {
                    eventlist.add(eventdata.getDocId());
                }
                db.collection("myevents").document(mItem.getUserId()).update("eventlist", eventlist).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("firebase","DocumentSnapshot successfully updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("firebase", "Error updating document", e);
                    }
                });
                MyEventFragment.adapter.notifyDataSetChanged();
            }
            if (v == shareButton1) {
                Log.d("tag","I am here----------------------");
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                View sheetview = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.bottom_sheet_layout, null);
                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();
                sheetview.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.hide();
                    }
                });
                sheetview.findViewById(R.id.whatsapp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String url = "https://www.whatsapp.com/";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            // Handle the exception here
                            Log.d("Whatsapp", "----ERROR-------");
                        }
                    }
                });
                sheetview.findViewById(R.id.instagram).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String url = "https://www.instagram.com/";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            // Handle the exception here
                            Log.d("Whatsapp", "----ERROR-------");
                        }
                    }
                });
                sheetview.findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String url = "https://www.facebook.com/";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            // Handle the exception here
                            Log.d("Whatsapp", "----ERROR-------");
                        }
                    }
                });
                sheetview.findViewById(R.id.gmail).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String url = "https://mail.google.com/";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            // Handle the exception here
                            Log.d("Whatsapp", "----ERROR-------");
                        }
                    }
                });
                sheetview.findViewById(R.id.message).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String url = "https://messages.google.com/web";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            // Handle the exception here
                            Log.d("Whatsapp", "----ERROR-------");
                        }
                    }
                });
            }
        }

    }
}