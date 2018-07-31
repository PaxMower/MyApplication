//package com.casa.myapplication.Adapter;
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.TextView;
//
//import com.casa.myapplication.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ExpandableListAdapter extends BaseExpandableListAdapter {
//
//    private Context context;
//    private List<String> ParentItem;
//    private HashMap<String, List<String>> ChildItem;
//
//    public ExpandableListAdapter(Context context, List<String> ParentItem,
//                                 HashMap<String, List<String>> ChildItem) {
//        this.context = context;
//        this.ParentItem = ParentItem;
//        this.ChildItem = ChildItem;
//    }
//
//    @Override
//    public Object getChild(int listPosition, int expandedListPosition) {
//        return this.ChildItem.get(this.ParentItem.get(listPosition))
//                .get(expandedListPosition);
//    }
//
//    @Override
//    public long getChildId(int listPosition, int expandedListPosition) {
//        return expandedListPosition;
//    }
//
//    @Override
//    public View getChildView(int listPosition, final int expandedListPosition,
//                             boolean isLastChild, View convertView, ViewGroup parent) {
//        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
//        if (convertView == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) this.context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.item_child, null);
//        }
//        TextView text1 = (TextView) convertView.findViewById(R.id.item1);
//        TextView text2 = (TextView) convertView.findViewById(R.id.item2);
//        text1.setText(""+expandedListPosition);
//        text2.setText(""+expandedListText);
//        return convertView;
//    }
//
//    @Override
//    public int getChildrenCount(int listPosition) {
//        return this.ChildItem.get(this.ParentItem.get(listPosition))
//                .size();
//    }
//
//    @Override
//    public Object getGroup(int listPosition) {
//        return this.ParentItem.get(listPosition);
//    }
//
//    @Override
//    public int getGroupCount() {
//        return this.ParentItem.size();
//    }
//
//    @Override
//    public long getGroupId(int listPosition) {
//        return listPosition;
//    }
//
//    @Override
//    public View getGroupView(int listPosition, boolean isExpanded,
//                             View convertView, ViewGroup parent) {
//        String listTitle = (String) getGroup(listPosition);
//        if (convertView == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) this.context.
//                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.item_parent, null);
//        }
//        TextView listTitleTextView = (TextView) convertView
//                .findViewById(R.id.listTitle);
//        listTitleTextView.setTypeface(null, Typeface.BOLD);
//        listTitleTextView.setText(listTitle);
//        return convertView;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
//        return true;
//    }
//
//
//    public static HashMap<String, List<String>> getData() {
//        final HashMap<String, List<String>> ParentItem = new HashMap<String, List<String>>();
//
//        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
//        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//        String userID = user.getUid();
//
//        /*List<String> Colors = new ArrayList<String>();
//        Colors.add("Red");
//        Colors.add("Green");
//        Colors.add("Blue");
//        Colors.add("Maroon");
//        Colors.add("Yellow");
//        Colors.add("Violet");
//        Colors.add("Pink");
//
//        List<String> Animals = new ArrayList<String>();
//        Animals.add("Lion");
//        Animals.add("Tiger");
//        Animals.add("Leopard");
//        Animals.add("Cheetah");
//        Animals.add("Bear");
//
//        List<String> Sports = new ArrayList<String>();
//        Sports.add("Cricket");
//        Sports.add("Football");
//        Sports.add("Tennis");
//        Sports.add("Basket Ball");
//        Sports.add("Base Ball");
//
//
//        ParentItem.put("Colors", Colors);
//        ParentItem.put("Animals", Animals);
//        ParentItem.put("Sports", Sports);*/
//
//
//        DatabaseReference mDatabase = mFirebaseDatabase.getReference().child("Users").child(userID).child("Orders");
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                //collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
//                for(DataSnapshot ds1 : dataSnapshot.getChildren()){
//                    Log.v("FIREBASE__1", ds1.toString());
//                    for (DataSnapshot ds2 : ds1.getChildren()){
//                        Log.v("FIREBASE__2", ds2.toString());
//                        for (DataSnapshot ds3 : ds2.getChildren()){
//                            Log.v("FIREBASE__3", ds3.toString());
//                            Log.v("FIREBASE__VALUE", ds3.getValue().toString());
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//        return ParentItem;
//
//    }
//
//    private static void collectPhoneNumbers(Map<String, Object> ordersDates) {
//
//        Log.v("FIREBASE__1", ordersDates.toString());
//
///*        ArrayList<String> phoneNumbers = new ArrayList<String>();
//
//        //iterate through each user, ignoring their UID
//        for (Map.Entry<String, Object> entry : ordersDates.entrySet()){
//
//            //Get user map
//            Map singleUser = (Map) entry.getValue();
//            //Get phone field and append to list
//            phoneNumbers.add((String) singleUser.get("minute"));
//        }
//
//        System.out.println(phoneNumbers.toString());*/
//    }
//}