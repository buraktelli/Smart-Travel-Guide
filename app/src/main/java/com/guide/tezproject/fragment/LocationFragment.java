package com.guide.tezproject.fragment;

import android.R.layout;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.guide.tezproject.R;
import com.guide.tezproject.api.ArananYerService;
import com.guide.tezproject.api.callback.ArananYerCallback;
import com.guide.tezproject.api.model.MostFrequentAranan;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.*;

public class LocationFragment extends Fragment implements OnMapReadyCallback, PermissionsListener , ArananYerCallback {
    //,OnLocationClickListener

    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private LocationComponent locationComponent;
    private EditText etSearch;
    private Double originLat, originLng;

    ImageButton ibLocation, ibNavigate;
    ListView lvArananYer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getActivity().getString(R.string.mapbox_access_token));
        View v = inflater.inflate(R.layout.fragment_location, container, false);
        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        etSearch = v.findViewById(R.id.et_Search);
        lvArananYer = v.findViewById(R.id.lv_ArananYer);


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //String savedUsername = sharedPref.getString("username","uname yok");
        String savedToken = sharedPref.getString("token","token yok");

        ArananYerService arananYerService = new ArananYerService(getActivity());
        arananYerService.getMostFrequent("Bearer "+savedToken,this);


        ibLocation = v.findViewById(R.id.ib_zoomRota);
        ibLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableLocationComponent(mapboxMap.getStyle());

            }
        });

        ibNavigate = v.findViewById(R.id.ib_search);
        ibNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sehir = etSearch.getText().toString();

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                GecisFragment gecisFragment = new GecisFragment();
                Bundle args = new Bundle();
                args.putString("Sehir", sehir);
                //args.putDouble("lat",originLat);
                //args.putDouble("lng",originLng);
                gecisFragment.setArguments(args);
                fragmentTransaction.replace(R.id.container, gecisFragment).addToBackStack("tag").commit();
            }
        });

        return v;
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                UiSettings uiSettings = mapboxMap.getUiSettings();
                uiSettings.setLogoEnabled(false);
                uiSettings.setAttributionEnabled(false);
                enableLocationComponent(style);
                //originLat = locationComponent.getLastKnownLocation().getLatitude();
                //originLng = locationComponent.getLastKnownLocation().getLongitude();

            }
        });
    }

    //  -----------------------------------------------------------------------------------------------------
    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);


            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(locationComponent.getLastKnownLocation().getLatitude(), locationComponent.getLastKnownLocation().getLongitude())) // Sets the new camera position
                    .zoom(17) // Sets the zoom
                    .bearing(0) // Rotate the camera
                    .tilt(0) // Set the camera tilt
                    .build(); // Creates a CameraPosition from the builder

            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

//  -----------------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), "KONUM IZINI GEREKLI", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getActivity(), "KONUM IZINI GEREKLI", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }
//  -----------------------------------------------------------------------------------------------------

    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void getMostFrequentAranan(@NonNull ArrayList<MostFrequentAranan> mostFrequentAranans) {
        ArrayAdapter<MostFrequentAranan> veriAdaptoru=new ArrayAdapter<MostFrequentAranan>(getContext(), simple_list_item_1, mostFrequentAranans);
        lvArananYer.setAdapter(veriAdaptoru);
    }

    @Override
    public void onError2(@NonNull Throwable throwable) {

    }
}

