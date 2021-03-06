package com.example.quang.project_sdo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.quang.project_sdo.Adapters.HomeListDrugAdapter;
import com.example.quang.project_sdo.Models.ListDrugForHomeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Quang on 3/11/2018.
 */

public class HomeFragment extends Fragment {
    ListView listView;
    ArrayList<ListDrugForHomeModel> listHomeDrug = new ArrayList<ListDrugForHomeModel>();
    HomeListDrugAdapter adapter = null;
    FirebaseAuth mAuth;
    DatabaseReference root;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.home_layout, container, false);

        //Ini firebase
        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference("Article");


        setHasOptionsMenu(true);
        //ListView
        listView = (ListView) view.findViewById(R.id.listViewHome);
        adapter = new HomeListDrugAdapter((AppCompatActivity) getContext(), R.layout.listview_home_custom, listHomeDrug);
        listView.setAdapter(adapter);
        loadData();
        return view;
    }

    public void loadData() {
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final ListDrugForHomeModel data = dataSnapshot.getValue(ListDrugForHomeModel.class);
                listHomeDrug.add(new ListDrugForHomeModel(data.drugName, data.drugImage, data.drugPost, data.drugDescription, data.id, data.mota));
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Bundle bundle = new Bundle();
                        bundle.putString("hinhanh",listHomeDrug.get(i).drugImage);
                        bundle.putString("mota",listHomeDrug.get(i).mota);
                        bundle.putString("tenthuoc",listHomeDrug.get(i).drugName);
                        Intent intent = new Intent(getActivity(),ArcticleDetailActivity.class);
                        intent.putExtra("Data",bundle);
                        startActivity(intent);
                    }
                });
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
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // dua nut search vao action bar

        inflater.inflate(R.menu.menu_search_home, menu);
        // tao 1 search view
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.menuSearchHome).getActionView();
        //bat su kien cho nut search

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(BackStackActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase(Locale.getDefault());
//
                if (newText != null && !newText.isEmpty()) {
                    ArrayList<ListDrugForHomeModel> listFound = new ArrayList<ListDrugForHomeModel>();
                    for (ListDrugForHomeModel item : listHomeDrug) {
                        if (item.getDrugName().toLowerCase(Locale.getDefault()).contains(newText)) {
                            listFound.add(item);
                        }
                    }
                    adapter = new HomeListDrugAdapter((AppCompatActivity) getActivity(), R.layout.listview_home_custom, listFound);
                    listView.setAdapter(adapter);

                } else {
                    adapter = new HomeListDrugAdapter((AppCompatActivity) getActivity(), R.layout.listview_home_custom, listHomeDrug);
                    listView.setAdapter(adapter);
                }

                adapter.notifyDataSetChanged();
                if (newText.length() == 0) {
                    searchView.clearFocus();
                }

                return true;

            }

        });
        super.onCreateOptionsMenu(menu, inflater);
    }


}
