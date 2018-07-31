package com.casa.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.casa.myapplication.Logic.Order;
import com.casa.myapplication.R;

import java.util.HashMap;
import java.util.List;

public class CustomExpandableListView extends BaseExpandableListAdapter {

    Context context;
    List<String> listHeader;
    HashMap<String, List<Order>> listChild;

    public CustomExpandableListView(Context context, List<String> listHeader, HashMap<String, List<Order>> listChild) {
        this.context = context;
        this.listHeader = listHeader;
        this.listChild = listChild;
    }

//    public CustomExpandableListView(WatchOrdersActivity context, List<String> listDataHeader, HashMap<String, List<Order>> listDataChild) {
//    }


    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String header = (String) getGroup(groupPosition);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_parent, null);
        TextView textHeader = (TextView) convertView.findViewById(R.id.headerText);
        //TextView textHeader2 = (TextView) convertView.findViewById(R.id.headerText2);
        textHeader.setText(header);
        //textHeader2.setText(header);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Order item = (Order) getChild(groupPosition, childPosition);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_child, null);

        TextView date = (TextView) convertView.findViewById(R.id.dateRetrieve);
        TextView truckId = (TextView) convertView.findViewById(R.id.truckIdRetrieve);
        TextView truckNum = (TextView) convertView.findViewById(R.id.truckNumRetrieve);
        TextView platformId = (TextView) convertView.findViewById(R.id.platformIdRetrieve);
        TextView containerId = (TextView) convertView.findViewById(R.id.containerIdRetrieve);
        TextView charger = (TextView) convertView.findViewById(R.id.chargerRetrieve);
        TextView client = (TextView) convertView.findViewById(R.id.clientRetrieve);
        TextView address = (TextView) convertView.findViewById(R.id.addressRetrieve);
        TextView city = (TextView) convertView.findViewById(R.id.cityRetrieve);
        TextView state = (TextView) convertView.findViewById(R.id.stateRetrieve);
        TextView price = (TextView) convertView.findViewById(R.id.priceRetrieve);
        TextView apertureHour = (TextView) convertView.findViewById(R.id.apertureHourRetrieve);
        TextView phone = (TextView) convertView.findViewById(R.id.phoneRetrieve);
        TextView arrivalHour = (TextView) convertView.findViewById(R.id.arrivalHourRetrieve);
        TextView departureHour = (TextView) convertView.findViewById(R.id.departureHourRetrieve);
        TextView ccd= (TextView) convertView.findViewById(R.id.containerChargeDayRetrieve);
        TextView cch = (TextView) convertView.findViewById(R.id.containerChargeHourRetrieve);
        TextView cdd = (TextView) convertView.findViewById(R.id.containerDischargeDayRetrieve);
        TextView cdh = (TextView) convertView.findViewById(R.id.containerDischargeHourRetrieve);
        TextView observations = (TextView) convertView.findViewById(R.id.observationsRetrieve);

        date.setText(item.getDate());
        truckId.setText(item.getTruckID());
        truckNum.setText(item.getTruckNumber());
        platformId.setText(item.getPlatformID());
        containerId.setText(item.getContainerNumber());
        charger.setText(item.getCharger());
        client.setText(item.getClient());
        address.setText(item.getAddress());
        apertureHour.setText(item.getApertureHour());
        phone.setText(item.getPhone());
        arrivalHour.setText(item.getArrivalHour());
        departureHour.setText(item.getDepartureHour());
        ccd.setText(item.getContainerChargeDay());
        cch.setText(item.getContainerChargeHour());
        cdd.setText(item.getContainerDischargeDay());
        cdh.setText(item.getContainerDischargeHour());
        observations.setText(item.getTextArea());
        city.setText(item.getCity());
        state.setText(item.getState());
        price.setText(item.getPrice());


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
