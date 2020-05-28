/**
 * Author: Mohammed Basel Nasrini
 * Last edited: 2020-05-21
 */
package com.ofthedaynews.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ofthedaynews.MapsActivity;
import com.ofthedaynews.R;
import com.ofthedaynews.controllers.MainController;


public class MainFragment extends Fragment {
    private Button btnNews;
    private Button btnWeather;
    private Button btnMaps;
    private MainController mainController;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setRetainInstance(true);
        initializeComponents(view);
        registerListeners();
        return view;
    }

    private void initializeComponents(View view) {
        btnNews = view.findViewById(R.id.btnNews);
        btnWeather = view.findViewById(R.id.btnWeather);
        btnMaps = view.findViewById(R.id.btnMap);
    }

    private void registerListeners() {
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.viewNews();
            }
        });
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);

            }
        });
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.viewWeather();
            }
        });
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }
}
