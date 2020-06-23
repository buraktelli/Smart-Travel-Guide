package com.guide.tezproject;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.guide.tezproject.api.AccountController;
import com.guide.tezproject.api.model.CheckResponse;
import com.guide.tezproject.database.PathDatabaseHelper;
import com.guide.tezproject.fragment.LocationFragment;
import com.guide.tezproject.fragment.LoginFragment;
import com.guide.tezproject.api.callback.UsernameSender;
import com.guide.tezproject.fragment.MapFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UsernameSender {

    ProgressDialog progressDialog;
    View header ;
    String hUname;
    String savedUsername;
    private PathDatabaseHelper pathDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        statusCheck();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        //ImageView photo = (ImageView)header.findViewById(R.id.iv_menuUser);


        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        savedUsername = sharedPref.getString("username","uname yok");
        String savedToken = sharedPref.getString("token","token yok");

        if (savedUsername.equals("uname yok")&& savedToken.equals("token yok")){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            LoginFragment loginFragment= new LoginFragment(this);
            fragmentTransaction.add(R.id.container,loginFragment).commit();

        }else {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Dogrulama yapiliyor");
            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://tezproject.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AccountController accountController = retrofit.create(AccountController.class);

            Call<CheckResponse> call = accountController.check(savedUsername,savedToken);
            call.enqueue(new Callback<CheckResponse>() {
                @Override
                public void onResponse(Call<CheckResponse> call, Response<CheckResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()){
                        CheckResponse checkResponse= response.body();
                        if (!checkResponse.getUsername().isEmpty()){
                            SharedPreferences preferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("username");
                            editor.remove("token");
                            editor.commit();

                            MainActivity.this.hUname =checkResponse.getUsername();
                            ((TextView) header.findViewById(R.id.tv_HUname)).setText(MainActivity.this.hUname);

                            editor.putString("username",checkResponse.getUsername());
                            editor.putString("token",checkResponse.getToken());
                            editor.commit();

                            FragmentTransaction fragmentTransaction =  MainActivity.this.getSupportFragmentManager().beginTransaction();
                            LocationFragment locationFragment = new LocationFragment();
                            fragmentTransaction.replace(R.id.container,locationFragment).commit();
                        }
                    }else{
                        Toast.makeText(MainActivity.this,"CheckApi hatali cevap geldi",Toast.LENGTH_LONG).show();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        LoginFragment loginFragment= new LoginFragment(MainActivity.this);
                        fragmentTransaction.add(R.id.container,loginFragment).commit();
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<CheckResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this,"Sunucuya baglanilamadi.",Toast.LENGTH_LONG).show();
                }
            });

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            pathDatabaseHelper = new PathDatabaseHelper(this);
           if(!savedUsername.equals("uname yok")) {
               if (pathDatabaseHelper.getAllPoints().size()>0) {
                   FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                   MapFragment mapFragment = new MapFragment();
                   fragmentTransaction.replace(R.id.container, mapFragment).addToBackStack("tagg").commit();
               }
               else
                   Toast.makeText(this,"Rota Bulunamadi.",Toast.LENGTH_LONG).show();
           }else {
               Toast.makeText(this,"Lutfen Giris Yapiniz.",Toast.LENGTH_LONG).show();
           }

        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(),"MENU2",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(getApplicationContext(),"MENU3",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_tools) {
            Toast.makeText(getApplicationContext(),"MENU4",Toast.LENGTH_SHORT).show();
        } /*else if (id == R.id.nav_logout) {
            SharedPreferences preferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("username");
            editor.remove("token");
            editor.commit();

            ((TextView) header.findViewById(R.id.tv_HUname)).setText("");

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            LoginFragment loginFragment= new LoginFragment(MainActivity.this);
            fragmentTransaction.replace(R.id.container,loginFragment).commit();
        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void getUsername(@NonNull String username) {
        ((TextView) header.findViewById(R.id.tv_HUname)).setText(username);

    }

    @Override
    public void errorGetUsername(@NonNull Throwable throwable) {

    }
}
