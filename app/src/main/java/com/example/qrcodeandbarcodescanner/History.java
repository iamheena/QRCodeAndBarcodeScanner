package com.example.qrcodeandbarcodescanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.qrcodeandbarcodescanner.adapter.Product;
import com.example.qrcodeandbarcodescanner.adapter.ProductAdaapter;
import com.example.qrcodeandbarcodescanner.adapter.RecyclerTouchListener;
import com.example.qrcodeandbarcodescanner.database.SqlDb;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    static SqlDb database;
    static RecyclerView recyclerView;
    ProductAdaapter recycler;
    static ArrayList<Product> productArrayList;
    public  static Activity a;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datashow);
        productArrayList=new ArrayList<Product>();
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        a=this;
        database=new SqlDb(this);
        //database.add_new();
        productArrayList=database.getAlldata();

        recycler = new ProductAdaapter(this, productArrayList);
        Log.i("data", "" + productArrayList);
        RecyclerView.LayoutManager reLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ProductAdaapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i=new Intent(History.this,ResultActivity.class);
                i.putExtra("content",productArrayList.get(position).getContent());
                i.putExtra("format",productArrayList.get(position).getFormate());
                i.putExtra("bitmap",productArrayList.get(position).getPath());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        /*public static void notifychange(Context ctx){
            productArrayList=database.getAlldata();
            ProductAdaapter mAdapter=new ProductAdaapter(ctx,productArrayList);
            recyclerView.setAdapter(mAdapter);

        }
*/

    }
    public static void notifysetchange(Context ctx) {
        productArrayList = database.getAlldata();
        ProductAdaapter mAdapter = new ProductAdaapter(ctx, productArrayList);
        recyclerView.setAdapter(mAdapter);
    }


}
