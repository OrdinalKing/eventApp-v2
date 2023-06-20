package com.ui.event;


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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.LoginActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.ui.placeholder.EventData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyEventRecyclerViewAdapter extends RecyclerView.Adapter<MyEventRecyclerViewAdapter.ViewHolder>  {

    private final List<EventData> mValues;
    private Context context;
    private OnClickListener onClickListener;

    public MyEventRecyclerViewAdapter(Context context, List<EventData> items) {
        this.mValues = items;
        this.context = context;
    }

    public interface OnClickListener {
        void onClick(EventData eventData);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event, parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.eventHeadline.setText(mValues.get(position).name);
        holder.eventDetails.setText(mValues.get(position).details);
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
                    onClickListener.onClick(holder.mItem);
                }
            }
        });

        if (LoginActivity.isGuestMode){
            holder.shareImage.setVisibility(View.GONE);
            holder.shareButton.setVisibility(View.GONE);
        }
        else {
            if (holder.mItem.getShare()){
                holder.shareImage.setVisibility(View.VISIBLE);
                holder.shareButton.setVisibility(View.GONE);
            } else {
                holder.shareButton.setVisibility(View.VISIBLE);
                holder.shareImage.setVisibility(View.GONE);
            }
        }
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView eventHeadline;
        public final TextView eventDetails;
        public final ImageView eventImageSrc;
        public final Button shareButton;

        public final Button shareButton1;
        public final ImageView shareImage;
        public final TextView locationView;
        public final TextView dateView;

        //public final RecyclerView.Adapter adapter;
        public EventData mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //adapter = ((RecyclerView) itemView.getParent()).getAdapter();

            eventHeadline = itemView.findViewById(R.id.eventHeadline);
            eventDetails = itemView.findViewById(R.id.eventDetails);
            eventImageSrc = itemView.findViewById(R.id.eventImageSrc);
            shareButton = itemView.findViewById(R.id.shareButton);
            shareImage = itemView.findViewById(R.id.shareImage);
            shareButton1 = itemView.findViewById(R.id.shareButton1);
            locationView = itemView.findViewById(R.id.location);
            dateView = itemView.findViewById(R.id.date);

            shareButton.setOnClickListener(this);
            shareButton1.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == shareButton) {
                int position = getAdapterPosition();
                // Do something with this position, like remove the item from the list
                Log.d("tag-recyclerview", "--------------------Here I am ---------------------");
                String userId = mItem.getUserId();
                String docId = mItem.getDocId();
                Boolean share = mItem.getShare();
                if (!share)
                {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("myevents").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String> eventlist;
                            if (documentSnapshot.exists()) {
                                eventlist = (ArrayList<String>) documentSnapshot.get("eventlist");
                                assert eventlist != null;
                                eventlist.add(docId);
                                db.collection("myevents").document(userId).update("eventlist", eventlist).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            }
                            else{
                                eventlist = new ArrayList<>();
                                eventlist.add(docId);
                                Map<String, Object> eventData = new HashMap<>();
                                eventData.put("eventlist", eventlist);
                                db.collection("myevents").document(userId).set(eventData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("firebase","DocumentSnapshot successfully written!");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("firebase", "Error writing document", e);
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("firebase","Error getting document", e);
                        }
                    });
                    mItem.setShare(true);
                    EventFragment.adapter.notifyItemChanged(position);
                }
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
        @Override
        public String toString() {
            return super.toString() + " '" + eventDetails.getText() + "'";
        }
    }
}