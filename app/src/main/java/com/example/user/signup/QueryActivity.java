package com.example.user.signup;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        String[] func = {"星期一","星期二","星期三"};

        listView = (ListView) findViewById(R.id.listview);

        //ArrayAdapter adapter = new ArrayAdapter(QueryActivity.this, android.R.layout.simple_list_item_1, func);

        //listView.setAdapter(adapter);

        new TheTask().execute("123456789");

    }

    public void showListView()
    {

    }

    class TheTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {

            String phoneNumber = arg0[0];

            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {

                data = "?phone=" + URLEncoder.encode(phoneNumber, "UTF-8");

                link = "http://140.130.33.101/QueryMessage.php" + data;

                URL url = new URL(link);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                result = bufferedReader.readLine();

                return result;

            } catch (Exception e) {

                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result)
        {
            String jsonStr = result;

            ArrayList<String> stringArray = new ArrayList<String>();

            Log.d("result=",result);

            if (jsonStr != null) try {

                JSONArray new_array = new JSONArray(result);

                for (int i = 0, count = new_array.length(); i < count; i++) {
                    try {

                        JSONObject jsonObject = new_array.getJSONObject(i);

                        String msg = jsonObject.getString("msg").toString();

                        stringArray.add(msg);

                        Log.d("result=", msg);

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }

                ArrayAdapter adapter = new ArrayAdapter(QueryActivity.this, android.R.layout.simple_list_item_1, stringArray);

                listView.setAdapter(adapter);

            } catch (JSONException e) {

                e.printStackTrace();

                Toast.makeText(QueryActivity.this, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();

            }
            else {

                Toast.makeText(QueryActivity.this, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();

            }
        }
    }

}
