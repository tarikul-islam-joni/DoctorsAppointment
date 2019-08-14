package com.tarikulislamjoni95.doctorsappointment.DatabasePart;

import android.app.Activity;
import android.hardware.camera2.DngCreator;

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
import com.tarikulislamjoni95.doctorsappointment.Interface.PatientAccountDBInterface;

import java.util.HashMap;

public class PatientAccountDB
{
    private Activity activity;
    private PatientAccountDBInterface patientAccountDBInterface;
    public PatientAccountDB(Activity activity)
    {
        this.activity=activity;
        patientAccountDBInterface=(PatientAccountDBInterface)activity;
    }

    public void GetPatientAccountInformation(final String UID)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.PatientAccount).child(UID).child(DBConst.AccountInformation);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        PatientAccountDM patientAccountDM=new PatientAccountDM(
                                UID,
                                dataSnapshot.child(DBConst.ProfileImageUrl).getValue().toString(),
                                dataSnapshot.child(DBConst.Name).getValue().toString(),
                                dataSnapshot.child(DBConst.FatherName).getValue().toString(),
                                dataSnapshot.child(DBConst.MotherName).getValue().toString(),
                                dataSnapshot.child(DBConst.PhoneNumber).getValue().toString(),
                                dataSnapshot.child(DBConst.Gender).getValue().toString(),
                                dataSnapshot.child(DBConst.DateOfBirth).getValue().toString(),
                                dataSnapshot.child(DBConst.BloodGroup).getValue().toString(),
                                dataSnapshot.child(DBConst.Address).getValue().toString(),
                                dataSnapshot.child(DBConst.BirthCertificateNumber).getValue().toString(),
                                dataSnapshot.child(DBConst.BirthCertificateImageUrl).getValue().toString(),
                                dataSnapshot.child(DBConst.AnotherDocumentImageUrl).getValue().toString()

                        );
                        patientAccountDBInterface.GetPatientAccountData(DBConst.DATA_EXIST,patientAccountDM);
                    }
                    else
                    {
                        PatientAccountDM patientAccountDM=new PatientAccountDM();
                        patientAccountDBInterface.GetPatientAccountData(DBConst.DATA_NOT_EXIST,patientAccountDM);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    PatientAccountDM patientAccountDM=new PatientAccountDM();
                    patientAccountDBInterface.GetPatientAccountData(DBConst.DATABASE_ERROR,patientAccountDM);
                }
            });
        }
        else
        {
            PatientAccountDM patientAccountDM=new PatientAccountDM();
            patientAccountDBInterface.GetPatientAccountData(DBConst.NULL_USER,patientAccountDM);
        }
    }

    public void SaveAccountInformation(String UID, final PatientAccountDM patientAccountDM)
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.PatientAccount).child(UID).child(DBConst.AccountInformation);
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put(DBConst.ProfileImageUrl,patientAccountDM.getProfileImageUrl());
            hashMap.put(DBConst.Name,patientAccountDM.getName());
            hashMap.put(DBConst.FatherName,patientAccountDM.getFatherName());
            hashMap.put(DBConst.MotherName,patientAccountDM.getMotherName());
            hashMap.put(DBConst.PhoneNumber,patientAccountDM.getPhoneNumber());
            hashMap.put(DBConst.Gender,patientAccountDM.getGender());
            hashMap.put(DBConst.DateOfBirth,patientAccountDM.getDateOfBirth());
            hashMap.put(DBConst.BloodGroup,patientAccountDM.getBloodGroup());
            hashMap.put(DBConst.Address,patientAccountDM.getAddress());
            hashMap.put(DBConst.BirthCertificateNumber,patientAccountDM.getBirthNumber());
            hashMap.put(DBConst.BirthCertificateImageUrl,patientAccountDM.getBirthNumberImageUrl());
            hashMap.put(DBConst.AnotherDocumentImageUrl,patientAccountDM.getAnotherDocumentImageUrl());
            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete())
                    {
                        patientAccountDBInterface.GetPatientAccountData(DBConst.SUCCESSFULL,patientAccountDM);
                    }
                    else
                    {
                        patientAccountDBInterface.GetPatientAccountData(DBConst.UNSUCCESSFULL,patientAccountDM);
                    }
                }
            });
        }
        else
        {
            patientAccountDBInterface.GetPatientAccountData(DBConst.NULL_USER,patientAccountDM);
        }
    }
}
