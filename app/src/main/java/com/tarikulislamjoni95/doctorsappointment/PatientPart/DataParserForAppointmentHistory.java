package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import com.google.firebase.database.DataSnapshot;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;

import java.util.ArrayList;
import java.util.HashMap;

public class DataParserForAppointmentHistory
{
    HashMap<String,Object> hashMap;
    ArrayList<HashMap<String,Object>> hashMapArrayList;
    private int NumberOfAppointments;
    public DataParserForAppointmentHistory(HashMap<String,Object> hashMap)
    {
        this.hashMap=hashMap;
        hashMapArrayList=new ArrayList<>();
        NumberOfAppointments=0;
        DataAnalyze();
    }

    private void DataAnalyze()
    {
        if (!hashMap.isEmpty())
        {
            if (hashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
            {
                DataSnapshot dataSnapshot=(DataSnapshot) hashMap.get(DBConst.DataSnapshot);
                NumberOfAppointments=(int)dataSnapshot.getChildrenCount();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    HashMap<String,Object> objectHashMap=new HashMap<>();
                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                    {
                        objectHashMap.put(dataSnapshot2.getKey(),dataSnapshot2.getValue());
                    }
                    hashMapArrayList.add(objectHashMap);
                }
            }
        }
    }

    public ArrayList<HashMap<String, Object>> getHashMapArrayList() {
        return hashMapArrayList;
    }

    public void setHashMapArrayList(ArrayList<HashMap<String, Object>> hashMapArrayList) {
        this.hashMapArrayList = hashMapArrayList;
    }

    public int getNumberOfAppointments() {
        return NumberOfAppointments;
    }

    public void setNumberOfAppointments(int numberOfAppointments) {
        NumberOfAppointments = numberOfAppointments;
    }
}
