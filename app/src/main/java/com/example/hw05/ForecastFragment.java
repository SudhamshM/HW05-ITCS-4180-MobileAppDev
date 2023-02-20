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
import android.widget.ArrayAdapter;

import com.example.hw05.DataService.City;
import com.example.hw05.databinding.ForecastListItemBinding;
import com.example.hw05.databinding.FragmentForecastBinding;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {

    private static final String ARG_PARAM1 = "FORECAST";
    private final OkHttpClient client = new OkHttpClient();
    public static final String TAG = "hw05";
    private City mCity;
    FragmentForecastBinding binder;
    String apiKey = ApiKey.API_KEY;

    public ForecastFragment() {
        // Required empty public constructor
    }


    public static ForecastFragment newInstance(City city) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (City) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binder = FragmentForecastBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("5 Day Forecast Fragment");
        getForecastInfo();
    }

    public void getForecastInfo()
    {
        HttpUrl urlWeather = HttpUrl
                .parse("https://api.openweathermap.org/data/2.5/forecast")
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
                Log.d(TAG, "onFailure: get forecast");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
            {
                if (response.isSuccessful())
                {
                    String body = response.body().string();
                    Gson gson = new Gson();
                    ForecastResponse forecastResponse = gson.fromJson(body, ForecastResponse.class);
                    Log.d(TAG, "onResponse: forecast response" + forecastResponse);
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setupUI(forecastResponse);
                        }
                    });
                }
            }
        });

    }

    public void setupUI(ForecastResponse forecastResponse)
    {
        ForecastAdapter adapter = new ForecastAdapter(getContext(), R.layout.forecast_list_item, forecastResponse.list);
        binder.listView.setAdapter(adapter);
    }

    public class ForecastAdapter extends ArrayAdapter<ForecastResponse.Forecast>
    {

        public ForecastAdapter(@NonNull Context context, int resource, @NonNull List<ForecastResponse.Forecast> objects)
        {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            ForecastListItemBinding mBinder;
            if (convertView == null)
            {
                mBinder = ForecastListItemBinding.inflate(getLayoutInflater(), parent, false);
                convertView = mBinder.getRoot();
                convertView.setTag(mBinder);
            }
            else
            {
                mBinder = (ForecastListItemBinding) convertView.getTag();
            }
            ForecastResponse.Forecast response = getItem(position);
            mBinder.textViewDateTime.setText(response.dt_txt);
            mBinder.textViewDesc.setText(response.weather[0].description);
            mBinder.textViewHumidity.setText("Humidity: " + response.main.humidity + "%");
            mBinder.textViewTemp.setText(response.main.temp + " F");
            mBinder.textViewTempMax.setText("Max: " + response.main.temp_max + " F");
            mBinder.textViewTempMin.setText("Min: " + response.main.temp_min + " F");
            String baseIconUrl = "https://openweathermap.org/img/wn/";
            Picasso.get()
                    .load(baseIconUrl + response.weather[0].icon + ".png")
                    .into(mBinder.imageViewWeatherIcon);
            return convertView;
        }

    }

}