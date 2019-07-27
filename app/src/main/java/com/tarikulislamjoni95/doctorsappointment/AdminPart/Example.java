package com.tarikulislamjoni95.doctorsappointment.AdminPart;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.tarikulislamjoni95.doctorsappointment.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Example extends AppCompatActivity
{
    int aaa;
    DatePickerDialog datePickerDialog;

    Button SelectBtn;
    TextView ShowDateTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example);

        SelectBtn=findViewById(R.id.select_btn);
        ShowDateTv=findViewById(R.id.show_date);

        aaa=Character.getNumericValue('5');
        //Log.d("myError","aaa : "+aaa);

        SelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Calendar calendar=Calendar.getInstance();
                int date=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);



                Map<String,Object> map=new HashMap<>();
                map.put("time",ServerValue.TIMESTAMP);

                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Example");
                reference.child("New")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                long time=(long)dataSnapshot.getValue();
                                Log.d("myError",time+"::::"+(time+5));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                DatePickerDialog.OnDateSetListener DatePickerListener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
                    {
                        ShowDateTv.setText(i+"/"+i1+"/"+i2);
                        //ShowPossibility(i2,i1+1,i);
                    }
                };

                datePickerDialog=new DatePickerDialog(Example.this,DatePickerListener,date,month,year);

                calendar.add(calendar.DATE,+1);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                calendar.add(Calendar.DATE,+60);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

    }
}
