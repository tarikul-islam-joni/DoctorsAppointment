package com.tarikulislamjoni95.doctorsappointment.PatientPart;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorListFragment extends Fragment {

    View view;

    RecyclerView recyclerView;
    public DoctorListFragment() {
        // Required empty public constructor
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
        androidx.appcompat.widget.SearchView searchView=view.findViewById(R.id.search_btn);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("myError","Called from onsubmit");
                String SearchString=query.substring(0,1).toUpperCase()+query.substring(1).toLowerCase();
                ShowSearchItem(SearchString);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("myError","Called from onchange");
                ShowSearchItem(newText);
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor);
        GetData(query);
    }

    public void ShowSearchItem(String string)
    {
        Query query= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor).orderByChild(DBConst.Name).startAt(string);
        GetData(query);
    }

    private void GetData(final Query query)
    {
        final FirebaseRecyclerOptions<DoctorListModel> options=new FirebaseRecyclerOptions.Builder<DoctorListModel>().setQuery(query, DoctorListModel.class).build();
        final FirebaseRecyclerAdapter<DoctorListModel,ViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<DoctorListModel, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i, @NonNull final DoctorListModel showAllDoctorModel)
            {
                viewHolder.NameTv.setText(showAllDoctorModel.getName());
                viewHolder.CategoryTv.setText(showAllDoctorModel.getCategory());
                viewHolder.AreaTv.setText(showAllDoctorModel.getAvailableArea());
                if (!showAllDoctorModel.getImage().matches("null"))
                {
                    Picasso.get().load(showAllDoctorModel.getImage()).into(viewHolder.ImageCiv);
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        String UID=getRef(i).getKey();
                        String TitleString=showAllDoctorModel.getTitle();
                        String NameString=showAllDoctorModel.getName();
                        String ImageString=showAllDoctorModel.getImage();
                        String BMDCRegNo=showAllDoctorModel.getBMDCRegNo();
                        String CategoryString=showAllDoctorModel.getCategory();
                        String AvailableAreaString=showAllDoctorModel.getAvailableArea();
                        String NoOfYearPracString=showAllDoctorModel.getNoOfPracYear();
                        String DegreeString = showAllDoctorModel.getDegree();
                        String StudiedCollege=showAllDoctorModel.getStudiedCollege();

                        ArrayList<String> arrayList=new ArrayList<>();
                        arrayList.add(UID);
                        arrayList.add(ImageString);
                        arrayList.add(TitleString);
                        arrayList.add(NameString);
                        arrayList.add(StudiedCollege);
                        arrayList.add(DegreeString);
                        arrayList.add(BMDCRegNo);
                        arrayList.add(NoOfYearPracString);
                        arrayList.add(CategoryString);
                        arrayList.add(AvailableAreaString);

                        Intent intent=new Intent(getActivity(),DoctorProfileVisitActivity.class);
                        intent.putStringArrayListExtra(DBConst.Doctor,arrayList);
                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.model_show_doctor,parent,false);
                return new ViewHolder(view);
            }
        };

        if (firebaseRecyclerAdapter!=null)
        {
            firebaseRecyclerAdapter.startListening();
        }
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView ImageCiv;
        TextView NameTv,CategoryTv,AreaTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageCiv=itemView.findViewById(R.id.image_civ);
            NameTv=itemView.findViewById(R.id.name_tv);
            CategoryTv=itemView.findViewById(R.id.speciality_tv);
            AreaTv=itemView.findViewById(R.id.available_area_tv);
        }
    }
}
