package com.example.fatihcemildemirhomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsDetailActivity extends AppCompatActivity {
    NewsItem selectedNews;
    TextView newsTitle;
    TextView newsDate;
    TextView newsDetail;
    ImageView newsImage;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        selectedNews = (NewsItem) getIntent().getSerializableExtra("selectedNews");
        newsDate = findViewById(R.id.news_date);
        newsDetail = findViewById(R.id.news_detail);
        newsTitle = findViewById(R.id.news_title);
        newsImage = findViewById(R.id.news_image);
        //Bitmap currentBitmap = getIntent().getParcelableExtra("bitmap");
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = formatter.format(selectedNews.getNewsDate());
        if (selectedNews.getBitmap() == null)
            new ImageDownloadTask(newsImage).execute(selectedNews);
        else {
            newsImage.setImageBitmap(selectedNews.getBitmap());
        }
        //newsImage.setImageBitmap(currentBitmap);
        newsDate.setText(strDate);
        newsDetail.setText(selectedNews.getText());
        newsTitle.setText(selectedNews.getTitle());
        String title = "News Details";
        id = String.valueOf(selectedNews.getId());

        setTitle("News Details");
        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        else if(item.getItemId() == R.id.comments)
        {
            Intent i = new Intent(NewsDetailActivity.this,CommentWindowActivity.class);
            Log.i("COMMA", id);
            i.putExtra("id", id);
            startActivity(i);
            return true;
        }
        return true;
    }

}