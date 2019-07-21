package com.tarikulislamjoni95.doctorsappointment.AdminPanel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

public class MultiplePatientValidityActivity extends AppCompatActivity
{
    MultipleIdAdapter multipleIdAdapter;
    ArrayList<MultipleIdDataModel> arrayList1;
    ListView listView;
    ArrayList<String> arrayList;
    TextView BirthNoTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_multiple_patient_validity);
        arrayList=new ArrayList<>();
        arrayList1=new ArrayList<MultipleIdDataModel>();
        listView=findViewById(R.id.list_view);

        BirthNoTv=findViewById(R.id.birth_no_tv);
        BirthNoTv.setText(getIntent().getStringExtra(DBConst.BirthCertificateNo));
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child(DBConst.BirthNoMultiplicity).child(BirthNoTv.getText().toString());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                if (dataSnapshot.exists())
                {
                    for(DataSnapshot data : dataSnapshot.getChildren())
                    {
                        if (!data.getKey().matches(DBConst.MultipleCheck))
                        {
                            arrayList.add(data.getKey());
                        }
                    }

                    GetAllData(arrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetAllData(ArrayList<String> arrayList)
    {
        arrayList1.clear();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Patient);
        for(int i=0; i<arrayList.size(); i++)
        {
            reference.child(arrayList.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        String UID=dataSnapshot.getKey();
                        String Name=dataSnapshot.child(DBConst.Name).getValue().toString();
                        String BirthDate=dataSnapshot.child(DBConst.BirthDate).getValue().toString();
                        String ContactNo=dataSnapshot.child(DBConst.ContactNo).getValue().toString();
                        String BirthImageUrl=dataSnapshot.child(DBConst.BirthCertificateImageUrl).getValue().toString();
                        String AnotherDocImageUrl=dataSnapshot.child(DBConst.AnotherDocumentImageUrl).getValue().toString();
                        arrayList1.add(new MultipleIdDataModel(UID,Name,BirthDate,ContactNo,BirthImageUrl,AnotherDocImageUrl));
                    }

                    multipleIdAdapter=new MultipleIdAdapter(BirthNoTv.getText().toString(),arrayList1,MultiplePatientValidityActivity.this);
                    listView.setAdapter(multipleIdAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("myError",l+"");
                            switch (adapterView.getId())
                            {
                                case R.id.invalid_btn:
                                    MultipleIdDataModel dataModel=arrayList1.get(i);
                                    Log.d("myError",dataModel.getUID());
                                    break;
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}
