package com.arazzi.plantaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arazzi.plantaria.explore.Explore;
import com.arazzi.plantaria.water.reminder.WaterReminder;
import com.arazzi.plantaria.water.reminder.weatherData;

import java.util.Calendar;
import java.util.Date;

import com.loopj.android.http.*;


import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.arazzi.plantaria.water.reminder.weatherData.weatherD;

public class Dashboard extends AppCompatActivity {

    final String OpenWeatherURL = "http://api.openweathermap.org/data/2.5/weather";
    final String APP_ID = "2d5c107eb7738cc2d10932e133329c1c";

    final long MIN_TIME = 5000;
    final long MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView City, Temperature, Humidity, Wind, Pressure, StatusWeather;
    CardView  CV_waterReminder, CV_explore;
    ImageView IconWeather;

    LocationManager mLocationManager;
    LocationListener mLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //hide actionbar
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //cardview water reminder
        CV_waterReminder = findViewById(R.id.water_reminder_cardview);
        CV_waterReminder.setOnClickListener(v -> startActivity(new Intent(Dashboard.this, WaterReminder.class)));
        //
        CV_explore = findViewById(R.id.explore_cardview);
        CV_explore.setOnClickListener(v -> startActivity(new Intent(Dashboard.this, Explore.class)));
        //textview paling atas greeting to user
        TextView userGreetings = findViewById(R.id.greetings_id);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour >= 0 && hour < 12) {
            userGreetings.setText("Good Morning");
        } else if (hour >= 12 && hour < 16) {
            userGreetings.setText("Good Afternoon");
        }
        if (hour >= 16 && hour < 21) {
            userGreetings.setText("Good Evening");
        }
        if (hour >= 21 && hour < 24) {
            userGreetings.setText("Good Night");
        }
        ///
        //text view city
        City = findViewById(R.id.city_id);
        Temperature = findViewById(R.id.temperature_id);
        Humidity = findViewById(R.id.humidity_id);
        Wind = findViewById(R.id.wind_id);
        Pressure = findViewById(R.id.pressure_id);
        StatusWeather = findViewById(R.id.statusWeather_id);
        IconWeather = findViewById(R.id.weatherIcon_id);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent = getIntent();
        String mCity = mIntent.getStringExtra("City");
        if (mCity!=null){
            getWeatherForNewCity(mCity);

        }
        else {
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherForNewCity(String mCity) {
        RequestParams params = new RequestParams();
        params.put("q", mCity);
        params.put("appid", APP_ID);
        letdoSomeNetworking(params);
    }

    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", APP_ID);
                letdoSomeNetworking(params);



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                //not able to get location
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE)
        {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(Dashboard.this, "Location Get Successfully",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            // else here
            // user denied the permission

        }
    }

    private void letdoSomeNetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(OpenWeatherURL, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                weatherD = weatherData.fromJson(response);
                updateUI(weatherD);


                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }




    private void updateUI(weatherData weather){
        Temperature.setText(weather.getmTemperature()+ "\u2103");
        City.setText(weather.getmCity());
        Humidity.setText(weather.getmHumidity()+ "%");
        Wind.setText(weather.getmWind()+ " km/h");
        Pressure.setText(weather.getmPressure()+ " Psi");
        StatusWeather.setText(weather.getmStatus());
        int resourceID = getResources().getIdentifier(weather.getmIcon(),"drawable",getPackageName());
        IconWeather.setImageResource(resourceID);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationManager != null){
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}