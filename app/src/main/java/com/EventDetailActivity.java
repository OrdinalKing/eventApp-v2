package com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.ui.review.MyReviewAdapter;
import com.ui.review.ReviewData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventDetailActivity extends AppCompatActivity {
    public static String eventId;
    public RecyclerView reviewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Button goBack = findViewById(R.id.back);
        reviewList = findViewById(R.id.list);
        Button reviewButton = findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailActivity.this, EventReviewActivity.class);
                startActivity(intent);
            }
        });
        if (LoginActivity.isGuestMode == true)
            reviewButton.setVisibility(View.GONE);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        db.collection("reviews").document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String userId;
                    if (LoginActivity.isGuestMode == true)
                        userId="";
                    else
                        userId = mAuth.getCurrentUser().getUid();

                    Boolean isExist = false;
                    HashMap<String, Object> reviewDatas = (HashMap<String, Object>) documentSnapshot.getData();
                    List<ReviewData> ITEMS = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : reviewDatas.entrySet()) {
                        String key = entry.getKey();
                        if (key.equals(userId)) isExist = true;
                        ArrayList<String> reviewData = (ArrayList<String>)entry.getValue();
                        Log.d("Tag",key);
                        Log.d("Tag",reviewData.get(0));
                        Log.d("Tag",reviewData.get(1));
                        ITEMS.add(new ReviewData(reviewData.get(0), reviewData.get(1)));
                    }

                    reviewList.setAdapter(new MyReviewAdapter(ITEMS));
                    if (isExist == true)
                        reviewButton.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("firebase", "Error getting document", e);
            }
        });
        db.collection("events").document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String title = documentSnapshot.getString("title");
                String description = documentSnapshot.getString("description");
                String imageUrl = documentSnapshot.getString("image");
                String hours = documentSnapshot.getString("hours");
                String location = documentSnapshot.getString("location");
                Timestamp date = documentSnapshot.getTimestamp("date");
                TextView titleView = findViewById(R.id.eventHeadline);
                TextView detailView = findViewById(R.id.eventDetails);
                TextView dateView = findViewById(R.id.date);
                TextView hoursView = findViewById(R.id.hours);
                TextView locationView = findViewById(R.id.location);

                ImageView eventImage = findViewById(R.id.eventImageSrc);
                if (date != null) {
                    Date newdate = date.toDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US);
                    String formattedDate = sdf.format(newdate);
                    dateView.setText(formattedDate);
                    long timestampMillis = newdate.getTime();
                    long currentMillis = System.currentTimeMillis();

                    if (currentMillis < timestampMillis) {
                        reviewButton.setVisibility(View.GONE);
                    } else {

                        // do something else
                    }
                }
                titleView.setText(title);
                detailView.setText(description);
                locationView.setText(location);

                hoursView.setText(hours + " hours");
                Picasso.get().load(imageUrl).into(eventImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("firebase", "Error getting document", e);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}