package com.guide.tezproject.fragment;


import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.guide.tezproject.R;
import com.guide.tezproject.database.PathDatabaseHelper;
import com.guide.tezproject.entity.Nokta;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.ui.v5.listeners.RouteListener;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

import java.util.ArrayList;

import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationFragment extends Fragment implements NavigationListener, ProgressChangeListener, OnNavigationReadyCallback, RouteListener ,Callback<DirectionsResponse> {

    public NavigationFragment() { }

    private static final String TAG = "DirectionsActivity";

    private NavigationView navigationView;

    private Point ORIGIN;
    private Point DESTINATION;

    private PathDatabaseHelper pathDatabaseHelper;
    private ArrayList<Nokta> path;

    int i=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navigation, container, false);
        getActivity().setTitle("Navigasyon");
        navigationView = v.findViewById(R.id.navigationView);

        Bundle bundle = getArguments();
        ORIGIN = (Point) bundle.getSerializable("origin");

        path = new ArrayList<>();
        pathDatabaseHelper = new PathDatabaseHelper(getContext());
        path = pathDatabaseHelper.getAllPoints();
        DESTINATION = Point.fromLngLat(path.get(0).getLongitude(), path.get(0).getLatitude());
        //DESTINATION =Point.fromLngLat(32.657688, 41.218002);

        CameraPosition initialPosition = new CameraPosition.Builder()
                .target(new LatLng(ORIGIN.latitude(), ORIGIN.longitude()))
                .zoom(16)
                .build();
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this, initialPosition);
        return v;
    }

    private void startNavigation(DirectionsRoute directionsRoute) {
        NavigationViewOptions.Builder options =
                NavigationViewOptions.builder()
                        .navigationListener(this)
                        .routeListener(this)
                        .directionsRoute(directionsRoute)
                        .shouldSimulateRoute(true)
                        .progressChangeListener(this);
        navigationView.startNavigation(options.build());
    }

    private void fetchRoute() {
        NavigationRoute.builder(getActivity())
                .accessToken(getActivity().getString(R.string.mapbox_access_token))
                .origin(ORIGIN)
                .destination(DESTINATION)
                .alternatives(true)
                .build()
                .getRoute(this);
    }

    @Override
    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        // You can get the generic HTTP info about the response
        Log.d(TAG, "Response code: " + response.code());
        if (response.body() == null) {
            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
            return;
        } else if (response.body().routes().size() < 1) {
            Log.e(TAG, "No routes found");
            return;
        }
        DirectionsRoute directionsRoute = response.body().routes().get(0);
        startNavigation(directionsRoute);
    }
    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
        Log.e(TAG, "Error: " + t.getMessage());
    }

//  ----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean allowRerouteFrom(Point offRoutePoint) {
        return false;
    }

    @Override
    public void onOffRoute(Point offRoutePoint) {
    }

    @Override
    public void onRerouteAlong(DirectionsRoute directionsRoute) {
    }

    @Override
    public void onFailedReroute(String errorMessage) {
    }

    @Override
    public void onArrival() {
        if (i==0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Haritaya donmek ister misiniz?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            pathDatabaseHelper.deletePoint(path.get(0).getName(), path.get(0).getCity());
                            dialog.cancel();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            MapFragment mapFragment = new MapFragment();
                            fragmentTransaction.replace(R.id.container, mapFragment).commit();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
            Toast.makeText(getContext(), "arrival", Toast.LENGTH_LONG).show();
            i++;
        }
    }

//  ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onProgressChange(Location location, RouteProgress routeProgress) {
    }

    @Override
    public void onNavigationReady(boolean isRunning) {
        fetchRoute();
    }

    @Override
    public void onCancelNavigation() {
    }

    @Override
    public void onNavigationFinished() {
    }

    @Override
    public void onNavigationRunning() {
    }

//  ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        navigationView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigationView.onDestroy();
    }


}