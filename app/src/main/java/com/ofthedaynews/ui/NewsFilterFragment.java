/**
 * Author: Mohammed Basel Nasrini
 * Last edited: 2020-05-21
 */
package com.ofthedaynews.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ofthedaynews.R;
import com.ofthedaynews.SharedViewModel;
import com.ofthedaynews.controllers.NewsController;
import com.ofthedaynews.models.CustomCountriesAdapter;

public class NewsFilterFragment extends Fragment {
    private NewsController newsController;
    private Button btnSave;
    private Switch switchFilter;
    private ToggleButton tBtnCountry, tBtnSource;
    private LinearLayout layoutCountry, layoutSource, layoutFilter, layoutMask;
    private final String COUNTRY_LAYOUT_VISIBILITY = "countryLayout";
    private final String FILTER_LAYOUT_VISIBILITY = "filterLayout";
    private final String SELECTED_COUNTRY = "selectedCountry";
    private SharedViewModel sharedViewModel;
    private SharedPreferences sharedPreferences;
    private Spinner countriesSpinner;
    private String [] countries, sources;
    private TypedArray flags;
    private ListView lvAlternatives;
    private boolean[] answers;
    private boolean sourceSelected = false;

    public NewsFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_filter, container, false);
        setRetainInstance(true);
        initializeComponents(view);

        if(savedInstanceState == null){
            Boolean isCountryOn = sharedPreferences.getBoolean(COUNTRY_LAYOUT_VISIBILITY, false);
            Boolean isFilterOn = sharedPreferences.getBoolean(FILTER_LAYOUT_VISIBILITY, false);
            int selectedCountry = sharedPreferences.getInt(SELECTED_COUNTRY, 0);
            getSelectedSources(sharedPreferences);

            if(isFilterOn){
                sharedViewModel.setIsFilterOn(true);
                if(isCountryOn){
                    sharedViewModel.setIsCountryFilterOn(true);
                    sharedViewModel.setSelectedCountry(selectedCountry);
                } else{
                    sharedViewModel.setIsCountryFilterOn(false);
                }
            } else{
                sharedViewModel.setIsFilterOn(false);
                sharedViewModel.setIsCountryFilterOn(false);
            }
        }

        registerListener();
        return view;
    }

    private void initializeComponents(View view){
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        btnSave = view.findViewById(R.id.btnSave);
        tBtnCountry = view.findViewById(R.id.btn_Country);
        tBtnSource = view.findViewById(R.id.btn_Source);
        layoutFilter = view.findViewById(R.id.layoutFilter);
        layoutCountry = view.findViewById(R.id.layoutCountry);
        layoutSource = view.findViewById(R.id.layoutSource);
        switchFilter = view.findViewById(R.id.switchFilter);
        sharedPreferences = this.getActivity().getSharedPreferences("MainActivity", Activity.MODE_PRIVATE);
        countriesSpinner = view.findViewById(R.id.spinnerCountries);
        countries = getResources().getStringArray(R.array.countries_array);
        flags = getResources().obtainTypedArray(R.array.flags_array);
        sources = getResources().getStringArray(R.array.source_array);
        lvAlternatives = view.findViewById(R.id.lvAlternatives);
    }

    private void registerListener(){

        CustomCountriesAdapter adapter = new CustomCountriesAdapter(this.getContext(),countries, flags);
        countriesSpinner.setAdapter(adapter);

        lvAlternatives.setAdapter(new SourcesAdapter(this.getContext(), sources));

        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedViewModel.setSelectedCountry(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sharedViewModel.getIsFilterOn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    switchFilter.setChecked(true);
                    layoutFilter.setVisibility(View.VISIBLE);
                } else {
                    switchFilter.setChecked(false);
                    layoutFilter.setVisibility(View.GONE);
                }
            }
        });

        sharedViewModel.getSelectedCountry().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                countriesSpinner.setSelection(integer);
            }
        });

        sharedViewModel.getIsCountryFilterOn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    tBtnSource.setChecked(false);
                    tBtnCountry.setChecked(true);
                    layoutSource.setVisibility(View.GONE);
                    layoutCountry.setVisibility(View.VISIBLE);
                } else {
                    tBtnCountry.setChecked(false);
                    tBtnSource.setChecked(true);
                    layoutCountry.setVisibility(View.GONE);
                    layoutSource.setVisibility(View.VISIBLE);
                }
            }
        });

        switchFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sharedViewModel.setIsFilterOn(true);
                }else{
                    sharedViewModel.setIsFilterOn(false);
                }
            }
        });

        tBtnCountry.setOnClickListener(new ToggleClickedListener());
        tBtnSource.setOnClickListener(new ToggleClickedListener());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (switchFilter.isChecked()){
                    if (layoutCountry.isShown()){
                        sharedViewModel.setSelectedCountry(countriesSpinner.getSelectedItemPosition());
                        editor.putInt(SELECTED_COUNTRY, countriesSpinner.getSelectedItemPosition());
                    }
                    else{
                        setSelectedSources(answers, sharedPreferences);
                        if (sourceSelected){
                            editor.putInt(SELECTED_COUNTRY, -1);
                            sharedViewModel.setSelectedCountry(-1);
                            sharedViewModel.setSelectedSources(answers);
                        }
                        else {
                            Toast.makeText(getContext(), "Choose at least one news source", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                editor.putBoolean(COUNTRY_LAYOUT_VISIBILITY, layoutCountry.isShown());
                editor.putBoolean(FILTER_LAYOUT_VISIBILITY, switchFilter.isChecked());
                editor.apply();
                newsController.goBack();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (layoutCountry.isShown()){
            sharedViewModel.setIsCountryFilterOn(true);
        } else{
            sharedViewModel.setIsCountryFilterOn(false);
            sharedViewModel.setSelectedSources(answers);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    public void setController(NewsController newsController) {
        this.newsController = newsController;
    }

    public void getSelectedSources(SharedPreferences sp){
        this.answers = new boolean[sources.length];
        for (int i=0;i<answers.length;i++){
            answers[i]=sp.getBoolean("sourceSelected_" + i, false);
        }
    }

    private void setSelectedSources(boolean[] array, SharedPreferences sp){
        SharedPreferences.Editor editor = sp.edit();
        sourceSelected = false;

        for(int i=0;i<array.length;i++){
            if (array[i]) {
                sourceSelected = true;
                break;
            }
        }
        if (sourceSelected){
            for(int i=0;i<array.length;i++){
                editor.putBoolean("sourceSelected_" + i, array[i]);
            }
        }
        editor.apply();
    }

    private class ToggleClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_Country:
                    sharedViewModel.setIsCountryFilterOn(true);
                    break;

                case R.id.btn_Source:
                    sharedViewModel.setIsCountryFilterOn(false);
                    break;
            }
        }
    }

    private class SourcesAdapter extends ArrayAdapter<String> {
        private LayoutInflater inflater;
        private View.OnClickListener listener = new CBListener();

        public SourcesAdapter(Context context, String[] content) {
            super(context, R.layout.source_row, content);
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String text = getItem(position);
            if(convertView==null) {
                convertView = inflater.inflate(R.layout.source_row,parent,false);
            }
            TextView tvAlternative = convertView.findViewById(R.id.tvSource);
            CheckBox cbAlternative = convertView.findViewById(R.id.cbSource);
            tvAlternative.setText(text);
            cbAlternative.setChecked(answers[position]);
            cbAlternative.setTag(position);
            cbAlternative.setOnClickListener(listener);
            return convertView;
        }
    }

    private class CBListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            CheckBox cb = (CheckBox)v;
            int index = (Integer)cb.getTag();
            answers[index] = cb.isChecked();
            sharedViewModel.setSelectedSources(answers);
        }
    }
}
