package com.tarikulislamjoni95.doctorsappointment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PatientListAdapter extends ArrayAdapter<DataModel>
{
    DataModel dataModel;
    private Activity activity;
    private ArrayList<DataModel> arrayList;
    public PatientListAdapter(Activity activity, ArrayList<DataModel> arrayList) {
        super(activity, R.layout.patient_list_model,arrayList);
        this.activity=activity;
        this.arrayList=arrayList;
    }
    class ViewHolder
    {
        TextView NameTv,HospitalNameTv,AppointmentDateTv,AppointmentTimeTv;
    }
    ViewHolder viewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        dataModel=arrayList.get(position);
        viewHolder=new ViewHolder();
        LayoutInflater inflater=activity.getLayoutInflater();
        convertView=inflater.inflate(R.layout.patient_list_model,parent,false);
        viewHolder.NameTv=convertView.findViewById(R.id.name_tv);
        viewHolder.AppointmentDateTv=convertView.findViewById(R.id.appointment_date_tv);
        viewHolder.AppointmentTimeTv=convertView.findViewById(R.id.appointment_time_tv);
        viewHolder.HospitalNameTv=convertView.findViewById(R.id.hospital_name_tv);
        /*
        if (convertView==null)
        {
            LayoutInflater inflater=activity.getLayoutInflater();
            convertView=inflater.inflate(R.layout.patient_list_model,parent,false);
            viewHolder.NameTv=convertView.findViewById(R.id.name_tv);
            viewHolder.AppointmentDateTv=convertView.findViewById(R.id.appointment_date_tv);
            viewHolder.AppointmentTimeTv=convertView.findViewById(R.id.appointment_time_tv);
            viewHolder.HospitalNameTv=convertView.findViewById(R.id.hospital_name_tv);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        } */

        viewHolder.NameTv.setText("Patient Name : "+dataModel.getName());
        viewHolder.AppointmentDateTv.setText("Appointment Date : "+dataModel.getAppointmentDate());
        viewHolder.AppointmentTimeTv.setText("Appointment Time : "+dataModel.getAppointmentTime());
        viewHolder.HospitalNameTv.setText("Hospital Name : "+dataModel.getHospitalName());

        return convertView;
    }
}
