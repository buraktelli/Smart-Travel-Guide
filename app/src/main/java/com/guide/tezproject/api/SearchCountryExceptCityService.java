package com.guide.tezproject.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.guide.tezproject.entity.Sonuc;
import com.guide.tezproject.api.callback.SearchCountryExceptCityCallback;
import com.mapbox.geojson.Point;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchCountryExceptCityService {

    private final String URL = "https://tezproject.herokuapp.com/";
    private Activity activity;
    ProgressDialog progressDialog;
    ArrayList<Sonuc> turkiye;

    public SearchCountryExceptCityService(Activity activity) {
        this.activity = activity;
    }

    public void searchGezilecekYerler(String token, String city, final SearchCountryExceptCityCallback searchCountryExceptCityCallback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SearchCountryExceptCity searchCountryExceptCity= retrofit.create(SearchCountryExceptCity.class);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Gezilecek Yerler Araniyor.");
        progressDialog.show();
        turkiye = new ArrayList<>();

        Call<ArrayList<Sonuc>> call = searchCountryExceptCity.getGezilecekYerler(token,city);

        call.enqueue(new Callback<ArrayList<Sonuc>>() {
            @Override
            public void onResponse(Call<ArrayList<Sonuc>> call, Response<ArrayList<Sonuc>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    turkiye = response.body();
                    for (int i=0;i<turkiye.size();i++){
                        turkiye.get(i).setGezilecekYerKord(Point.fromLngLat(turkiye.get(i).getLongitude(),turkiye.get(i).getLatitude()));
                    }
                    if (turkiye.size()==0){
                        Toast.makeText(activity,"Gezilecek yer bulunamadi.",Toast.LENGTH_LONG).show();
                    }else {
                        searchCountryExceptCityCallback.data2(turkiye);
                    }
                }else {
                    Toast.makeText(activity,"hatali cevap geldi",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Sonuc>> call, Throwable t) {
                Toast.makeText(activity,"Sunucuya baglanilamadi.",Toast.LENGTH_LONG).show();
            }
        });



    }

}
