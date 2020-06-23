package com.guide.tezproject.apimapbox;

import android.app.Activity;
import android.util.Log;

import com.guide.tezproject.R;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistanceService {

    private static final String TAG = "DirectionsActivity";
    private Activity activity;
    private double distance ;

    public DistanceService(Activity activity) {
        this.activity = activity;
    }

    public void getRoute(Point origin, Point destination, final int l, final DistanceServiceCallback distanceServiceCallback) {
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
                        DistanceService.this.distance = response.body().routes().get(0).distance();
                        if(distanceServiceCallback != null){
                            distanceServiceCallback.onSuccess1(DistanceService.this.distance,l);
                        }
                    }
                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Error: " + t.getMessage());
                        if(distanceServiceCallback != null){
                            distanceServiceCallback.onError1(t);
                        }
                    }
                });
    }

    public Double getDistance() {
        return distance;
    }
}
