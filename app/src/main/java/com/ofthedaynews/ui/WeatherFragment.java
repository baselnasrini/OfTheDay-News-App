package com.ofthedaynews.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ofthedaynews.R;
import com.ofthedaynews.SharedViewModel;
import com.ofthedaynews.controllers.WeatherController;
import com.ofthedaynews.models.WeatherForecast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherFragment extends Fragment {

    private WeatherController weatherController;
    private Button searchWeatherButton, searchCurrentLocationButton;
    private EditText cityToLookFor;
    private SharedViewModel sharedViewModel;
    TextView address, updatedAtTxt, statusTxt, tempTxt, tempMinTxt, tempMaxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt, feelsLikeTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        initializeComponents(view);
        registerListeners();
        if (sharedViewModel.getWeatherForeCast() != null && sharedViewModel.getWeatherForeCast().getValue() != null) {
            updateView(view, sharedViewModel.getWeatherForeCast().getValue());
        }
        return view;
    }

    private void initializeComponents(View view) {
        searchWeatherButton = view.findViewById(R.id.searchWeatherButton);
        searchCurrentLocationButton = view.findViewById(R.id.searchCurrentLocationButton);
        cityToLookFor = view.findViewById(R.id.cityToLookFor);
        address = view.findViewById(R.id.address);
        updatedAtTxt = view.findViewById(R.id.updatedAt);
        statusTxt = view.findViewById(R.id.status);
        tempTxt = view.findViewById(R.id.temp);
        tempMinTxt = view.findViewById(R.id.tempMin);
        tempMaxTxt = view.findViewById(R.id.tempMax);
        sunriseTxt = view.findViewById(R.id.sunrise);
        sunsetTxt = view.findViewById(R.id.sunset);
        windTxt = view.findViewById(R.id.wind);
        pressureTxt = view.findViewById(R.id.pressure);
        humidityTxt = view.findViewById(R.id.humidity);
        feelsLikeTxt = view.findViewById(R.id.feelsLike);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        view.findViewById(R.id.loader).setVisibility(View.VISIBLE);
    }

    private void registerListeners() {
        searchWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.loader).setVisibility(View.VISIBLE);
                String city = cityToLookFor.getText().toString();
                if (city == null || city.equals(""))
                    weatherController.updateWeatherFromLocation();
                else
                    weatherController.updateWeatherFromCity(city);
                hideKeyboard(getActivity());
            }
        });
        searchCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityToLookFor.setText("");
                weatherController.updateWeatherFromLocation();
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setController(WeatherController weatherController) {
        this.weatherController = weatherController;
    }

    public void updateView(WeatherForecast weatherForecast) {
        address.setText(weatherForecast.getCity() + ", " + weatherForecast.getCountry());
        updatedAtTxt.setText("Updated at : " + new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH).format(new Date(weatherForecast.getUpdatedAt() * 1000)));
        statusTxt.setText(weatherForecast.getWeatherDescription().toUpperCase());
        tempTxt.setText(new DecimalFormat("###.#").format(weatherForecast.getTemp()) + "°C");
        tempMinTxt.setText("Min temp : " + new DecimalFormat("###.#").format(weatherForecast.getTemp_min()) + "°C");
        tempMaxTxt.setText("Max temp : " + new DecimalFormat("###.#").format(weatherForecast.getTemp_max()) + "°C");
        sunriseTxt.setText(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(weatherForecast.getSunrise() * 1000)));
        sunsetTxt.setText(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(new Date(weatherForecast.getSunset() * 1000)));
        windTxt.setText((weatherForecast.getWind_speed() + "m/s"));
        pressureTxt.setText(weatherForecast.getPressure()+ "hPa");
        humidityTxt.setText(weatherForecast.getHumidity() + "%");
        feelsLikeTxt.setText(new DecimalFormat("###.#").format(weatherForecast.getFeels_like()) + "°C");
    }

    private void updateView(View view, WeatherForecast weatherForecast) {
        updateView(weatherForecast);
        view.findViewById(R.id.loader).setVisibility(View.GONE);
    }
}
