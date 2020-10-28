package com.example.qrcodeandbarcodescanner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrcodeandbarcodescanner.History;
import com.example.qrcodeandbarcodescanner.R;
import com.example.qrcodeandbarcodescanner.ResultActivity;
import com.example.qrcodeandbarcodescanner.database.SqlDb;

import java.util.ArrayList;

public class ProductAdaapter extends RecyclerView.Adapter<ProductAdaapter.Myholder> {
  private   Context context;
    ArrayList<Product> productArrayList;
    SqlDb myDB;

    public ProductAdaapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    class Myholder extends RecyclerView.ViewHolder {
        TextView date, content, formate;
        ImageView delete,code_image;
        CardView cardView;

        public Myholder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.txt_date);
            content = (TextView) itemView.findViewById(R.id.txt_content);
            formate = (TextView) itemView.findViewById(R.id.txt_formate);
            delete=(ImageView)itemView.findViewById(R.id.delete_btn);
            code_image=(ImageView)itemView.findViewById(R.id.code_img);
            cardView=(CardView)itemView.findViewById(R.id.card);
            //time=(TextView)itemView.findViewById(R.id.txttime);


        }
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_list, null);
        return new Myholder(view) {
        };

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final Myholder holder, @SuppressLint("RecyclerView") final int position) {
        final Product product=productArrayList.get(position);
        holder.content.setText(product.getContent());
        holder.date.setText(product.getDate());
        holder.formate.setText(product.getFormate());
        holder.code_image.setImageBitmap(StringToBitMap(product.getPath()));
       /* holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,ResultActivity.class);
                i.putExtra("content",productArrayList.get(position).getContent());
                i.putExtra("format",productArrayList.get(position).getFormate());
                i.putExtra("bitmap",productArrayList.get(position).getPath());
                context.startActivity(i);
            }
        });*/
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            myDB=new SqlDb(context);
           // myDB.add_new();
            int id=product.getId();
            myDB.DeleteData(String.valueOf(id));
            productArrayList.remove(position);
             myDB.close();
         History.notifysetchange(context);
            }
        });


    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }
}