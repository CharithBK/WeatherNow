package com.example.weathernow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView editText;
    Button searchButton;
    TextView result;



   class Weather extends AsyncTask<String,Void,String>{



       @Override
       protected String doInBackground(String... address) {

           try {
               URL url = new URL(address[0]);
               HttpURLConnection connection = (HttpURLConnection) url.openConnection();
               connection.connect();

               InputStream is =  connection.getInputStream();
               InputStreamReader isr = new InputStreamReader(is);


               int data = isr.read();
               String content ="";
               char ch;
               while (data !=-1)
               {
                   ch = (char) data;
                   content = content + ch;
                   data = isr.read();
               }
                    return  content;
           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
           return null;
       }
   }


    public void Search (View view)
    {
        editText = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.btn_search);
        result = findViewById(R.id.result);


        String CityName = editText.getText().toString();

        String content;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q="+CityName+"&appid=b6907d289e10d714a6e88b30761fae22").get();
            Log.i("content",content);


            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String maiTemp = jsonObject.getString("main");
            Log.i("weatherData",weatherData);

            //PutInArray

            JSONArray jsonArray = new JSONArray(weatherData);
            String main= "";
            String  description= "";
            String temperature = "";

            for(int i =0; i< jsonArray.length(); i++)
            {
                JSONObject weatherPart = jsonArray.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }

            JSONObject mainPart = new JSONObject(maiTemp);
            temperature = mainPart.getString("temp");

            Log.i("main",main);
            Log.i("description",description);
            Log.i("description",temperature);

            result.setText("Main : " +main+ " \n \nDescription :" +description + " \n \nTemperature :" +temperature );

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }


}
