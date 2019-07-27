package com.tarikulislamjoni95.doctorsappointment.AdminPart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tarikulislamjoni95.doctorsappointment.R;

public class AdminPanelMainActivity extends AppCompatActivity
{
    Button MultiplePatientValidity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        MultiplePatientValidity=findViewById(R.id.multiple_patient_validity);

        MultiplePatientValidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminPanelMainActivity.this, MultiplePatientValidityListViewActivity.class);
                startActivity(intent);
            }
        });
    }
}
