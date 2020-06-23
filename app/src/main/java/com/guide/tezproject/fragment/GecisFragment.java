package com.guide.tezproject.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guide.tezproject.R;
import com.guide.tezproject.api.SearchAllService;
import com.guide.tezproject.api.SearchCountryExceptCityService;
import com.guide.tezproject.api.callback.SearchCityCallback;
import com.guide.tezproject.api.callback.SearchCountryExceptCityCallback;
import com.guide.tezproject.apimapbox.DistanceService;
import com.guide.tezproject.apimapbox.DistanceServiceCallback;
import com.guide.tezproject.apimapbox.MatrixService;
import com.guide.tezproject.apimapbox.MatrixServiceCallBack;
import com.guide.tezproject.apimapbox.RouteService;
import com.guide.tezproject.apimapbox.RouteServiceCallback;
import com.guide.tezproject.database.DatabaseHelper;
import com.guide.tezproject.database.PathDatabaseHelper;
import com.guide.tezproject.entity.GezilecekYer;
import com.guide.tezproject.entity.Nokta;
import com.guide.tezproject.entity.Sonuc;
import com.guide.tezproject.fragment.util.Product;
import com.guide.tezproject.fragment.util.ProductAdapter;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GecisFragment extends Fragment implements SearchCityCallback, MatrixServiceCallBack, SearchCountryExceptCityCallback, RouteServiceCallback, DistanceServiceCallback {

    Button rota, btnUgrak, btnGezilcek;


    ArrayList<Sonuc> sonucArray;
    Point istanbul, destination;
    String sehir;
    int count = 0;

    ProductAdapter productAdapter1, productAdapter2;

    LinearLayoutManager linearLayoutManager1, linearLayoutManager2;
    RecyclerView recyclerView;

    String savedToken;

    DatabaseHelper databaseHelper;
    ArrayList<Nokta> noktalar, noktalarPath;
    ArrayList<String> yerler;
    ArrayList<Integer> path;
    double mesafe = 0.0;

    PathDatabaseHelper pathDatabaseHelper;

    public GecisFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gecis, container, false);

        getActivity().setTitle("Gezilecek Yerler");

        rota = v.findViewById(R.id.btn_rota);
        btnUgrak = v.findViewById(R.id.btn_ugrak);
        btnGezilcek = v.findViewById(R.id.btn_gezilecek);

        pathDatabaseHelper = new PathDatabaseHelper(getContext());

        recyclerView = (RecyclerView) v.findViewById(R.id.recylerview);

        if (getArguments() != null) {
            sehir = getArguments().getString("Sehir");
        }

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        savedToken = sharedPref.getString("token", "token yok");

        SearchAllService searchAllService = new SearchAllService(getActivity());
        searchAllService.searchAll("Bearer " + savedToken, sehir, this);

/*        SearchCityService searchCityService = new SearchCityService(getActivity());
        searchCityService.searchCity("Bearer " + savedToken, sehir, this);*/


        rota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper = new DatabaseHelper(getContext());
                noktalar = new ArrayList<>();
                yerler = new ArrayList<>();
                noktalar = databaseHelper.getAllPoints();

                if (pathDatabaseHelper.deleteAll()) {
                    Toast.makeText(getContext(), "Path silindi...", Toast.LENGTH_SHORT).show();
                }

                /*Kullanýcý konumu
                orilat = locationComponent.getLastKnownLocation().getLatitude();
                orilng = locationComponent.getLastKnownLocation().getLongitude();*/

                yerler.add(Double.toString(new LatLng(40.982925, 29.169885).getLongitude()) + "," + Double.toString(new LatLng(40.982925, 29.169885).getLatitude()));

                for (int i = 0; i < noktalar.size(); i++) {
                    yerler.add(Double.toString(new LatLng(noktalar.get(i).getLatitude(), noktalar.get(i).getLongitude()).getLongitude()) + "," + Double.toString(new LatLng(noktalar.get(i).getLatitude(), noktalar.get(i).getLongitude()).getLatitude()));
                }
                if (noktalar.size() > 0) {
                    MatrixService matrixService = new MatrixService(getActivity());
                    matrixService.matrix(yerler, GecisFragment.this);
                } else {
                    Toast.makeText(getContext(), "Gezilecek yer girmelisiniz...", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnUgrak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().setTitle("Ugrak Yerler");

                recyclerView.setAdapter(productAdapter2);
                recyclerView.setLayoutManager(linearLayoutManager2);

                btnUgrak.setVisibility(View.INVISIBLE);
                btnGezilcek.setVisibility(View.VISIBLE);

            }
        });

        btnGezilcek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().setTitle("Gezilecek Yerler");

                recyclerView.setAdapter(productAdapter1);
                recyclerView.setLayoutManager(linearLayoutManager1);

                btnGezilcek.setVisibility(View.INVISIBLE);
                btnUgrak.setVisibility(View.VISIBLE);
            }
        });
        return v;
    }


    @Override
    public void getGezilecekYerler(@NonNull ArrayList<GezilecekYer> gezilecekYerler) {
        //Ýstanbul konumu
        //yerler.add(Double.toString(new LatLng(40.982925, 29.169885).getLongitude())+","+Double.toString(new LatLng(40.982925, 29.169885).getLatitude()));

        istanbul = Point.fromLngLat(29.169885, 40.982925);
        destination = Point.fromLngLat(gezilecekYerler.get(0).getLongitude(), gezilecekYerler.get(0).getLatitude());
        for(int i=0;i<gezilecekYerler.size();i++){
            gezilecekYerler.get(i).setKeywords(gezilecekYerler.get(i).getName());
        }
        productAdapter1 = new ProductAdapter(getContext(), Product.getData(gezilecekYerler));
        recyclerView.setAdapter(productAdapter1);

        linearLayoutManager1 = new LinearLayoutManager(getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager1);

        SearchCountryExceptCityService searchCountryExceptCityService = new SearchCountryExceptCityService(getActivity());
        searchCountryExceptCityService.searchGezilecekYerler("Bearer " + savedToken, gezilecekYerler.get(0).getCity(), this);
    }

    @Override
    public void data2(@NonNull ArrayList<Sonuc> turkiye) {
        RouteService routeService = new RouteService(getActivity(), turkiye);
        routeService.getKusUcusu(istanbul, destination, this);
    }

    @Override
    public void onSuccess(@NonNull ArrayList<Sonuc> value) {
        if (value.size() == 0) {
            Toast.makeText(getActivity(), "Ugrak Nokta Bulunamadi", Toast.LENGTH_LONG).show();
        } else {
            this.sonucArray = value;
            DistanceService distanceService = new DistanceService(getActivity());
            for (int l = 0; l < value.size(); l++) {
                distanceService.getRoute(value.get(l).getStepKord(), value.get(l).getGezilecekYerKord(), l, this);
            }
        }
    }

    @Override
    public void onSuccess1(@NonNull double value, int l) {

        if (count < this.sonucArray.size() - 1) {
            this.sonucArray.get(l).setDistance(value);
            count++;
        } else if (count == this.sonucArray.size() - 1) {
            this.sonucArray.get(l).setDistance(value);
            count = 0;

            Collections.sort(sonucArray);

            Set<Sonuc> s = new HashSet<Sonuc>();
            s.addAll(sonucArray);
            sonucArray = new ArrayList<Sonuc>();
            sonucArray.addAll(s);

            for(int i=0;i<sonucArray.size();i++) {
                sonucArray.get(i).setKeywords(sonucArray.get(i).getName());
            }
            productAdapter2 = new ProductAdapter(getContext(), Product.getData2(sonucArray));
            linearLayoutManager2 = new LinearLayoutManager(getContext());
            linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

            btnUgrak.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void mat(@NonNull double[][] value, int boyut) {
        Double enKucuk;
        mesafe = 0.0;
        int t = 0, o = 0;
        path = new ArrayList<>();
        noktalarPath = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < boyut; i++) {
            value[i][0] = 1000;
            value[i][i] = 1000;
        }

        for (int i = 0; i < boyut - 1; i++) {
            enKucuk = 1000.00;
            for (int j = 0; j < boyut; j++) {
                if (value[o][j] < enKucuk) {
                    enKucuk = value[o][j];
                    t = j;
                }
            }
            mesafe += value[o][t];
            o = t;
            path.add(t);

            for (int s = 0; s < boyut; s++) {
                value[s][t] = 1000;
            }
        }

        for (int y = 0; y < path.size(); y++) {
            noktalarPath.add(noktalar.get(path.get(y) - 1));
        }

        pathDatabaseHelper = new PathDatabaseHelper(getContext());
        for (int i = 0; i < noktalarPath.size(); i++) {
            if (pathDatabaseHelper.insertPoint(noktalarPath.get(i).getName(), noktalarPath.get(i).getCity(), noktalarPath.get(i).getLatitude(), noktalarPath.get(i).getLongitude())) {

            } else {
                Toast.makeText(getContext(), noktalarPath.get(i).getName() + "Rota veritabanýna eklenemedi.", Toast.LENGTH_LONG).show();
            }
        }


        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        MapFragment mapFragment = new MapFragment();
        fragmentTransaction.replace(R.id.container, mapFragment).addToBackStack("tagg").commit();


    }

    @Override
    public void onError2(@NonNull Throwable throwable) {

    }

    @Override
    public void onError(@NonNull Throwable throwable) {

    }
    @Override
    public void onError1(@NonNull Throwable throwable) {

    }
}


