package com.example.quang.project_sdo;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.quang.project_sdo.Adapters.DrugAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BackStackActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference root;
    ImageButton imgHome;
    ImageButton imgDrug;
    ImageButton imgChat;
    ImageButton imgAccount;
    ImageView imgAccountA;
    TextView txtNameAcc;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_stack_layout);

        mAuth = FirebaseAuth.getInstance();

        //Process
        imgHome = (ImageButton) findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new HomeFragment());
            }
        });

        imgDrug = (ImageButton) findViewById(R.id.imgDrug);
        imgDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new ListDrugFragment());
            }
        });

        imgChat = (ImageButton) findViewById(R.id.imgChat);
        imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new ChatFragment());
            }
        });

        imgAccount = (ImageButton) findViewById(R.id.imgAccount);
        imgAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new AccountManagementFragment());
            }
        });


        addFragment(new HomeFragment());


        //NavigationView
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_drawer);




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ThuocA:
                        mDrawerLayout.closeDrawer(Gravity.LEFT, true);
                        startActivity(new Intent(BackStackActivity.this, CoughActivity.class));
                        return true;
                    case R.id.ThuocB:
                        //setThuocB();
                        mDrawerLayout.closeDrawer(Gravity.LEFT, true);
                        startActivity(new Intent(BackStackActivity.this, HeadacheActivity.class));
                        return true;
                    case R.id.ThuocC:
                        mDrawerLayout.closeDrawer(Gravity.LEFT, true);
                        startActivity(new Intent(BackStackActivity.this, StomachActivity.class));
                        return true;

                }
                return true;
            }
        });


        handler = new Handler();
    }

    protected void addFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flScreen, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // dua nut search vao action bar
//        getMenuInflater().inflate(R.menu.menu_search_home,menu);
//       // tao 1 search view
//        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.menuSearchHome).getActionView();
//        //bat su kien cho nut search
//
//       searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
//           @Override
//           public boolean onQueryTextSubmit(String query) {
//               Toast.makeText(BackStackActivity.this, query, Toast.LENGTH_SHORT).show();
//               return false;
//           }
//
//           @Override
//           public boolean onQueryTextChange(String newText) {
//               //adapter.filter(newText.trim());
//               Log.d("ABC", newText);
//               return false;
//           }
//       });
//        return super.onCreateOptionsMenu(menu);
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(navigationView)) {
            mDrawerLayout.closeDrawer(navigationView);
        } else {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setHandler();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void setHandler(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser() != null){
                    root = FirebaseDatabase.getInstance().getReference("Infomation account").child(mAuth.getUid());
                    root.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("email").getValue().toString();
                            String hinhanh = dataSnapshot.child("linkhinh").getValue().toString();
                            ImageView imgAccountA = (ImageView) navigationView.findViewById(R.id.imgAccount);
                            TextView txtNameAcc = (TextView) navigationView.findViewById(R.id.nameAccount);
                            txtNameAcc.setText(name);
                            if(hinhanh.equalsIgnoreCase("")){

                            }else{
                                Picasso.get().load(hinhanh).into(imgAccountA);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{

                }
            }
        },3000);
    }

}
