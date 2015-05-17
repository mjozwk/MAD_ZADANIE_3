package jozwiak.marcin.mad_zadanie_3.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jozwiak.marcin.mad_zadanie_3.R;
import jozwiak.marcin.mad_zadanie_3.utils.ImageLoader;
import jozwiak.marcin.mad_zadanie_3.utils.JSONParser;

/**
 * Created by Marcin on 2015-05-10.
 */
public class FragmentMain extends Fragment{

    private TextView textTemp;
    private TextView textWeather;
    private TextView textDescription;
//    private TextView refresh_icon;
//    private Button btnRefresh;
    private ImageButton refreshButton;
    private ImageView imageView;
    private static final String baseUrl = "http://api.openweathermap.org/data/2.5/weather?q=Szczecin,pl&units=metric";
    private static final String urlImg = "http://openweathermap.org/img/w/";
    private byte[] iconData;
    private JSONObject JSONWeather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
//        refresh_icon = (TextView) view.findViewById(R.id.refresh_icon);
        textTemp = (TextView) view.findViewById(R.id.textTemp);
        refreshButton = (ImageButton) view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               new JSONParse().execute(baseUrl);
            }
        });
        imageView = (ImageView) view.findViewById(R.id.imageView);
        textWeather= (TextView) view.findViewById(R.id.textWeather);
        textDescription = (TextView) view.findViewById(R.id.textDescription);
    }



    private class JSONParse extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(params[0]);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {

                JSONArray jArray = jsonObject.getJSONArray("weather");
                JSONWeather = jArray.getJSONObject(0);
                Thread thread = new Thread(new Runnable() {
                   public void run() {
                       try {
                           iconData = new ImageLoader().DownloadFromUrl(urlImg, JSONWeather.getString("icon"));

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               });
                thread.start();
                while(thread.isAlive()){}
                if (iconData != null && iconData.length > 0) {
                    Bitmap img = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
                    imageView.setImageBitmap(img);
                }
                textWeather.setText(JSONWeather.getString("main"));
                textDescription.setText(JSONWeather.getString("description"));

                JSONObject main = jsonObject.getJSONObject("main");
                textTemp.setText(
                        main.getInt("temp") + "\u2103");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



}
