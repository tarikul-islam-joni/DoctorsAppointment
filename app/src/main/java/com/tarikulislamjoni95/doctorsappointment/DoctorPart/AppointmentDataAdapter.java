package com.tarikulislamjoni95.doctorsappointment.DoctorPart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.MyToastClass;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.VARConst;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.ArrayList;

public class AppointmentDataAdapter extends ArrayAdapter<AppointmentDataModel> implements View.OnClickListener
{
    private MyToastClass myToastClass;

    private String FromWhich;

    private AutoCompleteTextView ACT;
    private TextView tv1,tv2,tv3,tv4,tv5;
    private LinearLayout l1;
    private RadioButton rbtn;

    private AlertDialog dialog;
    private AppointmentDataModel dataModel;
    private Activity activity;
    ArrayList<AppointmentDataModel> arrayList;
    public AppointmentDataAdapter(String FromWhich,Activity activity, ArrayList<AppointmentDataModel> arrayList)
    {
        super(activity, R.layout.model_edit_appointment, arrayList);
        this.FromWhich=FromWhich;
        this.arrayList=arrayList;
        this.activity=activity;

        myToastClass=new MyToastClass(activity);

        ACT=activity.findViewById(R.id.hospital_name_et);
        tv1=activity.findViewById(R.id.select_day_tv);
        tv2=activity.findViewById(R.id.av_staring_time_tv);
        tv3=activity.findViewById(R.id.av_ending_time_tv);
        tv4=activity.findViewById(R.id.starting_date_tv);
        tv5=activity.findViewById(R.id.ending_date_tv);
        rbtn=activity.findViewById(R.id.stop_appointment_rbtn);
        l1=activity.findViewById(R.id.stop_appointment_section);
    }

    @Override
    public void onClick(View view)
    {
        int position=(Integer) view.getTag();
        dataModel=(AppointmentDataModel)getItem(position);
        String UID=dataModel.getUID();
        String HospitalName=dataModel.getHospitalName();
        switch (view.getId())
        {
            case R.id.edit_btn:
                EditAppointmentMethod(HospitalName,dataModel.getAvailableDay(),dataModel.getAppointmentTime(),dataModel.getUnavaiableSDate(),dataModel.getUnavaiableEDate());
                break;
            case R.id.delete_btn:
                DeleteAppointmentMethod(UID,HospitalName);
                break;
        }
    }

    private void EditAppointmentMethod(String HospitalName,String s1,String s2,String s3,String s4)
    {
        ACT.setText(HospitalName);
        tv1.setText(s1);
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0; i<s2.length(); i++)
        {
            if (s2.charAt(i)=='~')
            {
                tv2.setText(stringBuilder.toString());
                stringBuilder=new StringBuilder();
            }
            else
            {
                stringBuilder.append(s2.charAt(i));
            }
        }
        tv3.setText(stringBuilder.toString());
        if (s3.matches(VARConst.Unavailable_Starting_Date)&&s4.matches(VARConst.Unavailable_Ending_Date))
        {
            rbtn.setChecked(false);
            l1.setVisibility(View.GONE);
        }
        else
        {
            rbtn.setChecked(true);
            l1.setVisibility(View.VISIBLE);
            tv4.setText(s3);
            tv5.setText(s4);
        }

    }
    private void DeleteAppointmentMethod(final String UID,final String HospitalName)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are sure to delete "+HospitalName+" appointment shedule from your list ? ");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child(DBConst.AppointmentShedule).child(UID);
                reference.child(HospitalName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        DeleteFromHospitalDirectory(HospitalName,UID);
                    }
                });
            }
        });
        dialog=builder.create();
        dialog.show();
    }
    private void DeleteFromHospitalDirectory(String HospitalName,String UID)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child(DBConst.HospitalName).child(HospitalName);
        reference.child(UID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    myToastClass.LToast("Data deleted successfully");
                    if (dialog.isShowing())
                    {
                        dialog.dismiss();
                    }
                }
                else
                {
                    if (dialog.isShowing())
                    {
                        dialog.dismiss();
                    }
                }
            }
        });
    }
    class ViewHolder
    {
        TextView HospitalNameTv,AppointmentSheduleTv,UnavailableDateTv,AppointmentDayTv,AppointmentFeeTv;
        Button EditBtn,DeleteBtn;
    }
    ViewHolder viewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        dataModel=arrayList.get(position);
        viewHolder=new ViewHolder();

        LayoutInflater inflater=activity.getLayoutInflater();
        convertView=inflater.inflate(R.layout.model_edit_appointment,parent,false);
        viewHolder.HospitalNameTv=convertView.findViewById(R.id.hospital_name_tv);
        viewHolder.AppointmentDayTv=convertView.findViewById(R.id.appointment_day_tv);
        viewHolder.AppointmentSheduleTv=convertView.findViewById(R.id.appointment_shedule_tv);
        viewHolder.AppointmentFeeTv=convertView.findViewById(R.id.appointment_fee_tv);
        viewHolder.UnavailableDateTv=convertView.findViewById(R.id.unavailable_date_tv);
        viewHolder.EditBtn=convertView.findViewById(R.id.edit_btn);
        viewHolder.EditBtn.setOnClickListener(this);
        viewHolder.EditBtn.setTag(position);
        viewHolder.DeleteBtn=convertView.findViewById(R.id.delete_btn);
        viewHolder.DeleteBtn.setOnClickListener(this);
        viewHolder.DeleteBtn.setTag(position);

        if (FromWhich.matches("Show"))
        {
            viewHolder.EditBtn.setEnabled(false);
            viewHolder.DeleteBtn.setEnabled(false);
            viewHolder.EditBtn.setVisibility(View.GONE);
            viewHolder.DeleteBtn.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.EditBtn.setEnabled(true);
            viewHolder.DeleteBtn.setEnabled(true);
            viewHolder.EditBtn.setVisibility(View.VISIBLE);
            viewHolder.DeleteBtn.setVisibility(View.VISIBLE);
        }

        viewHolder.HospitalNameTv.setText("Hospital Name : "+dataModel.getHospitalName());
        viewHolder.AppointmentDayTv.setText("Available Day : "+dataModel.getAvailableDay());
        viewHolder.AppointmentSheduleTv.setText("Appointment Time : "+dataModel.getAppointmentTime());
        viewHolder.AppointmentFeeTv.setText("Appointment Fee : "+dataModel.getAppointmentFee());
        viewHolder.UnavailableDateTv.setText("Unavailable date : "+dataModel.getUnavaiableSDate()+"~"+dataModel.getUnavaiableEDate());

        return convertView;
    }
}
