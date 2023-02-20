package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import com.example.hw05.DataService.City;
import com.example.hw05.databinding.FragmentCitiesBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class CitiesFragment extends Fragment {

    // TODO: Rename and change types of parameters
    final private ArrayList<City> cities = DataService.cities;
    FragmentCitiesBinding binder;

    public CitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binder = FragmentCitiesBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cities Fragment");
        ArrayAdapter<City> cityAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, cities);
        binder.listView.setAdapter(cityAdapter);
        binder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                City city = cities.get(position);
                mListener.goToCity(city);
            }
        });
    }

    @FunctionalInterface
    interface CitiesListener
    {
        void goToCity(City city);
    }

    CitiesListener mListener;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        mListener = (CitiesListener) context;
    }
}