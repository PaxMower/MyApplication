package com.casa.myapplication.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.casa.myapplication.Model.Maintenance;
import com.casa.myapplication.R;

import java.util.List;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.MaintenanceViewHolder>{

    private List<Maintenance> list;

    public MaintenanceAdapter(List<Maintenance> list) {
        this.list = list;
    }

    @Override
    public MaintenanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MaintenanceViewHolder(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.item_maintenance, parent, false));
    }

    @Override
    public void onBindViewHolder(MaintenanceViewHolder holder, int position) {
        Maintenance maintenance = list.get(position);
        holder.mTruckNum.setText(maintenance.getTruckNum());
        holder.mTruckId.setText(maintenance.getTruckId());
        holder.mKm.setText(maintenance.getKm());
        holder.mDate.setText(maintenance.getDate());
        holder.mType.setText(maintenance.getType());
        holder.mComment.setText(maintenance.getComments());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MaintenanceViewHolder extends RecyclerView.ViewHolder{

        TextView mType, mTruckNum, mKm, mComment, mTruckId, mDate;

        public MaintenanceViewHolder(View itemView) {
            super(itemView);
            mTruckId = (TextView) itemView.findViewById(R.id.watch_maintenance_truckId);
            mTruckNum = (TextView) itemView.findViewById(R.id.watch_maintenance_truck_number);
            mKm = (TextView) itemView.findViewById(R.id.watch_maintenance_km);
            mDate = (TextView) itemView.findViewById(R.id.watch_maintenance_date);
            mType = (TextView) itemView.findViewById(R.id.watch_maintenance_type);
            mComment = (TextView) itemView.findViewById(R.id.watch_maintenance_comments);
        }
    }

}
