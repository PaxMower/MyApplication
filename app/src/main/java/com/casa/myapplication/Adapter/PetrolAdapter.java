package com.casa.myapplication.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.casa.myapplication.Logic.Petrol;
import com.casa.myapplication.R;

import java.util.List;

public class PetrolAdapter extends RecyclerView.Adapter<PetrolAdapter.PetrolViewHolder>{

    private List<Petrol> list;

    public PetrolAdapter(List<Petrol> list) {
        this.list = list;
    }

    @Override
    public PetrolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PetrolAdapter.PetrolViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_petrol, parent, false));
    }

    @Override
    public void onBindViewHolder(PetrolViewHolder holder, int position) {
        Petrol petrol = list.get(position);
        holder.mDate.setText(petrol.getDate());
        holder.mHour.setText(petrol.getHour());
        holder.mTruckId.setText(petrol.getTruckId());
        holder.mTruckNum.setText(petrol.getTruckNumber());
        holder.mKm.setText(petrol.getKm());
        holder.mLiters.setText(petrol.getLiters());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PetrolViewHolder extends RecyclerView.ViewHolder{

        TextView mLiters, mTruckNum, mKm, mHour, mTruckId, mDate;

        public PetrolViewHolder(View itemView) {
            super(itemView);
            mLiters = (TextView) itemView.findViewById(R.id.watch_petrol_liters);
            mTruckNum = (TextView) itemView.findViewById(R.id.watch_petrol_truckNum);
            mTruckId = (TextView) itemView.findViewById(R.id.watch_petrol_truckId);
            mKm = (TextView) itemView.findViewById(R.id.watch_petrol_km);
            mHour = (TextView) itemView.findViewById(R.id.watch_petrol_hour);
            mDate = (TextView) itemView.findViewById(R.id.watch_petrol_date);
        }
    }
}
