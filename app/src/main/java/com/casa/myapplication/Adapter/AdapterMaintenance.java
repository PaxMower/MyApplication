package com.casa.myapplication.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.casa.myapplication.Logic.Maintenance;
import com.casa.myapplication.R;

import java.util.List;

public class AdapterMaintenance extends RecyclerView.Adapter<AdapterMaintenance.MaintenanceViewHolder>{

    List<Maintenance> maintenanceList;

    public AdapterMaintenance(List<Maintenance> maintenanceList) {
        this.maintenanceList = maintenanceList;
    }

    @Override
    public MaintenanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maintenance, parent, false);
        MaintenanceViewHolder holder = new MaintenanceViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MaintenanceViewHolder holder, int position) {
        Maintenance maint = maintenanceList.get(position);
        holder.tType.setText(maint.getType());
    }

    @Override
    public int getItemCount() {
        return maintenanceList.size();
    }

    public static class MaintenanceViewHolder extends RecyclerView.ViewHolder{

        TextView tType;

        public MaintenanceViewHolder(View itemView) {
            super(itemView);
            tType = (TextView) itemView.findViewById(R.id.id_item_recycler);
        }
    }

}
