package com.ofthedaynews.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ofthedaynews.R;
import com.ofthedaynews.SharedViewModel;
import com.ofthedaynews.models.NewsArticle;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;


public class ArticleFragment extends Fragment {
    private ImageView image;
    private TextView tvTitle,tvTime, tvContent, tvURL;
    private SharedViewModel sharedViewModel;
    private Toolbar toolbar;
    private FloatingActionButton btnShare;
    public ArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        initializeComponents(view);
        registerListeners();

        return view;
    }

    private void initializeComponents(View view) {
        image = view.findViewById(R.id.articleImg);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        tvTime = view.findViewById(R.id.txtTime);
        tvContent = view.findViewById(R.id.txtContent);
        tvTitle = view.findViewById(R.id.title_text);
        tvURL = view.findViewById(R.id.txtURL);
        btnShare = view.findViewById(R.id.fab);
        //tvSource = view.findViewById(R.id.txtSource);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

    }

    private void registerListeners() {
        sharedViewModel.getArticle().observe(getViewLifecycleOwner(), new Observer<NewsArticle>() {
            @Override
            public void onChanged(final NewsArticle newsArticles) {
                if(newsArticles != null){
                    tvTitle.setText(newsArticles.getTitle());
                    Picasso.get().load(newsArticles.getImageURL()).into(image);

                    tvTime.setText(String.format("Published: %s", newsArticles.getPublishedAt()));
                    tvContent.setText(newsArticles.getContent());
                    tvURL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsArticles.getUrl()));
                            startActivity(myIntent);
                        }
                    });

                    btnShare.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "OfTheDay News App");
                                String shareMessage= "Let me recommend you this article that I read in OfTheDay News App:\n\n" + newsArticles.getUrl() ;
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                startActivity(Intent.createChooser(shareIntent, "choose one"));
                            } catch(Exception e) {
                                //e.toString();
                            }
                        }
                    });
                }
            }
        });

    }
}
