package com.example.hw05;

import java.util.Arrays;

public class WeatherResponse
{
    Weather[] weather;
    MainWeather main;
    Wind wind;
    Cloud clouds;
    public static class Weather
    {
        String id, main, description, icon;

        @Override
        public String toString()
        {
            return "Weather{" +
                    "id='" + id + '\'' +
                    ", main='" + main + '\'' +
                    ", description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }

    public static class MainWeather
    {
        String temp, temp_min, temp_max, humidity;

        @Override
        public String toString()
        {
            return "MainWeather{" +
                    "temp='" + temp + '\'' +
                    ", temp_min='" + temp_min + '\'' +
                    ", temp_max='" + temp_max + '\'' +
                    ", humidity='" + humidity + '\'' +
                    '}';
        }
    }

    public static class Wind
    {
        String speed, deg;

        @Override
        public String toString()
        {
            return "Wind{" +
                    "speed='" + speed + '\'' +
                    ", deg='" + deg + '\'' +
                    '}';
        }
    }

    public class Cloud
    {
        String all;

        @Override
        public String toString()
        {
            return "Cloud{" +
                    "all='" + all + '\'' +
                    '}';
        }
    }

    @Override
    public String toString()
    {
        return "WeatherResponse{" +
                "weather=" + Arrays.toString(weather) +
                ", main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                '}';
    }
}
