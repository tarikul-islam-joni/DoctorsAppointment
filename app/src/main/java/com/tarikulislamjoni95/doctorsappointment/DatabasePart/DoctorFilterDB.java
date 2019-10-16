package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorFilterDB
{
    GetDataFromDBInterface getDataFromDBInterface;
    int count=0;
    public DoctorFilterDB(Activity activity)
    {
        getDataFromDBInterface=(GetDataFromDBInterface)activity;
    }

    public void SaveDoctorFilterData(final String UID, HashMap<String,String> hashMap)
    {
        if (hashMap.containsKey(DBConst.Name) && hashMap.containsKey(DBConst.Specialization) && hashMap.containsKey(DBConst.AvailableArea))
        {
            HashMap<String,String> hashMap1=new HashMap<>();
            hashMap1.put(DBConst.UID,UID);
            hashMap1.put(DBConst.ProfileImageUrl,hashMap.get(DBConst.ProfileImageUrl));
            hashMap1.put(DBConst.Name,hashMap.get(DBConst.Name));
            hashMap1.put(DBConst.Specialization,hashMap.get(DBConst.Specialization));
            hashMap1.put(DBConst.AvailableArea,hashMap.get(DBConst.AvailableArea));

            final String[] SpecializationList=hashMap.get(DBConst.Specialization).split(",",0);
            final String AvailableArea=hashMap.get(DBConst.AvailableArea);

            final DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.DoctorFiltration);

            reference.child(DBConst.DoctorList).child(UID).setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        reference.child(DBConst.AvailableArea).child(AvailableArea).child(UID).setValue(UID).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isComplete())
                                {
                                    count=0;
                                    for(int i=0; i<SpecializationList.length; i++)
                                    {
                                        reference.child(DBConst.Specialization).child(SpecializationList[i]).child(UID).setValue(UID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if(task.isComplete())
                                                {
                                                    if (count==SpecializationList.length-1)
                                                    {
                                                        HashMap<String,Object> hashMap2=new HashMap<>();
                                                        hashMap2.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                                                        getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSave_SS_SP_AA_InDoctorFiltration,hashMap2);
                                                    }
                                                    count++;
                                                }
                                                else
                                                {
                                                    HashMap<String,Object> hashMap2=new HashMap<>();
                                                    hashMap2.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                                                    getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSave_SS_SP_AA_InDoctorFiltration,hashMap2);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
        else
        {
            HashMap<String,Object> hashMap2=new HashMap<>();
            hashMap2.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
            getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSave_SS_SP_AA_InDoctorFiltration,hashMap2);
        }

    }

    public void SaveDoctorUIDInHospitalDir(String UID,HashMap<String,String> hashMap)
    {
        if (hashMap.containsKey(DBConst.HospitalName))
        {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.DoctorFiltration).child(DBConst.HospitalList);
            reference.child(hashMap.get(DBConst.HospitalName).toString()).child(UID).setValue(UID).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        HashMap<String,Object> hashMap1=new HashMap<>();
                        hashMap1.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveDoctorUIDInHospitalDir,hashMap1);
                    }
                    else
                    {
                        HashMap<String,Object> hashMap1=new HashMap<>();
                        hashMap1=new HashMap<>();
                        hashMap1.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveDoctorUIDInHospitalDir,hashMap1);
                    }
                }
            });
        }
        else
        {
            HashMap<String,Object> hashMap1=new HashMap<>();
            hashMap1.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
            getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveDoctorUIDInHospitalDir,hashMap1);
        }

    }

    public void DeleteDoctorUIDFromHospitalDir(String UID,HashMap<String,Object> hashMap)
    {
        if (hashMap.containsKey(DBConst.HospitalName))
        {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.DoctorFiltration).child(DBConst.HospitalList);
            reference.child(hashMap.get(DBConst.HospitalName).toString()).child(UID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnDeleteDoctorUIDFromHospitalDir,hashMap);
                    }
                    else
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnDeleteDoctorUIDFromHospitalDir,hashMap);
                    }
                }
            });
        }
        else
        {
            hashMap=new HashMap<>();
            hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
            getDataFromDBInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnDeleteDoctorUIDFromHospitalDir,hashMap);
        }
    }





    public void GetDoctorListByName(String DoctorNameString, final String OutputKey)
    {
        final HashMap<String,Object> hashMap=new HashMap<>();
        final ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.DoctorFiltration).child(DBConst.DoctorList);
        reference.orderByChild(DBConst.Name).startAt(DoctorNameString).endAt(DoctorNameString+"\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        hashMap.put(DBConst.UID,dataSnapshot1.getKey());
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            hashMap.put(dataSnapshot2.getKey(),dataSnapshot2.getValue());
                        }
                        arrayList.add(hashMap);
                    }
                    getDataFromDBInterface.GetMultipleDataFromDatabase(OutputKey,arrayList);
                }
                else
                {
                    arrayList.clear();
                    getDataFromDBInterface.GetMultipleDataFromDatabase(OutputKey,arrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                arrayList.clear();
                getDataFromDBInterface.GetMultipleDataFromDatabase(OutputKey,arrayList);
            }
        });
    }






    private int i=0;
    private ArrayList<HashMap<String,Object>> hashMapArrayList;
    private int ArrayListSize=0;
    private String OutputKey="";
    public void GetDoctorList(String WhichDB,ArrayList<String> arrayList)
    {
        hashMapArrayList=new ArrayList<>();
        OutputKey=WhichDB;
        ArrayListSize=arrayList.size();

        count=0;
        if (arrayList.size()!=0)
        {
            for(i=0; i<arrayList.size(); i++)
            {
                GetDoctorDataFromDB(WhichDB,arrayList.get(i));
            }
        }
        else
        {
            getDataFromDBInterface.GetMultipleDataFromDatabase(OutputKey,hashMapArrayList);
        }
    }

    private void GetDoctorDataFromDB(String WhichChild,String UID)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.DoctorFiltration).child(DBConst.DoctorList);
        databaseReference.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    HashMap<String,Object> MyHashMap=new HashMap<>();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        MyHashMap.put(dataSnapshot1.getKey(),dataSnapshot1.getValue());
                    }
                    AddDataToArrayList(DBConst.SUCCESSFUL,MyHashMap);
                }
                else
                {
                    HashMap<String,Object> MyHashMap=new HashMap<>();
                    AddDataToArrayList(DBConst.UNSUCCESSFUL,MyHashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> MyHashMap=new HashMap<>();
                AddDataToArrayList(DBConst.UNSUCCESSFUL,MyHashMap);
            }
        });
    }

    private void AddDataToArrayList(String Result, HashMap<String, Object> MyHashMap)
    {
        count++;
        if (Result.matches(DBConst.SUCCESSFUL))
        {
            hashMapArrayList.add(MyHashMap);
        }
        if (count==ArrayListSize)
        {
            getDataFromDBInterface.GetMultipleDataFromDatabase(OutputKey,hashMapArrayList);
        }
    }







    public void GetDoctorCategoryAndUIDListFrom(String WhichChild, final String OutputKey)
    {
        final HashMap<String,Object> hashMap=new HashMap<>();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.DoctorFiltration).child(WhichChild);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    hashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
                else
                {
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                getDataFromDBInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
            }
        });
    }
}
