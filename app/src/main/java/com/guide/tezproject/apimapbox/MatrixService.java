package com.guide.tezproject.apimapbox;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.guide.tezproject.R;
import com.guide.tezproject.fragment.MapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MatrixService {
    double[][] matris;
    int boyut;
    private Activity activity;
    public MatrixService(Activity activity){ this.activity = activity;}
    private String URL = "https://api.mapbox.com/directions-matrix/v1/mapbox/driving/";
    public void matrix(ArrayList<String> points, final MatrixServiceCallBack matrixServiceCallBack){
        int j=1;
        for ( int i=0;i<points.size();i++){
            URL = URL + points.get(i);
            if(j==1){
                URL = URL + ";";
                if(i==points.size()-2){
                    j=0;
                }
            }
        }
        URL = URL + "?annotations=distance&access_token="+activity.getString(R.string.mapbox_access_token);
        //System.out.println("**************************************************URL tamam.."+URL);
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("distances");
                            boyut = jsonArray.length();
                            matris = new double[boyut][boyut];

                            for (int i=0;i<jsonArray.length();i++){
                                for(int j=0;j<jsonArray.length();j++){
                                    JSONArray indis = jsonArray.getJSONArray(i);
                                    matris[i][j]=indis.getDouble(j)/1000;
                                }
                            }
                            if(matrixServiceCallBack != null){
                                matrixServiceCallBack.mat(MatrixService.this.matris,boyut);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,error.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("Error",error.getMessage());
            }
        });
        queue.add(request);
    }

}
