package com.example.quang.project_sdo.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quang.project_sdo.Models.OrderModel;
import com.example.quang.project_sdo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShoppingCartAdapter extends ArrayAdapter<OrderModel> {

    AppCompatActivity context;
    int layout;
    ArrayList<OrderModel> listShoppingCart = new ArrayList<OrderModel>();
    int dem, dem2;
    int soLuong = 0;
    DatabaseReference root;
    FirebaseAuth mAuth;
    ArrayList<String> keys = new ArrayList<String>();
    int sum, minus;
    int totalPrice;
    OrderModel orderModel;

    public ShoppingCartAdapter(@NonNull AppCompatActivity context, int resource, @NonNull ArrayList<OrderModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.listShoppingCart = objects;
    }

    public class ViewHolder {
        TextView drugName;
        TextView drugPrice;
        TextView drugAmount;
        ImageView drugImage;
        Button btnDecrease;
        Button btnIncrease;
        CheckBox chkSelected;

    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference("Order").child(mAuth.getUid());
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                orderModel = dataSnapshot.getValue(OrderModel.class);
                String key = dataSnapshot.getKey();
                keys.add(key);
                listShoppingCart.get(position).getKey().equalsIgnoreCase(keys + "");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.drugName = (TextView) convertView.findViewById(R.id.txtShoppingCartDrugName);
            viewHolder.drugPrice = (TextView) convertView.findViewById(R.id.txtShoppingCartPrice);
            viewHolder.drugAmount = (TextView) convertView.findViewById(R.id.txtPriceShoppingCart);
            viewHolder.drugImage = (ImageView) convertView.findViewById(R.id.imgShoppingCartDrug);
            viewHolder.btnDecrease = (Button) convertView.findViewById(R.id.btnDecrease);
            viewHolder.btnIncrease = (Button) convertView.findViewById(R.id.btnIncrease);
            viewHolder.chkSelected = (CheckBox) convertView.findViewById(R.id.chkSelected);
            convertView.setTag(viewHolder);

            if (!listShoppingCart.get(position).isChecked()) {
                totalPrice += listShoppingCart.get(position).getGia();
                for (int i = 0; i < listShoppingCart.size(); i++) {
                    root.child(listShoppingCart.get(i).getKey()).child("tongtien").setValue(totalPrice);
                }

            }


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.drugName.setText(listShoppingCart.get(position).getTen());
        viewHolder.drugPrice.setText(Integer.toString(listShoppingCart.get(position).getGia()));
        viewHolder.drugAmount.setText(Integer.toString(listShoppingCart.get(position).getSoLuong()));
        Picasso.get().load(listShoppingCart.get(position).getHinh()).into(viewHolder.drugImage);


        if (listShoppingCart.get(position).isChecked()) {
            viewHolder.chkSelected.setChecked(true);
        } else {
            viewHolder.chkSelected.setChecked(false);
        }
        if (listShoppingCart.get(position).getSoLuong() <= 1) {
            viewHolder.btnDecrease.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.btnDecrease.setVisibility(View.VISIBLE);
        }
        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.chkSelected.isChecked()) {
                    viewHolder.btnIncrease.setClickable(true);
                    viewHolder.btnDecrease.setClickable(true);
                    listShoppingCart.get(position).setChecked(true);
                    viewHolder.btnIncrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dem = listShoppingCart.get(position).getSoLuong();
                            dem++;
                            viewHolder.drugAmount.setText(Integer.toString(dem));
                            int defaultPrice = listShoppingCart.get(position).getGiagoc();
                            sum = dem * defaultPrice;
                            viewHolder.drugPrice.setText(sum + "");
                            listShoppingCart.get(position).setSoLuong(dem);
                            if (dem >= 10) {
                                viewHolder.btnIncrease.setVisibility(View.INVISIBLE);
                            } else if (dem > 1) {
                                viewHolder.btnDecrease.setVisibility(View.VISIBLE);
                                viewHolder.btnIncrease.setVisibility(View.VISIBLE);
                            } else if (dem <= 1) {
                                viewHolder.btnDecrease.setVisibility(View.INVISIBLE);
                            }
                            root.child(listShoppingCart.get(position).getKey()).child("gia").setValue(sum);
                            root.child(listShoppingCart.get(position).getKey()).child("soLuong").setValue(dem);
                            totalPrice += listShoppingCart.get(position).getGia();
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                root.child(listShoppingCart.get(i).getKey()).child("tongtien").setValue(totalPrice);
                            }
                            Toast.makeText(getContext(), "Tổng số tiền " + totalPrice, Toast.LENGTH_SHORT).show();

                        }
                    });
                    viewHolder.btnDecrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dem2 = listShoppingCart.get(position).getSoLuong();
                            dem2--;
                            viewHolder.drugAmount.setText(Integer.toString(dem2));
                            int defaultPrice = listShoppingCart.get(position).getGiagoc();
                            minus = dem2 * defaultPrice;
                            viewHolder.drugPrice.setText(minus + "");
                            listShoppingCart.get(position).setSoLuong(dem2);
                            if (dem2 >= 10) {
                                viewHolder.btnIncrease.setVisibility(View.INVISIBLE);
                            } else if (dem2 > 1) {
                                viewHolder.btnDecrease.setVisibility(View.VISIBLE);
                                viewHolder.btnIncrease.setVisibility(View.VISIBLE);
                            } else if (dem2 <= 1) {
                                viewHolder.btnDecrease.setVisibility(View.INVISIBLE);
                            }
                            root.child(listShoppingCart.get(position).getKey()).child("gia").setValue(minus);
                            root.child(listShoppingCart.get(position).getKey()).child("soLuong").setValue(dem2);
                            totalPrice -= listShoppingCart.get(position).getGia();
                            for (int i = 0; i < listShoppingCart.size(); i++) {
                                root.child(listShoppingCart.get(i).getKey()).child("tongtien").setValue(totalPrice);
                            }
                            Toast.makeText(getContext(), "Tổng số tiền " + totalPrice, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    listShoppingCart.get(position).setChecked(false);
                    viewHolder.btnIncrease.setClickable(false);
                    viewHolder.btnDecrease.setClickable(false);

                }
            }
        });


        return convertView;

    }


}
