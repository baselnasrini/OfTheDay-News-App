package com.ofthedaynews;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ofthedaynews.models.NewsArticle;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<ArrayList<NewsArticle>> mNewsArticlesArr;
    private MutableLiveData<NewsArticle> clickedArticle;

    public SharedViewModel () {
        mNewsArticlesArr = new MutableLiveData<>();
        clickedArticle = new MutableLiveData<>();
    }

    public void addNewArticle(NewsArticle article){
        //mIsLoading = true;

        //mNewsArticlesArr.add(article);

        //mIsLoading.setValue(false);
    }

    public LiveData<ArrayList<NewsArticle>> getNewsArticlesArr(){
        //Log.v("getNewsArticlesArr", mNewsArticlesArr.toString());
        if(mNewsArticlesArr == null){
            mNewsArticlesArr = new MutableLiveData<>();
        }
        return mNewsArticlesArr;
    }

    public void addNewsArticles(ArrayList<NewsArticle> result) {
        mNewsArticlesArr.postValue(result);
    }

    public LiveData<NewsArticle> getArticle(){
        //Log.v("getNewsArticlesArr", mNewsArticlesArr.toString());
        return clickedArticle;
    }

    public void setClickedArticle(NewsArticle article){
        clickedArticle.postValue(article);
    }
}
