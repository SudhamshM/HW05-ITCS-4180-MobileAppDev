package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.hw05.DataService.City;
import com.example.hw05.databinding.FragmentWeatherBinding;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {
    private final OkHttpClient client = new OkHttpClient();
    public static final String TAG = "hw05";
    FragmentWeatherBinding binder;
    private static final String ARG_CITY = "CITY";
    String apiKey = ApiKey.API_KEY;

    // TODO: Rename and change types of parameters
    private City mCity;

//    ?lat={lat}&lon={lon}&appid={API key}

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param city Parameter 1.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(City city) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (City) getArguments().getSerializable(ARG_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binder = FragmentWeatherBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Weather Fragment");
        getCityWeather(mCity);
        binder.buttonCheckForecast.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.goToForecast(mCity);
            }
        });

    }

    public void getCityWeather(City city)
    {
        HttpUrl urlWeather = HttpUrl
                .parse("https://api.openweathermap.org/data/2.5/weather")
                .newBuilder()
                .addQueryParameter("lat", String.valueOf(mCity.getLat()))
                .addQueryParameter("lon", String.valueOf(mCity.getLon()))
                .addQueryParameter("appid", apiKey)
                .addQueryParameter("units", "imperial")
                .build();

        Request request = new Request.Builder()
                .url(urlWeather)
                .build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e)
            {
                Log.d(TAG, "onFailure: get city weather");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
            {
                if (response.isSuccessful())
                {
                    Log.d(TAG, "onResponse: weather ");
                    ResponseBody responseBody = response.body();
                    Gson gson = new Gson();
                    WeatherResponse weatherResponse = gson.fromJson(responseBody.charStream(), WeatherResponse.class);
                    Log.d(TAG, "onResponse: weather resp" + weatherResponse);
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setupUI(weatherResponse);
                        }
                    });
                }
            }
        });
        binder.textViewCityName.setText(city.toString());
    }

    public void setupUI(WeatherResponse weatherResponse)
    {
        binder.textViewTemp.setText(weatherResponse.main.temp + " F");
        binder.textViewTempMax.setText(weatherResponse.main.temp_max + " F");
        binder.textViewTempMin.setText(weatherResponse.main.temp_min + " F");
        binder.textViewCloudiness.setText(weatherResponse.clouds.all + "%");

        binder.textViewDesc.setText(weatherResponse.weather[0].description);
        binder.textViewHumidity.setText(weatherResponse.main.humidity + "%");
        binder.textViewWindSpeed.setText(weatherResponse.wind.speed + " mi/hr");
        binder.textViewWindDegree.setText(weatherResponse.wind.deg + " degrees");
        String baseIconUrl = "https://openweathermap.org/img/wn/";
        Picasso.get()
                .load(baseIconUrl + weatherResponse.weather[0].icon + ".png")
                .into(binder.imageViewWeatherIcon);
    }

    interface WeatherListener
    {
        void goToForecast(City city);
    }

    WeatherListener mListener;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        mListener = (WeatherListener) context;
    }
}