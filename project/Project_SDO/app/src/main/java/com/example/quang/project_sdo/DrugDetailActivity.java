package com.example.quang.project_sdo;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quang.project_sdo.Models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DrugDetailActivity extends AppCompatActivity {
    private TextView txtCongDung, txtNguonGoc, txtMota, txtTenShop, txtGia;
    private ImageView imgHinhThuoc;
    private Button btnChat, btnAdd;
    private ActionBar actionBar;
    String idShop, tenshopA;
    FirebaseAuth mAuth;
    DatabaseReference root, rootOrder,rootAccount;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_detail_layout);

        //Ini
        txtCongDung = (TextView) findViewById(R.id.txtCongDungD);
        txtNguonGoc = (TextView) findViewById(R.id.txtNguonGocD);
        txtMota = (TextView) findViewById(R.id.txtMoTaD);
        txtTenShop = (TextView) findViewById(R.id.txtTenShopD);
        txtGia = (TextView) findViewById(R.id.txtGia);

        imgHinhThuoc = (ImageView) findViewById(R.id.imgDrugD);

        btnChat = (Button) findViewById(R.id.btnChat);
        btnAdd = (Button) findViewById(R.id.btnOrder);

        mAuth = FirebaseAuth.getInstance();

        //Process
        if (getIntent() != null && getIntent().getBundleExtra("data") != null) {
            Intent intent = getIntent();
             bundle = intent.getBundleExtra("data");
            txtCongDung.setText(bundle.getString("congdung"));
            txtNguonGoc.setText(bundle.getString("nguongoc"));
            txtMota.setText(bundle.getString("mota"));
            txtTenShop.setText(bundle.getString("tenshop"));
            txtGia.setText(bundle.getString("gia"));
            idShop = bundle.getString("idShop");
            tenshopA = bundle.getString("tenshop");
            Picasso.get().load(bundle.getString("hinhanh")).into(imgHinhThuoc);

        }

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getmImage();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDrug();
                Toast.makeText(DrugDetailActivity.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void getmImage() {
        if(mAuth.getCurrentUser() != null) {
            root = FirebaseDatabase.getInstance().getReference("Infomation account");
            root.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String img = dataSnapshot.child("linkhinh").getValue().toString();
                    if (img.equalsIgnoreCase("")) {
                        Toast.makeText(DrugDetailActivity.this, "Bạn cần thêm hình để chat", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DrugDetailActivity.this, BackStackActivity.class));
                    } else {
                        if (mAuth.getCurrentUser() != null) {
                            Intent newIntent = new Intent(DrugDetailActivity.this, ChatDetailActivity.class);
                            Bundle bundleA = new Bundle();
                            bundleA.putString("idshop", idShop);
                            bundleA.putString("tenshopA", tenshopA);
                            newIntent.putExtra("Data", bundleA);
                            startActivity(newIntent);
                        } else {
                            Toast.makeText(DrugDetailActivity.this, "Đăng nhâp mới chat được", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(DrugDetailActivity.this, "Bạn cần đăng nhập để chat", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDrug() {
        rootAccount = FirebaseDatabase.getInstance().getReference("Infomation account").child(mAuth.getUid());
        rootAccount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String diachi = dataSnapshot.child("address").getValue().toString();
                rootOrder = FirebaseDatabase.getInstance().getReference("Order");
                String idRandom = FirebaseDatabase.getInstance().getReference().push().getKey();
                int soLuong = 1;
                rootOrder.child(mAuth.getUid()).child(idRandom).setValue(new OrderModel(bundle.getString("tenThuoc"),bundle.getString("hinhanh"),Integer.parseInt(txtGia.getText() + ""),soLuong,idRandom,Integer.parseInt(txtGia.getText() + ""),0,diachi));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.cart_item:
                startActivity(new Intent(DrugDetailActivity.this,ShoppingCartActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
