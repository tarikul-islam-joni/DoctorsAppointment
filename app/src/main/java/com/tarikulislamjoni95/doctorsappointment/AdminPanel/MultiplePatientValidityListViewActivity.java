package com.tarikulislamjoni95.doctorsappointment.AdminPanel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class MultiplePatientValidityListViewActivity extends AppCompatActivity
{
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_multiple_patient_validity_lv);
        listView=findViewById(R.id.list_view);
        arrayList=new ArrayList<String>();
        DatabaseReference MuliplicityRef=FirebaseDatabase.getInstance().getReference().child(DBConst.BirthNoMultiplicity);
        MuliplicityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                arrayList.clear();
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        boolean MultipleCheck=(boolean)dataSnapshot1.child(DBConst.MultipleCheck).getValue();
                        if (MultipleCheck)
                        {
                            arrayList.add(dataSnapshot1.getKey().toString());
                        }
                    }

                    arrayAdapter=new ArrayAdapter<String>(MultiplePatientValidityListViewActivity.this,android.R.layout.simple_list_item_1,arrayList);
                    listView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String BirthNo=arrayAdapter.getItem(i).toString();
                Intent intent1=new Intent(MultiplePatientValidityListViewActivity.this,MultiplePatientValidityActivity.class);
                intent1.putExtra(DBConst.BirthCertificateNo,BirthNo);
                startActivity(intent1);
            }
        });
    }
}
