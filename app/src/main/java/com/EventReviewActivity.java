package com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class EventReviewActivity extends AppCompatActivity {
    Button backButton;
    Button saveButton;
    EditText reviewDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_review);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
        reviewDescription = findViewById(R.id.reviewDescription);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docId = EventDetailActivity.eventId;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String userId = mAuth.getCurrentUser().getUid();
                db.collection("reviews").document(docId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            HashMap<String, Object> reviewData = (HashMap<String, Object>) documentSnapshot.getData();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("username");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.getValue().toString();
                                    ArrayList<String> review = new ArrayList<>();
                                    review.add(name);
                                    review.add(reviewDescription.getText().toString());
                                    reviewData.put(userId, review);
                                    db.collection("reviews").document(docId).update(reviewData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("firebase","DocumentSnapshot successfully updated!");
                                            savePressed();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("firebase", "Error updating document", e);
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else
                        {
                            HashMap<String, Object> reviewData = new HashMap<> ();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("username");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.getValue().toString();
                                    ArrayList<String> review = new ArrayList<>();
                                    review.add(name);
                                    review.add(reviewDescription.getText().toString());
                                    reviewData.put(userId, review);
                                    db.collection("reviews").document(docId).set(reviewData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            savePressed();
                                            Log.d("firebase","DocumentSnapshot successfully written!");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("firebase", "Error writing document", e);
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("firebase", "Error getting document", e);
                    }
                });
            }
        });
    }
    public void savePressed() {
        startActivity(new Intent(EventReviewActivity.this, EventDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}