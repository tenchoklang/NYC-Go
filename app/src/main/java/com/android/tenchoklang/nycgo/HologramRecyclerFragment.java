package com.android.tenchoklang.nycgo;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HologramRecyclerFragment extends Fragment implements RecyclerItemClickListener.OnRecyclerClickListener {

    private final static String MY_GLOBAL_PREFS = "my_global_prefs";
    private final static String SAVED_LOC_KEY = "saved_loc_key";
    RecyclerView recyclerView;
    private HologramRecyclerViewAdapter recyclerViewAdapter;

    LinearLayoutManager linearLayoutManager;

    private static final String TAG = "HologramRecyclerFragmen";

    public HologramRecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_hologram_recycler, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.hologramRecyclerView);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, this));


        //get the data from shared prefs
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(SAVED_LOC_KEY, "");
        Type type = new TypeToken<List<Hologram>>(){}.getType();
        //retrieve the arraylist from JSON given the type
        List<Hologram> savedLoc= gson.fromJson(json, type);

        recyclerViewAdapter = new HologramRecyclerViewAdapter(savedLoc, getContext());
        recyclerView.setAdapter(recyclerViewAdapter);

        return rootView;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onItemDoubleClick(View view, int position) {
        Log.d(TAG, "onItemDoubleClick: ");
        Intent intent = new Intent(getContext(), HologramActivity.class);
        intent.putExtra("PHOTO", recyclerViewAdapter.getLocation(position).getPhotoUrl());
        startActivity(intent);
    }
}
