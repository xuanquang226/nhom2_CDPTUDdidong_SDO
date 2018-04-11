package com.example.quang.project_sdo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.quang.project_sdo.Adapters.CoughAdapter;
import com.example.quang.project_sdo.Adapters.DrugAdapter;
import com.example.quang.project_sdo.Adapters.HomeListDrugAdapter;
import com.example.quang.project_sdo.Models.CoughModel;
import com.example.quang.project_sdo.Models.ListDrugForHomeModel;
import com.example.quang.project_sdo.Models.ListDrugModel;

import java.util.ArrayList;

public class CoughActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<CoughModel> listCoughDrug = new ArrayList<CoughModel>();
    CoughAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cough_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //ListView
        listView = (ListView) findViewById(R.id.coughdrug_listview);
        listCoughDrug.clear();
        listCoughDrug = new ArrayList<CoughModel>();
        listCoughDrug.add(new CoughModel("thuốc đau đầu","ABC Store","120.000",R.drawable.drug));
        listCoughDrug.add(new CoughModel("thuốc sad","777 Store","150.000",R.drawable.drug));
        listCoughDrug.add(new CoughModel("thuốc phá thai","666 Store","180.000",R.drawable.drug));
        listCoughDrug.add(new CoughModel("thuốc bbb","5555 Store","220.000",R.drawable.drug));
        adapter = new CoughAdapter(this,R.layout.listview_drug_custom,listCoughDrug);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
