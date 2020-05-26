/**
 * Author: Mohammed Basel Nasrini
 * Last edited: 2020-05-21
 */
package com.ofthedaynews.controllers;

import androidx.fragment.app.Fragment;

import com.ofthedaynews.MainActivity;
import com.ofthedaynews.ui.MainFragment;

public class MainController {
    private MainActivity activity;
    private MainFragment mainFragment;
    private NewsController newsController;
    private WeatherController weatherController;
    
    public MainController(MainActivity mainActivity) {
        this.activity = mainActivity;
        this.mainFragment = new MainFragment();
        this.mainFragment.setMainController(this);
    }


    public Fragment getMainFragment() {
        return this.mainFragment;
    }

    public void viewNews() {
        this.newsController = new NewsController(this);
        this.activity.setFragment(this.newsController.getFragment() , true);
    }

    public void viewWeather() {
        this.weatherController = new WeatherController(this);
        this.activity.setFragment(this.weatherController.getFragment() , true);
    }

    public MainActivity getActivity(){
        return this.activity;
    }
}
