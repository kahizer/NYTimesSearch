package com.example.josevillanuva.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by josevillanuva on 2/13/16.
 */
public class AdvancedSearch implements Parcelable{
    String startDate;
    String endDate;
    String order;
    List<String> newsDesk = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getOrder() {
        return order;
    }

    public List<String> getNewsDesk() {
        return newsDesk;
    }

    public AdvancedSearch(Date start, Date end, String newsDesk, String order){
        this.startDate = dateFormat.format(start);
        this.endDate = dateFormat.format(end);
        this.order = order.toLowerCase();
        String delims = " and ";
        String[] tokens = newsDesk.split(delims);
        for(int i = 0; i < tokens.length; i++){
            this.newsDesk.add(tokens[i]);
        }
    }

    public AdvancedSearch(Parcel in){
        try{
            startDate = in.readString();
            endDate = in.readString();
            order = in.readString();
            newsDesk = new ArrayList<String>();
            in.readList(newsDesk, getClass().getClassLoader());

        }
        catch (Exception ex){
            Log.d("DEBUG", ex.toString());
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public AdvancedSearch createFromParcel(Parcel in){return new AdvancedSearch(in);}

        public AdvancedSearch[] newArray(int size) {return new AdvancedSearch[size]; }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try{
            dest.writeString(startDate);
            dest.writeString(endDate);
            dest.writeString(order);
            dest.writeList(newsDesk);
        }
        catch (Exception ex){
            Log.d("DEBUG", ex.toString());
        }
    }
}
