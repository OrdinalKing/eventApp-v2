package com.ui.myEvent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.EventDetailActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ui.placeholder.EventData;
import com.ui.placeholder.PlaceholderContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MyEventFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    public static MyMyEventRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyEventFragment() {
    }
    /*
    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyEventFragment newInstance(int columnCount) {
        MyEventFragment fragment = new MyEventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }
    */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myevent_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        List<EventData> ITEMS = new ArrayList<>();
        db.collection("myevents").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            ArrayList<String> eventlist;
            if (documentSnapshot.exists()) {
                eventlist = (ArrayList<String>) documentSnapshot.get("eventlist");

                for(String event : eventlist) {
                    db.collection("events").document(event).get().addOnSuccessListener(documentSnapshot1 -> {
                        String title = documentSnapshot1.getString("title");
                        String description = documentSnapshot1.getString("description");
                        String imageUrl = documentSnapshot1.getString("image");
                        String location = documentSnapshot1.getString("location");
                        Timestamp date = documentSnapshot1.getTimestamp("date");
                        ITEMS.add(new EventData(Integer.toString(ITEMS.size()), title, description, imageUrl,location, date, event, userId, false));

                       ///  Last event  ///
                                if (ITEMS.size() == eventlist.size())
                                {
                                    adapter = new MyMyEventRecyclerViewAdapter(view.getContext(), ITEMS);
                                    recyclerView.setAdapter(adapter);
                                    adapter.setOnClickListener(eventData -> {
                                        EventDetailActivity.eventId = eventData.getDocId();
                                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                                        startActivity(intent);
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("firebase","Error getting document", e);
                            }
                        });

                    }
                }
                else{
                    adapter = new MyMyEventRecyclerViewAdapter(view.getContext(), ITEMS);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnClickListener(new MyMyEventRecyclerViewAdapter.OnClickListener() {
                        @Override
                        public void onClick(EventData eventData) {
                            EventDetailActivity.eventId = eventData.getDocId();
                            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            });

    /*
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyMyEventRecyclerViewAdapter(PlaceholderContent.ITEMS));
        }

     */
        return view;
    }
}