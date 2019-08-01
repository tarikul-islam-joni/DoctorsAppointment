package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class HospitalListFragment extends Fragment
{
    private Activity activity;
    private View view;

    private HospitalAdpter adpter;
    private ArrayList<String> arrayList;
    private RecyclerView recyclerView;
    private SearchView searchView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity=(Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_hospital_list,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchView=view.findViewById(R.id.search_btn);

        arrayList=new ArrayList<>();
        adpter=new HospitalAdpter(activity,arrayList);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adpter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()!=0)
                {
                    String SearchString=query.substring(0,1).toUpperCase()+query.substring(1).toLowerCase();
                    GetHospitalDataFromFirebase(SearchString);
                }
                else
                {
                    GetHospitalDataFromFirebase("");
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length()!=0)
                {
                    String SearchString=query.substring(0,1).toUpperCase()+query.substring(1).toLowerCase();
                    GetHospitalDataFromFirebase(SearchString);
                }
                else
                {
                    GetHospitalDataFromFirebase("");
                }
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        GetHospitalDataFromFirebase("");
    }

    public void GetHospitalDataFromFirebase(String SearchString)
    {
        Query query= FirebaseDatabase.getInstance().getReference().child(DBConst.HospitalList).orderByKey().startAt(SearchString).endAt(SearchString+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        arrayList.add(dataSnapshot1.getKey());
                    }
                    adpter=new HospitalAdpter(activity,arrayList);
                    recyclerView.setAdapter(adpter);
                    adpter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static class HospitalAdpter extends RecyclerView.Adapter<ViewHolder>
    {
        ArrayList<String> arrayList;
        Activity activity;
        public HospitalAdpter(Activity activity,ArrayList<String> arrayList)
        {
            this.activity=activity;
            this.arrayList=arrayList;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view=LayoutInflater.from(activity).inflate(R.layout.model_hospital_list,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
        {
            String HospitalName=arrayList.get(position);
            holder.textView.setText(HospitalName);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent=new Intent(activity,HospitalActivity.class);
                    intent.putExtra(DBConst.HospitalName,arrayList.get(position));
                    activity.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_view);
        }
    }
}
