package com.example.junctionxasia;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

//        View root = inflater.inflate(R.layout.activity_supplier_main, container, false);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        if (index == 1) {
            View frag = inflater.inflate(R.layout.supplier_dashboard_fragment, container, false);
            Button upload = frag.findViewById(R.id.button);
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadFood();
                }
            });
            return frag;
        }
        else if (index ==2){
            View frag = inflater.inflate(R.layout.reviews, container, false);
            return frag;
        }
        else if (index ==3){
            View frag = inflater.inflate(R.layout.analysis, container, false);
//            BarChart barChart = frag.findViewById(R.id.barchart);
//            ArrayList<BarEntry> entries = new ArrayList<>();
//            entries.add(new BarEntry(8f, 0));
//            entries.add(new BarEntry(3f, 1));
//            entries.add(new BarEntry(5f, 2));
//
//            BarDataSet bardataset = new BarDataSet(entries, "Cells");
//
//            ArrayList<String> labels = new ArrayList<String>();
//            labels.add("Butter Chicken");
//            labels.add("Briyani");
//            labels.add("Garlic Naan");
//
//
//            BarData data = new BarData(labels, bardataset);
//            barChart.setData(data); // set the data and list of labels into chart
//            bardataset.setColors(ColorTemplate.COLORFUL_COLORS);



            return frag;
        }
        else{
            View root = inflater.inflate(R.layout.activity_supplier_main, container, false);
            return root;
        }

    }
    public void uploadFood()
    {
        Intent intent = new Intent(getActivity(), UploadFood.class);
        startActivity(intent);
    }
}