package com.example.fatihcemildemirhomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostCommentActivity extends AppCompatActivity {
    String id;
    EditText name;
    EditText message;
    Button sendBtn;
    ProgressDialog prgDialog;
    AlertDialog.Builder dlgAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        name = findViewById(R.id.user_name);
        message = findViewById(R.id.comment_txt);
        sendBtn = findViewById(R.id.post_comment_button);


        id = getIntent().getStringExtra("news_id");
        Log.i("SEND",id);
        setTitle("Post Comment");
        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);

    }
    public void taskCallClicked(View v)
    {
        Log.i("SEND","Send clicked");
        PostTask tsk = new PostTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment",
                name.getText().toString(),
                message.getText().toString());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return true;
    }
    class PostTask extends AsyncTask<String, Void,String>
    {
        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(PostCommentActivity.this);
            prgDialog.setTitle("Sending Comment");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            String urlStr = strings[0];
            String name = strings[1];
            String message = strings[2];
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", name);
                obj.put("text", message);
                obj.put("news_id", id);
            } catch (JSONException e) {
                Log.i("SEND","ilk JSON erroru");
                e.printStackTrace();
            }
            Log.i("SEND",obj.toString());
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(obj.toString());
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line= "";
                    while((line = reader.readLine())!= null)
                    {
                        stringBuilder.append(line);
                    }
                }
                prgDialog.dismiss();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("SEND","ilk url erroru");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("SEND","bir error ama hangisi");
            }
            Log.i("SEND",stringBuilder.toString());
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject inputObj = new JSONObject(s);
                if(inputObj.getString("serviceMessageCode").equals("1"))
                {
                    Log.i("SEND","success");
                    dlgAlert  = new AlertDialog.Builder(PostCommentActivity.this);
                    dlgAlert.setMessage("Comment sent succesfully");
                    dlgAlert.setTitle("Success!!!");
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();

                        }
                    });
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
                else
                {
                    dlgAlert  = new AlertDialog.Builder(PostCommentActivity.this);
                    dlgAlert.setMessage("Comment could not be sent. Please try again.");
                    dlgAlert.setTitle("Failure!!!");
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                    Log.i("SEND","fail");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("SEND","ikinci json erroru");
            }

        }
    }
}
