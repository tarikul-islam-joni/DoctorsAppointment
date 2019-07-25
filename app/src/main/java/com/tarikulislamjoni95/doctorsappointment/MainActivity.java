package com.tarikulislamjoni95.doctorsappointment;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tarikulislamjoni95.doctorsappointment.AdminPanel.AdminPanelMainActivity;
import com.tarikulislamjoni95.doctorsappointment.HelperClass.DBConst;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference myRef;
    private FirebaseRecyclerOptions<ShowAllDoctorModel> options;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Initialization();
        InitializationUI();
        InitializationClass();
    }
    private void Initialization()
    {
        myRef= FirebaseDatabase.getInstance().getReference().child(DBConst.Account).child(DBConst.Doctor);
        myRef.keepSynced(true);
        options=new FirebaseRecyclerOptions.Builder<ShowAllDoctorModel>().setQuery(myRef,ShowAllDoctorModel.class).build();
    }
    private void InitializationUI()
    {
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }
    private void InitializationClass()
    {

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapterMethod();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent=new Intent(MainActivity.this,PatientProfileView.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(MainActivity.this, AdminPanelMainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(MainActivity.this,SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void FirebaseRecyclerAdapterMethod()
    {
        FirebaseRecyclerAdapter<ShowAllDoctorModel,AllDoctorModelViewHolder> adapter=new FirebaseRecyclerAdapter<ShowAllDoctorModel, AllDoctorModelViewHolder>(options)
        {

            @Override
            protected void onBindViewHolder(@NonNull AllDoctorModelViewHolder allDoctorModelViewHolder, final int position, @NonNull ShowAllDoctorModel showAllDoctorModel)
            {
                allDoctorModelViewHolder.NameTv.setText(showAllDoctorModel.getName());
                allDoctorModelViewHolder.SpecialityTv.setText(showAllDoctorModel.getCategory());
                allDoctorModelViewHolder.AvailableAreaTv.setText(showAllDoctorModel.getAvailableArea());
                if (!showAllDoctorModel.getImage().matches("null"))
                {
                    Picasso.get().load(showAllDoctorModel.getImage()).into(allDoctorModelViewHolder.ImageCiv);
                }

                allDoctorModelViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        String VisitId=getRef(position).getKey();
                        Log.d("myErroor","Visit id : "+VisitId);
                        Intent intent=new Intent(MainActivity.this, DoctorProfileVisitActivity.class);
                        intent.putExtra(DBConst.UID,VisitId);
                        startActivity(intent);
                    }
                });
            }



            @NonNull
            @Override
            public AllDoctorModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.show_all_doctor_model,parent,false);
                return new AllDoctorModelViewHolder(view);
            }
        };

        if (adapter!=null)
        {
            adapter.startListening();
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public static class AllDoctorModelViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        TextView NameTv,SpecialityTv,AvailableAreaTv;
        CircleImageView ImageCiv;
        public AllDoctorModelViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            NameTv=itemView.findViewById(R.id.name_tv);
            SpecialityTv=itemView.findViewById(R.id.speciality_tv);
            AvailableAreaTv=itemView.findViewById(R.id.available_area_tv);
            ImageCiv=itemView.findViewById(R.id.imageView);
        }

    }
}
