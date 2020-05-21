/**
 * Author: Mohammed Basel Nasrini
 * Last edited: 2020-05-21
 */
package com.ofthedaynews.controllers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.format.DateUtils;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ofthedaynews.MainActivity;
import com.ofthedaynews.R;
import com.ofthedaynews.SharedViewModel;
import com.ofthedaynews.models.NewsArticle;
import com.ofthedaynews.ui.ArticleFragment;
import com.ofthedaynews.ui.NewsFilterFragment;
import com.ofthedaynews.ui.NewsFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NewsController {
    private MainController mainController;
    private NewsFragment newsFragment;
    private String url;
    private SharedViewModel sharedViewModel;
    private ArticleFragment articleFragment;
    private NewsFilterFragment filterFragment;

    public NewsController(MainController mainController) {
        this.mainController = mainController;
        this.sharedViewModel = ViewModelProviders.of(this.mainController.getActivity()).get(SharedViewModel.class);
        this.newsFragment = new NewsFragment();
        this.newsFragment.setController(this);
        loadNews();
    }

    public void loadNews(){
        SharedPreferences sharedPreferences = this.mainController.getActivity().getSharedPreferences("MainActivity", Activity.MODE_PRIVATE);
        Boolean isFilter = sharedPreferences.getBoolean("filterLayout", false);
        Boolean isCountryOn = sharedPreferences.getBoolean("countryLayout", true);
        int selectedCountry = sharedPreferences.getInt("selectedCountry", 0);
        boolean [] selectedSources = new boolean[this.mainController.getActivity().getResources().getStringArray(R.array.source_array).length];

        if(!isFilter){
            this.url = "https://newsapi.org/v2/top-headlines?language=en&pageSize=80&apiKey=7ddb1d07030d4feb91f7dd85811705e2";
        } else{
            if (isCountryOn){
                String countryCode = "se";
                switch (selectedCountry){
                    case 0: countryCode ="se"; break;
                    case 1: countryCode ="us"; break;
                    case 2: countryCode ="gb"; break;
                    case 3: countryCode ="es"; break;
                    case 4: countryCode ="fr"; break;
                    case 5: countryCode ="de"; break;
                    case 6: countryCode ="it"; break;
                    case 7: countryCode ="sa"; break;
                    case 8: countryCode ="ae"; break;
                    case 9: countryCode ="tr"; break;
                    case 10: countryCode ="gr"; break;
                }
                this.url = "https://newsapi.org/v2/top-headlines?country=" + countryCode + "&pageSize=80&apiKey=7ddb1d07030d4feb91f7dd85811705e2";
            } else{
                this.url = "https://newsapi.org/v2/top-headlines?sources=";
                String sources = "";
                for (int i=0;i<selectedSources.length;i++){
                    selectedSources[i] = sharedPreferences.getBoolean("sourceSelected_" + i, false);
                    if (selectedSources[i]){
                        if(i == 0) sources += "bbc-news,";
                        else if(i == 1) sources += "al-jazeera-english,";
                        else if(i == 2) sources += "cnn,";
                        else if(i == 3) sources += "svenska-dagbladet,";
                        else if(i == 4) sources += "bbc-sport,";
                        else if(i == 5) sources += "independent,";
                        else if(i == 6) sources += "abc-news,";
                        else if(i == 7) sources += "buzzfeed,";
                        else if(i == 8) sources += "cbc-news,";
                        else if(i == 9) sources += "reuters,";
                        else if(i == 10) sources += "the-huffington-post,";
                        else if(i == 11) sources += "the-washington-post,";
                        else if(i == 12) sources += "marca,";
                        else if(i == 13) sources += "goteborgs-posten,";
                        else if(i == 14) sources += "bild,";
                        else if(i == 15) sources += "der-tagesspiegel,";
                    }
                }
                this.url += sources + "&pageSize=80&apiKey=7ddb1d07030d4feb91f7dd85811705e2";
            }
        }
        new LoadNews().execute(this.url);
    }

    public Fragment getFragment() {
        return this.newsFragment;
    }

    public void showArticle(NewsArticle itemAtPosition) {
        sharedViewModel.setClickedArticle(itemAtPosition);
        this.articleFragment =  new ArticleFragment();
        this.mainController.getActivity().setFragment(this.articleFragment,true);
    }

    public void showFilter() {
        this.filterFragment = new NewsFilterFragment();
        this.filterFragment.setController(this);
        this.mainController.getActivity().setFragment(this.filterFragment,true);
    }

    public void goBack(){
        loadNews();
        ((MainActivity)this.getFragment().getContext()).getSupportFragmentManager().popBackStackImmediate();
    }

    public class LoadNews extends AsyncTask<String, Integer, ArrayList<NewsArticle>> {
        public LoadNews() {
        }

        @Override
        protected ArrayList<NewsArticle> doInBackground(String... params) {
            ArrayList<NewsArticle> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                InputStream input = connection.getInputStream();
                String str = new Scanner(input,"UTF-8").useDelimiter("\\A").next();

                JSONObject json = new JSONObject(str);
                JSONArray articlesArr = json.getJSONArray("articles");

                for(int a=0; a<articlesArr.length(); a++) {
                    JSONObject newsArticle = articlesArr.getJSONObject(a);
                    JSONObject source = newsArticle.getJSONObject("source");

                    String timeStr = newsArticle.getString("publishedAt");
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = inputFormat.parse(timeStr);
                    String ago = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);

                    String title = newsArticle.getString("title");
                    if (title.length() == 0)
                        continue;

                    String content = newsArticle.getString("content");
                    int index = content.indexOf("[+");
                    if(index == -1 && content == "null"){
                        content = newsArticle.getString("description");
                    } else if (index != -1 && content != null){
                        content = content.substring(0, index );
                    }

                    String imageUrl = newsArticle.getString("urlToImage");
                    if (imageUrl.length() < 10 )
                        imageUrl = "https://s3.distributorcentral.com/images/shared/img_not_available.png";
                    else if(imageUrl.charAt(4) != 's'){
                        imageUrl = new StringBuilder(imageUrl).insert(4, "s").toString();
                    }

                    NewsArticle article = new NewsArticle(title,source.getString("name"),newsArticle.getString("author"),newsArticle.getString("description"),newsArticle.getString("url"),imageUrl,ago,content);
                    result.add(article);
                 }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsArticle> result) {
            sharedViewModel.setNewsArticlesArr(result);
        }
    }
}
