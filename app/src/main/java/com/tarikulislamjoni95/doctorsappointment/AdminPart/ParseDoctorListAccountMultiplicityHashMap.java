package com.tarikulislamjoni95.doctorsappointment.AdminPart;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseDoctorListAccountMultiplicityHashMap
{
    private ArrayList<HashMap<String,Object>> hashMapArrayList;
    private ArrayList<String> arrayList;
    public ParseDoctorListFromDataParseModel ProcessDoctorList(HashMap<String, Object> object,String WhichType,Object object1)
    {
        hashMapArrayList=new ArrayList<>();
        arrayList=new ArrayList<>();
        if (object.containsKey(DBConst.RESULT))
        {
            if (object.get(DBConst.RESULT).toString().matches(DBConst.DATA_EXIST))
            {
                DataSnapshot dataSnapshot=(DataSnapshot)object.get(DBConst.DataSnapshot);
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    if (WhichType.matches(DBConst.Doctor))
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            if ((!(dataSnapshot2.getKey().matches(DBConst.MultipleCheck))) && (dataSnapshot2.getValue().toString().matches(String.valueOf(object1))))
                            {
                                hashMap.put(dataSnapshot2.getKey(),dataSnapshot2.getValue());
                                arrayList.add(dataSnapshot1.getKey());
                            }
                        }
                        hashMapArrayList.add(hashMap);
                    }
                    else if (WhichType.matches(DBConst.Patient))
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        arrayList.add(dataSnapshot1.getKey());
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            if ((!dataSnapshot2.getKey().matches(DBConst.MultipleCheck)))
                            {
                                hashMap.put(dataSnapshot2.getKey(),dataSnapshot2.getValue());
                            }
                        }
                        hashMapArrayList.add(hashMap);
                    }
                    else if ((WhichType.matches(DBConst.GetLockedDoctorAccountListFromDB) || WhichType.matches(DBConst.GetLockedPatientAccountListFromDB)))
                    {
                        arrayList.add(dataSnapshot1.getKey());
                    }
                }

            }
        }
        return new ParseDoctorListFromDataParseModel(arrayList,hashMapArrayList);

    }
}
