package com.example.qrcodeandbarcodescanner.adapter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Product {



    private String Date;
    private  String Content;
    private  String Formate;
    private String Path;
    int id;

    public String getPath()
    {
        return Path;
    }

    public void setPath(String path)
    {
        this.Path = path;
    }


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public String getDate()
    {
        return Date;
    }

    public void setDate(String date)
    {
        this.Date = date;
    }
    public String getFormate()
    {
        return Formate;
    }

    public void setFormate(String formate)
    {
        this.Formate = formate;
    }

    public String getContent()
    {
        return Content;
    }

    public void setContent(String content)
    {
        this.Content = content;
    }


  /*  public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }

    public void setScanDate(String scanDate) {
        this.scanDate = scanDate;
    }

    public String getScanTime() {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss" , Locale.getDefault());
        return  timeFormat.format(new Date());
    }

    public String getScanDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy",Locale.getDefault());
        return dateFormat.format(new Date());
    }
*/
    public Product( int id,String content,String formate,String path,String date) {
        this.id=id;
        this.Date=date;
        this.Path=path;
        this.Content = content;
        this.Formate = formate;
    }



}
