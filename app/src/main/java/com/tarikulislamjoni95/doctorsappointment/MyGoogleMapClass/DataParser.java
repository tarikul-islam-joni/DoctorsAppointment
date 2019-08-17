package com.tarikulislamjoni95.doctorsappointment.MyGoogleMapClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser
{
    private HashMap<String,String> getPlaces(JSONObject googlePlaceJson)
    {
        HashMap<String,String> myHashMap=new HashMap<>();
        String PlaceName="-NA-";
        String Vicinity="-NA-";
        String Latitude="";
        String Longitude="";
        String Reference="";

        try {
            if (!googlePlaceJson.isNull("name"))
            {
                PlaceName=googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity"))
            {
                Vicinity=googlePlaceJson.getString("vicinity");
            }
            if (!googlePlaceJson.isNull("formatted_address"))
            {
                Vicinity=googlePlaceJson.getString("formatted_address");
            }
            if (!googlePlaceJson.isNull("geometry"))
            {
                Latitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                Longitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            }

            if (!googlePlaceJson.isNull("reference"))
            {
                Reference=googlePlaceJson.getString("reference");
            }

            myHashMap.put("place_name",PlaceName);
            myHashMap.put("place_vicinity",Vicinity);
            myHashMap.put("place_latitude",Latitude);
            myHashMap.put("place_longitude",Longitude);
            myHashMap.put("place_reference",Reference);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return myHashMap;
    }

    public List<HashMap<String,String>> getAllPlaces(JSONArray googleJsonArray)
    {
        int count=googleJsonArray.length();
        List<HashMap<String,String>> hashMapList=new ArrayList<>();
        HashMap<String,String> hashMap=new HashMap<>();
        for(int i=0; i<count; i++)
        {
            try {
                hashMap=getPlaces((JSONObject)googleJsonArray.get(i));
                hashMapList.add(hashMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return hashMapList;
    }

    public List<HashMap<String,String>> Parse(String jsonData)
    {
        JSONObject jsonObject;
        JSONArray jsonArray=null;

        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getAllPlaces(jsonArray);
    }
}
