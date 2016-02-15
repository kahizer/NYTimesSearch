package com.example.josevillanuva.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by josevillanuva on 2/12/16.
 */
public class Article implements Serializable {
    String webUrl;
    String headline;
    String thumbNail;

    public String getThumbNail() {
        return thumbNail;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }


    public Article(JSONObject jsonObject){
        try{
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if(multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            }
            else {
                this.thumbNail = "";
            }
        }
        catch (JSONException ex){

        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> result = new ArrayList<>();

        for(int i = 0; i < array.length(); i++){
            try{
                result.add(new Article(array.getJSONObject(i)));
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
        }

        return result;
    }
}
