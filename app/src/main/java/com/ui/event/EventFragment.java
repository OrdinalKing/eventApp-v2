package com.ui.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.EventDetailActivity;
import com.LoginActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ui.placeholder.EventData;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class EventFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    List<EventData> ITEMS = new ArrayList<>();

    public static MyEventRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventFragment newInstance(int columnCount) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

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

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_event_list);
        SearchView searchView = (SearchView) view.findViewById(R.id.search);
        searchView.clearFocus();
        Context context = view.getContext();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText, recyclerView, context);
                return true;
            }
        });
        // Set the adapter
        if (recyclerView != null) {
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference eventsRef = db.collection("events");

            eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                int id = 1;
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Log.d("----tag----", document.getId());
                    String title = document.getString("title");
                    String description = document.getString("description");
                    String imageUrl = document.getString("image");
                    String location = document.getString("location");
                    Timestamp date = document.getTimestamp("date");
                    if (LoginActivity.isGuestMode)
                        ITEMS.add(new EventData(Integer.toString(id), title, description, imageUrl, location, date, document.getId(), "", false));
                    else {
                        ITEMS.add(new EventData(Integer.toString(id), title, description, imageUrl, location, date, document.getId(), mAuth.getCurrentUser().getUid(), false));
                    }
                    id = id + 1;
                }
                if (!LoginActivity.isGuestMode) {
                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                    db1.collection("myevents").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            ArrayList<String> eventlist = (ArrayList<String>) documentSnapshot.get("eventlist");

                            for (String event : eventlist) {
                                Log.d("firebase----", event);
                                for (EventData Item : ITEMS) {
                                    Log.d("firebase----", Item.getDocId());
                                    if (event.equals(Item.getDocId())) {
                                        Item.setShare(true);
                                        Log.d("firebase----", Item.getName());
                                    }
                                }

                            }
                        }
                        adapter = new MyEventRecyclerViewAdapter(context, ITEMS);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnClickListener(eventData -> {
                            EventDetailActivity.eventId = eventData.getDocId();
                            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                            startActivity(intent);
                        });
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("firebase", "Error getting document", e);
                        }
                    });
                } else {
                    adapter = new MyEventRecyclerViewAdapter(context, ITEMS);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnClickListener(new MyEventRecyclerViewAdapter.OnClickListener() {
                        @Override
                        public void onClick(EventData eventData) {
                            EventDetailActivity.eventId = eventData.getDocId();
                            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                            startActivity(intent);
                        }
                    });
                }

            });
        }
        return view;
    }

    private void filterList(String newText, RecyclerView recyclerView, Context context) {
        List<EventData> filteredList = new ArrayList<>();
        for(EventData item: ITEMS) {
            if(item.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter = new MyEventRecyclerViewAdapter(context, filteredList);
        recyclerView.setAdapter(adapter);
    }
}