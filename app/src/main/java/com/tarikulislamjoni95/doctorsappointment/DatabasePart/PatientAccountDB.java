package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.GetDataFromDBInterface;

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
    public void GetPatientAccountInformation(final String UID)
    {
        final HashMap<String,Object> hashMap=new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            reference.child(UID).child(DBConst.AccountInformation).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        String[] DataField={DBConst.Name,DBConst.FatherName,DBConst.MotherName,DBConst.PhoneNumber,
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



    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void GetAppointmentInformation(String UID)
    {
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void GetHistoryCloud(String UID)
    {

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
