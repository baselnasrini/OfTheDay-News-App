/**
 * Author: Mohammed Basel Nasrini
 * Last edited: 2020-05-21
 */
package com.ofthedaynews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ofthedaynews.models.NewsArticle;
import com.ofthedaynews.models.WeatherForecast;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<ArrayList<NewsArticle>> mNewsArticlesArr;
    private MutableLiveData<NewsArticle> mClickedArticle;
    private MutableLiveData<String> mNewsUrl;
    private MutableLiveData<Boolean> isCountryFilterOn;
    private MutableLiveData<Boolean> isFilterOn;
    private MutableLiveData<Integer> selectedCountry;
    private MutableLiveData<boolean[]> selectedSources;
    private MutableLiveData<WeatherForecast> weatherForecast;

    public SharedViewModel () {
        mNewsArticlesArr = new MutableLiveData<>();
        mClickedArticle = new MutableLiveData<>();
        mNewsUrl = new MutableLiveData<>();
        isCountryFilterOn = new MutableLiveData<>();
        isFilterOn = new MutableLiveData<>();
        selectedCountry = new MutableLiveData<>();
        selectedSources = new MutableLiveData<>();
        weatherForecast = new MutableLiveData<>();
    }

    public LiveData<ArrayList<NewsArticle>> getNewsArticlesArr(){
        if(mNewsArticlesArr == null){
            mNewsArticlesArr = new MutableLiveData<>();
        }
        return mNewsArticlesArr;
    }

    public void setNewsArticlesArr(ArrayList<NewsArticle> result) {
        mNewsArticlesArr.setValue(result);
        mNewsArticlesArr.postValue(result);
    }

    public LiveData<NewsArticle> getClickedArticle(){
        return mClickedArticle;
    }

    public void setClickedArticle(NewsArticle article){
        mClickedArticle.postValue(article);
    }

    public LiveData<String> getNewsUrl(){
        return mNewsUrl;
    }

    public void setNewsUrl(String url){
        mNewsUrl.setValue(url);
        mNewsUrl.postValue(url);
    }

    public LiveData<Boolean> getIsCountryFilterOn (){
        return isCountryFilterOn;
    }

    public void setIsCountryFilterOn(Boolean bool){
        isCountryFilterOn.setValue(bool);
        isCountryFilterOn.postValue(bool);
    }

    public LiveData<Boolean> getIsFilterOn (){
        return isFilterOn;
    }

    public void setIsFilterOn(boolean bool) {
        isFilterOn.setValue(bool);
        isFilterOn.postValue(bool);
    }

    public void setSelectedCountry(int selectedItemPosition) {
        selectedCountry.setValue(selectedItemPosition);
        selectedCountry.postValue(selectedItemPosition);
    }

    public LiveData<Integer> getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedSources(boolean[] answers) {
        selectedSources.setValue(answers);
        selectedSources.postValue(answers);
    }

    public LiveData<boolean[]> getSelectedSources(){
        return selectedSources;
    }

    public void setWeatherForecast(WeatherForecast result) {
        weatherForecast.setValue(result);
        weatherForecast.postValue(result);
    }

    public LiveData<WeatherForecast> getWeatherForeCast(){
        return  weatherForecast;
    }
}
