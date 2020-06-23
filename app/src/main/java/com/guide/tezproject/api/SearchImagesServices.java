package com.guide.tezproject.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.guide.tezproject.api.model.ImagesResponse;
import com.guide.tezproject.api.callback.SearchImagesCallback;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchImagesServices {

    private final String URL = "https://tezproject.herokuapp.com/";
    private Activity activity;
    ProgressDialog progressDialog;
    ArrayList<ImagesResponse> resimler;

    public SearchImagesServices(Activity activity) {
        this.activity = activity;
    }

    public void searchImages(String token, String name, String city, final SearchImagesCallback searchImagesCallback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SearchImages searchImages = retrofit.create(SearchImages.class);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Resimler bekleniyor.");
        progressDialog.show();
        resimler = new ArrayList<>();

        Call<ArrayList<ImagesResponse>> call = searchImages.getImages(token, name, city);

        call.enqueue(new Callback<ArrayList<ImagesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ImagesResponse>> call, Response<ArrayList<ImagesResponse>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    resimler = response.body();
                    if (resimler.size() == 0) {
                        Toast.makeText(activity, "Resim Bulunamadi", Toast.LENGTH_LONG).show();
                    } else {
                        searchImagesCallback.getImages(resimler);
                    }
                } else {
                    Toast.makeText(activity, "hatali cevap geldi", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ImagesResponse>> call, Throwable t) {
                Toast.makeText(activity, "Sunucuya baglanilamadi.", Toast.LENGTH_LONG).show();
            }
        });


    }

}
