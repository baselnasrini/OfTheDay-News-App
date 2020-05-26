package com.ofthedaynews.controllers;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ofthedaynews.R;
import com.ofthedaynews.SharedViewModel;
import com.ofthedaynews.models.WeatherForecast;
import com.ofthedaynews.ui.WeatherFragment;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class WeatherController {

    private MainController mainController;
    private WeatherFragment weatherFragment;
    private SharedViewModel sharedViewModel;
    private LocationManager locationManager;
    private double latitude, longitude;
    private final String API_KEY = "eabe7d13f3eba04b58d4d720717453fd";

    public WeatherController(MainController mainController) {
        this.mainController = mainController;
        this.sharedViewModel = ViewModelProviders.of(this.mainController.getActivity()).get(SharedViewModel.class);
        this.weatherFragment = new WeatherFragment();
        this.weatherFragment.setController(this);

        if (sharedViewModel.getWeatherForeCast() == null || sharedViewModel.getWeatherForeCast().getValue() == null) {
            updateWeatherFromLocation();
        }
    }

    public Fragment getFragment() {
        return this.weatherFragment;
    }

    public void getWeatherByLatLong(double latitude, double longitude) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";
        new LoadWeather().execute(url);
    }

    public void getWeatherByCityName(String cityName) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + API_KEY + "&units=metric";
        new LoadWeather().execute(url);
    }

    public void updateWeatherFromLocation() {
        locationManager = (LocationManager) mainController.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            updateLocation();
            getWeatherByLatLong(latitude, longitude);
        }
    }

    public void updateWeatherFromCity(String weatherCity) {
        getWeatherByCityName(weatherCity);
    }

    public class LoadWeather extends AsyncTask<String, Integer, WeatherForecast> {
        public LoadWeather() {
        }

        @Override
        protected WeatherForecast doInBackground(String... params) {
            WeatherForecast result = null;
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                InputStream input = connection.getInputStream();
                String str = new Scanner(input, "UTF-8").useDelimiter("\\A").next();

                JSONObject json = new JSONObject(str);
                result = new WeatherForecast(json);
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(WeatherForecast result) {
            getFragment().getActivity().findViewById(R.id.loader).setVisibility(View.GONE);
            if (result != null && result.isSuccess()) {
                sharedViewModel.setWeatherForecast(result);
                weatherFragment.updateView(result);
            } else {
                Toast.makeText(mainController.getActivity(), "Unable to find weather for this place.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainController.getActivity());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateWeatherFromLocation();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(mainController.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainController.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                this.latitude = locationGPS.getLatitude();
                this.longitude = locationGPS.getLongitude();
            } else {
                Toast.makeText(mainController.getActivity(), "Unable to find location.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
