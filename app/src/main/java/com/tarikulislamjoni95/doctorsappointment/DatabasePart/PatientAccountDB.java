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

public class PatientAccountDB
{
    private DatabaseReference reference;
    private GetDataFromDBInterface myDataInterface;
    public PatientAccountDB(Activity activity)
    {
        myDataInterface=(GetDataFromDBInterface)activity;
        reference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.PatientAccount);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void SaveAccountInformation(String UID,HashMap<String,String> DataHashMap)
    {
        DataHashMap.put(DBConst.UID,UID);
        final HashMap<String,Object> hashMap=new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            reference.child(UID).child(DBConst.AccountInformation).setValue(DataHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete())
                    {
                        hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.SavePatientAccountInformation,hashMap);
                    }
                    else
                    {
                        hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.SavePatientAccountInformation,hashMap);
                    }
                }
            });
        }
        else
        {
            hashMap.put(DBConst.RESULT,DBConst.NULL_USER);
            myDataInterface.GetSingleDataFromDatabase(DBConst.SavePatientAccountInformation,hashMap);
        }
    }

    String AccountUID;
    public void GetPatientAccountInformation(String UID)
    {
        AccountUID=UID;
        if (AccountUID.matches(DBConst.SELF))
        {
            AccountUID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        final HashMap<String,Object> hashMap=new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            reference.child(AccountUID).child(DBConst.AccountInformation).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        String[] DataField={DBConst.Name,DBConst.FatherName,DBConst.MotherName,DBConst.PhoneNumber,DBConst.Height,DBConst.Weight,
                                DBConst.Gender,DBConst.DateOfBirth,DBConst.BloodGroup,DBConst.Address,DBConst.BirthCertificateNumber,
                                DBConst.ProfileImageUrl,DBConst.BirthCertificateImageUrl,DBConst.AnotherDocumentImageUrl};
                        hashMap.put(DBConst.RESULT,DBConst.DATA_EXIST);
                        for(int i=0; i<DataField.length; i++)
                        {
                            if (dataSnapshot.child(DataField[i]).exists())
                            {
                                hashMap.put(DataField[i],dataSnapshot.child(DataField[i]).getValue());
                            }
                            else
                            {
                                hashMap.put(DataField[i],DBConst.UNKNOWN);
                            }
                        }
                        hashMap.put(DBConst.UID,AccountUID);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetPatientAccountInformation,hashMap);
                    }
                    else
                    {
                        hashMap.put(DBConst.RESULT,DBConst.DATA_NOT_EXIST);
                        myDataInterface.GetSingleDataFromDatabase(DBConst.GetPatientAccountInformation,hashMap);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    hashMap.put(DBConst.RESULT,DBConst.DATABASE_ERROR);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetPatientAccountInformation,hashMap);
                }
            });
        }
        else
        {
            hashMap.put(DBConst.RESULT,DBConst.NULL_USER);
            myDataInterface.GetSingleDataFromDatabase(DBConst.GetPatientAccountInformation,hashMap);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    public void SaveCreateAppointmentToThePatientAccount(final HashMap<String,Object> hashMap)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.PatientAccount).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(DBConst.AppointmentHistory);
        databaseReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    HashMap<String,Object> hashMap1=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveAppointmentCreationFromPatientDB,hashMap1);
                }
                else
                {
                    HashMap<String,Object> hashMap1=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetStatusOnSaveAppointmentCreationFromPatientDB,hashMap1);
                }
            }
        });
    }

    public void GetPatientAppointmentHistory(String UID)
    {
        if (UID.matches(DBConst.SELF))
        {
            UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.PatientAccount).child(UID).child(DBConst.AppointmentHistory);
        databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    HashMap<String,Object> objectHashMap=new HashMap<>();
                    objectHashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    objectHashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetPatientAppointmentHistoryFromPatientAccount,objectHashMap);
                }
                else
                {
                    HashMap<String,Object> objectHashMap=new HashMap<>();
                    objectHashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetPatientAppointmentHistoryFromPatientAccount,objectHashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> objectHashMap=new HashMap<>();
                objectHashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(DBConst.GetPatientAppointmentHistoryFromPatientAccount,objectHashMap);
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void GetAppointmentInformation(String UID)
    {
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void SaveMedicalReportToThePatientAccountDB(String UID,String ReportTitle,String ReportDetails,String ReportImageUrl,final String ResultReturnKey)
    {
        if (UID.matches(DBConst.SELF))
        {
            UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.PatientAccount).child(UID).child(DBConst.MedicalReportDB).child(ReportTitle);
        if (!ReportImageUrl.matches(DBConst.UNKNOWN))
        {
            databaseReference.push().setValue(ReportImageUrl);
        }
        databaseReference.child(DBConst.ReportTitle).setValue(ReportTitle);
        databaseReference.child(DBConst.ReportDetails).setValue(ReportDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
                }
            }
        });
    }


    public void GetReportDetailsFromPatientAccount(String UID)
    {
        if (UID.matches(DBConst.SELF))
        {
            UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.PatientAccount).child(UID).child(DBConst.MedicalReportDB);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    hashMap.put(DBConst.DataSnapshot,dataSnapshot);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetReportDetailsFromPatientAccountDB,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(DBConst.GetReportDetailsFromPatientAccountDB,hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(DBConst.GetReportDetailsFromPatientAccountDB,hashMap);
            }
        });
    }

    public void DeleteImageUrlFromPatientReportDB(String UID, String ReportTitle, String DeleteUrl, final String ResultReturnKey)
    {
        if (UID.matches(DBConst.SELF))
        {
            UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        if (DeleteUrl==null)
        {
            DeleteUrl=DBConst.UNKNOWN;
        }
        Log.d("myObs DB",UID+ReportTitle+DeleteUrl+ResultReturnKey);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.PatientAccount).child(UID).child(DBConst.MedicalReportDB).child(ReportTitle);
        databaseReference.orderByValue().equalTo(DeleteUrl).removeEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
                if (dataSnapshot.exists())
                {
                    Log.d("myError","new Data : "+dataSnapshot.toString());
                }
                else
                {
                    Log.d("myError","No  Data  Found ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d("myError","new Data : "+databaseError.toString());
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
            }
        });
    }

    public void DeleteEntireReportFileFromPatientAccountDB(String UID, String ReportTitle, final String ResultReturnKey)
    {
        if (UID.matches(DBConst.SELF))
        {
            UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        Log.d("muError",UID+" To be delete : "+ReportTitle);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.PatientAccount).child(UID).child(DBConst.MedicalReportDB);
        databaseReference.child(ReportTitle).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isComplete())
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.SUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
                }
                else
                {
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put(DBConst.RESULT,DBConst.UNSUCCESSFUL);
                    myDataInterface.GetSingleDataFromDatabase(ResultReturnKey,hashMap);
                }
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
