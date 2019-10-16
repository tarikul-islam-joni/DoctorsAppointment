package com.tarikulislamjoni95.doctorsappointment.PatientPart;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentDoctorProfileDetails extends Fragment
{
    private TextView[] textViews;
    private Activity activity;
    private View view;
    private RecyclerView recyclerView;
    private String DoctorUID;
    public FragmentDoctorProfileDetails(String DoctorUID)
    {
        this.DoctorUID=DoctorUID;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        activity=(Activity)context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.patient_doctor_profile_details,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitializationUI();
    }
    private void InitializationUI()
    {
        InitializationUIHelperClass initializationUIHelperClass=new InitializationUIHelperClass(view);
        int[] id={R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3,R.id.text_view_4,R.id.text_view_5,R.id.text_view_6,R.id.text_view_7};
        textViews=initializationUIHelperClass.setTextViews(id);

        recyclerView=view.findViewById(R.id.recycler_view_0);
        recyclerView.setLayoutManager(new GridLayoutManager(activity,1,RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
    }


    public void GetDataFromActivity(String WhichMethod, ArrayList<HashMap<String,Object>> arrayList)
    {
        switch (WhichMethod)
        {
            case DBConst.GetDoctorAccountInformation:
                SetDataToTheField(arrayList);
                break;
            case DBConst.GetRatingsAndReviewInfoFromDoctorAccountDB:
                SetDataToTheRatingAndReviewRecyclerView(arrayList);
                break;
        }
    }
    private void SetDataToTheField(ArrayList<HashMap<String,Object>> arrayList)
    {
        if (arrayList.size()!=0)
        {
            HashMap<String,Object> hashMap=arrayList.get(0);
            String[] DataKey={DBConst.Name,DBConst.StudiedCollege,DBConst.PassingYear,DBConst.CompletedDegree,DBConst.NoOfPracticingYear,DBConst.Specialization,DBConst.AvailableArea,DBConst.AuthorityValidity};
            for(int i=0; i<DataKey.length; i++)
            {
                if (hashMap.containsKey(DataKey[i]))
                {
                    textViews[i].setText(hashMap.get(DataKey[i]).toString());
                }
                else
                {
                    textViews[i].setText(DBConst.UNKNOWN);
                }

                if (i==(DataKey.length-1))
                {
                    if (hashMap.get(DataKey[i]).toString().matches(DBConst.VERIFIED))
                    {
                        textViews[i].setTextColor(ContextCompat.getColor(activity,R.color.colorGreen));
                    }
                    else
                    {
                        textViews[i].setTextColor(ContextCompat.getColor(activity,R.color.colorRed));
                    }
                }
            }

        }
    }
    private void SetDataToTheRatingAndReviewRecyclerView(ArrayList<HashMap<String, Object>> arrayList)
    {
        MyAdapter myAdapter=new MyAdapter(activity,arrayList);
        recyclerView.setAdapter(myAdapter);
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView circleImageView;
        private TextView[] textViews;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.image_civ_0);
            textViews=new InitializationUIHelperClass(itemView).setTextViews(new int[]{R.id.text_view_0,R.id.text_view_1,R.id.text_view_2});
        }
    }
    class MyAdapter extends RecyclerView.Adapter<ViewHolder>
    {

        private Context context;
        private ArrayList<HashMap<String, Object>> arrayList;
        public MyAdapter(Context context,ArrayList<HashMap<String, Object>> arrayList)
        {
            this.context=context;
            this.arrayList=arrayList;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.patient_show_doctor_rating_and_review_model,parent,false));
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap=arrayList.get(position);
            if (hashMap.containsKey(DBConst.Name))
            {
                holder.textViews[0].setText("Name : "+hashMap.get(DBConst.Name).toString());
            }
            if (hashMap.containsKey(DBConst.ProfileImageUrl))
            {
                if (!hashMap.get(DBConst.ProfileImageUrl).toString().matches(DBConst.UNKNOWN))
                {
                    Picasso.get().load(hashMap.get(DBConst.ProfileImageUrl).toString()).into(holder.circleImageView);
                }
            }

            if (hashMap.containsKey(DBConst.Ratings))
            {
                holder.textViews[1].setText("Rating : "+hashMap.get(DBConst.Ratings).toString()+"/5");
            }
            if (hashMap.containsKey(DBConst.Reviews))
            {
                holder.textViews[2].setText("Review : "+hashMap.get(DBConst.Reviews).toString());
            }
        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
}
