package com.guide.tezproject.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guide.tezproject.R;
import com.guide.tezproject.api.SearchImagesServices;
import com.guide.tezproject.api.model.ImagesResponse;
import com.guide.tezproject.entity.GezilecekYer;
import com.guide.tezproject.entity.ViewPagerAdapter;
import com.guide.tezproject.api.callback.SearchImagesCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class ContentFragment extends Fragment implements SearchImagesCallback {

    TextView tvBaslik,tvKonum,tvContent;
    ViewPager vPager;
    ArrayList<String> resimlerUrl;
    String savedToken;
    ArrayList<ImagesResponse> resimler;

    public ContentFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View v = inflater.inflate(R.layout.fragment_content, container, false);

        tvBaslik = v.findViewById(R.id.tv_Baslik);
        tvKonum = v.findViewById(R.id.tv_Konum);
        tvContent = v.findViewById(R.id.tv_Content);
        vPager = v.findViewById(R.id.viewPager);

        GezilecekYer gezilecekYer = new GezilecekYer();
        resimlerUrl = new ArrayList<>();

        Bundle b = getArguments();
        gezilecekYer = (GezilecekYer) b.getSerializable("Gonder");

        tvBaslik.setText(gezilecekYer.getKeywords());
        tvKonum.setText(gezilecekYer.getCity()+" / "+gezilecekYer.getIlce());
        tvContent.setText(gezilecekYer.getContent());

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        savedToken = sharedPref.getString("token","token yok");

        SearchImagesServices searchImagesServices = new SearchImagesServices(getActivity());
        searchImagesServices.searchImages("Bearer "+savedToken,gezilecekYer.getKeywords(),gezilecekYer.getCity(),this);

        return v;
    }

    @Override
    public void getImages(@NonNull ArrayList<ImagesResponse> resimler) {
        for (int i=0;i<resimler.size();i++){
            resimlerUrl.add(resimler.get(i).getUrl());
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
        viewPagerAdapter.getListe(resimlerUrl);
        vPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void errorGetImages(@NonNull Throwable throwable) {

    }
}