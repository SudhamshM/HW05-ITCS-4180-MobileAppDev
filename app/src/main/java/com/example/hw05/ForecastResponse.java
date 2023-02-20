package com.example.hw05;
import com.example.hw05.WeatherResponse;
import java.util.ArrayList;

public class ForecastResponse
{
    ArrayList<Forecast> list;

    @Override
    public String toString()
    {
        return "ForecastResponse{" +
                "list=" + list +
                '}';
    }

    static class Forecast extends WeatherResponse
    {
        String dt_txt;

        @Override
        public String toString()
        {
            String old = super.toString();
            return old + dt_txt;
        }
    }
}
