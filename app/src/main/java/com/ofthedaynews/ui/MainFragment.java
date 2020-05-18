package com.ofthedaynews.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ofthedaynews.R;
import com.ofthedaynews.controllers.MainController;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private Button btnNews;
    private Button btnWeather;
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
    }

    private void registerListeners() {
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainController.viewNews();
            }
        });
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }
}
