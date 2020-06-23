package com.guide.tezproject.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.guide.tezproject.R;
import com.guide.tezproject.api.AccountController;
import com.guide.tezproject.api.model.RegistrationRequest;
import com.guide.tezproject.api.model.RegistrationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterFragment extends Fragment {

    EditText etRegisterUN,etRegisterName,etRegisterMail,etRegisterPass,etConfirm,etYas,etCinsiyet,etRegSurname;
    Button btnRegister;
    ProgressDialog progressDialog;

    public RegisterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        etConfirm=v.findViewById(R.id.et_Confirm);
        etRegSurname = v.findViewById(R.id.et_RegisterSurn);
        etRegisterMail=v.findViewById(R.id.et_RegisterMail);
        etRegisterName=v.findViewById(R.id.et_RegisterName);
        etRegisterUN=v.findViewById(R.id.et_RegisterUN);
        etRegisterPass=v.findViewById(R.id.et_RegisterPass);
        etCinsiyet=v.findViewById(R.id.et_cinsiyet);
        etYas=v.findViewById(R.id.et_yas);
        btnRegister=v.findViewById(R.id.btn_Register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
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

                RegistrationRequest registrationRequest = new RegistrationRequest();

                registrationRequest.setName(etRegisterName.getText().toString());
                registrationRequest.setSurname(etRegSurname.getText().toString());
                registrationRequest.setUsername(etRegisterUN.getText().toString());
                registrationRequest.setPassword(etRegisterPass.getText().toString());
                registrationRequest.setEmail(etRegisterMail.getText().toString());
                registrationRequest.setDogumYili(Integer.parseInt(etYas.getText().toString()));
                registrationRequest.setCinsiyet(etCinsiyet.getText().toString());

                Call<RegistrationResponse> call = accountController.register(registrationRequest);

                call.enqueue(new Callback<RegistrationResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            RegistrationResponse registrationResponse = response.body();
                            if (registrationResponse.getSonuc()){
                                Toast.makeText(getActivity(),"Basariyla kaydolundu.",Toast.LENGTH_LONG).show();
                                FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
                                LoginFragment loginFragment= new LoginFragment();
                                fragmentTransaction.replace(R.id.container,loginFragment).commit();
                            }

                        }else {
                            Toast.makeText(getActivity(),"kayit basarisiz",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                        Toast.makeText(getActivity(),"Sunucuya baglanilamadi.",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return v;
    }

}
