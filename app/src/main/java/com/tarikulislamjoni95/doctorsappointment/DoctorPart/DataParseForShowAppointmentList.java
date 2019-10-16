package com.tarikulislamjoni95.doctorsappointment.DoctorPart;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DataParseForShowAppointmentList
{
    public DataParseForShowAppointmentList() { }
    public ArrayList<HashMap<String,Object>> GetSelectedKeyBasedAppointmentList(HashMap<String,Object> ReceivedDataHashMap,String WhichKeyString,String KeyMatchingString)
    {
        ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
        if (ReceivedDataHashMap.containsKey(DBConst.RESULT))
        {
            if (ReceivedDataHashMap.get(DBConst.RESULT).toString().matches(DBConst.SUCCESSFUL))
            {
                DataSnapshot dataSnapshot=(DataSnapshot) ReceivedDataHashMap.get(DBConst.DataSnapshot);
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    if (WhichKeyString.matches("SeventhDay"))
                    {
                        for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            hashMap.put(dataSnapshot2.getKey(),dataSnapshot2.getValue().toString());
                        }
                    }
                    else if (dataSnapshot1.child(WhichKeyString).exists())
                    {
                        if (dataSnapshot1.child(WhichKeyString).getValue().toString().matches(KeyMatchingString))
                        {
                            for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                            {
                                hashMap.put(dataSnapshot2.getKey(),dataSnapshot2.getValue().toString());
                            }
                        }
                    }
                    arrayList.add(hashMap);
                }
            }
        }
        if (arrayList.size()==0)
        {
            return arrayList;
        }
        else
        {
            return GetSortedByHospitalName(arrayList);
        }
    }

    private ArrayList<HashMap<String,Object>> GetSortedByHospitalName(ArrayList<HashMap<String,Object>> arrayList)
    {
        ArrayList<HashMap<String,Object>> arrayList1=new ArrayList<>();
        Set<String> HospitalSet=new HashSet<>();
        Set<String> AppointmentDateSet=new HashSet<>();
        Set<String> AppointmentTimeSet=new HashSet<>();
        for (int i=0; i<arrayList.size(); i++)
        {
            if (arrayList.get(i).containsKey(DBConst.HospitalName) && arrayList.get(i).containsKey(DBConst.AppointmentDate) && arrayList.get(i).containsKey(DBConst.AppointmentTime))
            {
                HospitalSet.add(arrayList.get(i).get(DBConst.HospitalName).toString());
                AppointmentDateSet.add(arrayList.get(i).get(DBConst.AppointmentDate).toString());
                AppointmentTimeSet.add(arrayList.get(i).get(DBConst.AppointmentTime).toString());
            }
        }
        Iterator<String> iterator=HospitalSet.iterator();
        while (iterator.hasNext())
        {
            String HospitalName=iterator.next();
            Iterator<String> iterator1=AppointmentDateSet.iterator();
            while (iterator1.hasNext())
            {
                String AppointmentDate=iterator1.next();
                Iterator<String> iterator2=AppointmentTimeSet.iterator();
                while (iterator2.hasNext())
                {
                    String AppointmentTime=iterator2.next();
                    for(int i=0; i<arrayList.size(); i++)
                    {
                        if ((arrayList.get(i).get(DBConst.HospitalName).toString().matches(HospitalName))
                                &&(arrayList.get(i).get(DBConst.AppointmentDate).toString().matches(AppointmentDate))
                                &&(arrayList.get(i).get(DBConst.AppointmentTime).toString().matches(AppointmentTime)))
                        {
                            arrayList1.add(arrayList.get(i));
                        }
                    }
                }
            }
        }
        return arrayList1;
    }
}
