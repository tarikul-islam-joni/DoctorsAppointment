package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import com.google.firebase.database.DataSnapshot;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;

import java.util.ArrayList;
import java.util.HashMap;

public class DataParseForRatingAndReview
{
    int TotalReviewer;
    int TotalRatings;
    ArrayList<HashMap<String,Object>> arrayList;
    HashMap<String ,Object> hashMap;

    public DataParseForRatingAndReview(HashMap<String,Object> hashMap)
    {
        TotalRatings=0;
        TotalReviewer=0;
        arrayList=new ArrayList<>();
        this.hashMap=hashMap;

        ParseData();
    }

    private void ParseData()
    {
        if (!hashMap.isEmpty())
        {
            if (hashMap.containsKey(DBConst.RESULT))
            {
                if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
                {
                    DataSnapshot dataSnapshot=(DataSnapshot) hashMap.get(DBConst.DataSnapshot);
                    TotalRatings=Integer.valueOf(dataSnapshot.child(DBConst.TotalRating).getValue().toString());
                    TotalReviewer=Integer.valueOf(dataSnapshot.child(DBConst.TotalReviewer).getValue().toString());

                    for(DataSnapshot dataSnapshot1:dataSnapshot.child(DBConst.Reviews).getChildren())
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.UID,dataSnapshot1.getKey());
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            hashMap.put(dataSnapshot2.getKey(),dataSnapshot2.getValue());
                        }
                        arrayList.add(hashMap);
                    }
                }
                else
                {
                    TotalRatings=0;
                    TotalReviewer=0;
                    arrayList=new ArrayList<>();
                }
            }
            else
            {
                TotalRatings=0;
                TotalReviewer=0;
                arrayList=new ArrayList<>();
            }
        }
        else
        {
            TotalRatings=0;
            TotalReviewer=0;
            arrayList=new ArrayList<>();
        }
    }

    public int getTotalReviewer() {
        return TotalReviewer;
    }

    public void setTotalReviewer(int totalReviewer) {
        TotalReviewer = totalReviewer;
    }

    public int getTotalRatings() {
        return TotalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        TotalRatings = totalRatings;
    }

    public ArrayList<HashMap<String, Object>> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<HashMap<String, Object>> arrayList) {
        this.arrayList = arrayList;
    }
}
