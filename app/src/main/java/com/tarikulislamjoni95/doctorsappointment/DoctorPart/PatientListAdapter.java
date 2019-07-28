package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class PatientListAdapter extends ArrayAdapter<DoctorDataModel>
{
    DoctorDataModel doctorDataModel;
    private Activity activity;
    private ArrayList<DoctorDataModel> arrayList;
    public PatientListAdapter(Activity activity, ArrayList<DoctorDataModel> arrayList) {
        super(activity, R.layout.model_appointment_list,arrayList);
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
        doctorDataModel =arrayList.get(position);
        viewHolder=new ViewHolder();
        LayoutInflater inflater=activity.getLayoutInflater();
        convertView=inflater.inflate(R.layout.model_appointment_list,parent,false);
        viewHolder.NameTv=convertView.findViewById(R.id.name_tv);
        viewHolder.AppointmentDateTv=convertView.findViewById(R.id.appointment_date_tv);
        viewHolder.AppointmentTimeTv=convertView.findViewById(R.id.appointment_time_tv);
        viewHolder.HospitalNameTv=convertView.findViewById(R.id.hospital_name_tv);

        /*
        if (convertView==null)
        {
            LayoutInflater inflater=activity.getLayoutInflater();
            convertView=inflater.inflate(R.layout.model_patient_list,parent,false);
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

        viewHolder.NameTv.setText("Patient Name : "+ doctorDataModel.getName());
        viewHolder.AppointmentDateTv.setText("Appointment Date : "+ doctorDataModel.getAppointmentDate());
        viewHolder.AppointmentTimeTv.setText("Appointment Time : "+ doctorDataModel.getAppointmentTime());
        viewHolder.HospitalNameTv.setText("Hospital Name : "+ doctorDataModel.getHospitalName());

        return convertView;
    }
}
