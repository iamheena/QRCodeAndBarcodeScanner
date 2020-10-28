package com.example.qrcodeandbarcodescanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.qrcodeandbarcodescanner.adapter.Product;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SqlDb extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Student.db";
    public static final String TABLE_NAME = "Scanner";
    public static final String COL_0="ID";
    public static final String COL_1 = "CONTENT";
    public static final String COL_2 = "FORMATE";
    public static final String COL_3 = "IMAGE_PATH";
    public static final String COL_4 = "DATE";
    private static final String TAG = "DictionaryDatabase";

    public SqlDb(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,CONTENT TEXT,FORMATE TEXT,IMAGE_PATH TEXT,DATE DATE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public  boolean insertdata(String content,String format,String bit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, content);
        contentValues.put(COL_2, format);
        contentValues.put(COL_3, bit);
        contentValues.put(COL_4, System.currentTimeMillis());
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
            else
                return true;


    }
    public  Integer DeleteData(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID=?",new String[]{id});

    }
    public Cursor MoveToNext() {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null,COL_0 );

        return c;
    }

    public ArrayList<Product>getAlldata()
    {
    ArrayList<Product> products=new ArrayList<>();
        Cursor c=MoveToNext();
        if (c.moveToFirst())
        {
            do {
                int id=c.getInt(c.getColumnIndex(COL_0));
                String content=c.getString(c.getColumnIndex(COL_1));
                String format=c.getString(c.getColumnIndex(COL_2));
                String path=c.getString(c.getColumnIndex(COL_3));
                DateFormat dateFormat=DateFormat.getDateTimeInstance();
                String date=dateFormat.format(new Date(c.getLong(c.getColumnIndex(COL_4))).getTime());
                Product product=new Product(id,content,format,path,date);
                products.add(product);

            }while (c.moveToNext());
        }
        return products;
    }
}
