package com.ofthedaynews.controllers;


import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ofthedaynews.SharedViewModel;
import com.ofthedaynews.models.NewsArticle;
import com.ofthedaynews.ui.ArticleFragment;
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

    public NewsController(MainController mainController) {
        this.mainController = mainController;
        this.sharedViewModel = ViewModelProviders.of(this.mainController.getActivity()).get(SharedViewModel.class);
        loadNews();
        this.newsFragment = new NewsFragment();
        this.newsFragment.setController(this);
    }

    public void loadNews(){
        this.url = "https://newsapi.org/v2/top-headlines?sources=al-jazeera-english,bbc-news,cnn,fox-news,independent,reuters,the-huffington-post,the-washington-post,usa-today,&pageSize=80&apiKey=7ddb1d07030d4feb91f7dd85811705e2";

        String[] a = {url};
        new LoadNews().execute(a);
    }

    public Fragment getFragment() {
        return this.newsFragment;
    }

    public void showArticle(NewsArticle itemAtPosition) {
        sharedViewModel.setClickedArticle(itemAtPosition);
        this.articleFragment =  new ArticleFragment();
        this.mainController.getActivity().setFragment(this.articleFragment,true);
    }

    public class LoadNews extends AsyncTask<String[], Integer, ArrayList<NewsArticle>> {
        public LoadNews() {
            //execute(url);
        }
        @Override
        protected ArrayList<NewsArticle> doInBackground(String[]... params) {
            ArrayList<NewsArticle> result = new ArrayList<>();
            try {
                for (int i =0 ; i< params[0].length ; i++){
                    URL url = new URL(params[0][i]);
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

                        String content = newsArticle.getString("content");

                        int index = content.indexOf("…");
                        content = content.substring(0, index + 2);

                        if (content == "null" || index == -1 )
                            content = newsArticle.getString("description");

                        String imageUrl = newsArticle.getString("urlToImage");
                        if (imageUrl == "null")
                            imageUrl = "https://s3.distributorcentral.com/images/shared/img_not_available.png";

                        NewsArticle article = new NewsArticle(newsArticle.getString("title"),source.getString("name"),newsArticle.getString("author"),newsArticle.getString("description"),newsArticle.getString("url"),imageUrl,ago,content);
                        result.add(article);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsArticle> result) {
            sharedViewModel.addNewsArticles(result);
        }
    }

}
