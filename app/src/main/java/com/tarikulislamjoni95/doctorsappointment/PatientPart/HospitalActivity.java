package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalActivity extends AppCompatActivity
{
    private ArrayList<PatientAccountDataModel> arrayList;
    private HospitalDoctorListAdapter adapter;

    private String HospitalNameString;
    private TextView HospitalNameTv;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        HospitalNameTv=findViewById(R.id.hospital_name_tv);
        HospitalNameString=getIntent().getStringExtra(DBConst.HospitalName);
        HospitalNameTv.setText(HospitalNameString);

        arrayList=new ArrayList<>();
        adapter=new HospitalDoctorListAdapter(HospitalActivity.this,arrayList);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GetDoctorList(HospitalNameString);
    }

    private void GetDoctorList(String hospitalNameString)
    {
        arrayList=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.HospitalList).child(hospitalNameString);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Log.d("myError1","datasnapshot : "+dataSnapshot.getValue().toString());
                arrayList.clear();
                if (dataSnapshot.exists())
                {
                    Log.d("myError1",dataSnapshot.getValue().toString()+" UID "+dataSnapshot.getKey());
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        Log.d("myError2",dataSnapshot1.getValue().toString()+" UID "+dataSnapshot1.getKey());
                        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).child(dataSnapshot1.getKey());
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                arrayList.clear();
                                if (dataSnapshot.exists())
                                {
                                    Log.d("myError",dataSnapshot.getValue().toString()+" UID "+dataSnapshot.getKey());
                                    arrayList.add(new PatientAccountDataModel(
                                            dataSnapshot.getKey(),
                                            dataSnapshot.child(DBConst.Image).getValue().toString(),
                                            dataSnapshot.child(DBConst.Title).getValue().toString(),
                                            dataSnapshot.child(DBConst.Name).getValue().toString(),
                                            dataSnapshot.child(DBConst.StudiedCollege).getValue().toString(),
                                            dataSnapshot.child(DBConst.Degree).getValue().toString(),
                                            dataSnapshot.child(DBConst.Category).getValue().toString(),
                                            dataSnapshot.child(DBConst.NoOfPracYear).getValue().toString(),
                                            dataSnapshot.child(DBConst.AvailableArea).getValue().toString(),
                                            dataSnapshot.child(DBConst.ContactNo).getValue().toString(),
                                            //dataSnapshot.child(DBConst.BMDCRegNo).getValue().toString(),
                                            //dataSnapshot.child(DBConst.NIDNo).getValue().toString(),
                                            //dataSnapshot.child(DBConst.BMDCRegImageUrl).getValue().toString(),
                                           //dataSnapshot.child(DBConst.NIDImageUrl).getValue().toString()
                                            "","","",""));
                                }
                                adapter=new HospitalDoctorListAdapter(HospitalActivity.this,arrayList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        public View mView;
        public CircleImageView ImageCiv;
        public TextView NameTv,CategoryTv,AreaTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            ImageCiv=itemView.findViewById(R.id.image_civ);
            NameTv=itemView.findViewById(R.id.name_tv);
            CategoryTv=itemView.findViewById(R.id.speciality_tv);
            AreaTv=itemView.findViewById(R.id.available_area_tv);
        }
    }
    public static class HospitalDoctorListAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        private Activity activity;
        private ArrayList<PatientAccountDataModel> arrayList;
        public HospitalDoctorListAdapter(Activity activity,ArrayList<PatientAccountDataModel> arrayList)
        {
            this.activity=activity;
            this.arrayList=arrayList;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view= LayoutInflater.from(activity).inflate(R.layout.model_show_doctor,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            final PatientAccountDataModel dataModel=arrayList.get(position);
            if (!dataModel.getProfileImageUrl().matches("null"))
            {
                Picasso.get().load(dataModel.getProfileImageUrl()).into(holder.ImageCiv);
            }
            holder.NameTv.setText(dataModel.getName());
            holder.CategoryTv.setText(dataModel.getCategory());
            holder.AreaTv.setText(dataModel.getAvailableArea());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    GetAccountStatusAndGotoDoctorProfileVisit(arrayList.get(0));
                }
            });
        }

        private void GetAccountStatusAndGotoDoctorProfileVisit(final PatientAccountDataModel patientAccountDataModel)
        {
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.AccountStatus).child(patientAccountDataModel.getUID());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    boolean AuthorityValidity=(boolean)dataSnapshot.child(DBConst.AuthorityValidity).getValue();
                    ArrayList<String> arrayList1=new ArrayList<>();
                    arrayList1.add(String.valueOf(AuthorityValidity));
                    arrayList1.add(patientAccountDataModel.getUID());
                    arrayList1.add(patientAccountDataModel.getProfileImageUrl());
                    arrayList1.add(patientAccountDataModel.getTitle());
                    arrayList1.add(patientAccountDataModel.getName());
                    arrayList1.add(patientAccountDataModel.getStudiedCollege());
                    arrayList1.add(patientAccountDataModel.getDegree());
                    arrayList1.add(patientAccountDataModel.getCategory());
                    arrayList1.add(patientAccountDataModel.getNoOfPracYear());
                    arrayList1.add(patientAccountDataModel.getAvailableArea());

                    Intent intent=new Intent(activity,DoctorProfileVisitActivity.class);
                    intent.putStringArrayListExtra(DBConst.Doctor,arrayList1);
                    activity.startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

}
