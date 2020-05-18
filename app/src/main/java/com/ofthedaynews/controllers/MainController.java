package com.ofthedaynews.controllers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ofthedaynews.MainActivity;
import com.ofthedaynews.ui.MainFragment;
import com.ofthedaynews.R;

public class MainController {
    private MainActivity activity;
    private MainFragment mainFragment;
    private NewsController newsController;
    
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
    
    public void setFragment(Fragment frag){
        FragmentManager fragmentManager = ((MainActivity)this.mainFragment.getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public MainActivity getActivity(){
        return this.activity;
    }
}
