package com.ofthedaynews.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.TimeZone;

public class WeatherForecast {
    private long sunrise,sunset,updatedAt;
    private double longitude, latitude, temp, temp_min, temp_max, feels_like, wind_deg;
    private String pressure, humidity, wind_speed;
    private String weatherMain, weatherDescription, country, city;
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getTemp() {
        return temp;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public double getWind_deg() {
        return wind_deg;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public WeatherForecast(JSONObject jsonObject) {
        try {
            if (jsonObject.getInt("cod") != 404) {
                this.success = true;

                updatedAt = jsonObject.getLong("dt");

                JSONObject coord = jsonObject.getJSONObject("coord");
                this.longitude = coord.getDouble("lon");
                this.latitude = coord.getDouble("lat");

                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                this.weatherMain = weather.getString("main");
                this.weatherDescription = weather.getString("description");

                JSONObject main = jsonObject.getJSONObject("main");
                this.temp = main.getDouble("temp");
                this.temp_min = main.getDouble("temp_min");
                this.temp_max = main.getDouble("temp_max");
                this.feels_like = main.getDouble("feels_like");
                this.pressure = main.getString("pressure");
                this.humidity = main.getString("humidity");

                JSONObject wind = jsonObject.getJSONObject("wind");
                this.wind_speed = wind.getString("speed");
                try {
                    this.wind_deg = wind.getDouble("deg");
                } catch(Exception e){
                    this.wind_deg = 0.0;
                }

                JSONObject sys = jsonObject.getJSONObject("sys");
                // Bit tricky to get the correct time for sunset and sunrise
                long timeCorrection = jsonObject.getLong("timezone") - (TimeZone.getDefault().getOffset(new Date().getTime()) / 1000);
                this.sunrise = sys.getLong("sunrise") + timeCorrection;
                this.sunset = sys.getLong("sunset")+ timeCorrection;
                try {
                    this.country = sys.getString("country");
                } catch (Exception e) {
                    this.country = "";
                }

                this.city = jsonObject.getString("name");
            } else
                this.success = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
