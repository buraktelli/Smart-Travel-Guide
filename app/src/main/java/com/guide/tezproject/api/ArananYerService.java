package com.guide.tezproject.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.guide.tezproject.api.callback.ArananYerCallback;
import com.guide.tezproject.api.callback.SearchCityCallback;
import com.guide.tezproject.api.model.MostFrequentAranan;
import com.guide.tezproject.entity.GezilecekYer;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArananYerService {

    private final String URL = "https://tezproject.herokuapp.com/";
    private Activity activity;
    ProgressDialog progressDialog;
    ArrayList<MostFrequentAranan> mostFrequentAranans;

    public ArananYerService(Activity activity) {
        this.activity = activity;
    }

    public void getMostFrequent(String token, final ArananYerCallback arananYerCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ArananYer arananYer = retrofit.create(ArananYer.class);
        progressDialog = new ProgressDialog(activity);
        progressDialog.show();
        mostFrequentAranans = new ArrayList<>();

        Call<ArrayList<MostFrequentAranan>> call = arananYer.getMostFrequent(token);

        call.enqueue(new Callback<ArrayList<MostFrequentAranan>>() {
            @Override
            public void onResponse(Call<ArrayList<MostFrequentAranan>> call, Response<ArrayList<MostFrequentAranan>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    mostFrequentAranans = response.body();
                    if (mostFrequentAranans.size() == 0) {
                        Toast.makeText(activity, "Gezilecek yer bulunamadi.", Toast.LENGTH_LONG).show();
                    } else {
                        arananYerCallback.getMostFrequentAranan(mostFrequentAranans);
                    }
                } else {
                    Toast.makeText(activity, "hatali cevap geldi", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MostFrequentAranan>> call, Throwable t) {
                Toast.makeText(activity, "Sunucuya baglanilamadi.", Toast.LENGTH_LONG).show();
            }
        });


    }

}
