package com.guide.tezproject.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guide.tezproject.R;
import com.guide.tezproject.database.DatabaseHelper;
import com.guide.tezproject.database.PathDatabaseHelper;
import com.guide.tezproject.entity.Nokta;
import com.guide.tezproject.apimapbox.MatrixService;
import com.guide.tezproject.apimapbox.MatrixServiceCallBack;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


public class MapFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {

    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";

    private MapboxMap mapboxMap;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private LocationComponent locationComponent;
    private PathDatabaseHelper pathDatabaseHelper;
    private ArrayList<Nokta> path;

    private Point origin;
    private Point destination;
    ArrayList<Point> coords;

    private static final String TAG = "DirectionsActivity";

    private double orilng,orilat;

    public MapFragment() {}

    Button btnNavigasyon;
    TextView tvNoktaSayisi,tvToplamMesafe,tvSiradakiMesafe,tvSiradakiDurak;
    ImageButton ibZoomRota;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getActivity().getString(R.string.mapbox_access_token));
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        getActivity().setTitle("Rota Bilgileri");
        mapView = v.findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        btnNavigasyon = v.findViewById(R.id.btn_navigasyon);
        tvNoktaSayisi = v.findViewById(R.id.tv_noktaSayisi);
        tvToplamMesafe = v.findViewById(R.id.tv_toplamMesafe);
        tvSiradakiMesafe = v.findViewById(R.id.tv_siradakiMesafe);
        tvSiradakiDurak = v.findViewById(R.id.tv_siradakidurak);
        ibZoomRota = v.findViewById(R.id.ib_zoomRota);
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
        path = new ArrayList<>();
        pathDatabaseHelper = new PathDatabaseHelper(getContext());
        path = pathDatabaseHelper.getAllPoints();

        coords = new ArrayList<>();
        for (int i = 0;i<path.size()-1;i++){
            coords.add(Point.fromLngLat(path.get(i).getLongitude(),path.get(i).getLatitude()));
        }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------
        if(path.isEmpty()){
            btnNavigasyon.setVisibility(View.INVISIBLE);
        }else{
            btnNavigasyon.setText(path.get(0).getName()+" için Navigasyonu Baslat");
            btnNavigasyon.setVisibility(View.VISIBLE);
            destination = Point.fromLngLat(path.get(path.size()-1).getLongitude(),path.get(path.size()-1).getLatitude());
        }

        btnNavigasyon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Point origin = Point.fromLngLat(orilng,orilat);*/

                FragmentTransaction fragmentTransaction =  getActivity().getSupportFragmentManager().beginTransaction();
                NavigationFragment navigationFragment = new NavigationFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("origin",origin);
                navigationFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.container,navigationFragment).commit();
            }
        });

        ibZoomRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLngBounds latLngBounds = new LatLngBounds.Builder()
                        .include(new LatLng(origin.latitude(),origin.longitude()))
                        .include(new LatLng(destination.latitude(),destination.longitude()))
                        .build();

                mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100), 5000);
            }
        });

        return v;
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap ;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                UiSettings uiSettings = mapboxMap.getUiSettings();
                uiSettings.setLogoEnabled(false);
                uiSettings.setAttributionEnabled(false);

                enableLocationComponent(style);
                /*orilat = locationComponent.getLastKnownLocation().getLatitude();
                orilng = locationComponent.getLastKnownLocation().getLongitude();
                origin = Point.fromLngLat(orilng,orilat);*/
                origin = Point.fromLngLat(29.169885,40.982925);

                if(path.size()==0){
                    Toast.makeText(getActivity(),"Rota tamamlandi...",Toast.LENGTH_LONG).show();
                }else{


                    initSource(style);
                    initLayers(style);

                    getRoute(origin,destination);
                    //setCameraPosition(new LatLng(orilat,orilng));
                }

            }
        });
    }

    private void initSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID, FeatureCollection.fromFeatures(new Feature[] {})));

        Feature[] features = new Feature[path.size()];
        //features[0] = Feature.fromGeometry(Point.fromLngLat(origin.longitude(), origin.latitude()));
        for (int i=0;i<path.size();i++){
            features[i] = Feature.fromGeometry(Point.fromLngLat(path.get(i).getLongitude(), path.get(i).getLatitude()));
        }

        GeoJsonSource iconGeoJsonSource = new GeoJsonSource(ICON_SOURCE_ID, FeatureCollection.fromFeatures(features));
        loadedMapStyle.addSource(iconGeoJsonSource);
    }

    private void initLayers(@NonNull Style loadedMapStyle) {
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        routeLayer.setProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(5f),
                lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);

        loadedMapStyle.addImage(RED_PIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.mipmap.red_marker)));

        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(RED_PIN_ICON_ID),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[] {0f, -9f})));
    }

    private void getRoute(final Point origin, final Point destination) {
        NavigationRoute.Builder builder = NavigationRoute.builder(getActivity())
                .accessToken(getActivity()
                        .getString(R.string.mapbox_access_token))
                .profile("driving")
                .origin(origin)
                .destination(destination);
        for (Point waypoint : coords) {
            builder.addWaypoint(waypoint);
        }
        builder.build().getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                currentRoute = response.body().routes().get(0);
                DecimalFormat df = new DecimalFormat("0.00");
                tvSiradakiDurak.setText(path.get(0).getName());
                tvNoktaSayisi.setText(""+path.size());
                tvToplamMesafe.setText(df.format(response.body().routes().get(0).distance()/1000)+" km" );
                tvSiradakiMesafe.setText(df.format(response.body().routes().get(0).legs().get(0).distance()/1000)+" km");
                if (mapboxMap != null) {
                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

                            GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);

                            if (source != null) {
                                Timber.d("onResponse: source != null");
                                source.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromPolyline(currentRoute.geometry(), PRECISION_6))));
                            }
                        }
                    });

                    LatLngBounds latLngBounds = new LatLngBounds.Builder()
                            .include(new LatLng(origin.latitude(),origin.longitude()))
                            .include(new LatLng(destination.latitude(),destination.longitude()))
                            .build();

                    mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100), 5000);

                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });

    }

    private void setCameraPosition(LatLng coord) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(coord.getLatitude(), coord.getLongitude()), 13));
    }


//  -----------------------------------------------------------------------------------------------------

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
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
            Toast.makeText(getActivity(), "KONUM IZNI VERILMEDI,IZIN GEREKLI", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), "Konum izni olmadan bu uygulama çalýþmaz.", Toast.LENGTH_LONG).show();
    }

//  -----------------------------------------------------------------------------------------------------

    @Override
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
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
