package com.example.fatihcemildemirhomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentWindowActivity extends AppCompatActivity implements CommentAdapter.CommentItemClickListener{
    ProgressDialog prgDialog;
    RecyclerView commentRecView;
    List<CommentItem> data;
    CommentAdapter adp;
    AlertDialog.Builder dlgAlert;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_window);
        setTitle("Geldik aq");
        data = new ArrayList<>();
        commentRecView = findViewById(R.id.comment_rec_view);
        Log.i("COMMA", "get intent öncesi");
        id = getIntent().getStringExtra("id");

        //id = 48;
        //Log.i("COMMA", id);
        String url = "http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/"+id;
        Log.i("COMMA", url);
        Log.i("COMMA", "get intent sonrası");
        CommentTask tsk = new CommentTask();
        tsk.execute(url);
        adp  = new CommentAdapter(data, this, this);
        commentRecView.setLayoutManager(new LinearLayoutManager(this));
        commentRecView.setAdapter(adp);
        setTitle("Comments");
        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        String url = "http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/"+id;
        Log.i("COMMA", url);
        Log.i("COMMA", "get intent sonrası");
        CommentTask tsk = new CommentTask();
        tsk.execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comment_window_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        else if(item.getItemId() == R.id.plus)
        {
            Intent i = new Intent(CommentWindowActivity.this, PostCommentActivity.class);
            i.putExtra("news_id", id);
            Log.i("COMM","intent başladı");
            startActivity(i);
            return true;
        }
        return true;
    }

    @Override
    public void CommentItemClicked(CommentItem selectedCommentItem) {
        dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(selectedCommentItem.getMessage());
        dlgAlert.setTitle("Commenter:" + selectedCommentItem.getName());
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    class CommentTask extends AsyncTask<String, Void,String>
    {

        @Override
        protected void onPreExecute() {
            Log.i("COMMA", "pre execte");
            prgDialog = new ProgressDialog(CommentWindowActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
            Log.i("COMMA", "pre exec bitti");
        }

        @Override
        protected String doInBackground(String... strings) {

            String urlStr = strings[0];
            //Log.i("COMMA", urlStr);
            StringBuilder buffer = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.i("COMMA", "doin bg de");
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
            Log.i("COMMA", "doin bg bitti");
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            data.clear();

            try {
                Log.i("COMMA", s);
                JSONObject obj = new JSONObject(s);
                if (obj.getInt("serviceMessageCode") ==1)
                {//service worked fine
                    JSONArray arr = obj.getJSONArray("items");
                    Log.i("COMMA", "post exec basladi");
                    for (int i =0; i<arr.length();i++)
                    {
                        JSONObject current = (JSONObject) arr.get(i);

                        CommentItem item = new CommentItem(current.getInt("id"), current.getString("name"),current.getString("text"));

                        data.add(item);

                    }

                   /*for (int i= 0; i<data.size();i++)
                   {
                       Log.i("COMMA", data.get(i).getName());
                   }*/
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
                Log.i("COMMA", "post exec bitti");
            }
            Log.i("COMMA", "post exec bitti");
        }
    }
}
