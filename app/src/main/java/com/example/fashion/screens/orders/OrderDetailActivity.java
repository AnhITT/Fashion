package com.example.fashion.screens.orders;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.fashion.R;
import com.example.fashion.data.adapter.OrderItemAdapter;
import com.example.fashion.data.model.order.ListOrderItem;
import com.example.fashion.data.model.order.Order;



import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    Order orderSelected;
    Toolbar toolbar;
    String idOrder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView listOrderDetail;
    ArrayList<ListOrderItem> orderItemArrayList;
    OrderItemAdapter orderItemAdapter;


    private void setEvent() {
    }

    private void setControl() {
        orderItemArrayList = new ArrayList<>();
        listOrderDetail = findViewById(R.id.listorderdetail);
        toolbar = findViewById(R.id.toolbar_orderdetail);
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tạo nút home
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getOrderbyOrderId(String idOrder) {
        db.collection("orderDetails")
                .whereEqualTo("orderId",idOrder)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<ListOrderItem> orderItemArrayList = queryDocumentSnapshots.toObjects(ListOrderItem.class);
                        orderItemAdapter = new OrderItemAdapter(getApplicationContext(), R.layout.line_order_item, (ArrayList<ListOrderItem>) orderItemArrayList);
                        listOrderDetail.setAdapter(orderItemAdapter);
                        orderItemAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure
                    }
                });
    }


    private int pageWidth = 1200, pageHeight = 2010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderSelected = (Order) getIntent().getSerializableExtra("order");
        idOrder = orderSelected.getId();

        setControl();
        actionToolBar();
        setEvent();
        getOrderbyOrderId(idOrder);
    }


    private void setOnPdfEvent() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PackageManager.PERMISSION_GRANTED);

        Date date = new Date();
        DecimalFormat df = new DecimalFormat("###,###,###");

        db.collection("orderDetails")
                .whereEqualTo("orderId",idOrder)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<ListOrderItem> data = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            ListOrderItem orderItem = document.toObject(ListOrderItem.class);
                            assert orderItem != null;
                            orderItem.setId(document.getId());
                            data.add(orderItem);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure
                    }
                });
    }


}