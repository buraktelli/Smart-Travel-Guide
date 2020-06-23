package com.guide.tezproject.apimapbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.guide.tezproject.R;
import com.guide.tezproject.entity.Sonuc;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteService {

    private static final String TAG = "RouteService";
    private final Activity activity;
    private ProgressDialog progressDialog;

    ArrayList<Point> steps ;
    ArrayList<Sonuc> gezilecekYerler ;
    ArrayList<Point> kusUcusu ;
    ArrayList<Sonuc> sonucArray;

    String str ="Rota Oluşturulup Kuş Uçuşu Yakın Noktalar Aranıyor...";

    public RouteService(Activity activity, ArrayList<Sonuc> gezilecekYerler) {
        this.activity = activity;
        this.gezilecekYerler = gezilecekYerler;
    }


    public void getKusUcusu(final Point origin, final Point destination, final RouteServiceCallback routeServiceCallback) {

        progressDialog = new ProgressDialog(activity);
        try {
            progressDialog.setMessage(new String(str.getBytes("ISO-8859-1"),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //progressDialog.setMessage(str);
        progressDialog.setCancelable(false);
        progressDialog.show();

        NavigationRoute.builder(activity)
                .accessToken(activity.getString(R.string.mapbox_access_token))
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {

                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        RouteService.this.steps = new ArrayList<Point>();
                        RouteService.this.kusUcusu = new ArrayList<Point>();

                        for (int i = 0; i < response.body().routes().get(0).legs().get(0).steps().size(); i++) {
                            for (int j = 0; j < response.body().routes().get(0).legs().get(0).steps().get(i).intersections().size(); j++) {
                                RouteService.this.steps.add(response.body().routes().get(0).legs().get(0).steps().get(i).intersections().get(j).location());
                            }
                        }
                        Sonuc sonuc;
                        sonucArray = new ArrayList<>();
                        double kus;
                        for (int j = 0; j < RouteService.this.gezilecekYerler.size(); j++) {
                            for (int i = 0; i < steps.size(); i++) {
                                kus = Math.abs(distFrom(RouteService.this.gezilecekYerler.get(j).getGezilecekYerKord().latitude(),
                                        RouteService.this.gezilecekYerler.get(j).getGezilecekYerKord().longitude(), steps.get(i).latitude(),
                                        steps.get(i).longitude()));
                                if ( kus < 10) {
                                    sonuc = new Sonuc();
                                    sonuc.setGezilecekYerKord(RouteService.this.gezilecekYerler.get(j).getGezilecekYerKord());
                                    sonuc.setStepKord(RouteService.this.steps.get(i));
                                    sonuc.setKusUcusu(kus);
                                    sonuc.setName(gezilecekYerler.get(j).getName());
                                    sonuc.setKeywords(gezilecekYerler.get(j).getKeywords());
                                    sonuc.setContent(gezilecekYerler.get(j).getContent());
                                    sonuc.setTur(gezilecekYerler.get(j).getTur());
                                    sonuc.setTurizmTur(gezilecekYerler.get(j).getTurizmTur());
                                    sonuc.setNasilGidilir(gezilecekYerler.get(j).getNasilGidilir());
                                    sonuc.setAdres(gezilecekYerler.get(j).getAdres());
                                    sonuc.setZiyaretSaatleri(gezilecekYerler.get(j).getZiyaretSaatleri());
                                    sonuc.setLatitude(gezilecekYerler.get(j).getGezilecekYerKord().latitude());
                                    sonuc.setLongitude(gezilecekYerler.get(j).getGezilecekYerKord().longitude());
                                    sonuc.setCountry(gezilecekYerler.get(j).getCountry());
                                    sonuc.setCity(gezilecekYerler.get(j).getCity());
                                    sonuc.setIlce(gezilecekYerler.get(j).getIlce());
                                    sonuc.setIconUrl(gezilecekYerler.get(j).getIconUrl());

                                    sonucArray.add(sonuc);
                                }
                            }
                        }

                        progressDialog.cancel();


                        if (routeServiceCallback != null){
                            routeServiceCallback.onSuccess(sonucArray);
                        }

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Error: " + t.getMessage());
                        if(routeServiceCallback != null){
                            routeServiceCallback.onError(t);
                        }
                    }
                });

    }

    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    public ArrayList<Point> getKusUcusu() {
        return kusUcusu;
    }
}
