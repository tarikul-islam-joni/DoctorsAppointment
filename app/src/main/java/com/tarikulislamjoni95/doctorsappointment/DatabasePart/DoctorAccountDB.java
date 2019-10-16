package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorAccountDB
{

    HashMap<String,Object> ASHashMap=new HashMap<>();
    ArrayList<HashMap<String,Object>> AShashMapArrayList=new ArrayList<>();


    private Activity activity;
    private GetDataFromDBInterface myDataInterface;
    private DatabaseReference reference;
    public DoctorAccountDB(Activity activity)
    {
        this.activity=activity;
        myDataInterface =(GetDataFromDBInterface)activity;
        reference= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount);
    }
    public void SaveDoctorAccountInformation(String UID, final HashMap<String,String> hashMap)
    {
        final HashMap<String,Object> hashMap1=new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            reference.child(UID).child(DBConst.AccountInformation).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        hashMap1.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.SaveDoctorAccountInformation,hashMap1);
                    }
                    else
                    {
                        hashMap1.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.SaveDoctorAccountInformation,hashMap1);
                    }
                }
            });
        }
        else
        {
            hashMap1.put(DBConst.RESULT,DBConst.NULL_USER);
            myDataInterface.GetSingleDataFromDatabase(DBConst.SaveDoctorAccountInformation,hashMap1);
        }
    }
    public void GetDoctorAccountInformation(final String UID)
    {
        final HashMap<String,Object> hashMap=new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            reference.child(UID).child(DBConst.AccountInformation).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        hashMap.put(DBConst.RESULT,DBConst.DATA_EXIST);
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                        {
                            hashMap.put(dataSnapshot1.getKey(),dataSnapshot1.getValue());
                        }
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorAccountInformation,hashMap);

                    }
                    else
                    {
                        hashMap.put(DBConst.RESULT,DBConst.DATA_NOT_EXIST);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorAccountInformation,hashMap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    hashMap.put(DBConst.RESULT,DBConst.DATABASE_ERROR);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorAccountInformation,hashMap);
                }
            });
        }
        else
        {
            hashMap.put(DBConst.RESULT,DBConst.NULL_USER);
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorAccountInformation,hashMap);
        }
    }


    public void GetDoctorAccountByKey(String UID, String WhichDB, final String OutputKey)
    {
        if (UID.matches(DBConst.SELF))
        {
            UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(UID);
        databaseReference.child(WhichDB).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    hashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
            }
        });
    }

    HashMap<String,Object> hashMap;
    public void GetDoctorFullDatabase(String UID)
    {
        hashMap=new HashMap<>();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(UID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                hashMap=new HashMap<>();
                if (dataSnapshot.exists())
                {
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    hashMap.put(DBConst.GetDoctorFullDatabase,dataSnapshot);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorFullDatabase,hashMap);
                }
                else
                {
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorFullDatabase,hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorFullDatabase,hashMap);
            }
        });
    }

    public void SaveAppointmentScheduleInfo(String UID, final HashMap<String,String> hashMap)
    {
        if (!hashMap.isEmpty())
        {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(UID).child(DBConst.AppointmentSchedule);
            reference.child(hashMap.get(DBConst.HospitalName)).child(hashMap.get(DBConst.AppointmentSTime)).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        HashMap<String,Object> hashMap1=new HashMap<>();
                        hashMap1.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveAppointmentScheduleInfoInAccount,hashMap1);
                    }
                    else
                    {
                        HashMap<String,Object> hashMap1=new HashMap<>();
                        hashMap1.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveAppointmentScheduleInfoInAccount,hashMap1);
                    }
                }
            });
        }
        else
        {
            HashMap<String,Object> hashMap1=new HashMap<>();
            hashMap1.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveAppointmentScheduleInfoInAccount,hashMap1);
        }
    }

    public void GetDoctorAppointmentScheduleInfo(String UID)
    {
        ASHashMap=new HashMap<>();
        AShashMapArrayList=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(UID).child(DBConst.AppointmentSchedule);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Log.d("myError : ","Data 0 : "+dataSnapshot.toString());
                if (dataSnapshot.exists())
                {
                    ASHashMap=new HashMap<>();
                    AShashMapArrayList=new ArrayList<>();
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren())
                        {
                            ASHashMap=GetParseData(dataSnapshot2);
                            AShashMapArrayList.add(ASHashMap);
                        }
                    }

                    myDataInterface.GetMultipleDataFromDatabase(DBConst.GetAppointmentScheduleInfo,AShashMapArrayList);
                }
                else
                {
                    AShashMapArrayList.clear();
                    myDataInterface.GetMultipleDataFromDatabase(DBConst.GetAppointmentScheduleInfo,AShashMapArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                AShashMapArrayList.clear();
                myDataInterface.GetMultipleDataFromDatabase(DBConst.GetAppointmentScheduleInfo,AShashMapArrayList);
            }
        });
    }


    private HashMap<String, Object> GetParseData(DataSnapshot dataSnapshot3)
    {
        Log.d("myError : ","Data 3 : "+dataSnapshot3.toString());
        HashMap<String,Object> hashMap=new HashMap<>();
        String[] DataKey={DBConst.HospitalName,DBConst.Specialization,DBConst.AvailableDay,DBConst.AppointmentSTime,DBConst.AppointmentETime,DBConst.AppointmentCapacity,DBConst.AppointmentFee,DBConst.UnavailableSDate,DBConst.UnavailableEDate};
        for(int i=0; i<DataKey.length; i++)
        {
            if (dataSnapshot3.child(DataKey[i]).exists())
            {
                hashMap.put(DataKey[i],dataSnapshot3.child(DataKey[i]).getValue());
            }
            else
            {
                hashMap.put(DataKey[i],"Unknown");
            }
        }
        return hashMap;
    }

    public void DeleteAppointScheduleInfo(String UID, final HashMap<String,Object> hashMap)
    {
        if (hashMap.containsKey(DBConst.HospitalName) && hashMap.containsKey(DBConst.AppointmentSTime))
        {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(UID).child(DBConst.AppointmentSchedule);
            reference.child(hashMap.get(DBConst.HospitalName).toString()).child(hashMap.get(DBConst.AppointmentSTime).toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        HashMap<String,Object> hashMap1=new HashMap<>();
                        hashMap1.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        hashMap1.put(DBConst.HospitalName,hashMap.get(DBConst.HospitalName));
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnDeleteAppointmentScheduleInfoFromAccount,hashMap1);
                    }
                    else
                    {
                        HashMap<String,Object> hashMap1=new HashMap<>();
                        hashMap1.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnDeleteAppointmentScheduleInfoFromAccount,hashMap1);
                    }
                }
            });
        }
        else
        {
            HashMap<String,Object> hashMap1=new HashMap<>();
            hashMap1.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnDeleteAppointmentScheduleInfoFromAccount,hashMap1);
        }
    }


    public void GetDoctorCurrentDateAppointmentNumber(String UID,String HospitalName,String AppointmentStartTime,String Year,String Month,String Date)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(UID).child(DBConst.AppointmentHistory);
        databaseReference.orderByChild(DBConst.AppointmentDate).equalTo(Date+"-"+Month+"-"+Year)
                .addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    HashMap<String,Object> objectHashMap=new HashMap<>();
                    objectHashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    objectHashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorSelectedDateAppointmentHistoryFromDoctorAccount,objectHashMap);
                }
                else
                {
                    HashMap<String,Object> objectHashMap=new HashMap<>();
                    objectHashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorSelectedDateAppointmentHistoryFromDoctorAccount,objectHashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> objectHashMap=new HashMap<>();
                objectHashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(DBConst.GetDoctorSelectedDateAppointmentHistoryFromDoctorAccount,objectHashMap);
            }
        });
    }

    public void SaveCreateAppointmentToTheDoctorAccount(String DoctorUID,HashMap<String,Object> AppointmentSaveInfo)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(DoctorUID).child(DBConst.AppointmentHistory);
        databaseReference.push().setValue(AppointmentSaveInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    Log.d("myError","Data Saved Successfully from SaveCreateAppointmentToTheDoctorAccount");
                    HashMap<String,Object> hashMap1=new HashMap<>();
                    hashMap1.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveAppointmentCreationFromDoctorDB,hashMap1);
                }
                else
                {
                    Log.d("myError","Data Not Saved Successfully from SaveCreateAppointmentToTheDoctorAccount");
                    HashMap<String,Object> hashMap1=new HashMap<>();
                    hashMap1.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveAppointmentCreationFromDoctorDB,hashMap1);
                }
            }
        });
    }



    public void SaveReviewAndRatingInToDoctorAccountDB(String DoctorUID, int totalRating, int totalReviewer, String MyUID, HashMap<String, Object> SaveData)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(DoctorUID).child(DBConst.RatingAndReviews);
        databaseReference.child(DBConst.TotalRating).setValue(totalRating);
        databaseReference.child(DBConst.TotalReviewer).setValue(totalReviewer);
        databaseReference.child(DBConst.Reviews).child(MyUID).setValue(SaveData).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveReviewAndRatingIntoToDoctorAccountDB,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveReviewAndRatingIntoToDoctorAccountDB,hashMap);
                }
            }
        });
    }

    public void GetDoctorReviewAndRatingFromDoctorAccountDB(String DoctorUID)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(DoctorUID).child(DBConst.RatingAndReviews);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    hashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetRatingsAndReviewInfoFromDoctorAccountDB,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetRatingsAndReviewInfoFromDoctorAccountDB,hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(DBConst.GetRatingsAndReviewInfoFromDoctorAccountDB,hashMap);
            }
        });
    }


    public void GetDoctorScheduledAppointmentListFromDoctorAccount(String UID, String WhichChild, String ChildDataMatching, final String OutputKey)
    {
        if (UID.matches(DBConst.SELF))
        {
            UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        Log.d("myErorr 1",UID+WhichChild+ChildDataMatching);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(UID).child(DBConst.AppointmentHistory);
        databaseReference.orderByChild(WhichChild).equalTo(ChildDataMatching).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Log.d("myErorr 1",dataSnapshot.toString());
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    hashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
                else
                {
                    Log.d("myErorr 1","Data not exist");
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d("myErorr 1","Database error");
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(OutputKey,hashMap);
            }
        });
    }

    public void VerifyTheDoctorAccount(String UID,String VerificationStatus)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.DoctorAccount).child(UID);
        databaseReference.child(DBConst.AccountInformation).child(DBConst.AuthorityValidity).setValue(VerificationStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {

                }
            }
        });
    }
}
