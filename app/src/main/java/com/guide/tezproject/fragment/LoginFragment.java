package com.guide.tezproject.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.guide.tezproject.R;
import com.guide.tezproject.api.AccountController;
import com.guide.tezproject.api.model.LoginRequest;
import com.guide.tezproject.api.model.LoginResponse;
import com.guide.tezproject.api.callback.UsernameSender;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    EditText etUsername,etPassword;
    Button btnLogin,btnRegister;
    ProgressDialog progressDialog;
    UsernameSender usernameSender;
    MenuItem logoutItem;

    public LoginFragment() {
    }

    public LoginFragment(UsernameSender usernameSender){
        this.usernameSender=usernameSender;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        etUsername = v.findViewById(R.id.et_username);
        etPassword = v.findViewById(R.id.et_password);
        btnLogin = v.findViewById(R.id.btn_login);
        btnRegister = v.findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
                RegisterFragment registerFragment= new RegisterFragment();
                fragmentTransaction.replace(R.id.container,registerFragment).addToBackStack("tag").commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://tezproject.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AccountController accountController = retrofit.create(AccountController.class);

                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Lütfen Bekleyiniz");
                progressDialog.show();

                LoginRequest loginRequest = new LoginRequest(etUsername.getText().toString(),etPassword.getText().toString());

                Call<LoginResponse> call = accountController.login(loginRequest);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()){
                            LoginResponse loginResponse = response.body();
                            if (!loginResponse.getUsername().isEmpty()){
                                Toast.makeText(getActivity(),"Basariyla giris yapildi.",Toast.LENGTH_LONG).show();

                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("username",loginResponse.getUsername());
                                editor.putString("token",loginResponse.getToken());
                                editor.commit();

                                usernameSender.getUsername(loginResponse.getUsername());

                                FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
                                LocationFragment locationFragment = new LocationFragment();
                                fragmentTransaction.replace(R.id.container,locationFragment).commit();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getActivity(),"Sunucuya baglanilamadi.",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        return v;
    }

}
