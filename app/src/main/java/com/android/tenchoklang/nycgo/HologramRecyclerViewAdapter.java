package com.android.tenchoklang.nycgo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tench on 5/6/2018.
 */

public class HologramRecyclerViewAdapter extends RecyclerView.Adapter<HologramRecyclerViewAdapter.SavedLocViewHolder> {

    public List<Hologram> hologramList;//crime data class can also be used as a users saved location
    private Context context;

    public HologramRecyclerViewAdapter(List<Hologram> hologramList, Context context) {
        this.hologramList = hologramList;
        this.context = context;
    }

    static class SavedLocViewHolder extends RecyclerView.ViewHolder{

        ImageView hologramImage;

        public SavedLocViewHolder(View itemView){//itemView is the inflated Layout
            super(itemView);
            hologramImage = itemView.findViewById(R.id.hologramImage);
        }
    }

    @Override
    public SavedLocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_for_hologram_recycler,parent,false);
        return new SavedLocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedLocViewHolder holder, int position) {
        Hologram hologram = hologramList.get(position);

        Picasso.with(context).load(hologram.getPhotoUrl()).into(holder.hologramImage);

    }

    @Override
    public int getItemCount() {
        return (hologramList != null && hologramList.size() !=0) ? hologramList.size(): 0;
    }


    //adds the data to the recycler view
    public void loadNewData(List<Hologram> locationData) {
        this.hologramList = locationData;
        notifyDataSetChanged();//notifies the recycler that the data has changed
    }

    public Hologram getLocation(int position){
        return ((hologramList != null) && (hologramList.size() != 0) ? hologramList.get(position) : null);
    }


}
