package com.arazzi.plantaria.water.reminder;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class weatherData {

    public static weatherData weatherD;
    private String mTemperature;
    private String mCity;
    private String mHumidity;
    private BigDecimal mPressure;
    private String mWind;
    private String mStatus;
    private int mCondition;
    private String mIcon;

    public static weatherData fromJson(JSONObject jsonObject)
    {
        try
        {
            weatherData weatherD = new weatherData();
            weatherD.mCity=jsonObject.getString("name");
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValueTemp = (int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValueTemp);

            double windResult = jsonObject.getJSONObject("wind").getDouble("speed")*3.6;
            int roundedValueWind = (int)Math.rint(windResult);
            weatherD.mWind=Integer.toString(roundedValueWind);

            double pressureResult = (jsonObject.getJSONObject("main").getDouble("pressure")*0.0145037738);
            BigDecimal bd = BigDecimal.valueOf(pressureResult);
            bd = bd.setScale(1, RoundingMode.HALF_UP);
            weatherD.mPressure=(bd);

            weatherD.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mStatus = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.mIcon = updateWeatherIcon(weatherD.mCondition);

            weatherD.mHumidity = jsonObject.getJSONObject("main").getString("humidity");







            return weatherD;



        }




        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    private static String updateWeatherIcon(int mCondition) {
        if(mCondition>=0 && mCondition<=300)
        {
            return "thunderstorm1";
        }
        else if(mCondition>=300 && mCondition<=500)
        {
            return "lightrain";
        }
        else if(mCondition>=500 && mCondition<=600)
        {
            return "shower";
        }
        else  if(mCondition>=600 && mCondition<=700)
        {
            return "snow2";
        }
        else if(mCondition>=701 && mCondition<=771)
        {
            return "fog";
        }

        else if(mCondition>=772 && mCondition<=800)
        {
            return "overcast";
        }
        else if(mCondition==800)
        {
            return "sunny";
        }
        else if(mCondition>=801 && mCondition<=804)
        {
            return "cloudy";
        }
        else  if(mCondition>=900 && mCondition<=902)
        {
            return "thunderstorm1";
        }
        if(mCondition==903)
        {
            return "snow1";
        }
        if(mCondition==904)
        {
            return "sunny";
        }
        if(mCondition>=905 && mCondition<=1000)
        {
            return "thunderstorm2";
        }

        return "dunno";
    }

    public String getmTemperature() {
        return mTemperature;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmHumidity() {
        return mHumidity;
    }

    public String getmWind() {
        return mWind;
    }

    public BigDecimal getmPressure() {
        return mPressure;
    }

    public String getmStatus() { return mStatus; }
    
    public String getmIcon() {
        return mIcon;
    }
}
