package com.tarikulislamjoni95.doctorsappointment.PatientPart;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import java.util.ArrayList;

public class DataParseForDoctorFiltration
{
    public DataParseModel GetTypeListAndUIDList(String WhichField,DataSnapshot dataSnapshot)
    {
        ArrayList<String> arrayList1=new ArrayList<>();
        ArrayList<ArrayList<String>> arrayList2=new ArrayList<>();

        if (dataSnapshot.getKey().matches(WhichField))
        {
            for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
            {
                arrayList1.add(dataSnapshot1.getKey());

                ArrayList<String> arrayList3=new ArrayList<>();
                for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                {
                    arrayList3.add(dataSnapshot2.getKey());
                }
                arrayList2.add(arrayList3);
            }
        }
        return new DataParseModel(arrayList1,arrayList2);
    }
}
