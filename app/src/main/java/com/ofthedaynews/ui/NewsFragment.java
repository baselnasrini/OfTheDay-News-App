/**
 * Author: Mohammed Basel Nasrini
 * Last edited: 2020-05-21
 */
package com.ofthedaynews.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ofthedaynews.R;
import com.ofthedaynews.SharedViewModel;
import com.ofthedaynews.controllers.NewsController;
import com.ofthedaynews.models.NewsArticle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NewsFragment extends Fragment {
    private NewsController newsController;
    private ListView lvNews;
    private SharedViewModel sharedViewModel;
    private ArticlesAdapter articlesAdapter;
    private ProgressBar progressBar;
    private FloatingActionButton btnSetting;
    private int position = 0;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initializeComponents(view);
        registerListeners();
        return view;
    }

    private void initializeComponents(View view) {
        lvNews = view.findViewById(R.id.newsListView);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        btnSetting = view.findViewById(R.id.btnSetting);
    }

    private void registerListeners() {

        sharedViewModel.getNewsArticlesArr().observe(getViewLifecycleOwner(), new Observer<ArrayList<NewsArticle>>() {
            @Override
            public void onChanged(ArrayList<NewsArticle> newsArticles) {
                progressBar.setVisibility(View.VISIBLE);
                if(newsArticles != null){
                    articlesAdapter = new ArticlesAdapter(getActivity().getApplicationContext(), newsArticles);
                    lvNews.setAdapter(articlesAdapter);
                }
                articlesAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
                lvNews.setSelection(position);
            }
        });

        lvNews.setOnItemClickListener(new NewsListViewListener());

        btnSetting.setOnClickListener(new NewsFilterButtonListener());
    }

    public void setController(NewsController newsController) {
        this.newsController = newsController;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        position = lvNews.getLastVisiblePosition() - 2;
        super.onSaveInstanceState(savedInstanceState);
    }

    private class ArticlesAdapter extends ArrayAdapter<NewsArticle> {
        private LayoutInflater inflater;

        public ArticlesAdapter(Context context, ArrayList<NewsArticle> articlesArr) {
            super(context, R.layout.article_row, articlesArr);
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final NewsArticle article = getItem(position);
            final ViewHolder holder;
            if(convertView==null) {
                convertView = inflater.inflate(R.layout.article_row,parent,false);
                holder = new ViewHolder();
                holder.tvTitle = convertView.findViewById(R.id.txtTitle);
                holder.tvDate = convertView.findViewById(R.id.txtTime);
                holder.image = convertView.findViewById(R.id.imageView);
                holder.image.setVisibility(View.GONE);
                holder.progressBar = convertView.findViewById(R.id.progressBarImage);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            String title = article.getTitle();
            holder.tvTitle.setText(title);

            Picasso.get().load(article.getImageURL())
                    .into(holder.image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.image.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.image.setVisibility(View.VISIBLE);
                            article.setImageURL("https://s3.distributorcentral.com/images/shared/img_not_available.png");
                            Picasso.get().load(article.getImageURL()).into(holder.image);
                        }
                    });
            holder.tvDate.setText(article.getPublishedAt());
            return convertView;
        }

        private class ViewHolder {
            private TextView tvTitle;
            private TextView tvDate;
            private ImageView image;
            private ProgressBar progressBar;
        }
    }

    private class NewsListViewListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
            position = i;
            newsController.showArticle((NewsArticle) parent.getItemAtPosition(i));
        }
    }

    private class NewsFilterButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            position = 0;
            newsController.showFilter();
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
