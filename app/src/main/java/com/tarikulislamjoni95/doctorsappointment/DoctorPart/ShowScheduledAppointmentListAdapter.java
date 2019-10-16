package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tarikulislamjoni95.doctorsappointment.DatabasePart.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.InitializationUIHelperClass;
import com.tarikulislamjoni95.doctorsappointment.PatientPart.PatientProfileVisitActivity;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowScheduledAppointmentListAdapter extends RecyclerView.Adapter<ShowScheduledAppointmentListAdapter.MyViewHolder>
{
    private TextView[] textViews;
    private View views_1,views_2;
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public MyViewHolder(View itemView)
        {
            super(itemView);
            textViews=new InitializationUIHelperClass(itemView).setTextViews(new int[]{R.id.text_view_0,R.id.text_view_1,R.id.text_view_2,R.id.text_view_3,R.id.text_view_4,R.id.text_view_5});
            views_1=itemView.findViewById(R.id.view_0);
            views_2=itemView.findViewById(R.id.view_1);
        }
    }

    private String WhichFragment;
    private Activity activity;
    private ArrayList<HashMap<String,Object>> arrayList;
    public ShowScheduledAppointmentListAdapter(String WhichFragment,Activity activity, ArrayList<HashMap<String,Object>> arrayList)
    {
        this.activity=activity;
        this.arrayList=arrayList;
        this.WhichFragment=WhichFragment;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.doctor_scheduled_appointment_list_show_model,parent,false));
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    String PreviousHospital= DBConst.UNKNOWN;
    String AppointmentDate=DBConst.UNKNOWN;
    String AppointmentTime= DBConst.UNKNOWN;
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position)
    {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap=arrayList.get(position);
        String[] DataKey={DBConst.HospitalName,DBConst.AppointmentDate,DBConst.AppointmentTime,DBConst.Name,DBConst.SerialNo,DBConst.TraxId};
        for (int i=0; i<DataKey.length; i++)
        {
            if (hashMap.containsKey(DataKey[i]))
            {
                textViews[i].setVisibility(View.VISIBLE);
                textViews[i].setText(DataKey[i] + " : " + hashMap.get(DataKey[i]).toString());
            }
        }
        if ((hashMap.get(DBConst.HospitalName).toString().matches(PreviousHospital))&&(hashMap.get(DBConst.AppointmentDate).toString().matches(AppointmentDate)) &&(hashMap.get(DBConst.AppointmentTime).toString().matches(AppointmentTime)))
        {
            textViews[0].setVisibility(View.GONE);
            textViews[1].setVisibility(View.GONE);
            textViews[2].setVisibility(View.GONE);
            views_1.setVisibility(View.GONE);
            views_2.setVisibility(View.GONE);
        }
        else
        {
            textViews[0].setVisibility(View.VISIBLE);
            textViews[1].setVisibility(View.VISIBLE);
            textViews[2].setVisibility(View.VISIBLE);
            views_1.setVisibility(View.VISIBLE);
            views_2.setVisibility(View.VISIBLE);
            PreviousHospital=hashMap.get(DBConst.HospitalName).toString();
            AppointmentDate=hashMap.get(DBConst.AppointmentDate).toString();
            AppointmentTime=hashMap.get(DBConst.AppointmentTime).toString();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(activity, PatientProfileVisitActivity.class);
                intent.putExtra(DBConst.UID,arrayList.get(position).get(DBConst.UID).toString());
                intent.putExtra(DBConst.BackActivity,DBConst.Doctor);
                activity.startActivity(intent);
            }
        });
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
