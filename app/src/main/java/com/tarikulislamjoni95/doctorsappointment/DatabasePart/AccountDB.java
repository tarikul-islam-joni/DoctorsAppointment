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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountDBInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountDB
{
    private AccountDBInterface accountDBInterface;
    private ArrayList<AccountDataModel> arrayList;
    public AccountDB(Activity activity)
    {
        accountDBInterface=(AccountDBInterface)activity;
    }
    public void GetPatientAccountData()
    {
        arrayList=new ArrayList<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Patient).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        arrayList.add(new AccountDataModel(
                                dataSnapshot.child(DBConst.Image).getValue().toString(),
                                dataSnapshot.child(DBConst.Name).getValue().toString(),
                                dataSnapshot.child(DBConst.FatherName).getValue().toString(),
                                dataSnapshot.child(DBConst.MotherName).getValue().toString(),
                                dataSnapshot.child(DBConst.ContactNo).getValue().toString(),
                                dataSnapshot.child(DBConst.Gender).getValue().toString(),
                                dataSnapshot.child(DBConst.BloodGroup).getValue().toString(),
                                dataSnapshot.child(DBConst.BirthDate).getValue().toString(),
                                dataSnapshot.child(DBConst.Address).getValue().toString(),
                                dataSnapshot.child(DBConst.BirthCertificateNo).getValue().toString(),
                                dataSnapshot.child(DBConst.BirthCertificateImageUrl).getValue().toString(),
                                dataSnapshot.child(DBConst.AnotherDocumentImageUrl).getValue().toString()
                        ));
                        accountDBInterface.GetAccount(true,arrayList);
                    }
                    else
                    {
                        arrayList=new ArrayList<>();
                        accountDBInterface.GetAccount(false,arrayList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    arrayList=new ArrayList<>();
                    accountDBInterface.GetAccount(false,arrayList);
                }
            });
        }
    }

    public void SavePatientAccountDataIntoDB(ArrayList<AccountDataModel> arrayList)
    {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put(DBConst.Image,arrayList.get(0).getProfileImageUrl());
        hashMap.put(DBConst.Name,arrayList.get(0).getName());
        hashMap.put(DBConst.FatherName,arrayList.get(0).getFatherName());
        hashMap.put(DBConst.MotherName,arrayList.get(0).getMotherName());
        hashMap.put(DBConst.ContactNo,arrayList.get(0).getContactNo());
        hashMap.put(DBConst.Gender,arrayList.get(0).getGender());
        hashMap.put(DBConst.BloodGroup,arrayList.get(0).getBloodGroup());
        hashMap.put(DBConst.Address,arrayList.get(0).getAddress());
        hashMap.put(DBConst.BirthCertificateNo,arrayList.get(0).getBirthCertificateNo());
        hashMap.put(DBConst.BirthCertificateImageUrl,arrayList.get(0).getBirthCertificateImageUrl());
        hashMap.put(DBConst.AnotherDocumentImageUrl,arrayList.get(0).getAnotherDocumentImageUrl());
        hashMap.put(DBConst.BirthDate,arrayList.get(0).getBirthDate());

        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Patient).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete())
                    {
                        accountDBInterface.AccountSavingResult(true);
                    }
                    else
                    {
                        accountDBInterface.AccountSavingResult(false);
                    }
                }
            });
        }
        else
        {
            accountDBInterface.AccountSavingResult(false);
        }
    }


    public void GetDoctorAccountData(String UID)
    {
        arrayList=new ArrayList<>();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).child(UID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        arrayList.add(new AccountDataModel
                                (
                                        dataSnapshot.child(DBConst.Image).getValue().toString(),
                                        dataSnapshot.child(DBConst.Title).getValue().toString(),
                                        dataSnapshot.child(DBConst.Name).getValue().toString(),
                                        dataSnapshot.child(DBConst.StudiedCollege).getValue().toString(),
                                        dataSnapshot.child(DBConst.Degree).getValue().toString(),
                                        dataSnapshot.child(DBConst.Category).getValue().toString(),
                                        dataSnapshot.child(DBConst.NoOfPracYear).getValue().toString(),
                                        dataSnapshot.child(DBConst.AvailableArea).getValue().toString(),
                                        dataSnapshot.child(DBConst.ContactNo).getValue().toString(),
                                        dataSnapshot.child(DBConst.BMDCRegNo).getValue().toString(),
                                        dataSnapshot.child(DBConst.NIDNo).getValue().toString(),
                                        dataSnapshot.child(DBConst.BMDCRegImageUrl).getValue().toString(),
                                        dataSnapshot.child(DBConst.NIDImageUrl).getValue().toString()
                                ));
                        accountDBInterface.GetAccount(true,arrayList);
                        return;
                    }
                    else
                    {
                        arrayList.add(new AccountDataModel());
                        accountDBInterface.GetAccount(false,arrayList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    arrayList.add(new AccountDataModel());
                    accountDBInterface.GetAccount(false,arrayList);
                }
            });
        }
        else
        {
            accountDBInterface.GetAccount(false,arrayList);
        }
    }

    public void SaveDoctorData(ArrayList<AccountDataModel> arrayList)
    {
        this.arrayList=arrayList;
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put(DBConst.Image,arrayList.get(0).getProfileImageUrl());
            hashMap.put(DBConst.Title,arrayList.get(0).getTitle());
            hashMap.put(DBConst.Name,arrayList.get(0).getName());
            hashMap.put(DBConst.StudiedCollege,arrayList.get(0).getStudiedCollege());
            hashMap.put(DBConst.Degree,arrayList.get(0).getDegree());
            hashMap.put(DBConst.Category,arrayList.get(0).getCategory());
            hashMap.put(DBConst.NoOfPracYear,arrayList.get(0).getNoOfPracYear());
            hashMap.put(DBConst.AvailableArea,arrayList.get(0).getAvailableArea());
            hashMap.put(DBConst.ContactNo,arrayList.get(0).getContactNo());
            hashMap.put(DBConst.BMDCRegNo,arrayList.get(0).getBMDCRegNo());
            hashMap.put(DBConst.NIDNo,arrayList.get(0).getNIDNo());
            hashMap.put(DBConst.BMDCRegImageUrl,arrayList.get(0).getBMDCRegImageUrl());
            hashMap.put(DBConst.NIDImageUrl,arrayList.get(0).getNIDImageUrl());
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete())
                    {
                        accountDBInterface.AccountSavingResult(true);
                    }
                    else
                    {
                        accountDBInterface.AccountSavingResult(false);
                    }
                }
            });
        }
        else
        {
            accountDBInterface.AccountSavingResult(false);
        }
    }

    public void GetOnlyDoctorWithSearch(String SearchString)
    {
        arrayList=new ArrayList<>();
        Query query=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).orderByChild(DBConst.Name).startAt(SearchString).endAt(SearchString+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList.clear();
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        arrayList.add(new AccountDataModel(
                                dataSnapshot1.getKey(),
                                dataSnapshot1.child(DBConst.Image).getValue().toString(),
                                dataSnapshot1.child(DBConst.Title).getValue().toString(),
                                dataSnapshot1.child(DBConst.Name).getValue().toString(),
                                dataSnapshot1.child(DBConst.StudiedCollege).getValue().toString(),
                                dataSnapshot1.child(DBConst.Degree).getValue().toString(),
                                dataSnapshot1.child(DBConst.Category).getValue().toString(),
                                dataSnapshot1.child(DBConst.NoOfPracYear).getValue().toString(),
                                dataSnapshot1.child(DBConst.AvailableArea).getValue().toString(),
                                dataSnapshot1.child(DBConst.ContactNo).getValue().toString(),
                                "",//dataSnapshot1.child(DBConst.BMDCRegNo).getValue().toString(),
                                //dataSnapshot1.child(DBConst.NIDNo).getValue().toString(),
                                "","",""
                                //dataSnapshot1.child(DBConst.BMDCRegImageUrl).getValue().toString(),
                                //dataSnapshot1.child(DBConst.NIDImageUrl).getValue().toString()
                        ));
                    }
                    Log.d("myError : ","Size in account db :"+arrayList.size());
                    accountDBInterface.GetAccount(true,arrayList);
                    accountDBInterface.AccountSavingResult(true);
                }
                else
                {
                    arrayList.add(new AccountDataModel());
                    accountDBInterface.GetAccount(false,arrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                arrayList.add(new AccountDataModel());
                accountDBInterface.GetAccount(false,arrayList);
            }
        });
    }

    public void SignOut()
    {
        FirebaseAuth.getInstance().signOut();
    }
}
