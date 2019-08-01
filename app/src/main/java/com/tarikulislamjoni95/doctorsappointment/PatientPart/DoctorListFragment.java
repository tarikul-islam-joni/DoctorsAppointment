package com.tarikulislamjoni95.doctorsappointment.PatientPart;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDB;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.AccountDataModel;
import com.tarikulislamjoni95.doctorsappointment.DoctorPart.AppointmentDataModel;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.Interface.AccountDBInterface;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorListFragment extends Fragment
{

    private AccountDB accountDB;
    private ArrayList<AccountDataModel> arrayList;
    private DoctorListAdapter adapter;

    private String[] CategoryResourceArray;

    private Activity activity;
    private View view;
    private ImageView FilterIv;
    private TextView FilterTv;
    private Spinner spinner;
    private RecyclerView recyclerView;
    public DoctorListFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity=(Activity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        view= inflater.inflate(R.layout.fragment_doctor_list, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList=new ArrayList<>();
        adapter=new DoctorListAdapter(activity,arrayList);
        recyclerView.setAdapter(adapter);
        CategoryResourceArray= getResources().getStringArray(R.array.doctor_category_array);
        spinner=view.findViewById(R.id.spinner);
        spinner.setEnabled(false);

        FilterTv=view.findViewById(R.id.filter_tv);
        FilterIv=view.findViewById(R.id.filter_btn);
        FilterIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!spinner.isEnabled())
                {
                    FilterTv.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                    spinner.setEnabled(true);
                }
                else
                {
                    adapter=new DoctorListAdapter(activity,arrayList);
                    recyclerView.setAdapter(adapter);
                    FilterTv.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    spinner.setEnabled(false);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.isEnabled())
                {
                    FilterSearch(CategoryResourceArray[i]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        androidx.appcompat.widget.SearchView searchView=view.findViewById(R.id.search_btn);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()!=0)
                {
                    String SearchString=query.substring(0,1).toUpperCase()+query.substring(1).toLowerCase();
                    GetDataFromDatebase(SearchString);
                }
                else
                {
                    GetDataFromDatebase("");
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length()!=0)
                {
                    String SearchString=query.substring(0,1).toUpperCase()+query.substring(1).toLowerCase();
                    GetDataFromDatebase(SearchString);
                }
                else
                {
                    GetDataFromDatebase("");
                }

                return true;
            }
        });
    }

    private void FilterSearch(String s)
    {
        ArrayList<AccountDataModel> arrayList1=new ArrayList<>();
        for (int i=0; i<arrayList.size(); i++)
        {
            String category=arrayList.get(i).getCategory();
            String[] category_array=category.split(",",0);
            for (int k=0; k<category_array.length; k++)
            {
                if (s.matches(category_array[k]))
                {
                    arrayList1.add(arrayList.get(i));
                    break;
                }
            }
        }
        adapter=new DoctorListAdapter(activity,arrayList1);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        GetDataFromDatebase("");
    }

    private void GetDataFromDatebase(String SearchString)
    {
        GetOnlyDoctorWithSearch(SearchString);
        adapter.notifyDataSetChanged();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////DoctorList Adapter/////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static class DoctorListAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        private ArrayList<AccountDataModel> arrayList;
        private Activity activity;
        public DoctorListAdapter(Activity activity, ArrayList<AccountDataModel> arrayList)
        {
            this.activity=activity;
            this.arrayList=arrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(activity).inflate(R.layout.model_show_doctor,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
        {
            final AccountDataModel dataModel=arrayList.get(position);
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
                    ArrayList<String> arrayList1=new ArrayList<>();
                    arrayList1.add(dataModel.getUID());
                    arrayList1.add(dataModel.getProfileImageUrl());
                    arrayList1.add(dataModel.getTitle());
                    arrayList1.add(dataModel.getName());
                    arrayList1.add(dataModel.getStudiedCollege());
                    arrayList1.add(dataModel.getDegree());
                    arrayList1.add(dataModel.getCategory());
                    arrayList1.add(dataModel.getNoOfPracYear());
                    arrayList1.add(dataModel.getAvailableArea());

                    Intent intent=new Intent(activity,DoctorProfileVisitActivity.class);
                    intent.putStringArrayListExtra(DBConst.Doctor,arrayList1);
                    activity.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
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


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////Database Part////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
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
                    adapter=new DoctorListAdapter(activity,arrayList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    arrayList.clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                arrayList.clear();
            }
        });
    }
}
