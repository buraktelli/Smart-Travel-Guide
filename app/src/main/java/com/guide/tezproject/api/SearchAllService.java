package com.guide.tezproject.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.guide.tezproject.api.callback.SearchCityCallback;
import com.guide.tezproject.entity.GezilecekYer;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchAllService {

    private final String URL = "https://tezproject.herokuapp.com/";
    private Activity activity;
    ProgressDialog progressDialog;
    ArrayList<GezilecekYer> gezilecekYerler;

    public SearchAllService(Activity activity) {
        this.activity = activity;
    }

    public void searchAll(String token, String search, final SearchCityCallback searchCityCallback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SearchAll searchAll= retrofit.create(SearchAll.class);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Gezilecek Yerler Araniyor.");
        progressDialog.show();
        gezilecekYerler = new ArrayList<>();

        Call<ArrayList<GezilecekYer>> call = searchAll.getGezilecekYerler(token,search);

        call.enqueue(new Callback<ArrayList<GezilecekYer>>() {
            @Override
            public void onResponse(Call<ArrayList<GezilecekYer>> call, Response<ArrayList<GezilecekYer>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    gezilecekYerler = response.body();
                    if (gezilecekYerler.size()==0){
                        Toast.makeText(activity,"Gezilecek yer bulunamadi.",Toast.LENGTH_LONG).show();
                    }else {
                        searchCityCallback.getGezilecekYerler(gezilecekYerler);
                    }
                }else {
                    Toast.makeText(activity,"hatali cevap geldi",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GezilecekYer>> call, Throwable t) {
                Toast.makeText(activity,"Sunucuya baglanilamadi.",Toast.LENGTH_LONG).show();
            }
        });



    }

}
