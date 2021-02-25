package com.example.fatihcemildemirhomework3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsItemClickListener {
    ProgressDialog prgDialog;
    Spinner categorySpinner;
    RecyclerView newRecView;
    List<NewsItem> data;
    List<NewsCategory> categories;
    NewsAdapter adp;
    ArrayAdapter<NewsCategory> adpSpinner;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<>();
        categories = new ArrayList<>();
        newRecView = findViewById(R.id.newsrec);
        setTitle("News");

        categorySpinner = findViewById(R.id.spinner_category);
        CategoryTask task = new CategoryTask();
        NewsCategory all = new NewsCategory(-1,"All");
        categories.add(all);
        Log.i("DEV", "hey");
        task.execute("http://94.138.207.51:8080/NewsApp/service/news/getallnewscategories");
        Log.w("DEV", String.valueOf(categories.size()));



        adpSpinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        adpSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adpSpinner);


        adp  = new NewsAdapter(data, this, this);
        newRecView.setLayoutManager(new LinearLayoutManager(this));
        newRecView.setAdapter(adp);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                NewsCategory cat =(NewsCategory) parent.getSelectedItem();
                if(cat.getId() == -1)
                {
                    NewsTask tsk = new NewsTask();
                    tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
                    setTitle("News");
                }
                else
                {
                    try {
                        displayCategory(cat);
                        setTitle(cat.getName());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                NewsTask tsk = new NewsTask();
                tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
            }
        });


    }
    @Override
    public void newItemClicked(NewsItem selectedNewsItem) {
        Intent i = new Intent(this,NewsDetailActivity.class);
        i.putExtra("selectedNews",selectedNewsItem);
        //i.putExtra("bitmap", selectedNewsItem.getBitmap());
        Log.i("DET", selectedNewsItem.getBitmap().toString());
        startActivity(i);
    }

    public void displayCategory(NewsCategory category) throws UnsupportedEncodingException {
        NewsTask tsk = new NewsTask();
        String paramValue = String.valueOf(category.getId());
        String yourURLStr = "http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/" + java.net.URLEncoder.encode(paramValue, "UTF-8");
        tsk.execute(yourURLStr);

    }



    class CategoryTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i("DEV","in doing bg");
            String urlStr = strings[0];
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedReader reader  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                while((line = reader.readLine())!= null)
                {
                    buffer.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("DEV","url problem");

            } catch (IOException e) {
                e.printStackTrace();
                Log.i("DEV","io except");
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            //categories.clear();
            Log.i("DEV","in post exec");

            try {

                JSONObject obj = new JSONObject(s);

                if (obj.getInt("serviceMessageCode") ==1)
                {//service worked fine

                    JSONArray arr = obj.getJSONArray("items");

                    for (int i =0; i<arr.length();i++)
                    {

                        JSONObject current = (JSONObject) arr.get(i);


                        Log.i("DEV","trying to create obj");
                        int id = current.getInt("id");
                        Log.i("DEV","obj created");
                        String title = current.getString("name");
                        Log.i("DEV","obj created");
                        NewsCategory item = new NewsCategory(id, title);

                        categories.add(item);
                        Log.i("DEV",item.toString()+ " added");
                    }
                }
                else
                {
                    //can show server error msg
                    Log.i("DEV","could not get in");
                    //prgDialog.dismiss();
                }
                //adpSpinner.notifyDataSetChanged();

                //prgDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("DEV","json except");
            }
        }
    }


    class NewsTask extends AsyncTask<String, Void,String>
    {

        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(MainActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedReader reader  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                while((line = reader.readLine())!= null)
                {
                    buffer.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            data.clear();
            try {
                JSONObject obj = new JSONObject(s);
                if (obj.getInt("serviceMessageCode") ==1)
                {//service worked fine
                    JSONArray arr = obj.getJSONArray("items");

                    for (int i =0; i<arr.length();i++)
                    {
                        JSONObject current = (JSONObject) arr.get(i);
                        long date = current.getLong("date");
                        Date objDate = new Date(date);
                        NewsItem item = new NewsItem(current.getInt("id"),
                                current.getString("title"),
                                current.getString("text"),
                                current.getString("image"),
                                objDate,
                                null);
                        data.add(item);

                    }
                }
                else
                {
                    //can show server error msg
                    prgDialog.dismiss();
                }
                adp.notifyDataSetChanged();

                prgDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
